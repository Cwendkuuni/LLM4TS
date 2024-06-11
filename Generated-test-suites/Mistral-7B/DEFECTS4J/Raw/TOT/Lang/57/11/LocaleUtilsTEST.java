package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test class for {@link LocaleUtils}
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid input
     */
    @Test
    public void testToLocaleValidInput() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);
    }

    /**
     * Test toLocale method with empty input
     */
    @Test
    public void testToLocaleEmptyInput() {
        assertNull(LocaleUtils.toLocale(""));
    }

    /**
     * Test toLocale method with invalid input - missing separator
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleMissingSeparator() {
        LocaleUtils.toLocale("enUS");
    }

    /**
     * Test toLocale method with invalid input - invalid language code
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLanguageCode() {
        LocaleUtils.toLocale("Xx_YY");
    }

    /**
     * Test toLocale method with invalid input - invalid country code
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountryCode() {
        LocaleUtils.toLocale("en_XX");
    }

    /**
     * Test localeLookupList method with valid input
     */
    @Test
    public void testLocaleLookupListValidInput() {
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en"), new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("en", "US"), new Locale("fr", "CA"));
        assertEquals(expected, actual);
    }

    /**
     * Test localeLookupList method with null input
     */
    @Test
    public void testLocaleLookupListNullInput() {
        List<Locale> actual = LocaleUtils.localeLookupList(null, new Locale("fr", "CA"));
        assertTrue(Collections.isEmpty(actual));
    }

    /**
     * Test localeLookupList method with invalid input - null defaultLocale
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLocaleLookupListNullDefaultLocale() {
        LocaleUtils.localeLookupList(new Locale("en", "US"), null);
    }

    /**
     * Test availableLocaleList method
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.GERMAN, Locale.FRENCH, Locale.ITALIAN, Locale.ENGLISH);
        assertEquals(expected, actual);
    }

    /**
     * Test availableLocaleSet method
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(LocaleUtils.availableLocaleSet());
        Set<Locale> actual = new HashSet<>(Arrays.asList(Locale.GERMAN, Locale.FRENCH, Locale.ITALIAN, Locale.ENGLISH));
        assertEquals(expected, actual);
    }

    /**
     * Test isAvailableLocale method with available locale
     */
    @Test
    public void testIsAvailableLocaleAvailable() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
    }

    /**
     * Test isAvailableLocale method with invalid input - null locale
     */
    @Test
    public void testIsAvailableLocaleNullLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    /**
     * Test isAvailableLocale method with invalid input - non-locale object
     */
    @Test
    public void testIsAvailableLocaleInvalidObject() {
        assertFalse(LocaleUtils.isAvailableLocale(new Object()));
    }

    /**
     * Test languagesByCountry method with valid input
     */
    @Test
    public void testLanguagesByCountryValidInput() {
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"), new Locale("de", "CA"), new Locale("es", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, actual);
    }

    /**
     * Test languagesByCountry method with invalid input - null countryCode
     */
    @Test
    public void testLanguagesByCountryNullCountryCode() {
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        assertTrue(Collections.isEmpty(actual));
    }

    /**
     * Test countriesByLanguage method with valid input
     */
    @Test
    public void testCountriesByLanguageValidInput() {
        List<Locale> expected = Arrays.asList(new Locale("CA", "en"), new Locale("CA", "fr"), new Locale("CA", "es"), new Locale("CA", "de"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, actual);
    }

    /**
     * Test countriesByLanguage method with invalid input - null languageCode
     */
    @Test
    public void testCountriesByLanguageNullLanguageCode() {
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        assertTrue(Collections.isEmpty(actual));
    }
}