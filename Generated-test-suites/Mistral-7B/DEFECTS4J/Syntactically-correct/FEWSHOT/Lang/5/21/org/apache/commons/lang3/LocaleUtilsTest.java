package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;

public class LocaleUtilsTest {

    private static LocaleUtils utils;

    @BeforeClass
    public static void setup() {
        utils = new LocaleUtils();
    }

    @Test
    public void testToLocale_EmptyString() {
        assertNull(utils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidFormat() {
        assertNull(utils.toLocale("AB"));
        assertNull(utils.toLocale("en_"));
        assertNull(utils.toLocale("en_GB_"));
        assertNull(utils.toLocale("en_GB_xx"));
        assertNull(utils.toLocale("en_GB_xx_"));
        assertNull(utils.toLocale("en_GB_xx_xx"));
        assertNull(utils.toLocale("en_GB_xx_xx_"));
    }

    @Test
    public void testToLocale_ValidFormat() {
        assertEquals(Locale.ENGLISH, utils.toLocale("en"));
        assertEquals(Locale.ENGLISH, utils.toLocale("en_US"));
        assertEquals(Locale.ENGLISH, utils.toLocale("en_US_POSIX"));
        assertEquals(Locale.ENGLISH, utils.toLocale("en_GB"));
        assertEquals(Locale.ENGLISH, utils.toLocale("en_GB_POSIX"));
        assertEquals(Locale.ENGLISH, utils.toLocale("en_GB_en_GB"));
        assertEquals(Locale.ENGLISH, utils.toLocale("en_GB_en_GB_POSIX"));
        assertEquals(Locale.ENGLISH, utils.toLocale("en_GB_en-GB"));
        assertEquals(Locale.ENGLISH, utils.toLocale("en_GB_en-GB_POSIX"));
        assertEquals(Locale.ENGLISH, utils.toLocale("en_GB_en-GB_en-GB"));
        assertEquals(Locale.ENGLISH, utils.toLocale("en_GB_en-GB_en-GB_POSIX"));
        assertEquals(Locale.ENGLISH, utils.toLocale("en_GB_en-GB_en-GB_en-GB_POSIX"));
    }

    @Test
    public void testLocaleLookupList_SameLocale() {
        List<Locale> list = utils.localeLookupList(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, list.get(0));
        assertEquals(Collections.singletonList(Locale.ENGLISH), list);
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        List<Locale> list = utils.localeLookupList(Locale.ENGLISH, Locale.FRENCH);
        assertEquals(Locale.ENGLISH, list.get(0));
        assertEquals(Locale.ENGLISH, list.get(1));
        assertEquals(Locale.ENGLISH, list.get(2));
        assertEquals(Locale.FRENCH, list.get(3));
        assertEquals(4, list.size());
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> list = utils.localeLookupList(null);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertTrue(list.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertTrue(set.size() > 0);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("US");
        assertTrue(langs.size() > 0);
        assertEquals(Locale.ENGLISH, langs.get(0));
        assertEquals("en_US", langs.get(0).toLocaleString());
        assertEquals("en", langs.get(0).getLanguage());
        assertEquals("US", langs.get(0).getCountry());

        langs = LocaleUtils.languagesByCountry("GB");
        assertEquals(1, langs.size());
        assertEquals(Locale.ENGLISH, langs.get(0));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertTrue(countries.size() > 0);
        assertEquals(Locale.ENGLISH, countries.get(0));
        assertEquals("en_US", countries.get(0).toLocaleString());
        assertEquals("en", countries.get(0).getLanguage());
        assertEquals("US", countries.get(0).getCountry());

        List<Locale> countries2 = LocaleUtils.countriesByLanguage("fr");
        assertEquals(1, countries2.size());
        assertEquals(Locale.FRENCH, countries2.get(0));
    }
}