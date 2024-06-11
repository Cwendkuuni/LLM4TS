package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_London"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_London_FOO"));

        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_Paris"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_Paris_FOO"));

        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_AT"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_AT_Wien"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_AT_Wien_FOO"));

        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it"));
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it_CH"));
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it_CH_Lugano"));
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it_CH_Lugano_FOO"));

        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("en_XX"));
        assertNull(LocaleUtils.toLocale("en_XX_XX"));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();

        Locale englishUS = LocaleUtils.toLocale("en_US");
        List<Locale> englishUSList = LocaleUtils.localeLookupList(englishUS);
        assertEquals(englishUS, englishUSList.get(0));
        assertEquals(Locale.ENGLISH, englishUSList.get(1));
        assertEquals(Locale.US, englishUSList.get(2));
        assertEquals(Locale.ENGLISH, englishUSList.get(3));

        List<Locale> englishGBList = LocaleUtils.localeLookupList(Locale.ENGLISH, englishUS);
        assertEquals(englishUS, englishGBList.get(0));
        assertEquals(englishUS, englishGBList.get(1));
        assertEquals(Locale.ENGLISH, englishGBList.get(2));
        assertEquals(Locale.US, englishGBList.get(3));
        assertEquals(Locale.ENGLISH, englishGBList.get(4));

        List<Locale> unknownList = LocaleUtils.localeLookupList(Locale.ITALIAN, englishUS);
        assertEquals(englishUS, unknownList.get(0));
        assertEquals(Locale.ENGLISH, unknownList.get(1));
        assertEquals(Locale.ENGLISH, unknownList.get(2));
        assertEquals(Locale.US, unknownList.get(3));
        assertEquals(Locale.ENGLISH, unknownList.get(4));
        assertEquals(Locale.ITALIAN, unknownList.get(5));

        List<Locale> emptyList = LocaleUtils.localeLookupList(null);
        assertFalse(emptyList.isEmpty());
        assertEquals(0, emptyList.size());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> available = LocaleUtils.availableLocaleList();
        List<String> availableStrings = new ArrayList<String>(available.size());
        for (Locale locale : available) {
            availableStrings.add(locale.toString());
        }
        assertTrue(Arrays.equals(LocaleUtils.availableLocaleList().toArray(), available.toArray()));
        assertTrue(Arrays.equals(availableStrings.toArray(), Arrays.asList(LocaleUtils.availableLocaleList().toArray(new String[0])).toArray()));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> englishLocales = LocaleUtils.languagesByCountry("US");
        assertEquals(Locale.ENGLISH, englishLocales.get(0));
        assertEquals(Locale.ENGLISH, englishLocales.get(1));
        assertEquals(Locale.ENGLISH, englishLocales.get(2));

        List<Locale> frenchLocales = LocaleUtils.languagesByCountry("CA");
        assertEquals(Locale.FRENCH, frenchLocales.get(0));
        assertEquals(Locale.FRENCH, frenchLocales.get(1));
        assertEquals(Locale.FRENCH, frenchLocales.get(2));

        List<Locale> germanLocales = LocaleUtils.languagesByCountry("DE");
        assertEquals(Locale.GERMAN, germanLocales.get(0));
        assertEquals(Locale.GERMAN, germanLocales.get(1));
        assertEquals(Locale.GERMAN, germanLocales.get(2));

        List<Locale> italianLocales = LocaleUtils.languagesByCountry("IT");
        assertEquals(Locale.ITALIAN, italianLocales.get(0));
        assertEquals(Locale.ITALIAN, italianLocales.get(1));
        assertEquals(Locale.ITALIAN, italianLocales.get(2));

        List<Locale> emptyList = LocaleUtils.languagesByCountry(null);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> englishLocales = LocaleUtils.countriesByLanguage("en");
        assertEquals(Locale.US, englishLocales.get(0));
        assertEquals(Locale.GB, englishLocales.get(1));
        assertEquals(Locale.CA, englishLocales.get(2));

        List<Locale> frenchLocales = LocaleUtils.countriesByLanguage("fr");
        assertEquals(Locale.CA, frenchLocales.get(0));

        List<Locale> germanLocales = LocaleUtils.countriesByLanguage("de");
        assertEquals(Locale.DE, germanLocales.get(0));

        List<Locale> italianLocales = LocaleUtils.countriesByLanguage("it");
        assertEquals(Locale.IT, italianLocales.get(0));

        List<Locale> emptyList = LocaleUtils.countriesByLanguage(null);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALIAN));

        assertFalse(LocaleUtils.isAvailableLocale(Locale.CHINESE));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.JAPANESE));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.RUSSIAN));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.KOREAN));
    }
}