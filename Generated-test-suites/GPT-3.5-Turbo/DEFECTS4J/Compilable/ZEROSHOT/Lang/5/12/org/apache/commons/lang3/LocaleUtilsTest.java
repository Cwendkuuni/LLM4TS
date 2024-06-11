package org.apache.commons.lang3;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidString() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(new Locale("en", "GB"), locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidString() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList_WithDefaultLocale() {
        Locale locale = LocaleUtils.toLocale("fr_CA_xxx");
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                Locale.getDefault()
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupList_WithoutDefaultLocale() {
        Locale locale = LocaleUtils.toLocale("fr_CA");
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("fr"),
                Locale.getDefault()
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(locale, null);
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(availableLocales);
        Assert.assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(availableLocales);
        Assert.assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_ValidLocale() {
        Locale locale = LocaleUtils.toLocale("fr_CA");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(isAvailable);
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale locale = new Locale("xx", "XX");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(isAvailable);
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry("GB");
        List<Locale> expectedLanguages = Arrays.asList(
                new Locale("en"),
                new Locale("gd")
        );
        Assert.assertEquals(expectedLanguages, languages);
    }

    @Test
    public void testLanguagesByCountry_InvalidCountryCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry("XX");
        Assert.assertTrue(languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        List<Locale> expectedCountries = Arrays.asList(
                new Locale("FR"),
                new Locale("BE"),
                new Locale("CA"),
                new Locale("CH"),
                new Locale("LU")
        );
        Assert.assertEquals(expectedCountries, countries);
    }

    @Test
    public void testCountriesByLanguage_InvalidLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("xx");
        Assert.assertTrue(countries.isEmpty());
    }
}