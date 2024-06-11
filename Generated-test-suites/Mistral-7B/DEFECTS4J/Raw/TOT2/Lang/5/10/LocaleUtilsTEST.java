package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test cases for the LocaleUtils class.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid inputs.
     */
    @Test
    public void testToLocaleValidInput() {
        List<String> inputs = Arrays.asList("en", "en_GB", "en_GB_US", "fr_CA", "fr_CA_FR");
        for (String input : inputs) {
            Locale expected = new Locale(input);
            Locale actual = LocaleUtils.toLocale(input);
            assertEquals("toLocale method returned incorrect Locale for input: " + input, expected, actual);
        }
    }

    /**
     * Test toLocale method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        List<String> inputs = Arrays.asList("", "en_aB", "en_GB_aB", "en_aB_US", "fr_aB", "fr_CA_aB");
        for (String input : inputs) {
            LocaleUtils.toLocale(input);
        }
    }

    /**
     * Test localeLookupList method with valid inputs.
     */
    @Test
    public void testLocaleLookupListValidInput() {
        Locale locale = new Locale("fr", "CA", "XXX");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "XXX"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals("localeLookupList method returned incorrect list for locale: " + locale.toString() + " and defaultLocale: " + defaultLocale.toString(), expected, actual);
    }

    /**
     * Test localeLookupList method with locale equals to defaultLocale.
     */
    @Test
    public void testLocaleLookupListSameLocale() {
        Locale locale = new Locale("en");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Collections.singletonList(locale);
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals("localeLookupList method returned incorrect list for same locale: " + locale.toString() + " and defaultLocale: " + defaultLocale.toString(), expected, actual);
    }

    /**
     * Test localeLookupList method with null inputs.
     */
    @Test
    public void testLocaleLookupListNullInputs() {
        Locale locale = null;
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals("localeLookupList method returned incorrect list for null locale: " + locale, expected, actual);

        locale = new Locale("fr", "CA", "XXX");
        defaultLocale = null;
        expected = Collections.singletonList(locale);
        actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals("localeLookupList method returned incorrect list for null defaultLocale: " + defaultLocale, expected, actual);
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        int size = expected.size();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        assertEquals("availableLocaleList method returned incorrect list of size: " + size, actual, expected);
    }

    /**
     * Test availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        int size = expected.size();
        Set<Locale> actual = new HashSet<>(Locale.getAvailableLocales());
        assertEquals("availableLocaleSet method returned incorrect set of size: " + size, actual, expected);
    }

    /**
     * Test isAvailableLocale method with existing locale.
     */
    @Test
    public void testIsAvailableLocaleExistingLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue("isAvailableLocale method returned false for existing locale: " + locale.toString(), LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test isAvailableLocale method with non-existing locale.
     */
    @Test
    public void testIsAvailableLocaleNonExistingLocale() {
        Locale locale = new Locale("non-existing-locale");
        assertFalse("isAvailableLocale method returned true for non-existing locale: " + locale.toString(), LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test languagesByCountry method with valid input.
     */
    @Test
    public void testLanguagesByCountryValidInput() {
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals("languagesByCountry method returned incorrect list for countryCode: " + countryCode, expected, actual);
    }

    /**
     * Test languagesByCountry method with null input.
     */
    @Test
    public void testLanguagesByCountryNullInput() {
        String countryCode = null;
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals("languagesByCountry method returned incorrect list for null countryCode: " + countryCode, expected, actual);
    }

    /**
     * Test countriesByLanguage method with valid input.
     */
    @Test
    public void testCountriesByLanguageValidInput() {
        String languageCode = "en";
        List<Locale> expected = Arrays.asList(new Locale("CA"), new Locale("US"), new Locale("GB"), new Locale("IE"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals("countriesByLanguage method returned incorrect list for languageCode: " + languageCode, expected, actual);
    }

    /**
     * Test countriesByLanguage method with null input.
     */
    @Test
    public void testCountriesByLanguageNullInput() {
        String languageCode = null;
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals("countriesByLanguage method returned incorrect list for null languageCode: " + languageCode, expected, actual);
    }
}