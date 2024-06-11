package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleEmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLanguage() {
        LocaleUtils.toLocale("Xxx");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountry() {
        LocaleUtils.toLocale("en_Xxx");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidSeparator() {
        LocaleUtils.toLocale("en_GB_xxx");
    }

    @Test
    public void testToLocaleSimpleLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
        assertNull(locale.getCountry());
    }

    @Test
    public void testToLocaleSimpleLocaleWithCountry() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
    }

    @Test
    public void testToLocaleComplexLocale() {
        Locale locale = LocaleUtils.toLocale("en_GB_London");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("London", locale.getVariant());
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> list = LocaleUtils.localeLookupList(Locale.FRENCH);
        assertEquals(3, list.size());
        assertEquals(Locale.FRENCH, list.get(0));
        assertEquals(Locale.FRENCH, list.get(1));
        assertEquals(Locale.ENGLISH, list.get(2));
    }

    @Test
    public void testLocaleLookupListEmpty() {
        List<Locale> list = LocaleUtils.localeLookupList(null);
        assertEquals(0, list.size());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertEquals(Locale.SIMPLIFIED_CHINESE, list.get(0));
        assertEquals(Locale.TRADITIONAL_CHINESE, list.get(1));
        assertEquals(Locale.ENGLISH, list.get(2));
        assertEquals(Locale.FRENCH, list.get(3));
        assertEquals(Locale.GERMAN, list.get(4));
        assertEquals(Locale.ITALIAN, list.get(5));
        assertEquals(Locale.JAPANESE, list.get(6));
        assertEquals(Locale.RUSSIAN, list.get(7));
        assertEquals(Locale.SPANISH, list.get(8));
        // ... more assertions
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertTrue(set.contains(Locale.SIMPLIFIED_CHINESE));
        assertTrue(set.contains(Locale.TRADITIONAL_CHINESE));
        assertTrue(set.contains(Locale.ENGLISH));
        assertTrue(set.contains(Locale.FRENCH));
        assertTrue(set.contains(Locale.GERMAN));
        assertTrue(set.contains(Locale.ITALIAN));
        assertTrue(set.contains(Locale.JAPANESE));
        assertTrue(set.contains(Locale.RUSSIAN));
        assertTrue(set.contains(Locale.SPANISH));
        // ... more assertions
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SIMPLIFIED_CHINESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.TRADITIONAL_CHINESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.JAPANESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.RUSSIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SPANISH));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.GERMANIC));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertEquals(2, list.size());
        assertEquals(Locale.ENGLISH, list.get(0));
        assertEquals(Locale.SPANISH, list.get(1));
    }

    @Test
    public void testLanguagesByCountryEmpty() {
        List<Locale> list = LocaleUtils.languagesByCountry(null);
        assertEquals(0, list.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertEquals(2, list.size());
        assertEquals(Locale.US, list.get(0));
        assertEquals(Locale.GB, list.get(1));
    }

    @Test
    public void testCountriesByLanguageEmpty() {
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, list.size());
    }

}