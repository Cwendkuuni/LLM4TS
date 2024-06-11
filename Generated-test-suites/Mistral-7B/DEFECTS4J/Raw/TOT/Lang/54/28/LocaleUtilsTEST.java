package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        Assert.assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        Assert.assertEquals("fr", LocaleUtils.toLocale("fr").toString());
        Assert.assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        Assert.assertEquals("es_ES", LocaleUtils.toLocale("es_ES").toString());

        // Test invalid input
        try {
            LocaleUtils.toLocale(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("fr_XX");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_XX");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XX");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid separator
        try {
            LocaleUtils.toLocale("en_GB_XXX");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid country code
        try {
            LocaleUtils.toLocale("en_XX_US");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid language code
        try {
            LocaleUtils.toLocale("XX_US");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test empty language code
        Assert.assertEquals("", LocaleUtils.toLocale("").toString());
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        List availableLocales = LocaleUtils.availableLocaleList();
        List lookupList = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(availableLocales, lookupList);

        // Test default locale
        Locale defaultLocale = Locale.ENGLISH;
        List lookupListDefault = LocaleUtils.localeLookupList(defaultLocale);
        Assert.assertEquals(availableLocales, lookupListDefault);

        // Test specific locale
        Locale specificLocale = Locale.US;
        List lookupListSpecific = LocaleUtils.localeLookupList(specificLocale);
        Assert.assertEquals(Arrays.asList(specificLocale), lookupListSpecific);

        // Test locale with variant
        Locale localeWithVariant = new Locale("fr", "CA", "QC");
        List lookupListWithVariant = LocaleUtils.localeLookupList(localeWithVariant);
        Assert.assertEquals(Arrays.asList(localeWithVariant, new Locale("fr", "CA"), new Locale("fr")), lookupListWithVariant);

        // Test locale with empty country code
        Locale localeEmptyCountry = new Locale("fr", "");
        List lookupListEmptyCountry = LocaleUtils.localeLookupList(localeEmptyCountry);
        Assert.assertEquals(Arrays.asList(localeEmptyCountry, new Locale("fr")), lookupListEmptyCountry);

        // Test locale with empty language code
        Locale localeEmptyLanguage = new Locale("");
        List lookupListEmptyLanguage = LocaleUtils.localeLookupList(localeEmptyLanguage);
        Assert.assertEquals(Collections.EMPTY_LIST, lookupListEmptyLanguage);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test known locales
        List availableLocales = LocaleUtils.availableLocaleList();
        for (Locale locale : availableLocales) {
            Assert.assertTrue("Locale " + locale + " should be available", LocaleUtils.isAvailableLocale(locale));
        }

        // Test unknown locale
        Locale unknownLocale = new Locale("xx", "xx");
        Assert.assertFalse("Locale " + unknownLocale + " should not be available", LocaleUtils.isAvailableLocale(unknownLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test known country
        List languagesByCountry = LocaleUtils.languagesByCountry("US");
        List expected = Arrays.asList(new Locale("en"), new Locale("es"), new Locale("fr"));
        Assert.assertEquals(expected, languagesByCountry);

        // Test unknown country
        List languagesByCountryUnknown = LocaleUtils.languagesByCountry("XX");
        Assert.assertEquals(Collections.EMPTY_LIST, languagesByCountryUnknown);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test known language
        List countriesByLanguage = LocaleUtils.countriesByLanguage("fr");
        List expected = Arrays.asList(new Locale("CA"), new Locale("BE"), new Locale("CH"));
        Assert.assertEquals(expected, countriesByLanguage);

        // Test unknown language
        List countriesByLanguageUnknown = LocaleUtils.countriesByLanguage("XX");
        Assert.assertEquals(Collections.EMPTY_LIST, countriesByLanguageUnknown);
    }
}