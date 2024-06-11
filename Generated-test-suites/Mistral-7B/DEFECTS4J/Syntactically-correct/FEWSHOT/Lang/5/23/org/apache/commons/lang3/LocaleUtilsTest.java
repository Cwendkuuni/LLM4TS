package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Valid tests
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE"));

        // Invalid tests
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB__");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xxxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xxxx_xxxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xxxx_xxxx_xxxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xxxx_xxxx_xxxx_xxxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> list = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(5, list.size());
        assertEquals(Locale.ENGLISH, list.get(0));
        assertEquals(Locale.US, list.get(1));
        assertEquals(Locale.ENGLISH, list.get(2));
        assertEquals(Locale.ENGLISH, list.get(3));
        assertEquals(Locale.getDefault(), list.get(4));

        list = LocaleUtils.localeLookupList(Locale.FRENCH);
        assertEquals(5, list.size());
        assertEquals(Locale.FRENCH, list.get(0));
        assertEquals(Locale.FRANCE, list.get(1));
        assertEquals(Locale.FRENCH, list.get(2));
        assertSame(Locale.getDefault(), list.get(4));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        List<Locale> list = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.FRENCH);
        assertEquals(6, list.size());
        assertEquals(Locale.ENGLISH, list.get(0));
        assertEquals(Locale.US, list.get(1));
        assertEquals(Locale.ENGLISH, list.get(2));
        assertEquals(Locale.ENGLISH, list.get(3));
        assertEquals(Locale.FRENCH, list.get(4));
        assertSame(Locale.getDefault(), list.get(5));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        List<Locale> referenceList = Arrays.asList(Locale.getAvailableLocales());
        assertArrayEquals(referenceList.toArray(new Locale[0]), list.toArray(new Locale[0]));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        Set<Locale> referenceSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(referenceSet, set);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("US");
        assertEquals(2, langs.size());
        assertEquals(Locale.ENGLISH, langs.get(0));
        assertEquals(Locale.FRENCH, langs.get(1));

        langs = LocaleUtils.languagesByCountry("FR");
        assertEquals(1, langs.size());
        assertEquals(Locale.FRENCH, langs.get(0));

        langs = LocaleUtils.languagesByCountry("ES");
        assertEquals(2, langs.size());
        assertEquals(Locale.SPANISH, langs.get(0));
        assertEquals(Locale.CASTILIAN, langs.get(1));

        langs = LocaleUtils.languagesByCountry(null);
        assertEquals(0, langs.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(3, countries.size());
        assertEquals(Locale.ENGLISH, countries.get(0));
        assertEquals(Locale.US, countries.get(1));
        assertEquals(Locale.GB, countries.get(2));

        countries = LocaleUtils.countriesByLanguage("fr");
        assertEquals(2, countries.size());
        assertEquals(Locale.FRENCH, countries.get(0));
        assertEquals(Locale.FRANCE, countries.get(1));

        countries = LocaleUtils.countriesByLanguage("es");
        assertEquals(2, countries.size());
        assertEquals(Locale.CASTILIAN, countries.get(0));
        assertEquals(Locale.ESPANOL, countries.get(1));

        countries = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, countries.size());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH_SWITZERLAND));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SPANISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));

        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH_UK));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_XX", "GB")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_XX", "US")));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.JAPAN));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.KOREAN));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.TRADITIONAL_CHINESE));
    }
}