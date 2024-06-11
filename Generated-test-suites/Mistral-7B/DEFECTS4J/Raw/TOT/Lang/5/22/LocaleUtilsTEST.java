package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        Locale expectedLocale1 = new Locale("en", "");
        Locale actualLocale1 = LocaleUtils.toLocale("en");
        assertEquals(expectedLocale1, actualLocale1);

        Locale expectedLocale2 = new Locale("en", "GB");
        Locale actualLocale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(expectedLocale2, actualLocale2);

        Locale expectedLocale3 = new Locale("en", "GB", "VARIANT");
        Locale actualLocale3 = LocaleUtils.toLocale("en_GB_VARIANT");
        assertEquals(expectedLocale3, actualLocale3);

        // Test invalid inputs
        try {
            Locale actualLocale4 = LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actualLocale5 = LocaleUtils.toLocale("fr_FR_XXX");
            fail("Expected IllegalArgumentException for invalid input format");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test empty strings
        Locale expectedLocale6 = new Locale("", "");
        Locale actualLocale6 = LocaleUtils.toLocale("");
        assertEquals(expectedLocale6, actualLocale6);

        Locale expectedLocale7 = new Locale("", "GB");
        Locale actualLocale7 = LocaleUtils.toLocale("");
        assertEquals(expectedLocale7, actualLocale7);

        Locale expectedLocale8 = new Locale("", "GB", "VARIANT");
        Locale actualLocale8 = LocaleUtils.toLocale("");
        assertEquals(expectedLocale8, actualLocale8);
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid inputs
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocaleList1 = LocaleUtils.localeLookupList(locale);
        List<Locale> actualLocaleList1 = new ArrayList<Locale>(Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr")));
        assertEquals(expectedLocaleList1, actualLocaleList1);

        Locale locale2 = new Locale("en");
        List<Locale> expectedLocaleList2 = LocaleUtils.localeLookupList(locale2);
        List<Locale> actualLocaleList2 = Collections.unmodifiableList(new ArrayList<Locale>(Arrays.asList(locale2, new Locale("en"))));
        assertEquals(expectedLocaleList2, actualLocaleList2);

        // Test invalid inputs
        try {
            List<Locale> actualLocaleList3 = LocaleUtils.localeLookupList(null);
            fail("Expected empty list for null input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            List<Locale> actualLocaleList4 = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), null);
            fail("Expected empty list for null defaultLocale input");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Test available locales
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = new ArrayList<Locale>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test available locales
        List<Locale> locales = Arrays.asList(Locale.getAvailableLocales());
        for (Locale locale : locales) {
            assertTrue("Locale " + locale + " should be available", LocaleUtils.isAvailableLocale(locale));
        }

        // Test invalid locale
        Locale invalidLocale = new Locale("en", "XX");
        assertFalse("Locale " + invalidLocale + " should not be available", LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test valid inputs
        String countryCode = "CA";
        List<Locale> expectedLocaleList1 = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> actualLocaleList1 = new ArrayList<Locale>(Arrays.asList(new Locale("en", countryCode), new Locale("fr", countryCode), new Locale("es", countryCode)));
        assertEquals(expectedLocaleList1, actualLocaleList1);

        // Test empty input
        String emptyCountryCode = "";
        List<Locale> expectedLocaleList2 = Collections.emptyList();
        List<Locale> actualLocaleList2 = LocaleUtils.languagesByCountry(emptyCountryCode);
        assertEquals(expectedLocaleList2, actualLocaleList2);

        // Test invalid input
        String invalidCountryCode = "XX";
        List<Locale> expectedLocaleList3 = Collections.emptyList();
        List<Locale> actualLocaleList3 = LocaleUtils.languagesByCountry(invalidCountryCode);
        assertEquals(expectedLocaleList3, actualLocaleList3);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test valid inputs
        String languageCode = "en";
        List<Locale> expectedLocaleList1 = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> actualLocaleList1 = new ArrayList<Locale>(Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "GB"), new Locale(languageCode, "US")));
        assertEquals(expectedLocaleList1, actualLocaleList1);

        // Test empty input
        String emptyLanguageCode = "";
        List<Locale> expectedLocaleList2 = Collections.emptyList();
        List<Locale> actualLocaleList2 = LocaleUtils.countriesByLanguage(emptyLanguageCode);
        assertEquals(expectedLocaleList2, actualLocaleList2);

        // Test invalid input
        String invalidLanguageCode = "XX";
        List<Locale> expectedLocaleList3 = Collections.emptyList();
        List<Locale> actualLocaleList3 = LocaleUtils.countriesByLanguage(invalidLanguageCode);
        assertEquals(expectedLocaleList3, actualLocaleList3);
    }
}