package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Valid tests
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it_IT"));
        assertEquals(Locale.SIMPLIFIED_CHINESE, LocaleUtils.toLocale("zh_CN"));
        assertEquals(Locale.SPANISH, LocaleUtils.toLocale("es"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de"));

        // Invalid tests
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("abc");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_GB_xyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with extra separator
        try {
            LocaleUtils.toLocale("en__GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with invalid separator
        try {
            LocaleUtils.toLocale("en_GB_xyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale localeFrCA = Locale.FRENCH;
        Locale localeFr = new Locale("fr");
        Locale localeDefault = Locale.ENGLISH;

        List<Locale> list = LocaleUtils.localeLookupList(localeFrCA, localeDefault);
        assertEquals(4, list.size());
        assertEquals(localeFrCA, list.get(0));
        assertEquals(localeFr, list.get(1));
        assertEquals(Locale.FRENCH, list.get(2));
        assertEquals(localeDefault, list.get(3));

        list = LocaleUtils.localeLookupList(localeFr, localeDefault);
        assertEquals(3, list.size());
        assertEquals(localeFr, list.get(0));
        assertEquals(Locale.FRENCH, list.get(1));
        assertEquals(localeDefault, list.get(2));

        list = LocaleUtils.localeLookupList(null, localeDefault);
        assertEquals(1, list.size());
        assertNull(list.get(0));

        list = LocaleUtils.localeLookupList(localeFrCA, null);
        assertEquals(1, list.size());
        assertSame(localeFrCA, list.get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        List<Locale> expectedAvailableLocales = Arrays.asList(Locale.CHINESE, Locale.CHINESE, Locale.ENGLISH,
                Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.PORTUGUESE,
                Locale.RUSSIAN, Locale.SPANISH, Locale.TRADITIONAL_CHINESE, Locale.TRADITIONAL_CHINESE,
                Locale.TRADITIONAL_CHINESE_HONG_KONG);

        assertEquals(expectedAvailableLocales.size(), availableLocales.size());
        for (Locale locale : expectedAvailableLocales) {
            assertTrue(availableLocales.contains(locale));
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        Set<Locale> expectedAvailableLocales = new HashSet<Locale>(Arrays.asList(Locale.CHINESE, Locale.CHINESE,
                Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN,
                Locale.PORTUGUESE, Locale.RUSSIAN, Locale.SPANISH, Locale.TRADITIONAL_CHINESE,
                Locale.TRADITIONAL_CHINESE, Locale.TRADITIONAL_CHINESE_HONG_KONG));

        assertEquals(expectedAvailableLocales.size(), availableLocales.size());
        assertTrue(availableLocales.containsAll(expectedAvailableLocales));
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Locale localeFr = new Locale("fr");
        Locale localeFrCA = Locale.FRENCH;

        assertTrue(LocaleUtils.isAvailableLocale(localeFr));
        assertTrue(LocaleUtils.isAvailableLocale(localeFrCA));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ITALIAN_ITALY));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "xx")));

        // Test with available locales
        for (Locale locale : availableLocales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(2, LocaleUtils.languagesByCountry("US").size());
        assertEquals(Locale.ENGLISH, LocaleUtils.languagesByCountry("US").get(0));
        assertEquals(Locale.SPANISH, LocaleUtils.languagesByCountry("US").get(1));

        assertEquals(1, LocaleUtils.languagesByCountry("XX").size());
        assertNull(LocaleUtils.languagesByCountry("XX").get(0));
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(1, LocaleUtils.countriesByLanguage("xx").size());
        assertNull(LocaleUtils.countriesByLanguage("xx").get(0));

        assertEquals(2, LocaleUtils.countriesByLanguage("en").size());
        assertEquals(Locale.US, LocaleUtils.countriesByLanguage("en").get(0));
        assertEquals(Locale.GB, LocaleUtils.countriesByLanguage("en").get(1));
    }
}