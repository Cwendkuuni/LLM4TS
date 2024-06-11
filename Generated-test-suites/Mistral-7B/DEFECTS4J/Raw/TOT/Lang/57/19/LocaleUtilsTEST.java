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
     * Test toLocale() with valid inputs.
     */
    @Test
    public void testToLocaleValidInputs() {
        List<String> inputs = Arrays.asList(
                "en", "en_GB", "en_GB_US", "fr_FR", "fr_FR_CA", "es_ES_MX"
        );
        for (String input : inputs) {
            Locale expectedLocale = new Locale(input.split("_")[0], input.split("_")[1]);
            Locale actualLocale = LocaleUtils.toLocale(input);
            assertEquals(expectedLocale, actualLocale);
        }
    }

    /**
     * Test toLocale() with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInputs() {
        List<String> inputs = Arrays.asList(
                "en_GB_US_", "en_GB_US__", "en_GB_US_xx", "fr_FR_CA_", "fr_FR_CA__", "es_ES_MX_xx"
        );
        for (String input : inputs) {
            LocaleUtils.toLocale(input);
        }
    }

    /**
     * Test localeLookupList() with valid inputs.
     */
    @Test
    public void testLocaleLookupListValidInputs() {
        Locale baseLocale = new Locale("en", "US");
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("en", "US"), new Locale("en", "US", "en_US"),
                new Locale("en", "CA"), new Locale("en", "CA", "en_CA"),
                new Locale("en", null), new Locale("en")
        );
        List<Locale> actualLocales = LocaleUtils.localeLookupList(baseLocale, null);
        assertEquals(expectedLocales, actualLocales);

        Locale variantLocale = new Locale("en", "US", "en_US_XX");
        expectedLocales = Arrays.asList(
                new Locale("en", "US", "en_US"), new Locale("en", "US"),
                new Locale("en", "CA"), new Locale("en", "CA", "en_CA"),
                new Locale("en", null), new Locale("en"),
                new Locale("en", "US", "en_US_XX")
        );
        actualLocales = LocaleUtils.localeLookupList(variantLocale, null);
        assertEquals(expectedLocales, actualLocales);

        Locale defaultLocale = new Locale("fr", "FR");
        expectedLocales = Arrays.asList(
                new Locale("en", "US"), new Locale("en", "US", "en_US"),
                new Locale("en", "CA"), new Locale("en", "CA", "en_CA"),
                new Locale("en", null), new Locale("en"),
                new Locale("en", "US", "en_US_XX"), new Locale("fr", "FR")
        );
        actualLocales = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Test localeLookupList() with null input.
     */
    @Test
    public void testLocaleLookupListNullInput() {
        List<Locale> actualLocales = LocaleUtils.localeLookupList(null, null);
        assertEquals(Collections.emptyList(), actualLocales);
    }

    /**
     * Test availableLocaleList().
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedLocales = LocaleUtils.availableLocaleList();
        List<Locale> actualLocales = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Test availableLocaleSet().
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = new HashSet<>(Locale.getAvailableLocales());
        Set<Locale> actualSet = LocaleUtils.availableLocaleSet();
        assertEquals(expectedSet, actualSet);
    }

    /**
     * Test isAvailableLocale().
     */
    @Test
    public void testIsAvailableLocale() {
        List<Locale> locales = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE);
        for (Locale locale : locales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        Locale invalidLocale = new Locale("en", "US", "invalid");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    /**
     * Test languagesByCountry().
     */
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("en", "CA"), new Locale("fr", "CA"), new Locale("es", "MX")
        );
        List<Locale> actualLocales = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Test countriesByLanguage().
     */
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("es", "MX"), new Locale("en", "CA"), new Locale("fr", "FR")
        );
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage("es");
        assertEquals(expectedLocales, actualLocales);
    }
}