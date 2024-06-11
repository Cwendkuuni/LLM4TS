package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        String[] validInputs = {"en", "en_GB", "en_GB_US", "fr_CA", "fr_CA_FR"};
        for (String input : validInputs) {
            Locale expected = new Locale(input.split("_")[0], input.split("_")[1]);
            Locale actual = LocaleUtils.toLocale(input);
            assertEquals(expected, actual);
        }

        // Test empty input
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid input
        String[] invalidInputs = {"en_GB_xxx", "en_GB_US_xxx", "en_GB_US_en", "en_GB_US_fr", "en_GB_US_fr_FR", "en_GB_.US", "en_GB_US_en_en", "en_GB_US_en_EN", "en_GB_US_en_fr", "en_GB_US_fr_en", "en_GB_US_fr_FR_FR", "en_GB_US_fr_FR_en"};
        for (String input : invalidInputs) {
            try {
                LocaleUtils.toLocale(input);
                fail("Should have thrown IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty input
        List<Locale> expectedEmpty = Arrays.asList(Locale.getDefault());
        List<Locale> actualEmpty = LocaleUtils.localeLookupList(null);
        assertEquals(expectedEmpty, actualEmpty);

        // Test valid input
        Locale locale = new Locale("en", "US");
        List<Locale> expectedValid = Arrays.asList(locale, new Locale("en", "US"), new Locale("en"));
        List<Locale> actualValid = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedValid, actualValid);

        // Test defaultLocale
        List<Locale> expectedWithDefault = Arrays.asList(locale, new Locale("en", "US"), new Locale("en"), Locale.getDefault());
        List<Locale> actualWithDefault = LocaleUtils.localeLookupList(locale, Locale.getDefault());
        assertEquals(expectedWithDefault, actualWithDefault);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test available locale
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test invalid locale
        Locale invalidLocale = new Locale("en", "US_INVALID");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty input
        List<Locale> expectedEmpty = Collections.emptyList();
        List<Locale> actualEmpty = LocaleUtils.languagesByCountry(null);
        assertEquals(expectedEmpty, actualEmpty);

        // Test valid input
        List<Locale> expectedFrCA = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualFrCA = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedFrCA, actualFrCA);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty input
        List<Locale> expectedEmpty = Collections.emptyList();
        List<Locale> actualEmpty = LocaleUtils.countriesByLanguage(null);
        assertEquals(expectedEmpty, actualEmpty);

        // Test valid input
        List<Locale> expectedEn = Arrays.asList(new Locale("EN", "GB"), new Locale("EN"), new Locale("EN", "US"), new Locale("EN", "GB"));
        List<Locale> actualEn = LocaleUtils.countriesByLanguage("en");
        assertEquals(expectedEn, actualEn);
    }
}