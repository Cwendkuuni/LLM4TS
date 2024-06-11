package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Valid test cases
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("es_ES", LocaleUtils.toLocale("es_ES").toString());

        // Invalid test cases
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_us"); // lower case 's'
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_ invalid"); // whitespace
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xx");
        List<Locale> lookupList = LocaleUtils.localeLookupList(baseLocale);

        // Test base locale is included
        assertEquals(baseLocale, lookupList.get(0));

        // Test language-only locale
        assertEquals(new Locale("fr"), lookupList.get(1));

        // Test country-only locale
        assertEquals(new Locale("fr", ""), lookupList.get(2));

        // Test default locale
        Locale defaultLocale = new Locale("en");
        lookupList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        assertEquals(defaultLocale, lookupList.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();

        // Test empty list
        assertTrue(availableLocales.isEmpty());

        // Test non-empty list
        assertEquals(6, availableLocales.size()); // This number may vary depending on your system

        // Test specific locale presence
        assertTrue(availableLocales.contains(new Locale("en", "US")));

        // Test specific locale absence
        assertFalse(availableLocales.contains(new Locale("non-existent", "non-existent")));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> languages = LocaleUtils.languagesByCountry(countryCode);

        // Test empty list
        assertTrue(languages.isEmpty());

        // Test non-empty list
        List<String> languageCodes = Arrays.asList("fr", "en"); // This may vary depending on your system
        for (String languageCode : languageCodes) {
            Locale expectedLocale = new Locale(languageCode, countryCode);
            assertTrue(languages.contains(expectedLocale));
        }
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> countries = LocaleUtils.countriesByLanguage(languageCode);

        // Test empty list
        assertTrue(countries.isEmpty());

        // Test non-empty list
        List<String> countryCodes = Arrays.asList("CA", "FR"); // This may vary depending on your system
        for (String countryCode : countryCodes) {
            Locale expectedLocale = new Locale(languageCode, countryCode);
            assertTrue(countries.contains(expectedLocale));
        }
    }
}