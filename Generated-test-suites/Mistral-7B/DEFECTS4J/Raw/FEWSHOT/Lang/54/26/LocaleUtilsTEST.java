package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR_xxx"));
        assertNull(LocaleUtils.toLocale(null));
        try {
            LocaleUtils.toLocale("xx_yy_zzz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.FRENCH);
        assertEquals(Locale.FRENCH, localeList.get(0));
        assertEquals(Locale.FRENCH, localeList.get(1));
        assertEquals(Locale.FRENCH, localeList.get(2));
        assertEquals(Locale.ENGLISH, localeList.get(3));

        localeList = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.FRENCH);
        assertEquals(Locale.FRENCH, localeList.get(3));
        assertEquals(Locale.FRENCH, localeList.get(2));
        assertEquals(Locale.FRENCH, localeList.get(1));
        assertEquals(Locale.FRENCH, localeList.get(0));
        assertEquals(Locale.ENGLISH, localeList.get(4));

        localeList = LocaleUtils.localeLookupList(null, Locale.FRENCH);
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertEquals(Locale.ENGLISH, availableLocaleList.get(0));
        assertEquals(Locale.FRENCH, availableLocaleList.get(1));
        // assuming more available locales

        assertEquals(availableLocaleList, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "yy")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("US");
        assertEquals(Locale.ENGLISH, languagesByCountry.get(0));
        // assuming more languages for US

        List<Locale> languagesByCountryFr = LocaleUtils.languagesByCountry("FR");
        assertEquals(Locale.FRENCH, languagesByCountryFr.get(0));
        // assuming more languages for FR

        List<Locale> languagesByCountryNull = LocaleUtils.languagesByCountry(null);
        assertTrue(languagesByCountryNull.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        assertEquals(Locale.US, countriesByLanguage.get(0));
        // assuming more countries for en

        List<Locale> countriesByLanguageFr = LocaleUtils.countriesByLanguage("fr");
        assertEquals(Locale.FRANCE, countriesByLanguageFr.get(0));
        // assuming more countries for fr

        List<Locale> countriesByLanguageNull = LocaleUtils.countriesByLanguage(null);
        assertTrue(countriesByLanguageNull.isEmpty());
    }
}