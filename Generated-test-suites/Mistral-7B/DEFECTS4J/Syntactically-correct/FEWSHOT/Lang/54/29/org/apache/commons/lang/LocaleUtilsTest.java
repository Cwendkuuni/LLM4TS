package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR"));

        // Test invalid input
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_xx"));
        assertNull(LocaleUtils.toLocale("en_GB_xx"));
        assertNull(LocaleUtils.toLocale("en_GB_xx_xx"));
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty input
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));

        // Test base case
        List expected = Arrays.asList(Locale.ENGLISH, Locale.US, Locale.ENGLISH);
        assertEquals(expected, LocaleUtils.localeLookupList(Locale.US));

        // Test variant locale
        expected = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH, Locale.US, Locale.ENGLISH);
        assertEquals(expected, LocaleUtils.localeLookupList(Locale.US, Locale.ENGLISH));

        // Test unknown locale
        expected = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH, Locale.US, Locale.ENGLISH, Locale.ENGLISH);
        assertEquals(expected, LocaleUtils.localeLookupList(Locale.GERMAN, Locale.ENGLISH));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        assertTrue(LocaleUtils.availableLocaleList().isEmpty());

        // Test available locales
        List available = LocaleUtils.availableLocaleList();
        List expected = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.SPANISH);
        assertTrue(available.containsAll(expected));
        assertEquals(expected.size(), available.size());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test empty set
        assertTrue(LocaleUtils.availableLocaleSet().isEmpty());

        // Test available locales
        Set available = LocaleUtils.availableLocaleSet();
        Set expected = new HashSet(Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.SPANISH));
        assertTrue(available.containsAll(expected));
        assertEquals(expected.size(), available.size());
    }

    @Test
    public void testIsAvailableLocale() {
        // Test available locale
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));

        // Test unknown locale
        assertFalse(LocaleUtils.isAvailableLocale(Locale.GERMAN));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));

        // Test valid input
        List expected = Arrays.asList(Locale.FRENCH, Locale.BELGIAN, Locale.CANADIAN);
        assertEquals(expected, LocaleUtils.languagesByCountry("BE"));

        // Test unknown country
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("XX"));
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty list
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));

        // Test valid input
        List expected = Arrays.asList(Locale.FRANCE, Locale.BELGIUM, Locale.SWITZERLAND);
        assertEquals(expected, LocaleUtils.countriesByLanguage("fr"));

        // Test unknown language
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("XX"));
    }
}