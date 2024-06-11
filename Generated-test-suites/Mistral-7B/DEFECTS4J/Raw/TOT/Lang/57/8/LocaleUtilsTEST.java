package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        Assert.assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        Assert.assertEquals("en", LocaleUtils.toLocale("en").toString());
        Assert.assertEquals("en_GB", LocaleUtils.toLocale("en_GB").toString());
        Assert.assertEquals("fr_FR", LocaleUtils.toLocale("fr_FR").toString());

        // Test invalid input
        Assert.assertNull(LocaleUtils.toLocale(null));
        Assert.assertNull(LocaleUtils.toLocale(""));
        Assert.assertNull(LocaleUtils.toLocale("xx"));
        Assert.assertNull(LocaleUtils.toLocale("en_XX"));
        Assert.assertNull(LocaleUtils.toLocale("en_GB_XX"));
        Assert.assertNull(LocaleUtils.toLocale("en_XX_XX"));
        Assert.assertNull(LocaleUtils.toLocale("en_GB_xx"));

        // Test invalid format
        Assert.assertNull(LocaleUtils.toLocale("en_US_"));
        Assert.assertNull(LocaleUtils.toLocale("en_US_XX"));
        Assert.assertNull(LocaleUtils.toLocale("en_US__"));
        Assert.assertNull(LocaleUtils.toLocale("en_US_XX_"));
        Assert.assertNull(LocaleUtils.toLocale("en_US_XX__"));
        Assert.assertNull(LocaleUtils.toLocale("en_US__XX"));
        Assert.assertNull(LocaleUtils.toLocale("en_US_XX__"));

        // Test empty input
        Assert.assertEquals("", LocaleUtils.toLocale("").toString());
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid input
        List expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List result = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        Assert.assertEquals(expected, result);

        // Test empty input
        expected = Collections.EMPTY_LIST;
        result = LocaleUtils.localeLookupList(null, new Locale("en"));
        Assert.assertEquals(expected, result);

        // Test input with no more general locales
        expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr")
        );
        result = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), null);
        Assert.assertEquals(expected, result);

        // Test input with no less specific locales
        expected = Arrays.asList(
                new Locale("en")
        );
        result = LocaleUtils.localeLookupList(null, new Locale("en"));
        Assert.assertEquals(expected, result);

        // Test input with same locale
        expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en"),
                new Locale("fr", "CA", "xxx") // duplicate
        );
        result = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA", "xxx"));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List expected = Collections.EMPTY_LIST;
        List result = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expected, result);

        // Test non-empty list
        List locales = Arrays.asList(Locale.getAvailableLocales());
        expected = Collections.unmodifiableList(locales);
        result = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test valid input
        List locales = Arrays.asList(Locale.getAvailableLocales());
        for (Locale locale : locales) {
            Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        // Test null input
        Assert.assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test invalid input
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        List expected = Collections.EMPTY_LIST;
        List result = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(expected, result);

        // Test valid input
        String countryCode = "CA";
        List expectedLocales = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("en", "CA")
        );
        List resultLocales = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expectedLocales, resultLocales);

        // Test invalid input
        Assert.assertNull(LocaleUtils.languagesByCountry("XX"));
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty list
        List expected = Collections.EMPTY_LIST;
        List result = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(expected, result);

        // Test valid input
        String languageCode = "fr";
        List expectedCountries = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("fr", "BE"),
                new Locale("fr", "CH"),
                new Locale("fr", "FR")
        );
        List resultCountries = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expectedCountries, resultCountries);

        // Test invalid input
        Assert.assertNull(LocaleUtils.countriesByLanguage("XX"));
    }
}