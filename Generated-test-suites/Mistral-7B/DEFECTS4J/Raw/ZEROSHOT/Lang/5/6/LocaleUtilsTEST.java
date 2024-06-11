package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test class for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Test method for {@link LocaleUtils#toLocale(String)}.
     */
    @Test
    public void testToLocale() {
        // Test empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid locale
        assertNull(LocaleUtils.toLocale("xx"));

        // Test valid locale with language only
        assertEquals("en", LocaleUtils.toLocale("en").getLanguage());

        // Test valid locale with language and country
        assertEquals("en_GB", LocaleUtils.toLocale("en_GB").toString());

        // Test valid locale with language, country and variant
        assertEquals("en_GB_XXX", LocaleUtils.toLocale("en_GB_XXX").toString());
    }

    /**
     * Test method for {@link LocaleUtils#localeLookupList(Locale)}.
     */
    @Test
    public void testLocaleLookupList() {
        Locale locale = LocaleUtils.toLocale("fr_CA_XXX");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);

        assertEquals(4, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertTrue(localeList.get(1).getCountry().isEmpty());
        assertTrue(localeList.get(1).getLanguage().equals("fr"));
        assertTrue(localeList.get(2).getCountry().equals("CA"));
        assertTrue(localeList.get(2).getLanguage().equals("fr"));
        assertTrue(localeList.get(3).getCountry().isEmpty());
        assertTrue(localeList.get(3).getLanguage().equals("en"));
    }

    /**
     * Test method for {@link LocaleUtils#languagesByCountry(String)}.
     */
    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");

        assertEquals(3, languages.size());
        assertEquals("en", languages.get(0).getLanguage());
        assertEquals("fr", languages.get(1).getLanguage());
        assertEquals("es", languages.get(2).getLanguage());
    }

    /**
     * Test method for {@link LocaleUtils#countriesByLanguage(String)}.
     */
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");

        assertEquals(2, countries.size());
        assertEquals("US", countries.get(0).getCountry());
        assertEquals("GB", countries.get(1).getCountry());
    }

    /**
     * Test method for {@link LocaleUtils#availableLocaleList()}.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();

        assertEquals(54, locales.size());
        assertTrue(locales.contains(Locale.US));
        assertTrue(locales.contains(Locale.GERMANY));
        assertTrue(locales.contains(Locale.CHINA));
    }

    /**
     * Test method for {@link LocaleUtils#availableLocaleSet()}.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();

        assertEquals(54, locales.size());
        assertTrue(locales.contains(Locale.US));
        assertTrue(locales.contains(Locale.GERMANY));
        assertTrue(locales.contains(Locale.CHINA));
    }
}