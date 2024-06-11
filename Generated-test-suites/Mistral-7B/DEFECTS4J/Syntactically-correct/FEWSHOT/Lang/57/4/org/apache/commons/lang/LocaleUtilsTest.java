package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR_xxx"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE_xxx"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_AT"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_AT_xxx"));
    }

    @Test
    public void testLocaleLookupList() {
        List localeList = LocaleUtils.localeLookupList(Locale.FRENCH);
        assertEquals(Locale.FRENCH, localeList.get(0));
        assertEquals(Locale.FRENCH, localeList.get(1));
        assertEquals(Locale.FRENCH, localeList.get(2));
        assertEquals(Locale.ENGLISH, localeList.get(3));

        localeList = LocaleUtils.localeLookupList(Locale.GERMAN, Locale.ENGLISH);
        assertEquals(Locale.GERMAN, localeList.get(0));
        assertEquals(Locale.GERMAN, localeList.get(1));
        assertEquals(Locale.GERMAN, localeList.get(2));
        assertEquals(Locale.GERMAN, localeList.get(3));
        assertEquals(Locale.ENGLISH, localeList.get(4));

        localeList = LocaleUtils.localeLookupList(Locale.JAPANESE, Locale.ENGLISH);
        assertEquals(Locale.JAPANESE, localeList.get(0));
        assertEquals(Locale.JAPANESE, localeList.get(1));
        assertEquals(Locale.JAPANESE, localeList.get(2));
        assertEquals(Locale.JAPANESE, localeList.get(3));
        assertEquals(Locale.GERMAN, localeList.get(4));
        assertEquals(Locale.ENGLISH, localeList.get(5));
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocaleList = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocaleList);
        assertEquals(187, availableLocaleList.size());

        for (Object o : availableLocaleList) {
            Locale locale = (Locale) o;
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }
    }

    @Test
    public void testLanguagesByCountry() {
        List languages = LocaleUtils.languagesByCountry("US");
        assertEquals(5, languages.size());
        assertTrue(languages.contains(Locale.ENGLISH));
        assertTrue(languages.contains(Locale.ENGLISH));
        assertTrue(languages.contains(Locale.ENGLISH));
        assertTrue(languages.contains(Locale.ENGLISH));
        assertTrue(languages.contains(Locale.ENGLISH));

        languages = LocaleUtils.languagesByCountry("FR");
        assertEquals(2, languages.size());
        assertTrue(languages.contains(Locale.FRENCH));
        assertTrue(languages.contains(Locale.FRENCH));

        languages = LocaleUtils.languagesByCountry("DE");
        assertEquals(3, languages.size());
        assertTrue(languages.contains(Locale.GERMAN));
        assertTrue(languages.contains(Locale.GERMAN));
        assertTrue(languages.contains(Locale.GERMAN));

        languages = LocaleUtils.languagesByCountry("JA");
        assertEquals(2, languages.size());
        assertTrue(languages.contains(Locale.JAPANESE));
        assertTrue(languages.contains(Locale.JAPANESE));
    }

    @Test
    public void testCountriesByLanguage() {
        List countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(27, countries.size());
        assertTrue(countries.contains(Locale.ENGLISH));
        assertTrue(countries.contains(Locale.ENGLISH));
        assertTrue(countries.contains(Locale.ENGLISH));
        assertTrue(countries.contains(Locale.ENGLISH));
        // ...

        countries = LocaleUtils.countriesByLanguage("fr");
        assertEquals(11, countries.size());
        assertTrue(countries.contains(Locale.FRENCH));
        assertTrue(countries.contains(Locale.FRENCH));
        assertTrue(countries.contains(Locale.FRENCH));
        // ...

        countries = LocaleUtils.countriesByLanguage("de");
        assertEquals(21, countries.size());
        assertTrue(countries.contains(Locale.GERMAN));
        assertTrue(countries.contains(Locale.GERMAN));
        assertTrue(countries.contains(Locale.GERMAN));
        // ...

        countries = LocaleUtils.countriesByLanguage("ja");
        assertEquals(7, countries.size());
        assertTrue(countries.contains(Locale.JAPANESE));
        assertTrue(countries.contains(Locale.JAPANESE));
        // ...
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.JAPANESE));

        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH_US));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.FRENCH_FR_VARIANT));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.GERMAN_DE_VARIANT));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.JAPANESE_JP_VARIANT));
    }
}