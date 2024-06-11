package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertNull(LocaleUtils.toLocale(null));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("InvalidLocaleFormat"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedLocaleList = new ArrayList<>();
        expectedLocaleList.add(locale);
        expectedLocaleList.add(new Locale("fr", "CA"));
        expectedLocaleList.add(new Locale("fr"));
        List<Locale> defaultLocaleList = LocaleUtils.localeLookupList(locale, new Locale("en"));
        List<Locale> expectedDefaultLocaleList = new ArrayList<>();
        expectedDefaultLocaleList.add(locale);
        expectedDefaultLocaleList.add(locale); //locale is added twice in this case
        expectedDefaultLocaleList.add(new Locale("fr", "CA"));
        expectedDefaultLocaleList.add(new Locale("fr"));
        expectedDefaultLocaleList.add(new Locale("en"));

        assertEquals(expectedLocaleList, localeList);
        assertEquals(expectedDefaultLocaleList, defaultLocaleList);
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "FR")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("InvalidLocale", "InvalidCountry")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountryFrance = LocaleUtils.languagesByCountry("FR");
        List<Locale> expectedLanguagesByCountryFrance = new ArrayList<>();
        expectedLanguagesByCountryFrance.add(new Locale("fr", "FR"));
        expectedLanguagesByCountryFrance.add(new Locale("fr", "BE"));
        expectedLanguagesByCountryFrance.add(new Locale("fr", "CH"));
        // Add other locales for France if available

        assertEquals(expectedLanguagesByCountryFrance, languagesByCountryFrance);

        assertNull(LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguageEnglish = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedCountriesByLanguageEnglish = new ArrayList<>();
        expectedCountriesByLanguageEnglish.add(new Locale("en", "US"));
        expectedCountriesByLanguageEnglish.add(new Locale("en", "GB"));
        expectedCountriesByLanguageEnglish.add(new Locale("en", "CA"));
        // Add other locales for English if available

        assertEquals(expectedCountriesByLanguageEnglish, countriesByLanguageEnglish);

        assertNull(LocaleUtils.countriesByLanguage(null));
    }
}