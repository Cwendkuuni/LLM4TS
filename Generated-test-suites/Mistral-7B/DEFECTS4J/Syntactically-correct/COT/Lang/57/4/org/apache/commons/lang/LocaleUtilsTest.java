package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        // Test null input
        assertNull(LocaleUtils.toLocale(null));

        // Test empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid format
        try {
            LocaleUtils.toLocale("en_GB_xxx");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test incorrect language code
        try {
            LocaleUtils.toLocale("fr_GB_en");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test incorrect country code
        try {
            LocaleUtils.toLocale("en_gb_GB");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test separator other than underscore
        try {
            LocaleUtils.toLocale("en_GB_xxx_XX");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test invalid length
        try {
            LocaleUtils.toLocale("en_");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test invalid language code (lowercase)
        try {
            LocaleUtils.toLocale("fr");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test invalid country code (lowercase)
        try {
            LocaleUtils.toLocale("CA");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic test
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en"));
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(expected, actual);

        // Test empty list
        List<Locale> actualEmpty = LocaleUtils.localeLookupList(null);
        assertEquals(Collections.emptyList(), actualEmpty);

        // Test null defaultLocale
        List<Locale> actualNull = LocaleUtils.localeLookupList(Locale.US, null);
        assertTrue(actualNull.isEmpty());

        // Test same locale as defaultLocale
        List<Locale> actualSame = LocaleUtils.localeLookupList(Locale.US, Locale.US);
        assertEquals(Collections.singletonList(Locale.US), actualSame);

        // Test invalid locale
        List<Locale> actualInvalid = LocaleUtils.localeLookupList(Locale.US, new Locale("fr", "FR"));
        assertTrue(actualInvalid.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic test
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
        assertEquals(expected, actual);

        // Test null return value
        List<Locale> expectedNull = null;
        List<Locale> actualNull = LocaleUtils.availableLocaleList();
        assertEquals(expectedNull, actualNull);
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic test
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test null input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test invalid locale
        Locale invalidLocale = new Locale("fr", "FR_XX");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic test
        List<Locale> expected = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"), new Locale("en", "US"), new Locale("fr", "FR"));
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, actual);

        // Test null input
        List<Locale> actualNull = LocaleUtils.languagesByCountry(null);
        assertTrue(actualNull.isEmpty());

        // Test empty list
        List<Locale> actualEmpty = LocaleUtils.languagesByCountry("");
        assertTrue(actualEmpty.isEmpty());

        // Test invalid country code
        List<Locale> actualInvalid = LocaleUtils.languagesByCountry("ZZ");
        assertTrue(actualInvalid.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic test
        List<Locale> expected = Arrays.asList(new Locale("CA", "en"), new Locale("CA", "fr"), new Locale("US", "en"), new Locale("FR", "fr"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, actual);

        // Test null input
        List<Locale> actualNull = LocaleUtils.countriesByLanguage(null);
        assertTrue(actualNull.isEmpty());

        // Test empty list
        List<Locale> actualEmpty = LocaleUtils.countriesByLanguage("");
        assertTrue(actualEmpty.isEmpty());

        // Test invalid language code
        List<Locale> actualInvalid = LocaleUtils.countriesByLanguage("ZZ");
        assertTrue(actualInvalid.isEmpty());
    }
}