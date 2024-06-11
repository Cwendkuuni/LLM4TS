package org.apache.commons.lang3;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleNull() {
        Locale result = LocaleUtils.toLocale(null);
        assertNull(result);
    }

    @Test
    public void testToLocaleEmpty() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals(new Locale("", ""), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat_1() {
        LocaleUtils.toLocale("#en_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat_2() {
        LocaleUtils.toLocale("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat_3() {
        LocaleUtils.toLocale("en_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat_4() {
        LocaleUtils.toLocale("__");
    }

    @Test
    public void testToLocaleValidFormat_1() {
        Locale result = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en"), result);
    }

    @Test
    public void testToLocaleValidFormat_2() {
        Locale result = LocaleUtils.toLocale("en_US");
        assertEquals(new Locale("en", "US"), result);
    }

    @Test
    public void testToLocaleValidFormat_3() {
        Locale result = LocaleUtils.toLocale("en_US_1");
        assertEquals(new Locale("en", "US", "1"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat_5() {
        LocaleUtils.toLocale("_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat_6() {
        LocaleUtils.toLocale("_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat_7() {
        LocaleUtils.toLocale("_US_");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US", "1");
        Locale defaultLocale = new Locale("en", "CA");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("en", ""), defaultLocale);
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertTrue(result.contains(Locale.ENGLISH));
        assertTrue(result.contains(Locale.FRENCH));
        // Add more assertions for other available locales
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertTrue(result.contains(Locale.ENGLISH));
        assertTrue(result.contains(Locale.FRENCH));
        // Add more assertions for other available locales
    }

    @Test
    public void testIsAvailableLocale_True() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale_False() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "YY")));
    }

    @Test
    public void testLanguagesByCountry_Null() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testLanguagesByCountry_WithCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        // Add assertions for expected locales
    }

    @Test
    public void testCountriesByLanguage_Null() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCountriesByLanguage_WithLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        // Add assertions for expected locales
    }
}