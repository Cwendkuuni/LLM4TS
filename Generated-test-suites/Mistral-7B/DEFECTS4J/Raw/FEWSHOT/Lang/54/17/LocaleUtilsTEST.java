package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR_variants"));
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it_IT"));
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it_IT_variants"));
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xyz"));
        assertNull(LocaleUtils.toLocale("en_GB_variants")); // length < 2
        assertNull(LocaleUtils.toLocale("en_gb")); // lowercase country code
        assertNull(LocaleUtils.toLocale("en_GB_variants_xyz")); // length not correct
        assertNull(LocaleUtils.toLocale("en_GB_variants__")); // invalid separator
        assertNull(LocaleUtils.toLocale("en_GB_variants_xyz_xyz")); // multiple variants
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "variants");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(3, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(Locale.FRENCH, list.get(1));
        assertEquals(Locale.CANADA, list.get(2));

        Locale defaultLocale = Locale.ENGLISH;
        list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(Locale.FRENCH, list.get(1));
        assertEquals(Locale.CANADA, list.get(2));
        assertEquals(defaultLocale, list.get(3));

        list = LocaleUtils.localeLookupList(defaultLocale);
        assertEquals(1, list.size());
        assertEquals(defaultLocale, list.get(0));

        list = LocaleUtils.localeLookupList(null);
        assertEquals(0, list.size());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Locale locale = list.get(i);
            String language = locale.getLanguage();
            String country = locale.getCountry();
            if (language == null || country == null) {
                continue;
            }
            List<Locale> languagesByCountry = LocaleUtils.languagesByCountry(country);
            List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage(language);
            assertTrue(languagesByCountry.contains(locale));
            assertTrue(countriesByLanguage.contains(locale));
        }

        list = LocaleUtils.availableLocaleList();
        Locale locale = new Locale("xyz", "xyz");
        assertFalse(list.contains(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertEquals(7, list.size());
        for (Locale locale : list) {
            assertTrue(locale.getCountry().equals("US"));
        }

        list = LocaleUtils.languagesByCountry("FR");
        assertEquals(2, list.size());
        assertEquals(Locale.FRENCH, list.get(0));
        assertEquals(Locale.FRENCH, list.get(1));

        list = LocaleUtils.languagesByCountry("GB");
        assertEquals(3, list.size());
        for (Locale locale : list) {
            assertTrue(locale.getCountry().equals("GB"));
        }

        list = LocaleUtils.languagesByCountry(null);
        assertEquals(0, list.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertEquals(26, list.size());
        for (Locale locale : list) {
            assertTrue(locale.getLanguage().equals("en"));
        }

        list = LocaleUtils.countriesByLanguage("fr");
        assertEquals(9, list.size());
        for (Locale locale : list) {
            assertTrue(locale.getLanguage().equals("fr"));
        }

        list = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, list.size());
    }
}