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
        // Test valid locales
        Assert.assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        Assert.assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        Assert.assertEquals("es_ES", LocaleUtils.toLocale("es_ES").toString());

        // Test empty locale
        Assert.assertNull(LocaleUtils.toLocale(null));

        // Test invalid locales
        try {
            LocaleUtils.toLocale("aA");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        try {
            LocaleUtils.toLocale("en_a");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        try {
            LocaleUtils.toLocale("en_GB_a");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        try {
            LocaleUtils.toLocale("en_GB__");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        try {
            LocaleUtils.toLocale("en_a_US");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        try {
            LocaleUtils.toLocale("en_GB_a_US");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty locale
        List lookupList = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(Collections.emptyList(), lookupList);

        // Test locale with variant
        Locale locale = LocaleUtils.toLocale("fr_CA_MA");
        List lookupList1 = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr")), lookupList1);

        // Test locale without variant
        locale = LocaleUtils.toLocale("fr_CA");
        List lookupList2 = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr")), lookupList2);

        // Test locale with country
        locale = LocaleUtils.toLocale("fr");
        List lookupList3 = LocaleUtils.localeLookupList(locale, Locale.getDefault());
        List availableLocales = LocaleUtils.availableLocaleList();
        List expectedList = new ArrayList(availableLocales.size() + 1);
        expectedList.add(locale);
        for (int i = 0; i < availableLocales.size(); i++) {
            expectedList.add(availableLocales.get(i));
        }
        Assert.assertEquals(expectedList, lookupList3);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List availableLocaleList = LocaleUtils.availableLocaleList();
        Assert.assertEquals(Collections.emptyList(), availableLocaleList);

        // Test non-empty list
        List availableLocaleList1 = LocaleUtils.availableLocaleList();
        Assert.assertFalse(availableLocaleList1.isEmpty());

        // Test adding and getting locales
        LocaleUtils.addLocale(new Locale("test", "test"));
        List availableLocaleList2 = LocaleUtils.availableLocaleList();
        Assert.assertEquals(1 + availableLocaleList1.size(), availableLocaleList2.size());
        Assert.assertTrue(availableLocaleList2.contains(new Locale("test", "test")));

        // Test removing locale
        LocaleUtils.removeLocale(new Locale("test", "test"));
        List availableLocaleList3 = LocaleUtils.availableLocaleList();
        Assert.assertEquals(availableLocaleList1.size(), availableLocaleList3.size());
        Assert.assertFalse(availableLocaleList3.contains(new Locale("test", "test")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty country
        List languagesByCountry = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(Collections.emptyList(), languagesByCountry);

        // Test valid country
        List languagesByCountry1 = LocaleUtils.languagesByCountry("US");
        List expectedList1 = Arrays.asList(Locale.getLocale("en", "US"), Locale.getLocale("en"));
        Assert.assertEquals(expectedList1, languagesByCountry1);

        // Test invalid country
        try {
            LocaleUtils.languagesByCountry("aA");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty language
        List countriesByLanguage = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(Collections.emptyList(), countriesByLanguage);

        // Test valid language
        List countriesByLanguage1 = LocaleUtils.countriesByLanguage("en");
        List expectedList1 = Arrays.asList(Locale.getLocale("en", "US"), Locale.getLocale("en", "GB"), Locale.getLocale("en"));
        Assert.assertEquals(expectedList1, countriesByLanguage1);

        // Test invalid language
        try {
            LocaleUtils.countriesByLanguage("aA");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
}