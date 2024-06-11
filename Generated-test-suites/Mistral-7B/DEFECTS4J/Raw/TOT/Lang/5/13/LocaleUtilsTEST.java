package org.apache.commons.lang3.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;

/**
 * Tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale() method with valid input.
     */
    @Test
    public void testToLocaleValidInput() {
        List<String> testCases = Arrays.asList("en", "en_GB", "en_GB_xxx", "fr_CA", "fr_CA_xxx", "fr_FR", "fr_FR_xxx");
        for (String testCase : testCases) {
            Locale expected = new Locale(testCase.substring(0, 2), testCase.substring(3, 5));
            Locale actual = LocaleUtils.toLocale(testCase);
            assertEquals(expected, actual);
        }
    }

    /**
     * Test toLocale() method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        LocaleUtils.toLocale(null);
        LocaleUtils.toLocale("en_GB_xxx_");
        LocaleUtils.toLocale("es_AR_Mendoza");
        LocaleUtils.toLocale("es_AR_Mendoza_");
        LocaleUtils.toLocale("es_AR_Mendoza_xxx");
        LocaleUtils.toLocale("es_AR_Mendoza_xxx_");
    }

    /**
     * Test localeLookupList() method with valid input.
     */
    @Test
    public void testLocaleLookupListValidInput() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(expected, actual);
    }

    /**
     * Test localeLookupList() method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLocaleLookupListInvalidInput() {
        LocaleUtils.localeLookupList(null);
        LocaleUtils.localeLookupList(new Locale("es", "AR", "Mendoza"), null);
        LocaleUtils.localeLookupList(new Locale("es", "AR", "Mendoza"), new Locale("en"));
        LocaleUtils.localeLookupList(new Locale("es", "AR", "Mendoza"), new Locale("en"), null);
        LocaleUtils.localeLookupList(new Locale("es", "AR", "Mendoza"), new Locale("en"), new Locale("es"));
    }

    /**
     * Test availableLocaleList() method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    /**
     * Test availableLocaleSet() method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    /**
     * Test isAvailableLocale() method with available locales.
     */
    @Test
    public void testIsAvailableLocaleAvailableLocales() {
        for (Locale locale : LocaleUtils.availableLocaleList()) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }
    }

    /**
     * Test isAvailableLocale() method with invalid input.
     */
    @Test
    public void testIsAvailableLocaleInvalidInput() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "AR", "Mendoza")));
    }

    /**
     * Test languagesByCountry() method with valid input.
     */
    @Test
    public void testLanguagesByCountryValidInput() {
        List<Locale> expected = Arrays.asList(new Locale("en", "CA"), new Locale("en", "US"), new Locale("es", "ES"), new Locale("es", "MX"));
        List<Locale> actual = LocaleUtils.languagesByCountry("ES");
        assertEquals(expected, actual);
    }

    /**
     * Test languagesByCountry() method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountryInvalidInput() {
        LocaleUtils.languagesByCountry(null);
    }

    /**
     * Test countriesByLanguage() method with valid input.
     */
    @Test
    public void testCountriesByLanguageValidInput() {
        List<Locale> expected = Arrays.asList(new Locale("CA", "en"), new Locale("US", "en"), new Locale("ES", "es"), new Locale("MX", "es"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("es");
        assertEquals(expected, actual);
    }

    /**
     * Test countriesByLanguage() method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCountriesByLanguageInvalidInput() {
        LocaleUtils.countriesByLanguage(null);
    }
}