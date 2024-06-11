package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        String[] testCases = new String[]{"en", "en_GB", "en_GB_US", "fr_FR", "fr_FR_CA"};
        for (String input : testCases) {
            Locale expected = new Locale(input.substring(0, 2), input.substring(3));
            if (input.length() > 5) {
                expected = new Locale(input.substring(0, 2), input.substring(3, 5), input.substring(6));
            }
            Locale actual = LocaleUtils.toLocale(input);
            Assert.assertEquals(expected, actual);
        }

        // Test invalid input
        String[] invalidInputs = new String[]{"en_GB_zx", "en_zG", "en_GB_23", "en_GB_en", "en_GB_en_GB", "en_GB_en_en"};
        for (String input : invalidInputs) {
            try {
                LocaleUtils.toLocale(input);
                Assert.fail("Expected IllegalArgumentException for input: " + input);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }

        // Test null input
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid input
        Locale locale = new Locale("fr", "CA", "xxx");
        List expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), Locale.getDefault());
        List actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);

        // Test default locale only
        locale = null;
        actual = LocaleUtils.localeLookupList(Locale.getDefault(), null);
        Assert.assertEquals(Arrays.asList(Locale.getDefault()), actual);

        // Test empty list
        actual = LocaleUtils.localeLookupList(null, null);
        Assert.assertTrue(actual.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        // Test unmodifiable list
        List availableLocales = LocaleUtils.availableLocaleList();
        Assert.assertFalse(availableLocales.isEmpty());
        try {
            availableLocales.add(new Locale("test", "test"));
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test empty list
        List emptyAvailableLocales = Collections.emptyList();
        Assert.assertEquals(emptyAvailableLocales, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testIsAvailableLocale() {
        // Test known locale
        Locale knownLocale = new Locale("en", "US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(knownLocale));

        // Test unknown locale
        Locale unknownLocale = new Locale("test", "test");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(unknownLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test valid input
        Collection expected = Arrays.asList(new Locale("en", "US"), new Locale("fr", "CA"), new Locale("de", "DE"));
        Collection actual = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(expected, actual);

        // Test empty list
        actual = LocaleUtils.languagesByCountry(null);
        Assert.assertTrue(actual.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test valid input
        Collection expected = Arrays.asList(new Locale("ca", "CA"), new Locale("fr", "FR"), new Locale("de", "DE"));
        Collection actual = LocaleUtils.countriesByLanguage("fr");
        Assert.assertEquals(expected, actual);

        // Test empty list
        actual = LocaleUtils.countriesByLanguage(null);
        Assert.assertTrue(actual.isEmpty());
    }
}