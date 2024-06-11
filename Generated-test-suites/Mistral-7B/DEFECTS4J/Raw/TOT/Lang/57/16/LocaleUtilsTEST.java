package org.apache.commons.lang.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_FR", LocaleUtils.toLocale("fr_FR").toString());
        assertEquals("en", LocaleUtils.toLocale("en").toString());

        // Test invalid inputs
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException for empty string input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_aB");
            fail("Expected IllegalArgumentException for invalid language code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_aB_cD");
            fail("Expected IllegalArgumentException for invalid country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_aB_cD_eF");
            fail("Expected IllegalArgumentException for invalid variant code");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid inputs
        List<Locale> expected = Arrays.asList(new Locale("en_US"), new Locale("en_CA"), new Locale("en"));
        List<Locale> result = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(expected, result);

        expected = Arrays.asList(new Locale("fr_CA"), new Locale("fr_FR"), new Locale("fr"), new Locale("en"));
        result = LocaleUtils.localeLookupList(new Locale("fr_CA"), Locale.ENGLISH);
        assertEquals(expected, result);

        // Test empty input
        result = LocaleUtils.localeLookupList(null);
        assertEquals(Collections.EMPTY_LIST, result);

        // Test defaultLocale as input
        result = LocaleUtils.localeLookupList(Locale.US, Locale.ENGLISH);
        assertEquals(Collections.singletonList(Locale.ENGLISH), result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.RUSSIAN, Locale.SPANISH);
        assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid", "invalid")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("es"));
        assertEquals(expected, result);

        result = LocaleUtils.languagesByCountry("CN");
        expected = Arrays.asList(new Locale("zh"), new Locale("zh_Hans"), new Locale("zh_Hant"));
        assertEquals(expected, result);

        result = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.EMPTY_LIST, result);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(new Locale("US"), new Locale("CA"), new Locale("GB"), new Locale("IE"));
        assertEquals(expected, result);

        result = LocaleUtils.countriesByLanguage("fr");
        expected = Arrays.asList(new Locale("FR"), new Locale("BE"), new Locale("CH"));
        assertEquals(expected, result);

        result = LocateUtils.countriesByLanguage(null);
        assertEquals(Collections.EMPTY_LIST, result);
    }
}