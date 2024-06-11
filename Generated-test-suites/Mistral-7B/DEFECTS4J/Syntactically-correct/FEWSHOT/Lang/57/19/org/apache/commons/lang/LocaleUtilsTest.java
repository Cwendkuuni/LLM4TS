package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_XXX"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_XXX_YYY"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_XXX"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_XXX_YYY"));

        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_XXX"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_XXX_YYY"));

        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("an"));
        assertNull(LocaleUtils.toLocale("an_"));
        assertNull(LocaleUtils.toLocale("an_XXX"));
        assertNull(LocaleUtils.toLocale("an_XXX_YYY"));
        assertNull(LocaleUtils.toLocale("an_XXX_YYY_ZZZ"));

        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_XXX_YYY_ZZZ"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_XXX_YYY_ZZZ"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_CA_XXX_YYY_ZZZ"));

        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_XXX_YYY_ZZZ"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_BE_XXX_YYY_ZZZ"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CH_XXX_YYY_ZZZ"));

        assertEquals(Locale.SPANISH, LocaleUtils.toLocale("es"));
        assertEquals(Locale.SPANISH, LocaleUtils.toLocale("es_"));
        assertEquals(Locale.SPANISH, LocaleUtils.toLocale("es_AR"));
        assertEquals(Locale.SPANISH, LocaleUtils.toLocale("es_AR_XXX"));
        assertEquals(Locale.SPANISH, LocaleUtils.toLocale("es_AR_XXX_YYY"));

        assertEquals(Locale.SPANISH, LocaleUtils.toLocale("es_CL"));
        assertEquals(Locale.SPANISH, LocaleUtils.toLocale("es_CL_"));
        assertEquals(Locale.SPANISH, LocaleUtils.toLocale("es_CL_XXX"));
        assertEquals(Locale.SPANISH, LocaleUtils.toLocale("es_CL_XXX_YYY"));

        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it"));
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it_"));
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it_IT"));
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it_IT_XXX"));
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it_IT_XXX_YYY"));

        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE_XXX"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE_XXX_YYY"));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> list = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, list.get(0));
        assertEquals(Locale.ENGLISH, list.get(1));
        assertEquals(Locale.ENGLISH, list.get(2));
        assertEquals(Locale.ENGLISH, list.get(3));

        list = LocaleUtils.localeLookupList(Locale.FRENCH);
        assertEquals(Locale.FRENCH, list.get(0));
        assertEquals(Locale.FRENCH, list.get(1));
        assertEquals(Locale.FRENCH, list.get(2));
        assertEquals(Locale.ENGLISH, list.get(3));

        list = LocaleUtils.localeLookupList(Locale.GERMAN);
        assertEquals(Locale.GERMAN, list.get(0));
        assertEquals(Locale.GERMAN, list.get(1));
        assertEquals(Locale.GERMAN, list.get(2));
        assertEquals(Locale.ENGLISH, list.get(3));

        list = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        assertEquals(Locale.FRENCH, list.get(0));
        assertEquals(Locale.FRENCH, list.get(1));
        assertEquals(Locale.FRENCH, list.get(2));
        assertEquals(Locale.FRENCH, list.get(3));
        assertEquals(Locale.ENGLISH, list.get(4));

        list = LocaleUtils.localeLookupList(Locale.GERMAN, Locale.ENGLISH);
        assertEquals(Locale.GERMAN, list.get(0));
        assertEquals(Locale.GERMAN, list.get(1));
        assertEquals(Locale.GERMAN, list.get(2));
        assertEquals(Locale.ENGLISH, list.get(3));
        assertEquals(Locale.ENGLISH, list.get(4));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertEquals(Locale.SIMPLIFIED_CHINESE, list.get(0));
        assertEquals(Locale.TRADITIONAL_CHINESE, list.get(1));
        // ...

        List<Locale> subList = list.subList(0, 10);
        assertEquals(Locale.SIMPLIFIED_CHINESE, subList.get(0));
        assertEquals(Locale.TRADITIONAL_CHINESE, subList.get(1));
        // ...
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertTrue(set.contains(Locale.SIMPLIFIED_CHINESE));
        assertTrue(set.contains(Locale.TRADITIONAL_CHINESE));
        // ...

        assertTrue(set.contains(Locale.ENGLISH));
        assertTrue(set.contains(Locale.FRENCH));
        assertTrue(set.contains(Locale.GERMAN));
        // ...

        assertEquals(set.size(), 79);
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SIMPLIFIED_CHINESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.TRADITIONAL_CHINESE));
        // ...

        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        // ...

        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH_JP));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.FRENCH_FR_VARIANT));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("CA");
        assertEquals(Locale.ENGLISH, langs.get(0));
        assertEquals(Locale.FRENCH, langs.get(1));

        List<Locale> list = LocaleUtils.availableLocaleList();
        Locale english = null;
        Locale french = null;
        for (Locale locale : list) {
            if (locale.getCountry().equals("CA")) {
                if (locale.getLanguage().equals("en")) {
                    english = locale;
                }
                if (locale.getLanguage().equals("fr")) {
                    french = locale;
                }
            }
        }
        assertEquals(english, langs.get(0));
        assertEquals(french, langs.get(1));

        langs = LocaleUtils.languagesByCountry("US");
        assertEquals(Locale.ENGLISH, langs.get(0));

        langs = LocaleUtils.languagesByCountry("FR");
        assertEquals(Locale.FRENCH, langs.get(0));

        langs = LocaleUtils.languagesByCountry("DE");
        assertEquals(Locale.GERMAN, langs.get(0));

        langs = LocaleUtils.languagesByCountry("GB");
        assertEquals(Locale.ENGLISH, langs.get(0));
        assertEquals(Locale.ENGLISH, langs.get(1));

        langs = LocaleUtils.languagesByCountry("XX");
        assertFalse(langs.isEmpty());
        List<Locale> emptyList = Collections.emptyList();
        assertSame(emptyList, langs);

        langs = LocaleUtils.languagesByCountry(null);
        assertFalse(langs.isEmpty());
        List<Locale> emptyList2 = Collections.emptyList();
        assertSame(emptyList2, langs);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(Locale.ENGLISH, countries.get(0));
        assertEquals(Locale.ENGLISH_CA, countries.get(1));
        assertEquals(Locale.ENGLISH_GB, countries.get(2));
        assertEquals(Locale.ENGLISH_US, countries.get(3));

        List<Locale> list = LocaleUtils.availableLocaleList();
        Locale englishCA = null;
        Locale englishGB = null;
        Locale englishUS = null;
        for (Locale locale : list) {
            if (locale.getLanguage().equals("en") && locale.getCountry().length() > 0) {
                if (locale.getCountry().equals("CA")) {
                    englishCA = locale;
                }
                if (locale.getCountry().equals("GB")) {
                    englishGB = locale;
                }
                if (locale.getCountry().equals("US")) {
                    englishUS = locale;
                }
            }
        }
        assertSame(englishCA, countries.get(1));
        assertSame(englishGB, countries.get(2));
        assertSame(englishUS, countries.get(3));

        countries = LocaleUtils.countriesByLanguage("fr");
        assertEquals(Locale.FRENCH, countries.get(0));
        assertEquals(Locale.FRENCH_CA, countries.get(1));
        assertEquals(Locale.FRENCH_BE, countries.get(2));
        assertEquals(Locale.FRENCH_CH, countries.get(3));

        countries = LocaleUtils.countriesByLanguage("de");
        assertEquals(Locale.GERMAN, countries.get(0));
        assertEquals(Locale.GERMAN_AT, countries.get(1));
        assertEquals(Locale.GERMAN_CH, countries.get(2));

        countries = LocaleUtils.countriesByLanguage("it");
        assertEquals(Locale.ITALIAN, countries.get(0));

        countries = LocaleUtils.countriesByLanguage("es");
        assertEquals(Locale.SPANISH, countries.get(0));
        assertEquals(Locale.SPANISH_AR, countries.get(1));
        assertEquals(Locale.SPANISH_CL, countries.get(2));

        countries = LocaleUtils.countriesByLanguage("zh");
        assertFalse(countries.isEmpty());
        List<Locale> emptyList = Collections.emptyList();
        assertSame(emptyList, countries);

        countries = LocaleUtils.countriesByLanguage(null);
        assertFalse(countries.isEmpty());
        List<Locale> emptyList2 = Collections.emptyList();
        assertSame(emptyList2, countries);
    }
}