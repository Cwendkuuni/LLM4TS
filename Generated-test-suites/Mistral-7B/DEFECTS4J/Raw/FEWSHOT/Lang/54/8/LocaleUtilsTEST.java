package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "XXX"), LocaleUtils.toLocale("en_GB_XXX"));

        // Test invalid inputs
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("aa_ZZ"));
        assertNull(LocaleUtils.toLocale("en_AA"));
        assertNull(LocaleUtils.toLocale("en_GB_XX"));

        // Test invalid separator
        assertNull(LocaleUtils.toLocale("en_GB-XX"));
        assertNull(LocaleUtils.toLocale("en_GB_XX"));

        // Test empty language and country
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("_"));

        // Test invalid language and country
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("xx_"));
        assertNull(LocaleUtils.toLocale("en_XX"));
        assertNull(LocaleUtils.toLocale("en_XX_"));

        // Test invalid length
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_GB_"));
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        List<Locale> list = LocaleUtils.localeLookupList(null);
        assertTrue(list.isEmpty());

        // Test null input
        List<Locale> list2 = LocaleUtils.localeLookupList(null, new Locale("en"));
        assertTrue(list2.isEmpty());

        // Test available locale
        List<Locale> list3 = LocaleUtils.localeLookupList(Locale.US);
        List<Locale> expected = Arrays.asList(Locale.US, Locale.ENGLISH);
        assertEquals(expected, list3);

        // Test unavailable locale
        List<Locale> list4 = LocaleUtils.localeLookupList(new Locale("xx", "XX"));
        assertTrue(list4.isEmpty());

        // Test locale with variant
        List<Locale> list5 = LocaleUtils.localeLookupList(new Locale("en", "GB", "XXX"));
        List<Locale> expected5 = Arrays.asList(new Locale("en", "GB", "XXX"),
                new Locale("en", "GB"), new Locale("en"));
        assertEquals(expected5, list5);

        // Test locale with country and language reversed
        List<Locale> list6 = LocaleUtils.localeLookupList(new Locale("GB", "en"));
        List<Locale> expected6 = Arrays.asList(new Locale("GB", "en"), new Locale("en", "GB"), new Locale("en"));
        assertEquals(expected6, list6);

        // Test default locale
        List<Locale> list7 = LocaleUtils.localeLookupList(new Locale("en"), new Locale("fr"));
        List<Locale> expected7 = Arrays.asList(new Locale("en", "GB"), new Locale("en"), new Locale("fr"));
        assertEquals(expected7, list7);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertTrue(list.isEmpty());

        // Test available locales
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN,
                Locale.JAPANESE, Locale.KOREAN, Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE);
        List<Locale> list2 = LocaleUtils.availableLocaleList();
        assertEquals(expected, list2);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test empty set
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertTrue(set.isEmpty());

        // Test available locales
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN,
                Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.SIMPLIFIED_CHINESE,
                Locale.TRADITIONAL_CHINESE));
        Set<Locale> set2 = LocaleUtils.availableLocaleSet();
        assertEquals(expected, set2);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test empty list
        List<Locale> list = new ArrayList<>();
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH, list));

        // Test available locale
        List<Locale> availableLocales = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN,
                Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.SIMPLIFIED_CHINESE,
                Locale.TRADITIONAL_CHINESE);
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH, availableLocales));

        // Test unavailable locale
        assertFalse(LocaleUtils.isAvailableLocale(Locale.SPANISH, availableLocales));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        List<Locale> list = LocaleUtils.languagesByCountry(null);
        assertTrue(list.isEmpty());

        // Test available languages for US
        List<Locale> expected = Arrays.asList(Locale.US, Locale.ENGLISH);
        List<Locale> list2 = LocaleUtils.languagesByCountry("US");
        assertEquals(expected, list2);

        // Test available languages for CA
        List<Locale> expected2 = Arrays.asList(Locale.CANADA, Locale.ENGLISH, Locale.FRENCH);
        List<Locale> list3 = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected2, list3);

        // Test unavailable country
        List<Locale> list4 = LocaleUtils.languagesByCountry("ZZ");
        assertTrue(list4.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty list
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        assertTrue(list.isEmpty());

        // Test available countries for EN
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.US, Locale.CANADA, Locale.AUSTRALIA,
                Locale.IRISH, Locale.NEW_ZEALAND, Locale.SOUTH_AFRICA, Locale.ZA, Locale.UK, Locale.GB);
        List<Locale> list2 = LocaleUtils.countriesByLanguage("EN");
        assertEquals(expected, list2);

        // Test unavailable language
        List<Locale> list3 = LocaleUtils.countriesByLanguage("ZZ");
        assertTrue(list3.isEmpty());
    }
}