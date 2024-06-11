package org.apache.commons.lang3;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test with null input
        Assert.assertNull(LocaleUtils.toLocale(null));

        // Test with empty string input
        Locale locale = LocaleUtils.toLocale("");
        Assert.assertEquals("", locale.getLanguage());
        Assert.assertEquals("", locale.getCountry());

        // Test with invalid format
        try {
            LocaleUtils.toLocale("en_US#");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Test with valid format
        locale = LocaleUtils.toLocale("en_US");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("US", locale.getCountry());

        locale = LocaleUtils.toLocale("en");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("", locale.getCountry());

        locale = LocaleUtils.toLocale("_US");
        Assert.assertEquals("", locale.getLanguage());
        Assert.assertEquals("US", locale.getCountry());

        locale = LocaleUtils.toLocale("_US_variant");
        Assert.assertEquals("", locale.getLanguage());
        Assert.assertEquals("US", locale.getCountry());
        Assert.assertEquals("variant", locale.getVariant());
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("en"), new Locale("", "US"), defaultLocale);

        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expectedList, result);

        result = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(expectedList, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(locales);
        Assert.assertFalse(locales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(locales);
        Assert.assertFalse(locales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        // Test with available locale
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.US));

        // Test with unavailable locale
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz", "ZZ")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test with null input
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());

        // Test with valid input
        result = LocaleUtils.languagesByCountry("US");
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test with null input
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());

        // Test with valid input
        result = LocaleUtils.countriesByLanguage("en");
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
    }
}