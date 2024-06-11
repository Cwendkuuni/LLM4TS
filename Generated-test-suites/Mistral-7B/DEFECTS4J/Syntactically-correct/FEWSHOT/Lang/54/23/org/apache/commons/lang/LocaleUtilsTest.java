package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test valid locales
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "en_GB"), LocaleUtils.toLocale("en_GB_en_GB"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
        assertEquals(new Locale("fr", "CA", "fr_CA_CA"), LocaleUtils.toLocale("fr_CA_fr_CA_CA"));

        // Test invalid language codes
        try {
            LocaleUtils.toLocale("Aa");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("aa");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("EN");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid country codes
        try {
            LocaleUtils.toLocale("en_aA");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_AA");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_aA");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test missing separator
        try {
            LocaleUtils.toLocale("enA");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GBA");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid country code length
        try {
            LocaleUtils.toLocale("en_AA");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_AAA");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "fr_CA_fr_CA_CA");
        List<Locale> baseLocaleList = LocaleUtils.localeLookupList(baseLocale);

        // Test empty input
        Locale defaultLocale = new Locale("en");
        List<Locale> emptyLocaleList = LocaleUtils.localeLookupList(null);
        assertTrue(Collections.isEmpty(emptyLocaleList));
        assertEquals(defaultLocale, emptyLocaleList.get(0));

        // Test base locale
        List<Locale> baseLocaleListExpected = Arrays.asList(
                baseLocale,
                new Locale("fr", "CA"),
                new Locale("fr")
        );
        assertSame(baseLocaleListExpected, baseLocaleList);

        // Test with default locale
        List<Locale> baseLocaleListWithDefault = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        List<Locale> baseLocaleListExpectedWithDefault = Arrays.asList(
                baseLocale,
                new Locale("fr", "CA"),
                new Locale("fr"),
                defaultLocale
        );
        assertSame(baseLocaleListExpectedWithDefault, baseLocaleListWithDefault);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> emptyLocaleList = LocaleUtils.availableLocaleList();
        assertTrue(Collections.isEmpty(emptyLocaleList));

        // Test available locales
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> availableLocaleListExpected = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN);
        assertSame(availableLocaleListExpected, availableLocaleList);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test empty set
        Set<Locale> emptyLocaleSet = LocaleUtils.availableLocaleSet();
        assertTrue(emptyLocaleSet.isEmpty());

        // Test available locales
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        Set<Locale> availableLocaleSetExpected = new HashSet<>(Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN));
        assertSame(availableLocaleSetExpected, availableLocaleSet);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test empty list
        List<Locale> emptyLocaleList = Collections.emptyList();
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr"), emptyLocaleList));

        // Test available locale
        List<Locale> availableLocaleList = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr"), availableLocaleList));

        // Test invalid locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es"), availableLocaleList));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty country code
        List<Locale> emptyLocaleList = Collections.emptyList();
        List<Locale> emptyLanguageList = LocaleUtils.languagesByCountry(null);
        assertSame(emptyLocaleList, emptyLanguageList);

        // Test valid country code
        List<Locale> validLocaleList = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
        List<Locale> languageList = LocaleUtils.languagesByCountry("CA");
        assertSame(validLocaleList, languageList);

        // Test invalid country code
        List<Locale> emptyLanguageList2 = LocaleUtils.languagesByCountry("ZZ");
        assertTrue(Collections.isEmpty(emptyLanguageList2));
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty language code
        List<Locale> emptyLocaleList = Collections.emptyList();
        List<Locale> emptyCountryList = LocaleUtils.countriesByLanguage(null);
        assertSame(emptyLocaleList, emptyCountryList);

        // Test valid language code
        List<Locale> validLocaleList = Arrays.asList(Locale.ENGLISH, new Locale("en_CA"), new Locale("en_GB"));
        List<Locale> countryList = LocaleUtils.countriesByLanguage("en");
        List<Locale> countryListExpected = Arrays.asList(Locale.CANADA, Locale.UK);
        assertSame(countryListExpected, countryList);

        // Test invalid language code
        List<Locale> emptyCountryList2 = LocaleUtils.countriesByLanguage("ZZ");
        assertTrue(Collections.isEmpty(emptyCountryList2));
    }
}