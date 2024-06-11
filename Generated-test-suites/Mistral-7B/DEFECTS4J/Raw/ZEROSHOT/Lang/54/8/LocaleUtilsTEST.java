package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        Assert.assertEquals(expected, actual);

        expected = new Locale("fr", "FR");
        actual = LocaleUtils.toLocale("fr_FR");
        Assert.assertEquals(expected, actual);

        expected = new Locale("es", "ES");
        actual = LocaleUtils.toLocale("es_ES");
        Assert.assertEquals(expected, actual);

        // Test empty locale string
        actual = LocaleUtils.toLocale("");
        Assert.assertNull(actual);

        // Test invalid locale strings
        try {
            LocaleUtils.toLocale("en_GB_xxx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xxx_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB");
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
            LocaleUtils.toLocale("en_US_xx_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx");
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
            LocaleUtils.toLocale("en_US_xxx_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xxx_xx_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx");
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
            LocaleUtils.toLocale("en");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("fr");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("fr_FR");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("es");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("es_ES");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expectedList, actualList);

        locale = new Locale("fr", "CA");
        expectedList = Arrays.asList(locale, new Locale("fr"), new Locale("en"));
        actualList = LocaleUtils.localeLookupList(locale, new Locale("en"));
        Assert.assertEquals(expectedList, actualList);

        locale = null;
        expectedList = Collections.emptyList();
        actualList = LocaleUtils.localeLookupList(locale, new Locale("en"));
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> actualList = Locale.getAvailableLocales();
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = LocaleUtils.availableLocaleSet();
        Set<Locale> actualSet = new HashSet<>(Locale.getAvailableLocales());
        Assert.assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "FR");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA", "xxx");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "XX");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "XXX");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = null;
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        List<Locale> actualList = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(expectedList, actualList);

        Assert.assertFalse(LocaleUtils.languagesByCountry(null).isEmpty());
        Assert.assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = Arrays.asList(new Locale("ca", "CA"), new Locale("fr", "CA"));
        List<Locale> actualList = LocaleUtils.countriesByLanguage("fr");
        Assert.assertEquals(expectedList, actualList);

        Assert.assertFalse(LocaleUtils.countriesByLanguage(null).isEmpty());
        Assert.assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }
}