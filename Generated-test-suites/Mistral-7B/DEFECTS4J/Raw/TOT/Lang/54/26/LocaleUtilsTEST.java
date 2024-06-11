package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        expected = new Locale("fr", "");
        actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA", "XX");
        actual = LocaleUtils.toLocale("fr_CA_XX");
        assertEquals(expected, actual);

        // Test invalid inputs
        try {
            Locale actual = LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_US_XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_US_XXX_YYY");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_US_XXX_YYY_ZZZ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_US_XXX_YYY_ZZZ_WWW");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_US_XXX_YYY_ZZZ_WWW_XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_US_XXX_YYY_ZZZ_WWW_XXX_YYY");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid inputs
        Locale locale = new Locale("fr", "CA", "XX");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        locale = new Locale("en");
        expected = Arrays.asList(locale, new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale, null);
        assertEquals(expected, actual);

        // Test invalid inputs
        try {
            LocaleUtils.localeLookupList(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        Locale localeWithVariant = new Locale("fr", "CA", "XXX");
        try {
            List<Locale> actual = LocaleUtils.localeLookupList(localeWithVariant, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        List<Locale> emptyList = Collections.emptyList();
        try {
            List<Locale> actual = LocaleUtils.localeLookupList(localeWithVariant, emptyList);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Test getting available locales
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Locale.getAvailableLocales();
        assertEquals(expected, actual);

        // Test invalid input
        try {
            LocaleUtils.availableLocaleList(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test getting available locales as a set
        Set<Locale> expected = Collections.unmodifiableSet(new HashSet<>(LocaleUtils.availableLocaleList()));
        Set<Locale> actual = Locale.getAvailableLocaleSet();
        assertEquals(expected, actual);

        // Test invalid input
        try {
            LocaleUtils.availableLocaleSet(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Test valid inputs
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA", "XX");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test invalid inputs
        Locale localeWithVariant = new Locale("fr", "CA", "XXX");
        assertFalse(LocaleUtils.isAvailableLocale(localeWithVariant));

        locale = null;
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test valid inputs
        String countryCode = "US";
        List<Locale> expected = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> actual = Arrays.asList(new Locale("en", countryCode), new Locale("es", countryCode), new Locale("fr", countryCode));
        assertEquals(expected, actual);

        countryCode = "CA";
        expected = Arrays.asList(new Locale("fr", countryCode), new Locale("en", countryCode));
        actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        countryCode = null;
        expected = Collections.emptyList();
        actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        // Test invalid input
        String invalidCountryCode = "";
        try {
            List<Locale> actual = LocaleUtils.languagesByCountry(invalidCountryCode);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCountriesByLanguage() {
        // Test valid inputs
        String languageCode = "en";
        List<Locale> expected = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> actual = Arrays.asList(new Locale("US", languageCode), new Locale("CA", languageCode), new Locale("GB", languageCode));
        assertEquals(expected, actual);

        languageCode = "fr";
        expected = Arrays.asList(new Locale("CA", languageCode), new Locale("FR", languageCode), new Locale("BE", languageCode));
        actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        languageCode = null;
        expected = Collections.emptyList();
        actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        // Test invalid input
        String invalidLanguageCode = "";
        try {
            List<Locale> actual = LocaleUtils.countriesByLanguage(invalidLanguageCode);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}