package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid input.
     */
    @Test
    public void testToLocaleValidInput() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);
    }

    /**
     * Test toLocale method with invalid input - language code not lowercase.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInputLanguageCodeNotLowerCase() {
        LocaleUtils.toLocale("EN_US");
    }

    /**
     * Test toLocale method with invalid input - country code not uppercase.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInputCountryCodeNotUppercase() {
        LocaleUtils.toLocale("en_us");
    }

    /**
     * Test toLocale method with invalid input - separator not underscore.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInputSeparatorNotUnderscore() {
        LocaleUtils.toLocale("en_US_x");
    }

    /**
     * Test toLocale method with invalid input - length less than 2.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInputLengthLessThanTwo() {
        LocaleUtils.toLocale("en");
    }

    /**
     * Test toLocale method with invalid input - length greater than 5.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInputLengthGreaterThanFive() {
        LocaleUtils.toLocale("en_US_xx_XX");
    }

    /**
     * Test toLocale method with invalid input - language code not two characters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInputLanguageCodeNotTwoCharacters() {
        LocaleUtils.toLocale("es_MX_XXXXXXXXXXXXXXXXXX");
    }

    /**
     * Test toLocale method with invalid input - country code not two characters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInputCountryCodeNotTwoCharacters() {
        LocaleUtils.toLocale("es_MX_XXXXXXXXXXXXXXXXXX");
    }

    /**
     * Test toLocale method with invalid input - variant not empty and country code is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInputVariantNotEmptyAndCountryCodeEmpty() {
        LocaleUtils.toLocale("es_XXXXXXXXXXXXXXXXXX");
    }

    /**
     * Test toLocale method with null input.
     */
    @Test
    public void testToLocaleNullInput() {
        assertEquals(null, LocaleUtils.toLocale(null));
    }

    /**
     * Test localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupListValidInput() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    /**
     * Test localeLookupList method with null input.
     */
    @Test
    public void testLocaleLookupListNullInput() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    /**
     * Test localeLookupList method with defaultLocale null.
     */
    @Test
    public void testLocaleLookupListDefaultLocaleNull() {
        Locale locale = new Locale("fr", "CA");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale, null);
        assertEquals(expected, actual);
    }

    /**
     * Test localeLookupList method with defaultLocale not null.
     */
    @Test
    public void testLocaleLookupListDefaultLocaleNotNull() {
        Locale locale = new Locale("fr", "CA");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    /**
     * Test availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    /**
     * Test isAvailableLocale method with available locale.
     */
    @Test
    public void testIsAvailableLocaleAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test isAvailableLocale method with invalid locale.
     */
    @Test
    public void testIsAvailableLocaleInvalidLocale() {
        Locale locale = new Locale("en", "US_XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test languagesByCountry method with valid input.
     */
    @Test
    public void testLanguagesByCountryValidInput() {
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    /**
     * Test languagesByCountry method with null input.
     */
    @Test
    public void testLanguagesByCountryNullInput() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    /**
     * Test languagesByCountry method with empty list.
     */
    @Test
    public void testLanguagesByCountryEmptyList() {
        String countryCode = "";
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    /**
     * Test languagesByCountry method with invalid country code.
     */
    @Test
    public void testLanguagesByCountryInvalidCountryCode() {
        String countryCode = "XX";
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    /**
     * Test countriesByLanguage method with valid input.
     */
    @Test
    public void testCountriesByLanguageValidInput() {
        String languageCode = "en";
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

    /**
     * Test countriesByLanguage method with null input.
     */
    @Test
    public void testCountriesByLanguageNullInput() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }

    /**
     * Test countriesByLanguage method with empty list.
     */
    @Test
    public void testCountriesByLanguageEmptyList() {
        String languageCode = "";
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

    /**
     * Test countriesByLanguage method with invalid language code.
     */
    @Test
    public void testCountriesByLanguageInvalidLanguageCode() {
        String languageCode = "XX";
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }
}