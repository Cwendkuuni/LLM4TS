package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class LocaleUtilsTest {
    private static List<Locale> availableLocalés;

    @BeforeClass
    public static void setUpBeforeClass() {
        availableLocalés = LocaleUtils.availableLocaleList();
    }

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
        assertNull(locale.getCountry());

        locale = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());

        locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("xxx", locale.getVariant());

        locale = LocaleUtils.toLocale("fr");
        assertEquals("fr", locale.getLanguage());
        assertNull(locale.getCountry());

        locale = LocaleUtils.toLocale("fr_CA");
        assertEquals("fr", locale.getLanguage());
        assertEquals("CA", locale.getCountry());

        locale = LocaleUtils.toLocale("fr_CA_xxx");
        assertEquals("fr", locale.getLanguage());
        assertEquals("CA", locale.getCountry());
        assertEquals("xxx", locale.getVariant());

        locale = LocaleUtils.toLocale("en_GB_xxx_yyy");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("xxx", locale.getVariant());
        assertEquals("yyy", locale.getVariant());

        locale = LocaleUtils.toLocale(null);
        assertNull(locale);

        locale = LocaleUtils.toLocale("xx_xx_xxx");
        assertNull(locale);

        locale = LocaleUtils.toLocale("en_");
        assertNull(locale);

        locale = LocaleUtils.toLocale("en_XX");
        assertEquals("en", locale.getLanguage());
        assertNull(locale.getCountry());

        locale = LocaleUtils.toLocale("en_XX_XX");
        assertEquals("en", locale.getLanguage());
        assertNull(locale.getCountry());
        assertNull(locale.getVariant());

        locale = LocaleUtils.toLocale("en_XX_XX_XX");
        assertEquals("en", locale.getLanguage());
        assertNull(locale.getCountry());
        assertNull(locale.getVariant());

        locale = LocaleUtils.toLocale("en_XX_XX_XX_XX");
        assertEquals("en", locale.getLanguage());
        assertNull(locale.getCountry());
        assertNull(locale.getVariant());

        locale = LocaleUtils.toLocale("en_XX_XX_XX_XX_XX");
        assertEquals("en", locale.getLanguage());
        assertNull(locale.getCountry());
        assertNull(locale.getVariant());
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> list = LocaleUtils.localeLookupList(Locale.FRENCH);
        List<Locale> expected = Arrays.asList(Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.ENGLISH);
        assertArrayEquals(expected.toArray(), list.toArray());

        list = LocaleUtils.localeLookupList(Locale.ENGLISH);
        expected = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH, Locale.ENGLISH);
        assertArrayEquals(expected.toArray(), list.toArray());

        list = LocaleUtils.localeLookupList(Locale.ITALIAN);
        expected = Arrays.asList(Locale.ITALIAN, Locale.ITALIAN, Locale.ITALIAN, Locale.ENGLISH);
        assertArrayEquals(expected.toArray(), list.toArray());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SPANISH));

        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH_XX));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.JAPANESE));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        List<Locale> expected = Arrays.asList(Locale.ENGLISH_CA, Locale.FRENCH_CA);
        assertArrayEquals(expected.toArray(), languages.toArray());

        languages = LocaleUtils.languagesByCountry("US");
        expected = Arrays.asList(Locale.ENGLISH_US);
        assertArrayEquals(expected.toArray(), languages.toArray());

        languages = LocaleUtils.languagesByCountry("ES");
        expected = Arrays.asList(Locale.SPANISH);
        assertArrayEquals(expected.toArray(), languages.toArray());

        languages = LocaleUtils.languagesByCountry("JP");
        expected = Arrays.asList(Locale.JAPANESE);
        assertArrayEquals(expected.toArray(), languages.toArray());

        languages = LocaleUtils.languagesByCountry("DE");
        expected = Arrays.asList(Locale.GERMAN);
        assertArrayEquals(expected.toArray(), languages.toArray());

        languages = LocaleUtils.languagesByCountry(null);
        assertArrayEquals(new Locale[0], languages.toArray());

        languages = LocaleUtils.languagesByCountry("");
        assertArrayEquals(new Locale[0], languages.toArray());

        languages = LocaleUtils.languagesByCountry("XX");
        assertArrayEquals(new Locale[0], languages.toArray());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("EN");
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH_CA, Locale.ENGLISH_US, Locale.ENGLISH_GB, Locale.ENGLISH_IE);
        assertArrayEquals(expected.toArray(), countries.toArray());

        countries = LocaleUtils.countriesByLanguage("FR");
        expected = Arrays.asList(Locale.FRENCH, Locale.FRENCH_CA, Locale.FRENCH_BE, Locale.FRENCH_CH, Locale.FRENCH_LU);
        assertArrayEquals(expected.toArray(), countries.toArray());

        countries = LocaleUtils.countriesByLanguage("ES");
        expected = Arrays.asList(Locale.SPANISH, Locale.SPANISH_ES, Locale.SPANISH_MX, Locale.SPANISH_US);
        assertArrayEquals(expected.toArray(), countries.toArray());

        countries = LocaleUtils.countriesByLanguage("DE");
        expected = Arrays.asList(Locale.GERMAN, Locale.GERMAN_AT, Locale.GERMAN_CH, Locale.GERMAN_DE, Locale.GERMAN_LI);
        assertArrayEquals(expected.toArray(), countries.toArray());

        countries = LocaleUtils.countriesByLanguage("IT");
        expected = Arrays.asList(Locale.ITALIAN, Locale.ITALIAN_CH, Locale.ITALIAN_IT);
        assertArrayEquals(expected.toArray(), countries.toArray());

        countries = LocaleUtils.countriesByLanguage("JA");
        expected = Arrays.asList(Locale.JAPANESE, Locale.JAPANESE_JP);
        assertArrayEquals(expected.toArray(), countries.toArray());

        countries = LocaleUtils.countriesByLanguage(null);
        assertArrayEquals(new Locale[0], countries.toArray());

        countries = LocaleUtils.countriesByLanguage("");
        assertArrayEquals(new Locale[0], countries.toArray());

        countries = LocaleUtils.countriesByLanguage("XX");
        assertArrayEquals(new Locale[0], countries.toArray());
    }

    @Test
    public void testAvailableLocaleListAndSet() {
        assertNotNull(LocaleUtils.availableLocaleList());
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());

        assertNotNull(LocaleUtils.availableLocaleSet());
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());

        Locale locale = new Locale("xx", "xx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
        assertFalse(LocaleUtils.availableLocaleList().contains(locale));
        assertFalse(LocaleUtils.availableLocaleSet().contains(locale));

        locale = new Locale("en", null);
        assertFalse(LocaleUtils.isAvailableLocale(locale));
        assertFalse(LocaleUtils.availableLocaleList().contains(locale));
        assertFalse(LocaleUtils.availableLocaleSet().contains(locale));

        locale = new Locale("en_GB", null);
        assertFalse(LocaleUtils.isAvailableLocale(locale));
        assertFalse(LocaleUtils.availableLocaleList().contains(locale));
        assertFalse(LocaleUtils.availableLocaleSet().contains(locale));

        locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
        assertTrue(LocaleUtils.availableLocaleList().contains(locale));
        assertTrue(LocaleUtils.availableLocaleSet().contains(locale));

        locale = new Locale("fr_CA", null);
        assertTrue(LocaleUtils.isAvailableLocale(locale));
        assertTrue(LocaleUtils.availableLocaleList().contains(locale));
        assertTrue(LocaleUtils.availableLocaleSet().contains(locale));

        locale = new Locale("fr_CA", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
        assertTrue(LocaleUtils.availableLocaleList().contains(locale));
        assertTrue(LocaleUtils.availableLocaleSet().contains(locale));

        locale = new Locale("fr_CA", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
        assertFalse(LocaleUtils.availableLocaleList().contains(locale));
        assertFalse(LocaleUtils.availableLocaleSet().contains(locale));

        locale = new Locale("fr_CA", null, "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
        assertFalse(LocaleUtils.availableLocaleList().contains(locale));
        assertFalse(LocaleUtils.availableLocaleSet().contains(locale));
    }

    @Test
    public void testToLocaleWithNull() {
        LocaleUtils localeUtils = new LocaleUtils();
        Locale locale = localeUtils.toLocale(null);
        assertNull(locale);
    }

    @Test
    public void testLocaleLookupListWithNull() {
        LocaleUtils localeUtils = new LocaleUtils();
        List<Locale> localeList = localeUtils.localeLookupList(null);
        assertArrayEquals(new Locale[0], localeList.toArray());
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        LocaleUtils localeUtils = new LocaleUtils();
        List<Locale> localeList = localeUtils.localeLookupList(Locale.ENGLISH, Locale.GERMAN);
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH, Locale.ENGLISH, Locale.GERMAN, Locale.GERMAN, Locale.GERMAN, Locale.GERMAN);
        assertArrayEquals(expected.toArray(), localeList.toArray());
    }

    @Test
    public void testLanguagesByCountryWithNull() {
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        assertArrayEquals(new Locale[0], languages.toArray());
    }

    @Test
    public void testLanguagesByCountryWithEmptyString() {
        List<Locale> languages = LocaleUtils.languagesByCountry("");
        assertArrayEquals(new Locale[0], languages.toArray());
    }

    @Test
    public void testLanguagesByCountryWithCountryCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry("ES");
        List<Locale> expected = Arrays.asList(Locale.SPANISH, Locale.SPANISH_ES, Locale.SPANISH_MX, Locale.SPANISH_US);
        assertArrayEquals(expected.toArray(), languages.toArray());
    }

    @Test
    public void testCountriesByLanguageWithNull() {
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertArrayEquals(new Locale[0], countries.toArray());
    }

    @Test
    public void testCountriesByLanguageWithEmptyString() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("");
        assertArrayEquals(new Locale[0], countries.toArray());
    }

    @Test
    public void testCountriesByLanguageWithLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("EN");
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH_CA, Locale.ENGLISH_US, Locale.ENGLISH_GB, Locale.ENGLISH_IE);
        assertArrayEquals(expected.toArray(), countries.toArray());
    }
}