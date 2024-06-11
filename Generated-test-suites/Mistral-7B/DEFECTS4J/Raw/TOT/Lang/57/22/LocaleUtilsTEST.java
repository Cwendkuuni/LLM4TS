package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Valid input
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("de", LocaleUtils.toLocale("de").toString());

        // Empty input
        assertNull(LocaleUtils.toLocale(""));

        // Invalid input - language code not lowercase
        try {
            LocaleUtils.toLocale("EN_US");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input - country code not uppercase
        try {
            LocaleUtils.toLocale("en_fr");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input - separator not underscore
        try {
            LocaleUtils.toLocale("en_GB_x");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input - length less than 2
        try {
            LocaleUtils.toLocale("en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input - length greater than 7
        try {
            LocaleUtils.toLocale("en_GB_XX_YY_ZZ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test with single locale
        List lookupList = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(1, lookupList.size());
        assertEquals(Locale.US, lookupList.get(0));

        // Test with multiple locales
        lookupList = LocaleUtils.localeLookupList(Locale.CANADA_FRENCH);
        assertEquals(3, lookupList.size());
        assertEquals(Locale.CANADA_FRENCH, lookupList.get(0));
        assertEquals(Locale.CANADA, lookupList.get(1));
        assertEquals(Locale.US, lookupList.get(2));

        // Test with default locale
        lookupList = LocaleUtils.localeLookupList(Locale.UK, Locale.US);
        assertEquals(4, lookupList.size());
        assertEquals(Locale.UK, lookupList.get(0));
        assertEquals(Locale.ENGLISH, lookupList.get(1));
        assertEquals(Locale.US, lookupList.get(2));
        assertEquals(Locale.US, lookupList.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(true, availableLocales.contains(Locale.US));
        assertEquals(true, availableLocales.contains(Locale.FRENCH));
        assertEquals(true, availableLocales.contains(Locale.GERMAN));
        assertEquals(false, availableLocales.contains(new Locale("xy", "xy")));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xy", "xy")));
    }

    @Test
    public void testLanguagesByCountry() {
        List languages = LocaleUtils.languagesByCountry("US");
        assertEquals(6, languages.size());
        assertEquals(Locale.ENGLISH, languages.get(0));
        assertEquals(Locale.US, languages.get(1));
        // ... Add checks for other expected languages for US

        languages = LocaleUtils.languagesByCountry("FR");
        assertEquals(1, languages.size());
        assertEquals(Locale.FRENCH, languages.get(0));

        // Test with unsupported country
        List emptyList = LocaleUtils.languagesByCountry("XY");
        assertEquals(0, emptyList.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(3, countries.size());
        assertEquals(Locale.US, countries.get(0));
        assertEquals(Locale.UK, countries.get(1));
        assertEquals(Locale.CA, countries.get(2));

        countries = LocaleUtils.countriesByLanguage("de");
        assertEquals(3, countries.size());
        // ... Add checks for other expected countries for German

        // Test with unsupported language
        List emptyList = LocaleUtils.countriesByLanguage("XY");
        assertEquals(0, emptyList.size());
    }
}