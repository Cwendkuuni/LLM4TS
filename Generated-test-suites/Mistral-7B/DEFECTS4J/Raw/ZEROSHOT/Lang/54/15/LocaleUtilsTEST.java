package org.apache.commons.lang.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.apache.commons.lang.LocaleUtils;

public class LocaleUtilsTest {

    @org.junit.Test
    public void testToLocale() {
        String[] testCases = new String[]{"en", "en_GB", "en_GB_XXX", "fr_FR"};
        Locale[] expectedLocales = new Locale[]{new Locale("en", ""), new Locale("en", "GB"), new Locale("en", "GB", "XXX"), new Locale("fr", "FR")};

        for (int i = 0; i < testCases.length; i++) {
            Locale actualLocale = LocaleUtils.toLocale(testCases[i]);
            assertEquals(expectedLocales[i], actualLocale);
        }
    }

    @org.junit.Test
    public void testInvalidLocaleFormat() {
        String[] testCases = new String[]{"en_GB_XXX_ invalid separator", "en_GB_XXX_ invalid language", "en_GB_XXX_ invalid country"};

        for (String testCase : testCases) {
            try {
                LocaleUtils.toLocale(testCase);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                //Expected
            }
        }
    }

    @org.junit.Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("en", "GB", "XXX");
        Locale defaultLocale = new Locale("fr", "CA");

        List<Locale> localeList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);

        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("en", "GB", "XXX"),
                new Locale("en", "GB"),
                new Locale("en"),
                new Locale("fr", "CA")
        );

        assertEquals(expectedLocaleList, localeList);
    }

    @org.junit.Test
    public void testAvailableLocaleList() {
        List<Locale> actualLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> expectedLocaleList = Arrays.asList(Locale.getAvailableLocales());

        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @org.junit.Test
    public void testAvailableLocaleSet() {
        Set<Locale> actualLocaleSet = LocaleUtils.availableLocaleSet();
        Set<Locale> expectedLocaleSet = new HashSet<>(Locale.getAvailableLocales());

        assertEquals(expectedLocaleSet, actualLocaleSet);
    }

    @org.junit.Test
    public void testIsAvailableLocale() {
        List<Locale> locales = Arrays.asList(Locale.CHINESE, Locale.ENGLISH, Locale.FRENCH);

        for (Locale locale : locales) {
            boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
            if (locale.equals(Locale.CHINESE)) {
                assertTrue(isAvailable);
            } else {
                assertFalse(isAvailable);
            }
        }
    }

    @org.junit.Test
    public void testLanguagesByCountry() {
        String countryCode = "US";

        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("en", "US"));

        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @org.junit.Test
    public void testCountriesByLanguage() {
        String languageCode = "en";

        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("US"), new Locale("GB"), new Locale("CA"));

        assertEquals(expectedLocaleList, actualLocaleList);
    }
}