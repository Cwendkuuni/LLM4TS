package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR"));

        // Test country with empty language
        assertEquals(Locale.EMPTY, LocaleUtils.toLocale(""));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale("_"));

        // Test invalid inputs
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_GB_"));
        assertNull(LocaleUtils.toLocale("en_GB_xxx"));
        assertNull(LocaleUtils.toLocale("en_GB_xxx_"));
        assertNull(LocaleUtils.toLocale("en_GB_xxx_xx"));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = Arrays.asList(Locale.US, Locale.ENGLISH, Locale.ROOT);
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.US, null);
        assertTrue(Collections.equals(expected, actual));

        expected = Arrays.asList(Locale.ENGLISH, Locale.US, Locale.ROOT);
        actual = LocaleUtils.localeLookupList(Locale.ENGLISH, null);
        assertTrue(Collections.equals(expected, actual));

        expected = Arrays.asList(Locale.US, Locale.ENGLISH, Locale.ROOT, Locale.FRANCE);
        actual = LocaleUtils.localeLookupList(Locale.US, Locale.FRANCE);
        assertTrue(Collections.equals(expected, actual));

        expected = Arrays.asList(Locale.ENGLISH, Locale.US, Locale.ROOT, Locale.FRANCE, Locale.FRENCH);
        actual = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.FRENCH);
        assertTrue(Collections.equals(expected, actual));

        List<Locale> empty = Collections.emptyList();
        List<Locale> nullList = null;
        List<Locale> localeList = Arrays.asList(Locale.US, Locale.ENGLISH);
        List<Locale> expectedList = Arrays.asList(Locale.US, Locale.ENGLISH, Locale.ROOT);
        List<Locale> actualList;

        actualList = LocaleUtils.localeLookupList(null, null);
        assertTrue(Collections.equals(empty, actualList));

        actualList = LocaleUtils.localeLookupList(null, Locale.US);
        assertTrue(Collections.equals(empty, actualList));

        actualList = LocaleUtils.localeLookupList(Locale.US, null);
        assertTrue(Collections.equals(localeList, actualList));

        actualList = LocaleUtils.localeLookupList(Locale.US, Locale.ENGLISH);
        assertTrue(Collections.equals(expectedList, actualList));

        actualList = LocaleUtils.localeLookupList(Locale.ENGLISH, null);
        assertTrue(Collections.equals(expectedList, actualList));

        actualList = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.FRENCH);
        assertTrue(Collections.equals(expectedList, actualList));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.ROOT, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN,
                Locale.JAPANESE, Locale.KOREAN, Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE);
        assertTrue(Collections.equals(expected, actual));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = new HashSet<>(LocaleUtils.availableLocaleList());
        assertTrue(Collections.disjoint(expected, null));
        assertTrue(Collections.equals(expected, actual));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(Locale.FRENCH, Locale.CANADA_FRENCH);
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertTrue(Collections.equals(expected, actual));

        expected = Collections.emptyList();
        actual = LocaleUtils.languagesByCountry(null);
        assertTrue(Collections.equals(expected, actual));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(Locale.FRANCE, Locale.CANADA);
        List<Locale> actual = LocaleUtils.countriesByLanguage("fr");
        assertTrue(Collections.equals(expected, actual));

        expected = Collections.emptyList();
        actual = LocaleUtils.countriesByLanguage(null);
        assertTrue(Collections.equals(expected, actual));
    }
}