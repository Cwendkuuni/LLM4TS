package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        String[] validInputs = {
                "en",
                "en_GB",
                "en_GB_London"
        };
        for (String input : validInputs) {
            Locale expected = new Locale(input.split("_")[0], input.split("_")[1]);
            Locale actual = LocaleUtils.toLocale(input);
            assertEquals(expected.getLanguage(), actual.getLanguage());
            assertEquals(expected.getCountry(), actual.getCountry());
            if (input.split("_").length > 2) {
                assertEquals(input.split("_")[2], actual.getVariants()[0]);
            }
        }

        // Test invalid input
        String[] invalidInputs = {
                null,
                "",
                "aa_bb_ccc",
                "en_gb_aaa",
                "en_gb_aaa_aaa"
        };
        for (String input : invalidInputs) {
            try {
                LocaleUtils.toLocale(input);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                // expected
            }
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> expected = Collections.singletonList(defaultLocale);
        List<Locale> actual = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(expected, actual);

        // Test single locale
        Locale locale = new Locale("fr", "CA");
        expected = Collections.singletonList(locale);
        actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);

        // Test multiple locales
        List<Locale> locales = new ArrayList<>();
        locales.add(new Locale("fr", "CA"));
        locales.add(new Locale("fr", "FR"));
        locales.add(new Locale("fr"));
        locales.add(defaultLocale);
        expected = locales;
        actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);

        // Test invalid locale
        try {
            LocaleUtils.localeLookupList(null, defaultLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);

        // Test non-empty list
        List<Locale> locales = Arrays.asList(Locale.getAvailableLocales());
        expected = Collections.unmodifiableList(locales);
        actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test empty set
        Set<Locale> expected = Collections.emptySet();
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        assertEquals(expected, actual);

        // Test non-empty set
        List<Locale> locales = Arrays.asList(Locale.getAvailableLocales());
        expected = Collections.unmodifiableSet(new HashSet<>(locales));
        actual = LocaleUtils.availableLocaleSet();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> locales = new ArrayList<>();
        locales.add(new Locale("fr", "CA"));
        locales.add(new Locale("fr", "FR"));
        locales.add(new Locale("fr"));
        locales.add(new Locale("en", "US"));

        for (Locale locale : locales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        List<String> invalidLocales = Arrays.asList("", "aa_bb_ccc", "en_gb_aaa", "en_gb_aaa_aaa");
        for (String localeString : invalidLocales) {
            try {
                Locale locale = LocaleUtils.toLocale(localeString);
                assertFalse(LocaleUtils.isAvailableLocale(locale));
            } catch (IllegalArgumentException e) {
                // expected
            }
        }
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";

        // Test empty list
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        // Test non-empty list
        List<Locale> locales = new ArrayList<>();
        locales.add(new Locale("fr", "CA"));
        locales.add(new Locale("en", "CA"));
        Map<String, List<Locale>> languagesByCountryMap = new HashMap<>();
        languagesByCountryMap.put(countryCode, locales);
        expected = Collections.unmodifiableList(locales);
        actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";

        // Test empty list
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        // Test non-empty list
        List<Locale> locales = new ArrayList<>();
        locales.add(new Locale("fr", "CA"));
        locales.add(new Locale("fr", "FR"));
        Map<String, List<Locale>> countriesByLanguageMap = new HashMap<>();
        countriesByLanguageMap.put(languageCode, locales);
        expected = Collections.unmodifiableList(locales);
        actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }
}