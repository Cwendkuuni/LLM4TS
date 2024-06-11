package org.apache.commons.lang.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Tests for LocaleUtils class.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid input.
     */
    @Test
    public void testToLocale_ValidInput() {
        // Test with 2-letter language code and empty country code
        Locale expected = new Locale("en", "");
        Locale actual = LocaleUtils.toLocale("en");
        assertEquals(expected, actual);

        // Test with 2-letter language code, 2-letter country code and empty variant
        expected = new Locale("en", "CA");
        actual = LocaleUtils.toLocale("en_CA");
        assertEquals(expected, actual);

        // Test with 2-letter language code, 2-letter country code and variant
        expected = new Locale("en", "CA", "en_CA");
        actual = LocaleUtils.toLocale("en_CA_en_CA");
        assertEquals(expected, actual);
    }

    /**
     * Test toLocale method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput() {
        // Test with null input
        LocaleUtils.toLocale(null);

        // Test with 1-letter language code or country code
        LocaleUtils.toLocale("e");

        // Test with less than 2 letters for language or country code
        LocaleUtils.toLocale("en_");

        // Test with more than 2 letters for language or country code
        LocaleUtils.toLocale("en_GB_UK");

        // Test with an invalid separator for the country code and variant
        LocaleUtils.toLocale("en_GB_UK_invalid");

        // Test with empty variant
        LocaleUtils.toLocale("en_GB_");
    }

    /**
     * Test localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupList_ValidInput() {
        // Test with null input
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertEquals(expected, actual);

        // Test with a valid locale
        Locale locale = new Locale("fr", "CA", "fr_CA_fr_CA");
        expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(expected, actual);

        // Test with a default locale only
        expected = Arrays.asList(new Locale("en"));
        actual = LocaleUtils.localeLookupList(null, new Locale("en"));
        assertEquals(expected, actual);
    }

    /**
     * Test localeLookupList method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLocaleLookupList_InvalidInput() {
        // Test with a null locale
        LocaleUtils.localeLookupList(null, null);
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        Collection<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    /**
     * Test availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Collection<Locale> actual = Collections.newSetFromMap(new HashMap<Locale, Boolean>() {{
            putAll(Locale.getAvailableLocales());
        }});
        assertEquals(expected, actual);
    }

    /**
     * Test isAvailableLocale method with valid input.
     */
    @Test
    public void testIsAvailableLocale_ValidInput() {
        // Test with a valid locale
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test with an invalid locale
        Locale invalidLocale = new Locale("fr", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    /**
     * Test languagesByCountry method with valid input.
     */
    @Test
    public void testLanguagesByCountry_ValidInput() {
        List<Locale> expected = LocaleUtils.languagesByCountry("CA");
        List<Locale> actual = Locale.getAvailableLocales();
        List<Locale> filteredLocales = new ArrayList<>();
        for (Locale locale : actual) {
            if ("CA".equals(locale.getCountry())) {
                filteredLocales.add(locale);
            }
        }
        assertEquals(expected, filteredLocales);
    }

    /**
     * Test languagesByCountry method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountry_InvalidInput() {
        LocaleUtils.languagesByCountry(null);
    }

    /**
     * Test countriesByLanguage method with valid input.
     */
    @Test
    public void testCountriesByLanguage_ValidInput() {
        List<Locale> expected = LocaleUtils.countriesByLanguage("en");
        List<Locale> actual = Locale.getAvailableLocales();
        List<Locale> filteredLocales = new ArrayList<>();
        for (Locale locale : actual) {
            if ("en".equals(locale.getLanguage())) {
                filteredLocales.add(locale);
            }
        }
        assertEquals(expected, filteredLocales);
    }

    /**
     * Test countriesByLanguage method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCountriesByLanguage_InvalidInput() {
        LocaleUtils.countriesByLanguage(null);
    }
}