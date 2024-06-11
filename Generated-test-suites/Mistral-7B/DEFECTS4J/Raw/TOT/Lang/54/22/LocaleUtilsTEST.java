package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid inputs.
     */
    @Test
    public void testToLocaleValid() {
        List<String> testCases = Arrays.asList("en", "en_GB", "en_GB_UK", "fr_FR", "fr_CH_fr", "es_ES", "es_MX", "es_ES_Castilian");
        for (String localeStr : testCases) {
            Locale expectedLocale = new Locale(localeStr.substring(0, 2), localeStr.substring(3, localeStr.length()));
            Locale actualLocale = LocaleUtils.toLocale(localeStr);
            assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
        }
    }

    /**
     * Test toLocale method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalid() {
        List<String> testCases = Arrays.asList("en_GB_123", "en_GB_xx", "en_GB_ZZ", "en_GB_en", "en_GB_GB", "en_GB");
        for (String localeStr : testCases) {
            LocaleUtils.toLocale(localeStr);
        }
    }

    /**
     * Test localeLookupList method with valid inputs.
     */
    @Test
    public void testLocaleLookupListValid() {
        List<Locale> testCases = Arrays.asList(Locale.FRENCH, Locale.FRENCH_CANADA, Locale.FRENCH_SWITZERLAND, Locale.ENGLISH, Locale.ENGLISH_UK, null);
        for (Locale locale : testCases) {
            List<Locale> result = LocaleUtils.localeLookupList(locale, Locale.ENGLISH);
            int indexOfExpectedLocale = testCases.indexOf(locale);
            int indexOfEnglishLocale = testCases.lastIndexOf(Locale.ENGLISH);
            assertEquals(indexOfExpectedLocale + 1, result.size());
            assertTrue(result.contains(locale));
            assertTrue(result.contains(Locale.ENGLISH));
            assertFalse(result.contains(Locale.FRENCH_CANADA_QUEBEC));
            assertFalse(result.contains(Locale.JAPANESE));
            assertEquals(locale, result.get(indexOfExpectedLocale));
            assertEquals(Locale.ENGLISH, result.get(result.size() - 1));
        }
    }

    /**
     * Test localeLookupList method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLocaleLookupListInvalid() {
        Locale locale = null;
        LocaleUtils.localeLookupList(locale, Locale.ENGLISH);
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(135, availableLocales.size());
        assertTrue(availableLocales.contains(Locale.ENGLISH));
        assertTrue(availableLocales.contains(Locale.FRENCH));
        assertTrue(availableLocales.contains(Locale.GERMAN));
        assertTrue(availableLocales.contains(Locale.SPANISH));
        assertTrue(availableLocales.contains(Locale.FRENCH_CANADA));
        assertTrue(availableLocales.contains(Locale.FRENCH_SWITZERLAND));
        assertTrue(availableLocales.contains(Locale.GERMAN_AUSTRIA));
        assertTrue(availableLocales.contains(Locale.GERMAN_GERMANY));
        assertTrue(availableLocales.contains(Locale.SPANISH_ESPANA));
        assertTrue(availableLocales.contains(Locale.SPANISH_MEXICO));
    }

    /**
     * Test isAvailableLocale method.
     */
    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SPANISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH_CANADA));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH_SWITZERLAND));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN_AUSTRIA));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN_GERMANY));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SPANISH_ESPANA));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SPANISH_MEXICO));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.JAPANESE));
    }

    /**
     * Test languagesByCountry method with valid inputs.
     */
    @Test
    public void testLanguagesByCountryValid() {
        List<Locale> testCases = Arrays.asList(Locale.FRENCH, Locale.GERMAN, Locale.SPANISH, null);
        for (Locale country : testCases) {
            List<Locale> result = LocaleUtils.languagesByCountry(country.getCountry());
            int indexOfExpectedLocale = testCases.indexOf(country);
            assertEquals(indexOfExpectedLocale + 1, result.size());
            assertTrue(result.contains(country));
            assertTrue(result.get(0).getLanguage().equalsIgnoreCase("en"));
            assertTrue(result.get(1).getLanguage().equalsIgnoreCase("fr"));
            assertTrue(result.get(2).getLanguage().equalsIgnoreCase("de"));
            assertTrue(result.get(3).getLanguage().equalsIgnoreCase("es"));
        }
    }

    /**
     * Test languagesByCountry method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountryInvalid() {
        LocaleUtils.languagesByCountry(null);
    }

    /**
     * Test countriesByLanguage method with valid inputs.
     */
    @Test
    public void testCountriesByLanguageValid() {
        List<Locale> testCases = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.SPANISH, null);
        for (Locale language : testCases) {
            List<Locale> result = LocaleUtils.countriesByLanguage(language.getLanguage());
            int indexOfExpectedLocale = testCases.indexOf(language);
            assertEquals(indexOfExpectedLocale + 1, result.size());
            assertTrue(result.contains(Locale.ENGLISH_UK));
            assertTrue(result.contains(Locale.ENGLISH_US));
            assertTrue(result.contains(Locale.FRENCH_CANADA));
            assertTrue(result.contains(Locale.FRENCH_FRANCE));
            assertTrue(result.contains(Locale.GERMAN_AUSTRIA));
            assertTrue(result.contains(Locale.GERMAN_GERMANY));
            assertTrue(result.contains(Locale.SPANISH_ESPANA));
            assertTrue(result.contains(Locale.SPANISH_MEXICO));
        }
    }

    /**
     * Test countriesByLanguage method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCountriesByLanguageInvalid() {
        LocaleUtils.countriesByLanguage(null);
    }
}