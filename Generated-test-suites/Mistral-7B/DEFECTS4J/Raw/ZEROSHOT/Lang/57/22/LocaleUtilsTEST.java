package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid input.
     */
    @Test
    public void testToLocaleValidInput() {
        // Test invalid input
        assertNull(LocaleUtils.toLocale(null));

        // Test language only
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("fr"), LocaleUtils.toLocale("fr"));

        // Test language and country
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("fr", "FR"), LocaleUtils.toLocale("fr_FR"));

        // Test language, country, and variant
        assertEquals(new Locale("en", "GB", "EN"), LocaleUtils.toLocale("en_GB_EN"));
        assertEquals(new Locale("fr", "FR", "FR"), LocaleUtils.toLocale("fr_FR_FR"));
    }

    /**
     * Test localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupListValidInput() {
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.FRENCH);
        List<Locale> expectedLocaleList = Arrays.asList(Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.ENGLISH);

        assertEquals(expectedLocaleList, localeList);
    }

    /**
     * Test localeLookupList method with null input.
     */
    @Test
    public void testLocaleLookupListNullInput() {
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
        assertTrue(Collections.isEmpty(localeList));
    }

    /**
     * Test localeLookupList method with valid locale and defaultLocale input.
     */
    @Test
    public void testLocaleLookupListValidInputWithDefaultLocale() {
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        List<Locale> expectedLocaleList = Arrays.asList(Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.ENGLISH);

        assertEquals(expectedLocaleList, localeList);
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        List<Locale> expectedAvailableLocales = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.SPANISH, Locale.PORTUGUESE);

        assertEquals(expectedAvailableLocales, availableLocales);
    }

    /**
     * Test availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();

        assertTrue(availableLocales.contains(Locale.ENGLISH));
        assertTrue(availableLocales.contains(Locale.FRENCH));
        assertTrue(availableLocales.contains(Locale.GERMAN));
        assertTrue(availableLocales.contains(Locale.ITALIAN));
        assertTrue(availableLocales.contains(Locale.SPANISH));
        assertTrue(availableLocales.contains(Locale.PORTUGUESE));

        assertFalse(availableLocales.contains(null));
    }

    /**
     * Test isAvailableLocale method with available locale.
     */
    @Test
    public void testIsAvailableLocaleAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    /**
     * Test isAvailableLocale method with invalid locale.
     */
    @Test
    public void testIsAvailableLocaleInvalidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid", "invalid")));
    }

    /**
     * Test languagesByCountry method with valid input.
     */
    @Test
    public void testLanguagesByCountryValidInput() {
        List<Locale> languages = LocaleUtils.languagesByCountry("GB");
        List<Locale> expectedLanguages = Arrays.asList(Locale.ENGLISH, Locale.SCOTTISH_Gaelic, Locale.WELSH);

        assertEquals(expectedLanguages, languages);
    }

    /**
     * Test languagesByCountry method with null input.
     */
    @Test
    public void testLanguagesByCountryNullInput() {
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        assertTrue(Collections.isEmpty(languages));
    }

    /**
     * Test countriesByLanguage method with valid input.
     */
    @Test
    public void testCountriesByLanguageValidInput() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedCountries = Arrays.asList(Locale.ENGLISH_AUSTRALIA, Locale.ENGLISH_CANADA, Locale.ENGLISH_IRISH, Locale.ENGLISH_NEW_ZEALAND, Locale.ENGLISH_SOUTH_AFRICA, Locale.ENGLISH_UK, Locale.ENGLISH_US, Locale.ENGLISH_ZA);

        assertEquals(expectedCountries, countries);
    }

    /**
     * Test countriesByLanguage method with null input.
     */
    @Test
    public void testCountriesByLanguageNullInput() {
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertTrue(Collections.isEmpty(countries));
    }
}