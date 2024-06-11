package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test single language
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));

        // Test single language with country
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));

        // Test single language with country and variant
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_London"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR_Paris"));

        // Test invalid input: language with lowercase country
        assertNull(LocaleUtils.toLocale("en_gb"));

        // Test invalid input: language with underscore in country
        assertNull(LocaleUtils.toLocale("en_US_CA"));

        // Test invalid input: country with lowercase letters
        assertNull(LocaleUtils.toLocale("en_GB_ca"));

        // Test invalid input: language with underscore in language
        assertNull(LocaleUtils.toLocale("en_US_English"));

        // Test invalid input: country with length > 2
        assertNull(LocaleUtils.toLocale("en_US_California"));

        // Test invalid input: language with length > 2
        assertNull(LocaleUtils.toLocale("en_ENGLISH"));

        // Test invalid input: variant with length > 0
        assertNull(LocaleUtils.toLocale("en_GB__"));

        // Test valid input: language with country and variant
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_London"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "GB", "London");

        // Test empty list
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));

        // Test single locale
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(1, list.size());
        assertSame(locale, list.get(0));

        // Test locale with country but no variant
        locale = new Locale("fr", "CA");
        list = LocaleUtils.localeLookupList(locale);
        assertEquals(2, list.size());
        assertTrue(list.contains(locale));
        assertSame(Locale.FRENCH, list.get(0));
        assertTrue(list.contains(new Locale("fr", "")));
        assertSame(Locale.FRENCH, list.get(1));

        // Test locale with country and variant
        locale = new Locale("fr", "CA", "Paris");
        list = LocaleUtils.localeLookupList(locale);
        assertEquals(3, list.size());
        assertTrue(list.contains(locale));
        assertSame(locale, list.get(0));
        assertTrue(list.contains(new Locale("fr", "")));
        assertSame(Locale.FRENCH, list.get(1));
        assertTrue(list.contains(Locale.ENGLISH));
        assertSame(Locale.ENGLISH, list.get(2));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertEquals(177, list.size());

        // Test empty list
        List<Locale> emptyList = Collections.emptyList();
        assertSame(emptyList, LocaleUtils.availableLocaleList());

        // Test same list
        List<Locale> list2 = Arrays.asList(Locale.getAvailableLocales());
        assertTrue(list.equals(list2));

        // Test different list
        List<Locale> list3 = new ArrayList<>();
        list3.add(new Locale("foo", "bar"));
        assertFalse(list.equals(list3));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty map
        Map<String, List<Locale>> map = (Map<String, List<Locale>>) LocaleUtils.cLanguagesByCountry;
        assertEquals(0, map.size());

        // Test empty list
        List<Locale> emptyList = Collections.emptyList();

        // Test empty country
        assertEquals(emptyList, LocaleUtils.languagesByCountry(null));

        // Test single language
        List<Locale> langs = LocaleUtils.languagesByCountry("US");
        assertEquals(11, langs.size());
        assertFalse(langs.contains(Locale.FRENCH));
        assertTrue(langs.contains(Locale.ENGLISH));

        // Test multiple languages for a country
        langs = LocaleUtils.languagesByCountry("CA");
        assertEquals(11, langs.size());
        assertTrue(langs.contains(Locale.ENGLISH));
        assertTrue(langs.contains(Locale.FRENCH));

        // Test same list
        List<Locale> langs2 = Arrays.asList(Locale.getAvailableLocales());
        Map<String, List<Locale>> map2 = new HashMap<>();
        for (Locale locale : langs2) {
            List<Locale> langs3 = new ArrayList<>();
            langs3.add(locale);
            map2.put(locale.getCountry(), langs3);
        }
        Map<String, List<Locale>> map3 = (Map<String, List<Locale>>) LocaleUtils.cLanguagesByCountry;
        assertTrue(map.equals(map3));
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty map
        Map<String, List<Locale>> map = (Map<String, List<Locale>>) LocaleUtils.cCountriesByLanguage;
        assertEquals(0, map.size());

        // Test empty list
        List<Locale> emptyList = Collections.emptyList();

        // Test empty language
        assertEquals(emptyList, LocaleUtils.countriesByLanguage(null));

        // Test single country
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(24, countries.size());
        assertFalse(countries.contains(Locale.FRENCH));
        assertTrue(countries.contains(Locale.ENGLISH));

        // Test multiple countries for a language
        countries = LocaleUtils.countriesByLanguage("fr");
        assertEquals(23, countries.size());
        assertTrue(countries.contains(Locale.FRENCH));
        assertTrue(countries.contains(Locale.CANADA));

        // Test same list
        List<Locale> countries2 = Arrays.asList(Locale.getAvailableLocales());
        Map<String, List<Locale>> map2 = new HashMap<>();
        for (Locale locale : countries2) {
            List<Locale> countries3 = new ArrayList<>();
            countries3.add(locale);
            map2.put(locale.getLanguage(), countries3);
        }
        Map<String, List<Locale>> map3 = (Map<String, List<Locale>>) LocaleUtils.cCountriesByLanguage;
        assertTrue(map.equals(map3));
    }
}