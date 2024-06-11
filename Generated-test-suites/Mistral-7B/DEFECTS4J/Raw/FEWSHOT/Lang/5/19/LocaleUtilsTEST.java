package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LocaleUtilsTest {

    private List<Locale> availableLocales;

    @Before
    public void setUp() {
        availableLocales = LocaleUtils.availableLocaleList();
    }

    @After
    public void tearDown() {
        availableLocales = null;
    }

    @Test
    public void testToLocale_EmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidLanguage() {
        assertNull(LocaleUtils.toLocale("XX"));
    }

    @Test
    public void testToLocale_InvalidCountry() {
        assertNull(LocaleUtils.toLocale("en_XX"));
    }

    @Test
    public void testToLocale_InvalidSeparator() {
        assertNull(LocaleUtils.toLocale("en:GB"));
    }

    @Test
    public void testToLocale_EmptyCountry() {
        assertEquals(Locale.EMPTY, LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocale_SimpleLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocale_LocaleWithCountry() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB"));
    }

    @Test
    public void testToLocale_LocaleWithCountryAndVariant() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_var"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageFormat() {
        LocaleUtils.toLocale("en_GB_XXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidCountryFormat() {
        LocaleUtils.toLocale("en__GB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidSeparatorFormat() {
        LocaleUtils.toLocale("en:GB");
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> locales = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(3, locales.size());
        assertEquals(Locale.ENGLISH, locales.get(0));
        assertEquals(Locale.ENGLISH, locales.get(1));
        assertEquals(Locale.getDefault(), locales.get(2));

        locales = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        assertEquals(4, locales.size());
        assertEquals(Locale.FRENCH, locales.get(0));
        assertEquals(Locale.FRENCH, locales.get(1));
        assertEquals(Locale.ENGLISH, locales.get(2));
        assertEquals(Locale.getDefault(), locales.get(3));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH_JP));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertEquals(2, languages.size());
        assertEquals(Locale.ENGLISH, languages.get(0));
        assertEquals(Locale.SPANISH, languages.get(1));

        languages = LocaleUtils.languagesByCountry("FR");
        assertEquals(1, languages.size());
        assertEquals(Locale.FRENCH, languages.get(0));

        languages = LocaleUtils.languagesByCountry("DE");
        assertEquals(3, languages.size());
        assertEquals(Locale.GERMAN, languages.get(0));
        assertEquals(Locale.GERMAN, languages.get(1));
        assertEquals(Locale.GERMAN_AUSTRIA, languages.get(2));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(2, countries.size());
        assertEquals(Locale.ENGLISH, countries.get(0));
        assertEquals(Locale.ENGLISH_CA, countries.get(1));

        countries = LocaleUtils.countriesByLanguage("fr");
        assertEquals(1, countries.size());
        assertEquals(Locale.FRENCH, countries.get(0));

        countries = LocaleUtils.countriesByLanguage("de");
        assertEquals(3, countries.size());
        assertEquals(Locale.GERMAN, countries.get(0));
        assertEquals(Locale.GERMAN_AUSTRIA, countries.get(1));
        assertEquals(Locale.GERMAN_SWITZERLAND, countries.get(2));
    }

}