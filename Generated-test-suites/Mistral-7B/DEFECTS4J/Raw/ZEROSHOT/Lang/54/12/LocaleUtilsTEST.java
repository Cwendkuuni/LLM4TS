package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        Assert.assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        Assert.assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        Assert.assertEquals("en", LocaleUtils.toLocale("en").toString());

        // Test invalid inputs
        try {
            LocaleUtils.toLocale(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("fr_CA_xxx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_xxx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_XXX");
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
            LocaleUtils.toLocale("en_US_x");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = LocaleUtils.toLocale("fr_CA");
        List lookupList = LocaleUtils.localeLookupList(locale);
        List expectedList = Arrays.asList(locale, new Locale("fr_CA"), new Locale("fr"));
        Assert.assertEquals(expectedList, lookupList);

        locale = LocaleUtils.toLocale("en");
        lookupList = LocaleUtils.localeLookupList(locale, LocaleUtils.toLocale("fr_CA"));
        expectedList = Arrays.asList(locale, new Locale("fr_CA"), new Locale("fr"), new Locale("en"));
        Assert.assertEquals(expectedList, lookupList);
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocaleList = LocaleUtils.availableLocaleList();
        List expectedLocaleList = new ArrayList(LocaleUtils.availableLocaleSet());
        Assert.assertEquals(expectedLocaleList, availableLocaleList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set availableLocaleSet = LocaleUtils.availableLocaleSet();
        List expectedLocaleList = new ArrayList(LocaleUtils.availableLocaleList());
        Assert.assertEquals(expectedLocaleList, new ArrayList(availableLocaleSet));
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = LocaleUtils.toLocale("fr_CA");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = LocaleUtils.toLocale("fr_CA_xxx");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = Locale.US;
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List languagesByCountry = LocaleUtils.languagesByCountry("CA");
        List expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        Assert.assertEquals(expectedList, languagesByCountry);
    }

    @Test
    public void testCountriesByLanguage() {
        List countriesByLanguage = LocaleUtils.countriesByLanguage("fr");
        List expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"),
                new Locale("fr", "FR"), new Locale("fr", "LU"));
        Assert.assertEquals(expectedList, countriesByLanguage);
    }
}