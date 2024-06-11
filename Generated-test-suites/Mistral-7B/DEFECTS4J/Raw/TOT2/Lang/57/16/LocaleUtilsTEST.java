package org.apache.commons.lang.test;

import org.junit.Test;
import org.junit.Assert.*;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        String input1 = "en";
        Locale expected1 = new Locale("en", "");
        Locale actual1 = LocaleUtils.toLocale(input1);
        assertEquals(expected1, actual1);

        String input2 = "en_GB";
        Locale expected2 = new Locale("en", "GB");
        Locale actual2 = LocaleUtils.toLocale(input2);
        assertEquals(expected2, actual2);

        String input3 = "en_GB_XXX";
        Locale expected3 = new Locale("en", "GB", "XXX");
        Locale actual3 = LocaleUtils.toLocale(input3);
        assertEquals(expected3, actual3);

        // Test invalid input
        String input4 = null;
        Locale actual4 = LocaleUtils.toLocale(input4);
        assertNull(actual4);

        String input5 = "xy_XY_klm";
        String errorMessage = "Invalid locale format: xy_XY_klm";
        try {
            LocaleUtils.toLocale(input5);
            fail("Expected IllegalArgumentException: " + errorMessage);
        } catch (IllegalArgumentException e) {
            assertEquals(errorMessage, e.getMessage());
        }

        String input6 = "en_123";
        String errorMessage2 = "Invalid locale format: en_123";
        try {
            LocaleUtils.toLocale(input6);
            fail("Expected IllegalArgumentException: " + errorMessage2);
        } catch (IllegalArgumentException e) {
            assertEquals(errorMessage2, e.getMessage());
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid input
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale expected1 = new Locale("fr", "CA", "xxx");
        List actual1 = LocaleUtils.localeLookupList(locale1);
        assertEquals(expected1, actual1);

        Locale locale2 = new Locale("en");
        List expected2 = Arrays.asList(locale2, new Locale("en", ""), new Locale("en", ""), new Locale("fr", "CA", "xxx"));
        List actual2 = LocaleUtils.localeLookupList(locale2, locale1);
        assertEquals(expected2, actual2);

        // Test empty input
        Locale locale3 = null;
        List actual3 = LocaleUtils.localeLookupList(locale3);
        assertTrue(actual3.isEmpty());

        // Test invalid locale
        Locale locale4 = new Locale("xy", "XY", "klm");
        List actual4 = LocaleUtils.localeLookupList(locale4, locale1);
        assertEquals(expected2, actual4);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test unmodifiable list
        List availableLocales = LocaleUtils.availableLocaleList();
        assertFalse(availableLocales.isEmpty());
        try {
            availableLocales.add(new Locale("en", "GB", "XXX"));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // Test contains
        Locale locale1 = new Locale("fr", "CA", "xxx");
        assertTrue(availableLocales.contains(locale1));

        // Test does not contain
        Locale locale2 = new Locale("xy", "XY", "klm");
        assertFalse(availableLocales.contains(locale2));
    }

    @Test
    public void testIsAvailableLocale() {
        // Test available locale
        Locale locale1 = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));

        // Test invalid locale
        Locale locale2 = new Locale("xy", "XY", "klm");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty country
        String country = null;
        List actual1 = LocaleUtils.languagesByCountry(country);
        assertTrue(actual1.isEmpty());

        // Test valid country
        String country1 = "CA";
        List expected1 = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        List actual1_ = LocaleUtils.languagesByCountry(country1);
        assertEquals(expected1, actual1_);

        // Test country with no languages
        String country2 = "XX";
        List actual2 = LocaleUtils.languagesByCountry(country2);
        assertTrue(actual2.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty language
        String language = null;
        List actual1 = LocaleUtils.countriesByLanguage(language);
        assertTrue(actual1.isEmpty());

        // Test valid language
        String language1 = "en";
        List expected1 = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"));
        List actual1_ = LocaleUtils.countriesByLanguage(language1);
        assertEquals(expected1, actual1_);

        // Test language with no countries
        String language2 = "XX";
        List actual2 = LocaleUtils.countriesByLanguage(language2);
        assertTrue(actual2.isEmpty());
    }
}