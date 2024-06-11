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
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        expected = new Locale("fr", "");
        actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA");
        actual = LocaleUtils.toLocale("fr_CA");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA", "QC");
        actual = LocaleUtils.toLocale("fr_CA_QC");
        assertEquals(expected, actual);

        // Edge cases
        expected = new Locale("es", "ES", "");
        actual = LocaleUtils.toLocale("es_ES_");
        assertEquals(expected, actual);

        expected = new Locale("en", "GB", "Belfast");
        actual = LocaleUtils.toLocale("en_GB_Belfast");
        assertEquals(expected, actual);

        expected = new Locale("es", "", "Madrid");
        actual = LocaleUtils.toLocale("es_Madrid");
        assertNull(actual);

        expected = new Locale("xx", "YY", "ZZ");
        actual = LocaleUtils.toLocale("xx_YY_ZZ");
        assertNull(actual);

        // Error cases
        try {
            Locale actual = LocaleUtils.toLocale("en_US_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_US_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_US_XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_US_XXX_YYY");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Valid test cases
        Locale locale = new Locale("fr", "CA", "QC");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        locale = new Locale("en");
        expected = Arrays.asList(locale, new Locale("en"), new Locale(""));
        actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        // Edge cases
        locale = new Locale("fr", "CA", "");
        expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(expected, actual);

        // Error cases
        locale = null;
        try {
            List<Locale> actual = LocaleUtils.localeLookupList(locale);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        locale = new Locale("en_US_XXX");
        try {
            List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Valid test cases
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Edge cases
        locale = new Locale("es", "ES", "");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Error cases
        locale = new Locale("es", "ES", "Madrid");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en_US_XXX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = null;
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Valid test cases
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(new Locale("en", countryCode), new Locale("fr", countryCode));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        // Edge cases
        countryCode = "";
        expected = Arrays.asList(new Locale("en", "US"), new Locale("fr", "FR"), new Locale("de", "DE"));
        actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        // Error cases
        countryCode = "ZZ";
        assertEmpty(LocaleUtils.languagesByCountry(countryCode));

        countryCode = null;
        assertEmpty(LocaleUtils.languagesByCountry(countryCode));
    }

    @Test
    public void testCountriesByLanguage() {
        // Valid test cases
        String languageCode = "fr";
        List<Locale> expected = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "CH"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        // Edge cases
        languageCode = "";
        expected = Arrays.asList(new Locale("en", "US"), new Locale("fr", "FR"), new Locale("de", "DE"));
        actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        // Error cases
        languageCode = "ZZ";
        assertEmpty(LocaleUtils.countriesByLanguage(languageCode));

        languageCode = null;
        assertEmpty(LocaleUtils.countriesByLanguage(languageCode));
    }

    private void assertEmpty(List<Locale> list) {
        assertTrue(list.isEmpty());
    }

}