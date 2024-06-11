package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Valid tests
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB_London"));

        // Invalid tests
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            LocaleUtils.toLocale("en_GB_London_ invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            LocaleUtils.toLocale("en_GB_London_ xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            LocaleUtils.toLocale("en_GB_London_ xxx_invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test with empty list
        List<Locale> emptyList = Collections.emptyList();
        List<Locale> list = LocaleUtils.localeLookupList(Locale.ENGLISH, emptyList);
        assertSame(emptyList, list);

        // Test with null input
        List<Locale> nullList = null;
        list = LocaleUtils.localeLookupList(Locale.ENGLISH, nullList);
        assertSame(Collections.singletonList(Locale.ENGLISH), list);

        // Test with valid locale
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> validList = Arrays.asList(Locale.FRENCH, Locale.FRENCH_CANADA, locale);
        List<Locale> result = LocaleUtils.localeLookupList(locale, validList);
        assertSame(validList, result);

        // Test with invalid locale
        locale = new Locale("fr", "CA", "xxx_invalid");
        List<Locale> invalidList = Arrays.asList(Locale.FRENCH, Locale.FRENCH_CANADA, locale);
        result = LocaleUtils.localeLookupList(locale, invalidList);
        assertTrue(result.contains(Locale.FRENCH));
        assertTrue(result.contains(Locale.FRENCH_CANADA));
        assertFalse(result.contains(locale));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test with empty list
        List<Locale> emptyList = Collections.emptyList();
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertSame(emptyList, result);

        // Test with available locales
        List<Locale> locales = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.SIMPLIFIED_CHINESE);
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(locales.size(), availableLocales.size());
        for (int i = 0; i < locales.size(); i++) {
            assertEquals(locales.get(i), availableLocales.get(i));
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test with empty set
        Set<Locale> emptySet = Collections.emptySet();
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertSame(emptySet, result);

        // Test with available locales
        Set<Locale> locales = new HashSet<>(Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.SIMPLIFIED_CHINESE));
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertEquals(locales.size(), availableLocales.size());
        assertTrue(locales.containsAll(availableLocales));
        assertTrue(availableLocales.containsAll(locales));
    }

    @Test
    public void testIsAvailableLocale() {
        // Test with null input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test with available locale
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));

        // Test with unavailable locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_US_Invalid")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test with null input
        assertSame(Collections.emptyList(), LocaleUtils.languagesByCountry(null));

        // Test with valid country code
        List<Locale> result = LocaleUtils.languagesByCountry("CA");
        List<Locale> expected = Arrays.asList(Locale.FRENCH, Locale.ENGLISH);
        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }

        // Test with invalid country code
        result = LocaleUtils.languagesByCountry("XX");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test with null input
        assertSame(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));

        // Test with valid language code
        List<Locale> result = LocaleUtils.countriesByLanguage("fr");
        List<Locale> expected = Arrays.asList(Locale.FRENCH, Locale.FRENCH_CANADA);
        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }

        // Test with invalid language code
        result = LocaleUtils.countriesByLanguage("XX");
        assertEquals(Collections.emptyList(), result);
    }
}