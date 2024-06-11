package org.apache.commons.lang.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Valid input
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());

        // Empty input
        assertNull(LocaleUtils.toLocale(null));

        // Invalid input: language code is not lowercase
        try {
            LocaleUtils.toLocale("EN_US");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input: country code is not uppercase
        try {
            LocaleUtils.toLocale("en_Fr");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input: separator is not underscore
        try {
            LocaleUtils.toLocale("en_US_XX");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input: length is incorrect
        try {
            LocaleUtils.toLocale("en__US");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input: language and country codes have wrong length
        try {
            LocaleUtils.toLocale("es_ES_ES");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input: extra separator and code
        try {
            LocaleUtils.toLocale("en_US__XX");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "XX");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);

        // Check that the list contains the locale passed as argument
        assertTrue(localeList.contains(locale));

        // Check that the list contains the locale without variant
        assertTrue(localeList.contains(new Locale("fr", "CA")));

        // Check that the list contains the locale with empty country code
        assertTrue(localeList.contains(new Locale("fr", "")));

        // Check that the list contains the default locale
        assertTrue(localeList.contains(Locale.getDefault()));

        // Check that the list does not contain duplicate locales
        assertEquals(localeList.size(), 4);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();

        // Check that the list is not empty
        assertFalse(availableLocales.isEmpty());

        // Check that the list does not contain duplicates
        assertEquals(availableLocales.size(), Locale.getAvailableLocales().length);
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();

        // Check that a known locale is available
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "CA")));

        // Check that an unknown locale is not available
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "XX")));

        // Check that null is not a locale
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");

        // Check that the list is not empty
        assertFalse(languages.isEmpty());

        // Check that the list contains expected locales
        assertTrue(languages.contains(new Locale("fr", "CA")));
        assertTrue(languages.contains(new Locale("en", "CA")));

        // Check that the list does not contain unexpected locales
        assertFalse(languages.contains(new Locale("es", "CA")));
        assertFalse(languages.contains(new Locale("de", "CA")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");

        // Check that the list is not empty
        assertFalse(countries.isEmpty());

        // Check that the list contains expected locales
        assertTrue(countries.contains(new Locale("US", "en")));
        assertTrue(countries.contains(new Locale("CA", "en")));

        // Check that the list does not contain unexpected locales
        assertFalse(countries.contains(new Locale("MX", "en")));
        assertFalse(countries.contains(new Locale("ES", "en")));
    }
}