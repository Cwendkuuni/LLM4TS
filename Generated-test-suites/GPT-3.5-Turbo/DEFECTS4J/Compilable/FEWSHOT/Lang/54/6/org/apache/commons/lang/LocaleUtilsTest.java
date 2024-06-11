package org.apache.commons.lang;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLanguageCode() {
        LocaleUtils.toLocale("ENG_GB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountryCode() {
        LocaleUtils.toLocale("en_GBR");
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("fr", "CA", "xxx"));
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("fr", "CA", "xxx"));
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        expected.add(new Locale("en"));
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocaleWithAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocaleWithNonAvailableLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", "GB"));
        expected.add(new Locale("en", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry("US");
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountryWithInvalidCountryCode() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry("XX");
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("es", "US"));
        expected.add(new Locale("xx", "US"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("US");
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguageWithInvalidLanguageCode() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage("XX");
        assertEquals(expected, actual);
    }
}