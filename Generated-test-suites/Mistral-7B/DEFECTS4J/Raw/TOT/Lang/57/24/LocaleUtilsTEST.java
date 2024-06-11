package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Locale;
import java.util.Arrays;
import java.util.Collections;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        expected = new Locale("en");
        actual = LocaleUtils.toLocale("en");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA", "xxx");
        actual = LocaleUtils.toLocale("fr_CA_xxx");
        assertEquals(expected, actual);

        // Test invalid inputs
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xxX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xxX_XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid inputs
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        locale = null;
        expected = Collections.emptyList();
        actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        // Test invalid input
        try {
            LocaleUtils.localeLookupList(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        Locale locale2 = new Locale("fr", "FR");

        assertTrue(LocaleUtils.isAvailableLocale(locale1));
        assertTrue(LocaleUtils.isAvailableLocale(locale2));

        Locale invalidLocale = new Locale("Invalid", "Code");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "US";

        List<Locale> expected = Arrays.asList(new Locale("en", countryCode), new Locale("en", ""));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        countryCode = null;
        actual = Collections.emptyList();
        assertEquals(actual, LocaleUtils.languagesByCountry(countryCode));
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";

        List<Locale> expected = Arrays.asList(new Locale(languageCode, "US"), new Locale(languageCode, ""), new Locale("", languageCode));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        languageCode = null;
        actual = Collections.emptyList();
        assertEquals(actual, LocaleUtils.countriesByLanguage(languageCode));
    }
}