package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR_var"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE_var"));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("xx_yy"));
        assertNull(LocaleUtils.toLocale("xx_yy_zz"));
        assertNull(LocaleUtils.toLocale("xx_yy_zz_aa"));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, localeList.get(0));
        assertEquals(Locale.ENGLISH, localeList.get(1));
        assertEquals(Locale.ENGLISH, localeList.get(2));
        assertSame(Locale.ENGLISH, localeList.get(3));

        localeList = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        assertEquals(Locale.FRENCH, localeList.get(0));
        assertEquals(Locale.FRENCH, localeList.get(1));
        assertEquals(Locale.FRENCH, localeList.get(2));
        assertEquals(Locale.ENGLISH, localeList.get(3));

        localeList = LocaleUtils.localeLookupList(Locale.GERMAN, Locale.ENGLISH);
        assertEquals(Locale.GERMAN, localeList.get(0));
        assertEquals(Locale.GERMAN, localeList.get(1));
        assertEquals(Locale.GERMAN, localeList.get(2));
        assertEquals(Locale.ENGLISH, localeList.get(3));

        localeList = LocaleUtils.localeLookupList(Locale.ITALIAN, Locale.ENGLISH);
        assertEquals(Locale.ITALIAN, localeList.get(0));
        assertEquals(Locale.ITALIAN, localeList.get(1));
        assertEquals(Locale.ITALIAN, localeList.get(2));
        assertEquals(Locale.ENGLISH, localeList.get(3));

        localeList = LocaleUtils.localeLookupList(Locale.ENGLISH, null);
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertEquals(Collections.size(Locale.getAvailableLocales()), availableLocaleList.size());
        assertNotNull(availableLocaleList);

        List<Locale> testList = new ArrayList<>();
        testList.addAll(availableLocaleList);
        for (Locale locale : testList) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "yy")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountryFr = LocaleUtils.languagesByCountry("FR");
        assertEquals(1, languagesByCountryFr.size());
        assertEquals(Locale.FRENCH, languagesByCountryFr.get(0));

        List<Locale> languagesByCountryUs = LocaleUtils.languagesByCountry("US");
        assertEquals(3, languagesByCountryUs.size());
        assertEquals(Locale.ENGLISH, languagesByCountryUs.get(0));
        assertEquals(Locale.ENGLISH, languagesByCountryUs.get(1));
        assertEquals(Locale.ENGLISH, languagesByCountryUs.get(2));

        List<Locale> languagesByCountryZa = LocaleUtils.languagesByCountry("ZA");
        assertEquals(11, languagesByCountryZa.size());
        // Check some locales
        assertTrue(languagesByCountryZa.contains(new Locale("af", "ZA")));
        assertTrue(languagesByCountryZa.contains(new Locale("en", "ZA")));
        assertTrue(languagesByCountryZa.contains(new Locale("nl", "ZA")));
        assertTrue(languagesByCountryZa.contains(new Locale("xh", "ZA")));
        assertTrue(languagesByCountryZa.contains(new Locale("zul", "ZA")));

        List<Locale> languagesByCountryNull = LocaleUtils.languagesByCountry(null);
        assertTrue(languagesByCountryNull.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguageEn = LocaleUtils.countriesByLanguage("en");
        assertEquals(25, countriesByLanguageEn.size());
        // Check some locales
        assertTrue(countriesByLanguageEn.contains(new Locale("AD", "AD")));
        assertTrue(countriesByLanguageEn.contains(new Locale("AE", "AE")));
        assertTrue(countriesByLanguageEn.contains(new Locale("AG", "AG")));
        // ...

        List<Locale> countriesByLanguageFr = LocaleUtils.countriesByLanguage("fr");
        assertEquals(11, countriesByLanguageFr.size());
        // Check some locales
        assertTrue(countriesByLanguageFr.contains(new Locale("BE", "BE")));
        assertTrue(countriesByLanguageFr.contains(new Locale("CA", "CA")));
        assertTrue(countriesByLanguageFr.contains(new Locale("CH", "CH")));
        // ...

        List<Locale> countriesByLanguageNull = LocaleUtils.countriesByLanguage(null);
        assertTrue(countriesByLanguageNull.isEmpty());
    }

}