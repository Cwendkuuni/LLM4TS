package org.apache.commons.lang;

import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat1() {
        LocaleUtils.toLocale("x");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat2() {
        LocaleUtils.toLocale("enGB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat3() {
        LocaleUtils.toLocale("eng_GB");
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("fr", "CA", "xxx"));
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx")));

        List<Locale> expected2 = new ArrayList<>();
        expected2.add(new Locale("fr", "CA", "xxx"));
        expected2.add(new Locale("fr", "CA"));
        expected2.add(new Locale("fr"));
        expected2.add(new Locale("en"));
        assertEquals(expected2, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en"));
        expected.add(new Locale("fr", "CA", "xxx"));
        expected.add(new Locale("ja"));
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>();
        expected.add(new Locale("en"));
        expected.add(new Locale("fr", "CA", "xxx"));
        expected.add(new Locale("ja"));
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("de")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en"));
        expected.add(new Locale("fr"));
        assertEquals(expected, LocaleUtils.languagesByCountry("CA"));

        List<Locale> expected2 = new ArrayList<>();
        expected2.add(new Locale("en"));
        expected2.add(new Locale("es"));
        assertEquals(expected2, LocaleUtils.languagesByCountry("US"));

        assertEquals(Collections.EMPTY_LIST, LocaleUtils.languagesByCountry("XX"));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("CA"));
        expected.add(new Locale("FR"));
        expected.add(new Locale("BE"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("fr"));

        List<Locale> expected2 = new ArrayList<>();
        expected2.add(new Locale("US"));
        expected2.add(new Locale("ES"));
        expected2.add(new Locale("AR"));
        assertEquals(expected2, LocaleUtils.countriesByLanguage("es"));

        assertEquals(Collections.EMPTY_LIST, LocaleUtils.countriesByLanguage("xx"));
    }
}