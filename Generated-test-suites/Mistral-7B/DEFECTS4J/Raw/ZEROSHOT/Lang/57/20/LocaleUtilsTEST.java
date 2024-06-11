package org.apache.commons.lang.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.LocaleUtils;

public class LocaleUtilsTest {

    @org.junit.Test
    public void testToLocale() {
        // Test valid locales
        assertEquals(Locale.US, LocaleUtils.toLocale("en"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, Locale.FRENCH);
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));

        // Test invalid locales
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("en_XX"));
        assertNull(LocaleUtils.toLocale("en_GB_XX"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_XXX"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_XX_XX"));
    }

    @org.junit.Test
    public void testLocaleLookupList() {
        // Test empty list
        List<Locale> emptyList = LocaleUtils.localeLookupList(null);
        assertTrue(Collections.isEmpty(emptyList));

        // Test simple locale
        List<Locale> list1 = LocaleUtils.localeLookupList(Locale.US);
        List<Locale> expected1 = Arrays.asList(Locale.US, Locale.US, Locale.ENGLISH);
        assertArrayEquals(expected1.stream().mapToInt(l -> l.hashCode()).toArray(), list1.stream().mapToInt(l -> l.hashCode()).toArray());

        // Test locale with variant
        List<Locale> list2 = LocaleUtils.localeLookupList(Locale.US, Locale.ENGLISH);
        List<Locale> expected2 = Arrays.asList(Locale.US, Locale.US, Locale.ENGLISH, Locale.ENGLISH);
        assertArrayEquals(expected2.stream().mapToInt(l -> l.hashCode()).toArray(), list2.stream().mapToInt(l -> l.hashCode()).toArray());

        // Test locale with country
        Locale localeFR_CA = new Locale("fr", "CA");
        List<Locale> list3 = LocaleUtils.localeLookupList(localeFR_CA);
        List<Locale> expected3 = Arrays.asList(localeFR_CA, new Locale("fr", "CA"), new Locale("fr"), Locale.FRENCH);
        assertArrayEquals(expected3.stream().mapToInt(l -> l.hashCode()).toArray(), list3.stream().mapToInt(l -> l.hashCode()).toArray());

        // Test locale with country and variant
        Locale localeFR_CA_XX = new Locale("fr", "CA", "XX");
        List<Locale> list4 = LocaleUtils.localeLookupList(localeFR_CA_XX);
        List<Locale> expected4 = Arrays.asList(localeFR_CA_XX, localeFR_CA, new Locale("fr", "CA"), new Locale("fr"), Locale.FRENCH);
        assertArrayEquals(expected4.stream().mapToInt(l -> l.hashCode()).toArray(), list4.stream().mapToInt(l -> l.hashCode()).toArray());
    }

    @org.junit.Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.CHINESE, Locale.CHINESE_HANS, Locale.CHINESE_TRAADITIONAL, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.JAPANESE, Locale.KOREAN, Locale.SPANISH, Locale.TRADITIONAL_CHINESE, Locale.TRADITIONAL_CHINESE_HONG_KONG, Locale.TRADITIONAL_CHINESE_TAIWAN);
        assertArrayEquals(expected.stream().mapToInt(l -> l.hashCode()).toArray(), availableLocales.stream().mapToInt(l -> l.hashCode()).toArray());
    }

    @org.junit.Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.CHINESE, Locale.CHINESE_HANS, Locale.CHINESE_TRAADITIONAL, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.JAPANESE, Locale.KOREAN, Locale.SPANISH, Locale.TRADITIONAL_CHINESE, Locale.TRADITIONAL_CHINESE_HONG_KONG, Locale.TRADITIONAL_CHINESE_TAIWAN));
        assertEquals(expected.size(), availableLocales.size());
        for (Locale locale : expected) {
            assertTrue(availableLocales.contains(locale));
        }
    }

    @org.junit.Test
    public void testIsAvailableLocale() {
        List<Locale> locales = Arrays.asList(Locale.CHINESE, Locale.ENGLISH, new Locale("xx", "xx"), new Locale("en", "XX"));
        for (Locale locale : locales) {
            if (locale.equals(Locale.ENGLISH)) {
                assertTrue(LocaleUtils.isAvailableLocale(locale));
            } else {
                assertFalse(LocaleUtils.isAvailableLocale(locale));
            }
        }
    }

    @org.junit.Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountryFR = LocaleUtils.languagesByCountry("FR");
        List<Locale> expected1 = Arrays.asList(Locale.FRENCH, Locale.FRENCH_BE, Locale.FRENCH_BELGIUM, Locale.FRENCH_CANADA, Locale.FRENCH_SWITZERLAND);
        assertArrayEquals(expected1.stream().mapToInt(l -> l.hashCode()).toArray(), languagesByCountryFR.stream().mapToInt(l -> l.hashCode()).toArray());

        List<Locale> languagesByCountryDE = LocaleUtils.languagesByCountry("DE");
        List<Locale> expected2 = Arrays.asList(Locale.GERMAN, Locale.GERMAN_AUSTRIA, Locale.GERMAN_AUSTRIA_CARINTHIAN_SLOVENIA, Locale.GERMAN_AUSTRIA_BURGENLAND, Locale.GERMAN_SWITZERLAND);
        assertArrayEquals(expected2.stream().mapToInt(l -> l.hashCode()).toArray(), languagesByCountryDE.stream().mapToInt(l -> l.hashCode()).toArray());

        List<Locale> languagesByCountryUnknown = LocaleUtils.languagesByCountry("XX");
        assertTrue(Collections.isEmpty(languagesByCountryUnknown));
    }

    @org.junit.Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguageEN = LocaleUtils.countriesByLanguage("EN");
        List<Locale> expected1 = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH_AU, Locale.ENGLISH_AUSTRALIA, Locale.ENGLISH_BELIZE, Locale.ENGLISH_BF, Locale.ENGLISH_CA, Locale.ENGLISH_IE, Locale.ENGLISH_IN, Locale.ENGLISH_KE, Locale.ENGLISH_NG, Locale.ENGLISH_NZ, Locale.ENGLISH_PK, Locale.ENGLISH_SG, Locale.ENGLISH_TZ, Locale.ENGLISH_US, Locale.ENGLISH_ZA);
        assertArrayEquals(expected1.stream().mapToInt(l -> l.hashCode()).toArray(), countriesByLanguageEN.stream().mapToInt(l -> l.hashCode()).toArray());

        List<Locale> countriesByLanguageFR = LocaleUtils.countriesByLanguage("FR");
        List<Locale> expected2 = Arrays.asList(Locale.FRENCH, Locale.FRENCH_BE, Locale.FRENCH_BELGIUM, Locale.FRENCH_CHAD, Locale.FRENCH_COTE_DIVORCE, Locale.FRENCH_DJIBOUTI, Locale.FRENCH_FRENCH_GUYANA, Locale.FRENCH_GUINEA, Locale.FRENCH_GUINEA_BISAU, Locale.FRENCH_HAIITI, Locale.FRENCH_ISLE_DE_PAINT, Locale.FRENCH_LIBYA, Locale.FRENCH_LUXEMBOURG, Locale.FRENCH_MONACO, Locale.FRENCH_REUNION, Locale.FRENCH_RWANDA, Locale.FRENCH_SENEGAL, Locale.FRENCH_SWITZERLAND, Locale.FRENCH_SWISS, Locale.FRENCH_SYRIA, Locale.FRENCH_TOGO);
        assertArrayEquals(expected2.stream().mapToInt(l -> l.hashCode()).toArray(), countriesByLanguageFR.stream().mapToInt(l -> l.hashCode()).toArray());

        List<Locale> countriesByLanguageUnknown = LocaleUtils.countriesByLanguage("XX");
        assertTrue(Collections.isEmpty(countriesByLanguageUnknown));
    }
}