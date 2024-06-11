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
    public void testToLocale() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test toLocale method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        LocaleUtils.toLocale(null);
    }

    /**
     * Test localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test localeLookupList method with null input.
     */
    @Test
    public void testLocaleLookupListNullInput() {
        List<Locale> actual = LocaleUtils.localeLookupList(null, new Locale("en"));
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual.isEmpty());
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
     * Test isAvailableLocale method.
     */
    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        boolean expected = true;
        boolean actual = LocaleUtils.isAvailableLocale(locale);
        Assert.assertEquals(expected, actual);

        locale = new Locale("es", "ES");
        expected = false;
        actual = LocaleUtils.isAvailableLocale(locale);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test languagesByCountry method.
     */
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("en", "CA")
        );
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test languagesByCountry method with null input.
     */
    @Test
    public void testLanguagesByCountryNullInput() {
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual.isEmpty());
    }

    /**
     * Test countriesByLanguage method.
     */
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(
                new Locale("CA", "fr"),
                new Locale("US", "en")
        );
        List<Locale> actual = LocaleUtils.countriesByLanguage("fr");
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test countriesByLanguage method with null input.
     */
    @Test
    public void testCountriesByLanguageNullInput() {
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        Assert.assertNotNull(actual);
        Assert.assertTrue(actual.isEmpty());
    }
}