package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr", "FR"), LocaleUtils.toLocale("fr_FR"));
        assertEquals(new Locale("es"), LocaleUtils.toLocale("es"));

        // Test invalid inputs
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_US_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_US_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_US_xx_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test empty language or country
        assertEquals(new Locale(""), LocaleUtils.toLocale(""));
        assertEquals(new Locale("es"), LocaleUtils.toLocale("es_"));
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en_"));
    }

    @Test
    public void testLocaleLookupList() {
        // Test simple case
        List<Locale> list = LocaleUtils.localeLookupList(new Locale("fr", "CA", "XX"));
        assertEquals(Arrays.asList(new Locale("fr", "CA", "XX"), new Locale("fr", "CA"), new Locale("fr")), list);

        // Test default locale
        List<Locale> listDefault = LocaleUtils.localeLookupList(new Locale("fr", "CA", "XX"), new Locale("en"));
        assertEquals(Arrays.asList(new Locale("fr", "CA", "XX"), new Locacle("fr", "CA"), new Locale("fr"), new Locale("en")), listDefault);

        // Test null input
        List<Locale> listNull = LocaleUtils.localeLookupList(null);
        assertEquals(Collections.emptyList(), listNull);

        // Test empty list
        List<Locale> listEmpty = LocaleUtils.localeLookupList(new Locale(""));
        assertEquals(Collections.emptyList(), listEmpty);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test size
        int sizeExpected = Locale.getAvailableLocales().length;
        int sizeActual = LocaleUtils.availableLocaleList().size();
        assertEquals(sizeExpected, sizeActual);

        // Test contains
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
        assertTrue(LocaleUtils.availableLocaleList().contains(locale));

        // Test not contains
        Locale localeNotAvailable = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(localeNotAvailable));
        assertFalse(LocaleUtils.availableLocaleList().contains(localeNotAvailable));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty country
        List<Locale> languagesByCountryEmpty = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.emptyList(), languagesByCountryEmpty);

        // Test valid country
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        assertEquals(expectedList, languagesByCountry);

        // Test invalid country
        List<Locale> languagesByCountryInvalid = LocaleUtils.languagesByCountry("XX");
        assertEquals(Collections.emptyList(), languagesByCountryInvalid);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty language
        List<Locale> countriesByLanguageEmpty = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.emptyList(), countriesByLanguageEmpty);

        // Test valid language
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedList = Arrays.asList(new Locale("US"), new Locale("GB"), new Locale("CA"));
        assertEquals(expectedList, countriesByLanguage);

        // Test invalid language
        List<Locale> countriesByLanguageInvalid = LocaleUtils.countriesByLanguage("XX");
        assertEquals(Collections.emptyList(), countriesByLanguageInvalid);
    }
}