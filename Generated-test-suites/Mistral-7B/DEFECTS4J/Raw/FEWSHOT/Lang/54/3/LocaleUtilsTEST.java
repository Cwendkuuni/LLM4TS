package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // valid locales
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, Locale.FRENCH);
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_XX"));

        // invalid locales
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("en_US_XX"));
        assertNull(LocaleUtils.toLocale("en_GB_XX"));
        assertNull(LocaleUtils.toLocale("en_GB_xx"));
        assertNull(LocaleUtils.toLocale("en_GB_XXX"));
        assertNull(LocaleUtils.toLocale("en_GB"));
        assertNull(LocaleUtils.toLocale("en"));
        assertNull(LocaleUtils.toLocale("en_US"));
        assertNull(LocaleUtils.toLocale("fr"));
        assertNull(LocaleUtils.toLocale("fr_CA"));
        assertNull(LocaleUtils.toLocale("fr_CA_XX"));
        assertNull(LocaleUtils.toLocale("fr_XX"));
        assertNull(LocaleUtils.toLocale("fr_XX_XX"));
        assertNull(LocaleUtils.toLocale("fr_XX_XXX"));

        // test empty strings
        assertEquals(Locale.EMPTY, LocaleUtils.toLocale(""));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale(""));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale("_"));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale("__"));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale("____"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "GB", "XXX");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("en", "GB"), localeList.get(1));
        assertEquals(new Locale("en"), localeList.get(2));
        assertEquals(new Locale("en_US"), localeList.get(3));

        locale = null;
        localeList = LocaleUtils.localeLookupList(locale, Locale.ENGLISH);
        assertNotNull(localeList);
        assertTrue(localeList.isEmpty());

        locale = new Locale("fr", "CA");
        localeList = LocaleUtils.localeLookupList(locale, null);
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr"), localeList.get(1));
        assertEquals(new Locale("fr_CA"), localeList.get(2));
        assertEquals(new Locale("fr_CA_XX"), localeList.get(3));
        assertEquals(Locale.ENGLISH, localeList.get(4));

        locale = new Locale("en", "GB");
        localeList = LocaleUtils.localeLookupList(locale, new Locale("fr"));
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("en"), localeList.get(1));
        assertEquals(new Locale("en_US"), localeList.get(2));
        assertEquals(new Locale("fr"), localeList.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(availableLocales.size(), 61);

        List<String> availableLocaleStrings = new ArrayList<String>(availableLocales.size());
        for (Locale locale : availableLocales) {
            availableLocaleStrings.add(locale.toString());
        }
        assertTrue(Arrays.asList(Locale.getAvailableLocales()).equals(availableLocaleStrings));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountryFrance = LocaleUtils.languagesByCountry("FR");
        assertEquals(languagesByCountryFrance.size(), 13);
        // check first element
        Locale fr = new Locale("fr", "");
        assertEquals(fr, languagesByCountryFrance.get(0));
        assertEquals("fr", fr.getLanguage());
        assertEquals("", fr.getCountry());

        List<Locale> languagesByCountryUS = LocaleUtils.languagesByCountry("US");
        assertEquals(languagesByCountryUS.size(), 7);
        // check first element
        Locale en = new Locale("en", "");
        assertEquals(en, languagesByCountryUS.get(0));
        assertEquals("en", en.getLanguage());
        assertEquals("", en.getCountry());

        List<Locale> languagesByCountryUnknown = LocaleUtils.languagesByCountry(null);
        assertTrue(languagesByCountryUnknown.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguageEnglish = LocaleUtils.countriesByLanguage("en");
        assertEquals(countriesByLanguageEnglish.size(), 28);
        // check first element
        Locale us = new Locale("en", "US");
        assertEquals(us, countriesByLanguageEnglish.get(0));
        assertEquals("en", us.getLanguage());
        assertEquals("US", us.getCountry());

        List<Locale> countriesByLanguageSpanish = LocaleUtils.countriesByLanguage("es");
        assertEquals(countriesByLanguageSpanish.size(), 21);
        // check first element
        Locale es = new Locale("es", "ES");
        assertEquals(es, countriesByLanguageSpanish.get(0));
        assertEquals("es", es.getLanguage());
        assertEquals("ES", es.getCountry());

        List<Locale> countriesByLanguageUnknown = LocaleUtils.countriesByLanguage(null);
        assertTrue(countriesByLanguageUnknown.isEmpty());
    }

}