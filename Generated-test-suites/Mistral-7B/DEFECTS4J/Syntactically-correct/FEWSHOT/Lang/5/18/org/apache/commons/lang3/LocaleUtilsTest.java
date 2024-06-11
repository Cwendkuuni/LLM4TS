package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "XXX"), LocaleUtils.toLocale("en_GB_XXX"));

        // Test invalid input
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("EN"));
        assertNull(LocaleUtils.toLocale("en_GB_xxx_"));
        assertNull(LocaleUtils.toLocale("en_GB_xxxX"));
        assertNull(LocaleUtils.toLocale("en_GB_xxx ZZ"));
        assertNull(LocaleUtils.toLocale("en_GB_xxx.1"));
        assertNull(LocaleUtils.toLocale("en_GB_xxx_1"));

        // Test empty country code
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));

        // Test empty language code
        assertEquals(new Locale("", "GB"), LocaleUtils.toLocale(""));
        assertEquals(new Locale("", "GB", "XXX"), LocaleUtils.toLocale(""));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);

        List<Locale> expectedLocaleList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expectedLocaleList, localeList);

        locale = new Locale("fr");
        localeList = LocaleUtils.localeLookupList(locale);
        expectedLocaleList = Arrays.asList(locale, new Locale("fr"), new Locale("en")); // default locale
        assertEquals(expectedLocaleList, localeList);

        locale = null;
        localeList = LocaleUtils.localeLookupList(locale);
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        List<Locale> expectedLocaleList = Arrays.asList(Locale.getAvailableLocales()); // extra safe
        assertEquals(expectedLocaleList, localeList);

        List<Locale> subsetLocaleList = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
        List<Locale> filteredLocaleList = new ArrayList<>();
        for (Locale locale : localeList) {
            if (LocaleUtils.isAvailableLocale(locale) && Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN).contains(locale)) {
                filteredLocaleList.add(locale);
            }
        }
        assertEquals(subsetLocaleList, filteredLocaleList);
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();

        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "FR")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("de")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("de", "DE")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("zh", "CN")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("zh", "TW")));

        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "XX")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr", "XX")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("de", "XX")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zh", "XX")));

        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(""));
        assertFalse(LocaleUtils.isAvailableLocale("EN"));
        assertFalse(LocaleUtils.isAvailableLocale("en_US_XXX"));
        assertFalse(LocaleUtils.isAvailableLocale("en_US_XXX_"));
        assertFalse(LocaleUtils.isAvailableLocale("en_US_XXX_ZZ"));
        assertFalse(LocaleUtils.isAvailableLocale("en_US_XXX.1"));
        assertFalse(LocaleUtils.isAvailableLocale("en_US_XXX_1"));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> englishLocales = LocaleUtils.languagesByCountry("US");
        List<Locale> expectedEnglishLocales = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en"));
        assertEquals(expectedEnglishLocales, englishLocales);

        List<Locale> frenchLocales = LocaleUtils.languagesByCountry("FR");
        List<Locale> expectedFrenchLocales = Arrays.asList(new Locale("fr", "FR"), new Locale("fr", "BE"), new Locale("fr", "CH"), new Locale("fr"), new Locale("fr", "CA"));
        assertEquals(expectedFrenchLocales, frenchLocales);

        List<Locale> germanLocales = LocaleUtils.languagesByCountry("DE");
        List<Locale> expectedGermanLocales = Arrays.asList(new Locale("de", "DE"), new Locale("de", "AT"), new Locale("de", "CH"), new Locale("de"));
        assertEquals(expectedGermanLocales, germanLocales);

        List<Locale> emptyLocaleList = LocaleUtils.languagesByCountry(null);
        assertTrue(emptyLocaleList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> englishLocales = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedEnglishLocales = Arrays.asList(new Locale("US"), new Locale("CA"), new Locale("GB"), new Locale("IN"), new Locale("IE"), new Locale("JM"), new Locale("MD"), new Locale("NZ"), new Locale("TT"), new Locale("ZA"));
        assertEquals(expectedEnglishLocales, englishLocales);

        List<Locale> frenchLocales = LocaleUtils.countriesByLanguage("fr");
        List<Locale> expectedFrenchLocales = Arrays.asList(new Locale("FR"), new Locale("BE"), new Locale("CH"), new Locale("CA"), new Locale("DZ"), new Locale("GF"), new Locale("LC"), new Locale("MA"), new Locale("MC"), new Locale("TD"));
        assertEquals(expectedFrenchLocales, frenchLocales);

        List<Locale> germanLocales = LocaleUtils.countriesByLanguage("de");
        List<Locale> expectedGermanLocales = Arrays.asList(new Locale("AT"), new Locale("BE"), new Locale("CH"), new Locale("DE"), new Locale("LI"), new Locale("LU"));
        assertEquals(expectedGermanLocales, germanLocales);

        List<Locale> emptyLocaleList = LocaleUtils.countriesByLanguage(null);
        assertTrue(emptyLocaleList.isEmpty());
    }
}