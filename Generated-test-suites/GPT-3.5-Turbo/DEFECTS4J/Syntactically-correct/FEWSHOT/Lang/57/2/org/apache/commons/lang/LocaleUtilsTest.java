package org.apache.commons.lang;

import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("fr", "CA", "xxx"), LocaleUtils.toLocale("fr_CA_xxx"));
        assertEquals(new Locale("fr", ""), LocaleUtils.toLocale("fr"));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLanguageCode() {
        LocaleUtils.toLocale("EN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountryCode() {
        LocaleUtils.toLocale("en_gb");
    }

    @Test
    public void testLocaleLookupList_nullLocale() {
        List<Locale> expectedList = Arrays.asList(Locale.getDefault());
        assertEquals(expectedList, LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_nullDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), Locale.getDefault());
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, null));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        assertNotNull(LocaleUtils.availableLocaleList());
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry_nullCountryCode() {
        List<Locale> expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testLanguagesByCountry_validCountryCode() {
        List<Locale> expectedList = Arrays.asList(new Locale("en"), new Locale("fr"));
        assertEquals(expectedList, LocaleUtils.languagesByCountry("CA"));
    }

    @Test
    public void testLanguagesByCountry_invalidCountryCode() {
        List<Locale> expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.languagesByCountry("XX"));
    }

    @Test
    public void testCountriesByLanguage_nullLanguageCode() {
        List<Locale> expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.countriesByLanguage(null));
    }

    @Test
    public void testCountriesByLanguage_validLanguageCode() {
        List<Locale> expectedList = Arrays.asList(new Locale("CA"), new Locale("FR"));
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("fr"));
    }

    @Test
    public void testCountriesByLanguage_invalidLanguageCode() {
        List<Locale> expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("xx"));
    }
}