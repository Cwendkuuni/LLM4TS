package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        // Test valid inputs
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH_CANADA, LocaleUtils.toLocale("fr_CA"));
        assertEquals(Locale.FRENCH_SUISSE, LocaleUtils.toLocale("fr_CH"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de"));
        assertEquals(Locale.GERMAN_AUSTRIA, LocaleUtils.toLocale("de_AT"));
        assertEquals(Locale.GERMAN_SWITZERLAND, LocaleUtils.toLocale("de_CH"));

        // Test invalid inputs
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("xx_XX"));
        assertNull(LocaleUtils.toLocale("en_XX"));
        assertNull(LocaleUtils.toLocale("en_XX_XX"));
        assertNull(LocaleUtils.toLocale("en_XXX"));
        assertNull(LocaleUtils.toLocale("en_XXX_XXX"));
        assertNull(LocaleUtils.toLocale("en_XXX_XXX_XXX"));
        assertNull(LocaleUtils.toLocale("en_XXX_XXX_XXX_XXX"));
        assertNull(LocaleUtils.toLocale("en_a"));
        assertNull(LocaleUtils.toLocale("en_A"));
        assertNull(LocaleUtils.toLocale("en_AA"));
        assertNull(LocaleUtils.toLocale("en_AA_aa"));
        assertNull(LocaleUtils.toLocale("en_AA_aa_AA"));
        assertNull(LocaleUtils.toLocale("en_AA_aa_AA_aa"));
        assertNull(LocaleUtils.toLocale("en_AA_aa_AA_aa_aa"));
        assertNull(LocaleUtils.toLocale("en_AA_aa_AA_aa_aa_aa"));
        assertNull(LocaleUtils.toLocale("en_AA_aa_AA_aa_aa_aa_aa"));
        assertNull(LocaleUtils.toLocale("en_AA_aa_AA_aa_aa_aa_aa_aa"));

        assertNull(LocaleUtils.toLocale("en_XX_en"));
        assertNull(LocaleUtils.toLocale("en_en_XX"));
        assertNull(LocaleUtils.toLocale("en_en"));
        assertNull(LocaleUtils.toLocale("en_XX"));
        assertNull(LocaleUtils.toLocale("en"));

        // Test invalid separator
        assertNull(LocaleUtils.toLocale("en_XX-XX"));
        assertNull(LocaleUtils.toLocale("en_XX_XX-XX"));
        assertNull(LocaleUtils.toLocale("en_XX-XX-XX"));
        assertNull(LocaleUtils.toLocale("en_XX_XX-XX-XX"));
        assertNull(LocaleUtils.toLocale("en_XX-XX-XX-XX"));
        assertNull(LocaleUtils.toLocale("en_XX_XX-XX-XX-XX"));
        assertNull(LocaleUtils.toLocale("en_XX_XX-XX-XX-XX-XX"));
        assertNull(LocaleUtils.toLocale("en_XX_XX-XX-XX-XX-XX-XX"));
        assertNull(LocaleUtils.toLocale("en_XX_XX-XX-XX-XX-XX-XX-XX"));
        assertNull(LocaleUtils.toLocale("en_XX_XX-XX-XX-XX-XX-XX-XX-XX"));

        // Test empty country or language
        assertEquals(Locale.ROOT, LocaleUtils.toLocale(""));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale("_"));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale("en_"));

        // Test empty language and country
        assertEquals(Locale.ROOT, LocaleUtils.toLocale(""));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale("_"));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale("_"));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale("_"));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = Arrays.asList(
                Locale.FRENCH_CANADA, Locale.FRENCH, Locale.ROOT);
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.FRENCH_CANADA);
        assertEquals(expected, actual);

        expected = Arrays.asList(
                Locale.FRENCH_SUISSE, Locale.FRENCH, Locale.GERMAN_SWITZERLAND, Locale.GERMAN, Locale.ENGLISH, Locale.ROOT);
        actual = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.GERMAN);
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.JAPANESE, Locale.KOREAN, Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ITALIAN, Locale.SPANISH, Locale.DUTCH, Locale.POLISH, Locale.PORTUGuese, Locale.RUSSIAN, Locale.CZECH, Locale.HUNGARIAN, Locale.SLAVONIAN, Locale.ROMANIAN, Locale.SERBIAN, Locale.UKRAINIAN, Locale.BULGARIAN, Locale.LATVIAN, Locale.ESTONIAN, Locale.LITHUANIAN, Locale.LATIN, Locale.TURKISH, Locale.HEBREW, Locale.ARABIC, Locale.THAI, Locale.VIETNAMESE, Locale.FILIPINO, Locale.INDONESIAN, Locale.MALAY, Locale.SINGAPORE, Locale.TAIWAN, Locale.CHINESE, Locale.TRADITIONAL_CHINESE_HONG_KONG, Locale.TRADITIONAL_CHINESE_TAIWAN, Locale.SIMPLIFIED_CHINESE_SINGAPORE, Locale.SIMPLIFIED_CHINESE_CHINA, Locale.SIMPLIFIED_CHINESE_HONG_KONG, Locale.SIMPLIFIED_CHINESE_TAIWAN, Locale.ENGLISH_AUSTRALIA, Locale.ENGLISH_CANADA, Locale.ENGLISH_INDIA, Locale.ENGLISH_IRELAND, Locale.ENGLISH_NEW_ZEALAND, Locale.ENGLISH_SOUTH_AFRICA, Locale.ENGLISH_UNITED_KINGDOM, Locale.ENGLISH_UNITED_STATES, Locale.ENGLISH_WALES, Locale.ENGLISH_ZA, Locale.ITALIAN_SWITZERLAND, Locale.ITALIAN_SUISSE, Locale.JAPAN, Locale.JAPAN_BR, Locale.KOREAN_KR, Locale.POLISH_PL, Locale.PORTUGUESE_BR, Locale.PORTUGUESE_PT, Locale.RUSSIAN_RU, Locale.RUSSIAN_UA, Locale.SPANISH_ES, Locale.SPANISH_MX, Locale.SPANISH_US, Locale.DUTCH_BE, Locale.DUTCH_NL, Locale.POLISH_PL_PL, Locale.GERMAN_AT, Locale.GERMAN_BE, Locale.GERMAN_CH, Locale.GERMAN_DE, Locale.GERMAN_LI, Locale.GERMAN_LU, Locale.GERMAN_Austria, Locale.GERMAN_Switzerland);
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(Locale.FRENCH_CANADA, Locale.FRENCH, Locale.FRENCH_BELGIUM, Locale.FRENCH_BELGIUM_WALLONIA, Locale.FRENCH_BELGIUM_FLANDERS);
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, actual);

        expected = Arrays.asList(Locale.GERMAN, Locale.GERMAN_AUSTRIA, Locale.GERMAN_SWITZERLAND, Locale.GERMAN_LI, Locale.GERMAN_LU);
        actual = LocaleUtils.languagesByCountry("CH");
        assertEquals(expected, actual);

        List<Locale> empty = Collections.emptyList();
        actual = LocaleUtils.languagesByCountry(null);
        assertEquals(empty, actual);
        actual = LocaleUtils.languagesByCountry("");
        assertEquals(empty, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(Locale.FRENCH_CANADA, Locale.FRENCH, Locale.FRENCH_BELGIUM, Locale.FRENCH_BELGIUM_WALLONIA, Locale.FRENCH_BELGIUM_FLANDERS);
        List<Locale> actual = LocaleUtils.countriesByLanguage("fr");
        assertEquals(expected, actual);

        expected = Arrays.asList(Locale.GERMAN, Locale.GERMAN_AUSTRIA, Locale.GERMAN_SWITZERLAND, Locale.GERMAN_LI, Locale.GERMAN_LU);
        actual = LocaleUtils.countriesByLanguage("de");
        assertEquals(expected, actual);

        List<Locale> empty = Collections.emptyList();
        actual = LocaleUtils.countriesByLanguage(null);
        assertEquals(empty, actual);
        actual = LocaleUtils.countriesByLanguage("");
        assertEquals(empty, actual);
    }
}