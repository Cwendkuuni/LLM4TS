package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals("en", LocaleUtils.toLocale("en").getLanguage());
        assertEquals("en_GB", LocaleUtils.toLocale("en_GB").toString());
        assertEquals("en_GB_XXX", LocaleUtils.toLocale("en_GB_XXX").toString());

        // Test invalid format
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("XX"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_GB_"));
        assertNull(LocaleUtils.toLocale("en_GB_XX"));
        assertNull(LocaleUtils.toLocale("en_GB_XXX_"));
        assertNull(LocaleUtils.toLocale("en_GB_XXX_XX"));

        // Test upper case language and country
        assertEquals("en", LocaleUtils.toLocale("en").getLanguage());
        assertEquals("EN", LocaleUtils.toLocale("EN").getDisplayLanguage());
        assertEquals("en_GB", LocaleUtils.toLocale("en_GB").toString());
        assertEquals("EN_GB", LocaleUtils.toLocale("EN_GB").getDisplayCountry());

        // Test separator
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("en_GB_en-GB", LocaleUtils.toLocale("en_GB_en-GB").toString());

        // Test empty variant
        assertEquals("en_GB", LocaleUtils.toLocale("en_GB").toString());
        assertEquals("en_GB", LocaleUtils.toLocale("en_GB_").toString());
        assertEquals("en_GB", LocaleUtils.toLocale("en_GB_XX").toString());

        // Test variant with upper case
        assertEquals("en_GB_en-GB", LocaleUtils.toLocale("en_GB_en-GB").toString());
        assertEquals("en_GB_en-GB_XX", LocaleUtils.toLocale("en_GB_en-GB_XX").toString());
        assertEquals("en_GB_en-GB_XX_AA", LocaleUtils.toLocale("en_GB_en-GB_XX_AA").toString());

        // Test empty language and country
        assertNull(LocaleUtils.toLocale(""));
        assertEquals("", LocaleUtils.toLocale("").getLanguage());
        assertEquals("", LocaleUtils.toLocale("").getCountry());
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.US);
        List<Locale> expectedList = Arrays.asList(Locale.US, Locale.ENGLISH, Locale.ROOT);
        assertEquals(expectedList, localeList);

        localeList = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        expectedList = Arrays.asList(Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.ENGLISH);
        assertEquals(expectedList, localeList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> expectedLocaleList = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.RUSSIAN, Locale.SPANISH, Locale.TRADITIONAL_TURKISH);
        assertEquals(expectedLocaleList, availableLocaleList);
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SIMPLIFIED_CHINESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.JAPANESE));

        assertFalse(LocaleUtils.isAvailableLocale(Locale.FRENCH_FRANCE_YYYY));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz", "aa")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        List<Locale> expectedLanguages = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH);
        assertEquals(expectedLanguages, languages);

        languages = LocaleUtils.languagesByCountry("FR");
        expectedLanguages = Arrays.asList(Locale.FRENCH, Locale.FRENCH);
        assertEquals(expectedLanguages, languages);

        languages = LocaleUtils.languagesByCountry("CH");
        expectedLanguages = Collections.emptyList();
        assertEquals(expectedLanguages, languages);

        languages = LocaleUtils.languagesByCountry(null);
        expectedLanguages = Collections.emptyList();
        assertEquals(expectedLanguages, languages);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedCountries = Arrays.asList(Locale.US, Locale.CANADA, Locale.UK, Locale.AU, Locale.NZ, Locale.IE);
        assertEquals(expectedCountries, countries);

        countries = LocaleUtils.countriesByLanguage("fr");
        expectedCountries = Arrays.asList(Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.BELGIUM, Locale.LUXEMBOURG, Locale.SWITZERLAND);
        assertEquals(expectedCountries, countries);

        countries = LocaleUtils.countriesByLanguage("xx");
        expectedCountries = Collections.emptyList();
        assertEquals(expectedCountries, countries);

        countries = LocaleUtils.countriesByLanguage(null);
        expectedCountries = Collections.emptyList();
        assertEquals(expectedCountries, countries);
    }
}