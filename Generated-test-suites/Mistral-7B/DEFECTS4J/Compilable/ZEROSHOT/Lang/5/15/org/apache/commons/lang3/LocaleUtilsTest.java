package org.apache.commons.lang3;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test file for the LocaleUtils class.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale() method with valid input.
     */
    @Test
    public void testToLocaleValidInput() {
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale("en_US");
        Assert.assertEquals(expectedLocale, actualLocale);
    }

    /**
     * Test toLocale() method with invalid input - null.
     */
    @Test
    public void testToLocaleNullInput() {
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    /**
     * Test toLocale() method with invalid input - empty string.
     */
    @Test
    public void testToLocaleEmptyStringInput() {
        Assert.assertNull(LocaleUtils.toLocale(""));
    }

    /**
     * Test toLocale() method with invalid input - invalid format.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatInput() {
        LocaleUtils.toLocale("en_GB_xx");
    }

    /**
     * Test localeLookupList() method with valid input.
     */
    @Test
    public void testLocaleLookupListValidInput() {
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actualLocales = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        Assert.assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Test localeLookupList() method with null input.
     */
    @Test
    public void testLocaleLookupListNullInput() {
        Locale locale = null;
        List<Locale> actualLocales = LocaleUtils.localeLookupList(locale, new Locale("en"));
        Assert.assertTrue(actualLocales.isEmpty());
    }

    /**
     * Test isAvailableLocale() method with available locale.
     */
    @Test
    public void testIsAvailableLocaleAvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test isAvailableLocale() method with invalid locale.
     */
    @Test
    public void testIsAvailableLocaleInvalidLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test languagesByCountry() method with valid input.
     */
    @Test
    public void testLanguagesByCountryValidInput() {
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA")
        );
        List<Locale> actualLocales = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Test languagesByCountry() method with null input.
     */
    @Test
    public void testLanguagesByCountryNullInput() {
        Assert.assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
    }

    /**
     * Test countriesByLanguage() method with valid input.
     */
    @Test
    public void testCountriesByLanguageValidInput() {
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("CA", "en"),
                new Locale("US", "en")
        );
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Test countriesByLanguage() method with null input.
     */
    @Test
    public void testCountriesByLanguageNullInput() {
        Assert.assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }
}