package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.CHINESE, LocaleUtils.toLocale("zh_CN"));

        // Test invalid inputs
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("aaXXX");
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
            LocaleUtils.toLocale("en_US_XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US__XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty input
        List<Locale> locales = LocaleUtils.localeLookupList(null);
        assertTrue(locales.isEmpty());

        // Test valid inputs
        Locale locale = new Locale("fr", "CA", "xxx");
        locales = LocaleUtils.localeLookupList(locale);
        assertEquals(3, locales.size());
        assertEquals(locale, locales.get(0));
        assertTrue(locale.getCountry().isEmpty() || locale.getCountry().equals("CA"));
        assertFalse(locale.getLanguage().isEmpty() || locale.getLanguage().equals(""));
        assertFalse(locale.getVariant().isEmpty() || locale.getVariant().equals("xxx"));

        assertEquals(locale, locales.get(1));
        assertTrue(locale.getCountry().isEmpty());
        assertFalse(locale.getLanguage().isEmpty() || locale.getLanguage().equals("fr"));
        assertFalse(locale.getVariant().isEmpty());

        assertEquals(Locale.ENGLISH, locales.get(2));
        assertTrue(Locale.ENGLISH.getCountry().isEmpty());
        assertFalse(Locale.ENGLISH.getLanguage().isEmpty() || Locale.ENGLISH.getLanguage().equals("en"));
        assertFalse(Locale.ENGLISH.getVariant().isEmpty());

        // Test default locale
        Locale defaultLocale = Locale.ENGLISH;
        locales = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, locales.size());
        assertEquals(locale, locales.get(0));
        assertEquals(defaultLocale, locales.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertTrue(locales.isEmpty());

        // Test valid locales
        locales = LocaleUtils.availableLocaleList();
        assertEquals(223, locales.size());
        assertTrue(locales.contains(Locale.ENGLISH));
        assertTrue(locales.contains(Locale.FRENCH));
        assertTrue(locales.contains(Locale.CHINESE));

        // Test custom locales
        Locale locale = new Locale("xx", "YY");
        locales = LocaleUtils.availableLocaleList();
        assertFalse(locales.contains(locale));

        LocaleUtils.toLocale("xx_YY");
        locales = LocaleUtils.availableLocaleList();
        assertTrue(locales.contains(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty input
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        assertTrue(languages.isEmpty());

        // Test valid inputs
        List<Locale> languagesByCountryFr = LocaleUtils.languagesByCountry("FR");
        assertEquals(2, languagesByCountryFr.size());
        assertTrue(languagesByCountryFr.contains(Locale.FRENCH));
        assertTrue(languagesByCountryFr.contains(new Locale("fr", "CA")));

        List<Locale> languagesByCountryZh = LocaleUtils.languagesByCountry("ZH");
        assertEquals(2, languagesByCountryZh.size());
        assertTrue(languagesByCountryZh.contains(Locale.CHINESE));
        assertTrue(languagesByCountryZh.contains(new Locale("zh", "TW")));

        // Test invalid input
        List<Locale> languagesByCountryInvalid = LocaleUtils.languagesByCountry("XX");
        assertTrue(languagesByCountryInvalid.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty input
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertTrue(countries.isEmpty());

        // Test valid inputs
        List<Locale> countriesByLanguageEn = LocaleUtils.countriesByLanguage("EN");
        assertEquals(2, countriesByLanguageEn.size());
        assertTrue(countriesByLanguageEn.contains(Locale.ENGLISH));
        assertTrue(countriesByLanguageEn.contains(new Locale("en", "US")));

        List<Locale> countriesByLanguageFr = LocaleUtils.countriesByLanguage("FR");
        assertEquals(2, countriesByLanguageFr.size());
        assertTrue(countriesByLanguageFr.contains(Locale.FRENCH));
        assertTrue(countriesByLanguageFr.contains(new Locale("fr", "CA")));

        List<Locale> countriesByLanguageZh = LocaleUtils.countriesByLanguage("ZH");
        assertEquals(2, countriesByLanguageZh.size());
        assertTrue(countriesByLanguageZh.contains(Locale.CHINESE));
        assertTrue(countriesByLanguageZh.contains(new Locale("zh", "CN")));

        // Test invalid input
        List<Locale> countriesByLanguageInvalid = LocaleUtils.countriesByLanguage("XX");
        assertTrue(countriesByLanguageInvalid.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        // Test available locales
        List<Locale> locales = LocaleUtils.availableLocaleList();
        for (Locale locale : locales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        // Test invalid locale
        Locale invalidLocale = new Locale("xx", "YY");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }
}