package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Tests for {@link LocaleUtils} class.
 */
public class LocaleUtilsTest {

    /**
     * Test for {@link LocaleUtils#toLocale(String)} method.
     */
    @Test
    public void testToLocale() {
        // Valid cases
        Locale expectedLocale = new Locale("en", "US");
        Locale locale = LocaleUtils.toLocale("en_US");
        assertEquals(expectedLocale, locale);

        expectedLocale = new Locale("fr", "FR");
        locale = LocaleUtils.toLocale("fr_FR");
        assertEquals(expectedLocale, locale);

        expectedLocale = new Locale("it", "IT");
        locale = LocaleUtils.toLocale("it_IT");
        assertEquals(expectedLocale, locale);

        expectedLocale = new Locale("de", "");
        locale = LocaleUtils.toLocale("de");
        assertEquals(expectedLocale, locale);

        expectedLocale = new Locale("", "ES");
        locale = LocaleUtils.toLocale("es");
        assertEquals(expectedLocale, locale);

        // Invalid cases
        try {
            Locale locale1 = LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale locale2 = LocaleUtils.toLocale("en_US_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale locale3 = LocaleUtils.toLocale("en_US_xyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale locale4 = LocaleUtils.toLocale("en_US_xyz_ab");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale locale5 = LocaleUtils.toLocale("en_US_xyz_ab_cd");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale locale6 = LocaleUtils.toLocale("en_US_xyz_ab_cd_ef");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale locale7 = LocaleUtils.toLocale("xyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale locale8 = LocaleUtils.toLocale("xyz_ab");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale locale9 = LocaleUtils.toLocale("xyz_ab_cd");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale locale10 = LocaleUtils.toLocale("xyz_ab_cd_ef");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for {@link LocaleUtils#localeLookupList(Locale)} method.
     */
    @Test
    public void testLocaleLookupList() {
        // Test with defaultLocale = null
        Locale locale = new Locale("fr", "CA", "XX");
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("fr", "CA", "XX"),
                new Locale("fr", "CA"),
                new Locale("fr")
        );
        List<Locale> locales = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedLocales, locales);

        // Test with defaultLocale = Locale.US
        locale = new Locale("fr", "CA", "XX");
        expectedLocales = Arrays.asList(
                new Locale("fr", "CA", "XX"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        locales = LocaleUtils.localeLookupList(locale, Locale.US);
        assertEquals(expectedLocales, locales);
    }

    /**
     * Test for {@link LocaleUtils#availableLocaleList()} method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("ar", "AE"),
                new Locale("ar", "BH"),
                new Locale("ar", "DZ"),
                // ...
                new Locale("zh", "CN"),
                new Locale("zh", "HK"),
                new Locale("zh", "TW"),
                // ...
                new Locale("en", "US"),
                new Locale("en", "GB"),
                new Locale("en", "CA"),
                // ...
                new Locale("", "JA")
        );
        assertEquals(expectedLocales, locales);
    }

    /**
     * Test for {@link LocaleUtils#isAvailableLocale(Locale)} method.
     */
    @Test
    public void testIsAvailableLocale() {
        // Valid cases
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "FR");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("zh", "CN");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Invalid cases
        locale = new Locale("xyz", "xyz");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "XY");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("es", "ES_es_es");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test for {@link LocaleUtils#languagesByCountry(String)} method.
     */
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("en", "US"),
                new Locale("es", "ES"),
                new Locale("fr", "FR"),
                new Locale("de", "DE"),
                new Locale("ru", "RU")
        );
        List<Locale> locales = LocaleUtils.languagesByCountry("US");
        assertEquals(expectedLocales, locales);
    }

    /**
     * Test for {@link LocaleUtils#countriesByLanguage(String)} method.
     */
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("US", "en"),
                new Locale("ES", "es"),
                new Locale("FR", "fr"),
                new Locale("DE", "de"),
                new Locale("RU", "ru")
        );
        List<Locale> locales = LocaleUtils.countriesByLanguage("en");
        assertEquals(expectedLocales, locales);
    }
}