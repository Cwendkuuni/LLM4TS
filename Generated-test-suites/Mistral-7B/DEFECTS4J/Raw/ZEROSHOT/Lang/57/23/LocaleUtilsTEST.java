package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid input.
     */
    @Test
    public void testToLocaleValidInput() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(expected.getLanguage(), actual.getLanguage());
        Assert.assertEquals(expected.getCountry(), actual.getCountry());
        Assert.assertArrayEquals(expected.getVariants(), actual.getVariants());
    }

    /**
     * Test toLocale method with invalid input length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInputLength() {
        LocaleUtils.toLocale("en");
    }

    /**
     * Test toLocale method with invalid input format.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_xx");
    }

    /**
     * Test localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupListValidInput() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(
                locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test localeLookupList method with null input.
     */
    @Test
    public void testLocaleLookupListNullInput() {
        List<Locale> actual = LocaleUtils.localeLookupList(null, new Locale("en"));
        Assert.assertEquals(Collections.emptyList(), actual);
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = new HashSet<>(Locale.getAvailableLocales());
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test isAvailableLocale method with available locale.
     */
    @Test
    public void testIsAvailableLocaleAvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        boolean expected = true;
        boolean actual = LocaleUtils.isAvailableLocale(locale);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test isAvailableLocale method with invalid locale.
     */
    @Test
    public void testIsAvailableLocaleInvalidLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        boolean expected = false;
        boolean actual = LocaleUtils.isAvailableLocale(locale);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test languagesByCountry method with valid input.
     */
    @Test
    public void testLanguagesByCountryValidInput() {
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(
                new Locale("en", countryCode), new Locale("fr", countryCode)
        );
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test languagesByCountry method with null input.
     */
    @Test
    public void testLanguagesByCountryNullInput() {
        String countryCode = null;
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(Collections.emptyList(), actual);
    }

    /**
     * Test countriesByLanguage method with valid input.
     */
    @Test
    public void testCountriesByLanguageValidInput() {
        String languageCode = "en";
        List<Locale> expected = Arrays.asList(
                new Locale(languageCode, "CA"), new Locale(languageCode, "US")
        );
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test countriesByLanguage method with null input.
     */
    @Test
    public void testCountriesByLanguageNullInput() {
        String languageCode = null;
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(Collections.emptyList(), actual);
    }
}