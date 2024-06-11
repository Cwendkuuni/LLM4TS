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
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale(""));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR_var"));
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it_IT"));
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it_IT_var"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE_var"));
        assertEquals(Locale.CHINESE, LocaleUtils.toLocale("zh_CN"));
        assertEquals(Locale.CHINESE, LocaleUtils.toLocale("zh_CN_Hans_CN"));
        assertEquals(Locale.JAPANESE, LocaleUtils.toLocale("ja_JP"));
        assertEquals(Locale.JAPANESE, LocaleUtils.toLocale("ja_JP_JP"));
        assertEquals(Locale.KOREAN, LocaleUtils.toLocale("ko_KR"));
        assertEquals(Locale.KOREAN, LocaleUtils.toLocale("ko_KR_KR"));

        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xy")); // invalid country code
        assertNull(LocaleUtils.toLocale("en_gb_xxx")); // invalid variant
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.FRENCH);
        assertEquals(Locale.FRENCH, localeList.get(0));
        assertEquals(Locale.FRENCH, localeList.get(1));
        assertEquals(Locale.ROOT, localeList.get(2));
        assertEquals(Locale.ENGLISH, localeList.get(3));

        localeList = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.FRENCH);
        assertEquals(Locale.ENGLISH, localeList.get(0));
        assertEquals(Locale.FRENCH, localeList.get(1));
        assertEquals(Locale.FRENCH, localeList.get(2));
        assertEquals(Locale.ROOT, localeList.get(3));

        localeList = LocaleUtils.localeLookupList(Locale.ENGLISH, null);
        assertEquals(Locale.ENGLISH, localeList.get(0));
        assertEquals(Locale.ROOT, localeList.get(1));

        localeList = LocaleUtils.localeLookupList(null, Locale.FRENCH);
        assertTrue(localeList.isEmpty());

        localeList = LocaleUtils.localeLookupList(null, null);
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertEquals(Locale.FRENCH, localeList.get(14)); // index of Locale.FRENCH not guaranteed
        assertEquals(Locale.FRENCH, localeList.get(LocaleUtils.toLocale("fr").hashCode()));

        localeList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(localeList, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertEquals(localeSet.size(), LocaleUtils.availableLocaleList().size());
        assertTrue(localeSet.contains(Locale.FRENCH));
        assertTrue(localeSet.contains(Locale.ENGLISH));

        localeSet = new HashSet<>(LocaleUtils.availableLocaleList());
        assertEquals(localeSet, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ITALIAN_VARIANT));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.JAPANESE_JP));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langList = LocaleUtils.languagesByCountry("US");
        assertEquals(Locale.ENGLISH, langList.get(0));
        assertEquals(Locale.SPANISH, langList.get(1));

        langList = LocaleUtils.languagesByCountry("FR");
        assertEquals(Locale.FRENCH, langList.get(0));

        langList = LocaleUtils.languagesByCountry("DE");
        assertEquals(Locale.GERMAN, langList.get(0));

        langList = LocaleUtils.languagesByCountry(null);
        assertTrue(langList.isEmpty());

        // test empty country
        langList = LocaleUtils.languagesByCountry("");
        assertTrue(langList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countryList = LocaleUtils.countriesByLanguage("en");
        assertEquals(Locale.US, countryList.get(0));
        assertEquals(Locale.GB, countryList.get(1));
        assertEquals(Locale.CA, countryList.get(2));

        countryList = LocaleUtils.countriesByLanguage("fr");
        assertEquals(Locale.FR, countryList.get(0));
        assertEquals(Locale.BE, countryList.get(1));
        assertEquals(Locale.CH, countryList.get(2));

        countryList = LocaleUtils.countriesByLanguage("it");
        assertEquals(Locale.IT, countryList.get(0));
        assertEquals(Locale.CH, countryList.get(1));

        countryList = LocaleUtils.countriesByLanguage(null);
        assertTrue(countryList.isEmpty());

        // test empty language
        countryList = LocaleUtils.countriesByLanguage("");
        assertTrue(countryList.isEmpty());
    }
}