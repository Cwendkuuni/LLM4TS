package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertNull(LocaleUtils.toLocale(null));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("xx_XX"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_XX"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_XX_xx"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
        assertEquals(new Locale("en"), localeList.get(3));

        locale = new Locale("fr");
        localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", ""), localeList.get(1));
        assertEquals(new Locale("en"), localeList.get(2));

        locale = new Locale("en");
        localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, localeList.get(0));
        assertEquals(Collections.emptyList(), localeList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertEquals(true, localeList.contains(new Locale("en")));
        assertEquals(true, localeList.contains(new Locale("fr")));
        assertEquals(true, localeList.contains(new Locale("es")));
        assertEquals(true, localeList.contains(new Locale("de")));
        assertEquals(false, localeList.contains(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languageList = LocaleUtils.languagesByCountry("CA");
        assertEquals(true, languageList.contains(new Locale("en", "CA")));
        assertEquals(true, languageList.contains(new Locale("fr", "CA")));
        assertEquals(false, languageList.contains(new Locale("es", "CA")));

        languageList = LocaleUtils.languagesByCountry("ES");
        assertEquals(true, languageList.contains(new Locale("es")));
        assertEquals(false, languageList.contains(new Locale("en")));
        assertEquals(false, languageList.contains(new Locale("fr")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countryList = LocaleUtils.countriesByLanguage("en");
        assertEquals(true, countryList.contains(new Locale("CA")));
        assertEquals(true, countryList.contains(new Locale("GB")));
        assertEquals(false, countryList.contains(new Locale("ES")));

        countryList = LocaleUtils.countriesByLanguage("es");
        assertEquals(true, countryList.contains(new Locale("ES")));
        assertEquals(false, countryList.contains(new Locale("CA")));
        assertEquals(false, countryList.contains(new Locale("GB")));
    }
}