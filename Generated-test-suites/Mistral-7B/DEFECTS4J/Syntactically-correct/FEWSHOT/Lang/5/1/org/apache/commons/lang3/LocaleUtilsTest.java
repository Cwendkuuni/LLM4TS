package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("en", LocaleUtils.toLocale("en").toString());
        assertEquals("en_GB", LocaleUtils.toLocale("en_GB").toString());
        assertEquals("fr_FR", LocaleUtils.toLocale("fr_FR").toString());
        assertEquals("es", LocaleUtils.toLocale("es").toString());
        assertEquals("fr", LocaleUtils.toLocale("fr").toString());

        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("es_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("es_ES_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.ENGLISH);
        List<Locale> expectedList = Arrays.asList(Locale.ENGLISH,
                new Locale("en", ""), new Locale("en", "US"), Locale.getDefault());
        assertEquals(expectedList, localeList);

        localeList = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        expectedList = Arrays.asList(Locale.FRENCH, new Locale("fr", ""),
                new Locale("fr", "CA"), new Locale("fr", "FR"), Locale.ENGLISH);
        assertEquals(expectedList, localeList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> expectedAvailableLocaleList = Arrays.asList(Locale.CHINESE,
                Locale.CHINESE_HONG_KONG, Locale.CHINESE_SINGAPORE, Locale.DUTCH,
                Locale.ENGLISH, Locale.ENGLISH_AUSTRALIA, Locale.ENGLISH_CA,
                Locale.ENGLISH_GB, Locale.ENGLISH_IE, Locale.ENGLISH_INDIA,
                Locale.ENGLISH_NZ, Locale.ENGLISH_SG, Locale.ENGLISH_US,
                Locale.FRENCH, Locale.FRENCH_BE, Locale.FRENCH_CA, Locale.FRENCH_CH,
                Locale.GERMAN, Locale.GERMAN_AT, Locale.GERMAN_CH, Locale.GERMAN_DE,
                Locale.GERMAN_LI, Locale.GERMAN_LI_CH, Locale.ITALIAN,
                Locale.ITALIAN_CH, Locale.JAPANESE, Locale.KOREAN,
                Locale.PORTUGUESE, Locale.PORTUGUESE_BR, Locale.RUSSIAN,
                Locale.SPANISH, Locale.SPANISH_ES, Locale.SWEDISH,
                Locale.SWEDISH_FI, Locale.TRADITIONAL_CHINESE,
                Locale.TRADITIONAL_CHINESE_HK, Locale.TRADITIONAL_CHINESE_TW);
        assertEquals(expectedAvailableLocaleList, availableLocaleList);

        // test empty list
        List<Locale> emptyList = LocaleUtils.availableLocaleList();
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));

        assertFalse(LocaleUtils.isAvailableLocale(Locale.ITALIAN_ITALY_XX));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> englishLocales = LocaleUtils.languagesByCountry("US");
        List<Locale> expectedEnglishLocales = Arrays.asList(Locale.ENGLISH,
                Locale.ENGLISH_CA, Locale.ENGLISH_GB, Locale.ENGLISH_IE,
                Locale.ENGLISH_INDIA, Locale.ENGLISH_NZ, Locale.ENGLISH_SG,
                Locale.ENGLISH_US);
        assertEquals(expectedEnglishLocales, englishLocales);

        List<Locale> frenchLocales = LocaleUtils.languagesByCountry("CA");
        List<Locale> expectedFrenchLocales = Arrays.asList(Locale.FRENCH,
                Locale.FRENCH_CA);
        assertEquals(expectedFrenchLocales, frenchLocales);

        List<Locale> emptyList = LocaleUtils.languagesByCountry(null);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> englishLocales = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedEnglishLocales = Arrays.asList(Locale.ENGLISH,
                Locale.ENGLISH_AUSTRALIA, Locale.ENGLISH_CA, Locale.ENGLISH_GB,
                Locale.ENGLISH_IE, Locale.ENGLISH_INDIA, Locale.ENGLISH_NZ,
                Locale.ENGLISH_SG, Locale.ENGLISH_US);
        assertEquals(expectedEnglishLocales, englishLocales);

        List<Locale> spanishLocales = LocaleUtils.countriesByLanguage("es");
        List<Locale> expectedSpanishLocales = Arrays.asList(Locale.ESPANOL,
                Locale.ESPANOL_AR, Locale.ESPANOL_BO, Locale.ESPANOL_CL,
                Locale.ESPANOL_CO, Locale.ESPANOL_CR, Locale.ESPANOL_DO,
                Locale.ESPANOL_EC, Locale.ESPANOL_ES, Locale.ESPANOL_GT,
                Locale.ESPANOL_HN, Locale.ESPANOL_MX, Locale.ESPANOL_NI,
                Locale.ESPANOL_PA, Locale.ESPANOL_PE, Locale.ESPANOL_PR,
                Locale.ESPANOL_PY, Locale.ESPANOL_SV, Locale.ESPANOL_US,
                Locale.ESPANOL_UY, Locale.ESPANOL_VE);
        assertEquals(expectedSpanishLocales, spanishLocales);

        List<Locale> emptyList = LocaleUtils.countriesByLanguage(null);
        assertTrue(emptyList.isEmpty());
    }
}