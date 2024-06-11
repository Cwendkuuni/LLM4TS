package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.List;
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
        LocaleUtils.toLocale("invalid_locale_format");
    }

    @Test
    public void testToLocaleNullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = new ArrayList<>(Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr")));
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = new ArrayList<>(Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale));
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testLocaleLookupListWithNullLocale() {
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = new ArrayList<>(Collections.singletonList(defaultLocale));
        assertEquals(expectedList, LocaleUtils.localeLookupList(null, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedList, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expectedSet, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        assertEquals(expectedList, LocaleUtils.languagesByCountry("CA"));
    }

    @Test
    public void testLanguagesByCountryWithNullCountryCode() {
        List<Locale> expectedList = new ArrayList<>();
        assertEquals(expectedList, LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"));
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("en"));
    }

    @Test
    public void testCountriesByLanguageWithNullLanguageCode() {
        List<Locale> expectedList = new ArrayList<>();
        assertEquals(expectedList, LocaleUtils.countriesByLanguage(null));
    }
}