package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        Assert.assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        Assert.assertEquals("fr", LocaleUtils.toLocale("fr").toString());
        Assert.assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        Assert.assertEquals("fr_CA_XX", LocaleUtils.toLocale("fr_CA_XX").toString());

        // Test invalid inputs
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
            LocaleUtils.toLocale("xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_XX");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_XX_XX");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "XX");
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);

        // Test with given locale as first element
        Assert.assertEquals(locale, localeLookupList.get(0));

        // Test without variant
        Assert.assertEquals(new Locale("fr", "CA"), localeLookupList.get(1));
        Assert.assertEquals(new Locale("fr"), localeLookupList.get(2));

        // Test with default locale
        List<Locale> localeLookupListDefault = LocaleUtils.localeLookupList(locale, Locale.getDefault());
        Assert.assertEquals(locale, localeLookupListDefault.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), localeLookupListDefault.get(1));
        Assert.assertEquals(new Locale("fr"), localeLookupListDefault.get(2));
        Assert.assertEquals(Locale.getDefault(), localeLookupListDefault.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        List<String> availableLocaleListStrings = new ArrayList<>();

        for (Locale locale : availableLocaleList) {
            availableLocaleListStrings.add(locale.toString());
        }

        // Test size
        Assert.assertEquals(174, availableLocaleList.size());

        // Test elements
        Assert.assertTrue(availableLocaleList.contains(new Locale("en", "US")));
        Assert.assertTrue(availableLocaleList.contains(new Locale("fr", "FR")));
        Assert.assertTrue(availableLocaleList.contains(new Locale("es", "ES")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");

        // Test size
        Assert.assertEquals(2, languagesByCountry.size());

        // Test elements
        Assert.assertEquals(new Locale("en", "CA"), languagesByCountry.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), languagesByCountry.get(1));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");

        // Test size
        Assert.assertEquals(6, countriesByLanguage.size());

        // Test elements
        Assert.assertEquals(new Locale("CA", "CA"), countriesByLanguage.get(0));
        Assert.assertEquals(new Locale("US", "US"), countriesByLanguage.get(1));
        Assert.assertEquals(new Locale("GB", "GB"), countriesByLanguage.get(2));
        Assert.assertEquals(new Locale("AU", "AU"), countriesByLanguage.get(3));
        Assert.assertEquals(new Locale("NZ", "NZ"), countriesByLanguage.get(4));
        Assert.assertEquals(new Locale("IE", "IE"), countriesByLanguage.get(5));
    }
}