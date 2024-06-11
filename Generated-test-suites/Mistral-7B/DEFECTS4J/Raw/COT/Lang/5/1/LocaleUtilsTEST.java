package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;

import org.junit.Test;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    /**
     * Test method for {@link LocaleUtils#toLocale(String)}.
     */
    @Test
    public void testToLocale() {
        // Basic test
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals("en_GB", locale.toLanguageTag());

        // Test null input
        assertNull(LocaleUtils.toLocale(null));

        // Test empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid format
        try {
            LocaleUtils.toLocale("en_GB_xx");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test empty language code
        try {
            LocaleUtils.toLocale("_GB");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test empty country code
        try {
            LocaleUtils.toLocale("en_");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid separator
        try {
            LocaleUtils.toLocale("en_GB_");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test too short
        try {
            LocaleUtils.toLocale("en");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test too long
        try {
            LocaleUtils.toLocale("en_GB_xx_xx");
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test valid but uppercase country code
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertSame(locale, LocaleUtils.toLocale("en_GB"));
        assertSame(locale, LocaleUtils.toLocale("EN_GB"));

        // Test valid but variant code
        Locale locale3 = LocaleUtils.toLocale("en_GB_US");
        assertSame(locale, LocaleUtils.toLocale("en_GB_US"));
        assertSame(locale, LocaleUtils.toLocale("en_GB_us"));
        assertSame(locale, LocaleUtils.toLocale("en_GB_US_variant"));
        assertSame(locale, LocaleUtils.toLocale("en_GB_us_variant"));
        assertSame(locale, LocaleUtils.toLocale("en_GB_US_variant_"));
        assertSame(locale, LocaleUtils.toLocale("en_GB_US_variant__"));

        // Test valid but empty country code
        Locale locale4 = LocaleUtils.toLocale("en_");
        assertSame(locale, LocaleUtils.toLocale("en"));
        assertSame(locale, LocaleUtils.toLocale("en_"));

        // Test valid but empty language code
        Locale locale5 = LocaleUtils.toLocale("_GB");
        assertSame(locale, Locale.GERMAN);
        assertSame(locale, Locale.GERMANY);
        assertSame(locale, Locale.GERMANY_GERMAN);
        assertSame(locale, Locale.GERMANY_de);

        // Test valid but empty separator
        Locale locale6 = LocaleUtils.toLocale("enGB");
        assertSame(locale, LocaleUtils.toLocale("en_GB"));
        assertSame(locale, LocaleUtils.toLocale("en_GB"));
        assertSame(locale, LocaleUtils.toLocale("enGB"));
        assertSame(locale, LocaleUtils.toLocale("en_GB"));

        // Test valid but variant code without separator
        Locale locale7 = LocaleUtils.toLocale("en_US_variant");
        assertSame(locale, LocaleUtils.toLocale("en_US_variant"));
        assertSame(locale, LocaleUtils.toLocale("en_US_variant"));
        assertSame(locale, LocaleUtils.toLocale("enUS_variant"));
        assertSame(locale, LocaleUtils.toLocale("en_US_variant"));
        assertSame(locale, LocaleUtils.toLocale("en_US_variant"));
        assertSame(locale, LocaleUtils.toLocale("enUS_variant"));
        assertSame(locale, LocaleUtils.toLocale("en_US_variant"));
    }

    /**
     * Test method for {@link LocaleUtils#localeLookupList(Locale)}.
     */
    @Test
    public void testLocaleLookupList() {
        Locale locale = Locale.ENGLISH;
        List<Locale> result = LocaleUtils.localeLookupList(locale);

        // Basic test
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertTrue(locale.getLanguage().isEmpty() || locale.getLanguage().equals("en"));
        assertTrue(locale.getCountry().isEmpty() || locale.getCountry().equals(""));
        assertTrue(locale.getVariant().isEmpty() || locale.getVariant().equals(""));
        assertEquals("en_US", result.get(1).toLanguageTag());
        assertEquals("en", result.get(1).getLanguage());
        assertEquals("US", result.get(1).getCountry());
        assertTrue(result.get(1).getVariant().isEmpty() || result.get(1).getVariant().equals(""));
        assertEquals("en_GB", result.get(2).toLanguageTag());
        assertEquals("en", result.get(2).getLanguage());
        assertEquals("GB", result.get(2).getCountry());
        assertTrue(result.get(2).getVariant().isEmpty() || result.get(2).getVariant().equals(""));

        // Test null input
        result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());

        // Test empty locale
        result = LocaleUtils.localeLookupList(Locale.EMPTY);
        assertTrue(result.isEmpty());

        // Test invalid locale
        Locale invalidLocale = new Locale("xx_xx");
        result = LocaleUtils.localeLookupList(invalidLocale);
        assertTrue(result.isEmpty());
    }

    /**
     * Test method for {@link LocaleUtils#languagesByCountry(String)}.
     */
    @Test
    public void testLanguagesByCountry() {
        // Basic test
        List<Locale> result = LocaleUtils.languagesByCountry("GB");
        assertEquals(3, result.size());
        assertEquals("en", result.get(0).getLanguage());
        assertEquals("en_GB", result.get(0).toLanguageTag());
        assertEquals("en", result.get(1).getLanguage());
        assertEquals("en_US", result.get(1).toLanguageTag());
        assertEquals("en", result.get(2).getLanguage());
        assertEquals("en_CA", result.get(2).toLanguageTag());

        // Test empty country code
        result = LocaleUtils.languagesByCountry("");
        assertTrue(result.isEmpty());

        // Test null country code
        result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());

        // Test invalid country code
        result = LocaleUtils.languagesByCountry("XX");
        assertTrue(result.isEmpty());

        // Test country code with only one language
        result = LocaleUtils.languagesByCountry("JP");
        assertEquals(1, result.size());
        assertEquals("ja", result.get(0).getLanguage());
        assertEquals("ja_JP", result.get(0).toLanguageTag());

        // Test country code with multiple languages
        result = LocaleUtils.languagesByCountry("CH");
        assertEquals(2, result.size());
        assertEquals("de", result.get(0).getLanguage());
        assertEquals("de_CH", result.get(0).toLanguageTag());
        assertEquals("fr", result.get(1).getLanguage());
        assertEquals("fr_CH", result.get(1).toLanguageTag());
    }

    /**
     * Test method for {@link LocaleUtils#countriesByLanguage(String)}.
     */
    @Test
    public void testCountriesByLanguage() {
        // Basic test
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(4, result.size());
        assertEquals("US", result.get(0).getCountry());
        assertEquals("US", result.get(0).toLanguageTag().substring(0, 2));
        assertEquals("CA", result.get(1).getCountry());
        assertEquals("CA", result.get(1).toLanguageTag().substring(0, 2));
        assertEquals("GB", result.get(2).getCountry());
        assertEquals("GB", result.get(2).toLanguageTag().substring(0, 2));
        assertEquals("IE", result.get(3).getCountry());
        assertEquals("IE", result.get(3).toLanguageTag().substring(0, 2));

        // Test empty language code
        result = LocaleUtils.countriesByLanguage("");
        assertTrue(result.isEmpty());

        // Test null language code
        result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());

        // Test invalid language code
        result = LocaleUtils.countriesByLanguage("XX");
        assertTrue(result.isEmpty());

        // Test language code with only one country
        result = LocaleUtils.countriesByLanguage("zh");
        assertEquals(1, result.size());
        assertEquals("CN", result.get(0).getCountry());
        assertEquals("zh_CN", result.get(0).toLanguageTag());

        // Test language code with multiple countries
        result = LocaleUtils.countriesByLanguage("fr");
        assertEquals(3, result.size());
        assertEquals("BE", result.get(0).getCountry());
        assertEquals("BE", result.get(0).toLanguageTag().substring(0, 2));
        assertEquals("CA", result.get(1).getCountry());
        assertEquals("CA", result.get(1).toLanguageTag().substring(0, 2));
        assertEquals("CH", result.get(2).getCountry());
        assertEquals("CH", result.get(2).toLanguageTag().substring(0, 2));
    }
}