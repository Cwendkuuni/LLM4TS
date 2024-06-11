package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Tests for LocaleUtils class.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale() method with valid inputs and expected outputs.
     */
    @Test
    public void testToLocale() {
        // Test with language only
        Locale expected = new Locale("en");
        Locale actual = LocaleUtils.toLocale("en");
        assertEquals(expected, actual);

        // Test with language and country
        expected = new Locale("en", "GB");
        actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        // Test with language, country and variant
        expected = new Locale("en", "GB", "XXX");
        actual = LocaleUtils.toLocale("en_GB_XXX");
        assertEquals(expected, actual);
    }

    /**
     * Test toLocale() method with invalid inputs and expected exceptions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        // Test with empty input
        LocaleUtils.toLocale("");

        // Test with null input
        LocaleUtils.toLocale(null);

        // Test with invalid format
        LocaleUtils.toLocale("fr_CA_XXX");
    }

    /**
     * Test localeLookupList() method with valid inputs and expected outputs.
     */
    @Test
    public void testLocaleLookupList() {
        // Test with single Locale input
        Locale locale = new Locale("fr", "CA", "XXX");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        // Test with defaultLocale input
        locale = new Locale("fr", "CA", "XXX");
        Locale defaultLocale = new Locale("en");
        expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);
    }

    /**
     * Test localeLookupList() method with empty input.
     */
    @Test
    public void testLocaleLookupListEmptyInput() {
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertTrue(actual.isEmpty());
    }

    /**
     * Test availableLocaleList() method with valid input and expected output.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Collections.list(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    /**
     * Test availableLocaleSet() method with valid input and expected output.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    /**
     * Test isAvailableLocale() method with valid input and expected output.
     */
    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA", "XXX");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test isAvailableLocale() method with invalid input and expected output.
     */
    @Test
    public void testIsAvailableLocaleInvalidInput() {
        Locale locale = new Locale("fr", "CA");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test languagesByCountry() method with valid input and expected output.
     */
    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(new Locale("fr", countryCode), new Locale("en", countryCode));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    /**
     * Test languagesByCountry() method with empty input.
     */
    @Test
    public void testLanguagesByCountryEmptyInput() {
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        assertTrue(actual.isEmpty());
    }

    /**
     * Test countriesByLanguage() method with valid input and expected output.
     */
    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expected = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "BE"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

    /**
     * Test countriesByLanguage() method with empty input.
     */
    @Test
    public void testCountriesByLanguageEmptyInput() {
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        assertTrue(actual.isEmpty());
    }
}