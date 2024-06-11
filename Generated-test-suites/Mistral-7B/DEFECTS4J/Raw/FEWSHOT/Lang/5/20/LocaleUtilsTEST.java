package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertNull(LocaleUtils.toLocale(null));

        // Test invalid input
        try {
            LocaleUtils.toLocale("");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("xx");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_XX");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_GB_XXX_xxx");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_GB_XXX");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_GB_-");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_-GB");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));

        assertEquals(new Locale("fr", "CA", "xxx"), localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");

        assertTrue(languages.contains(new Locale("fr", "CA", "xxx")));
        assertTrue(languages.contains(new Locale("fr", "CA")));
        assertTrue(languages.contains(new Locale("fr")));
        assertTrue(Collections.emptyList().equals(LocaleUtils.languagesByCountry(null)));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");

        assertTrue(countries.contains(new Locale("fr", "CA", "xxx")));
        assertTrue(countries.contains(new Locale("fr", "CA")));
        assertTrue(countries.contains(new Locale("fr")));
        assertTrue(Collections.emptyList().equals(LocaleUtils.countriesByLanguage(null)));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "GB")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "GB", "xxx")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "XX")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_GB_XXX")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_GB_XXX_xxx")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_GB_-")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_-GB")));
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertTrue(locales.contains(new Locale("en")));
        assertTrue(locales.contains(new Locale("en", "GB")));
        assertTrue(locales.contains(new Locale("en", "GB", "xxx")));
        assertFalse(locales.contains(new Locale("en", "XX")));
        assertFalse(locales.contains(new Locale("en_GB_XXX")));
        assertFalse(locales.contains(new Locale("en_GB_XXX_xxx")));
        assertFalse(locales.contains(new Locale("en_GB_-")));
        assertFalse(locales.contains(new Locale("en_-GB")));
        assertFalse(locales.contains(null));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertTrue(locales.contains(new Locale("en")));
        assertTrue(locales.contains(new Locale("en", "GB")));
        assertTrue(locales.contains(new Locale("en", "GB", "xxx")));
        assertFalse(locales.contains(new Locale("en", "XX")));
        assertFalse(locales.contains(new Locale("en_GB_XXX")));
        assertFalse(locales.contains(new Locale("en_GB_XXX_xxx")));
        assertFalse(locales.contains(new Locale("en_GB_-")));
        assertFalse(locales.contains(new Locale("en_-GB")));
        assertFalse(locales.contains(null));
    }
}