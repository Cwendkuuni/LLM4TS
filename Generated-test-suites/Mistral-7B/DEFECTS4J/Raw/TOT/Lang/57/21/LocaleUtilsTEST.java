package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Tests for {@link LocaleUtils} class.
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

        expected = new Locale("es", "ES");
        actual = LocaleUtils.toLocale("es_ES");
        assertEquals(expected, actual);

        expected = new Locale("fr", "");
        actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);
    }

    /**
     * Test toLocale method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        LocaleUtils.toLocale(null);

        LocaleUtils.toLocale("en_GB_xx"); // invalid variant
        LocaleUtils.toLocale("en_XX"); // unknown country code
        LocaleUtils.toLocale("es_XX_xx"); // invalid country code and variant
    }

    /**
     * Test localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupListValidInput() {
        List<Locale> expected = Arrays.asList(new Locale("en", "CA"), new Locale("en", "US"), new Locale("en"));
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("en", "CA"), new Locale("en"));
        assertEquals(expected, actual);

        expected = Arrays.asList(new Locale("fr"), new Locale("fr_CA"));
        actual = LocaleUtils.localeLookupList(new Locale("fr"));
        assertEquals(expected, actual);

        expected = Collections.singletonList(new Locale("en"));
        actual = LocaleUtils.localeLookupList(null);
        assertEquals(expected, actual);
    }

    /**
     * Test localeLookupList method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLocaleLookupListInvalidInput() {
        LocaleUtils.localeLookupList(null, null);

        LocaleUtils.localeLookupList(new Locale("fr"), null);
        LocaleUtils.localeLookupList(new Locale("en", "CA"), null);
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
     * Test isAvailableLocale method with valid input.
     */
    @Test
    public void testIsAvailableLocaleValidInput() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("es", "ES")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr")));
    }

    /**
     * Test isAvailableLocale method with invalid input.
     */
    @Test
    public void testIsAvailableLocaleInvalidInput() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "XX")));
    }

    /**
     * Test languagesByCountry method with valid input.
     */
    @Test
    public void testLanguagesByCountryValidInput() {
        List<Locale> expected = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, actual);

        expected = Collections.singletonList(new Locale("en"));
        actual = LocaleUtils.languagesByCountry("GB");
        assertEquals(expected, actual);

        List<Locale> empty = Collections.emptyList();
        actual = LocaleUtils.languagesByCountry(null);
        assertEquals(empty, actual);
    }

    /**
     * Test languagesByCountry method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountryInvalidInput() {
        LocaleUtils.languagesByCountry(null);
        LocaleUtils.languagesByCountry("XX");
    }

    /**
     * Test countriesByLanguage method with valid input.
     */
    @Test
    public void testCountriesByLanguageValidInput() {
        List<Locale> expected = Arrays.asList(new Locale("CA"), new Locale("US"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, actual);

        expected = Collections.singletonList(new Locale("ES"));
        actual = LocaleUtils.countriesByLanguage("es");
        assertEquals(expected, actual);

        List<Locale> empty = Collections.emptyList();
        actual = LocaleUtils.countriesByLanguage(null);
        assertEquals(empty, actual);
    }

    /**
     * Test countriesByLanguage method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCountriesByLanguageInvalidInput() {
        LocaleUtils.countriesByLanguage(null);
        LocaleUtils.countriesByLanguage("XX");
    }

}