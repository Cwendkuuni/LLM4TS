package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Valid test cases
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr", "FR"), LocaleUtils.toLocale("fr_FR"));
        assertEquals(new Locale("es", "ES"), LocaleUtils.toLocale("es_ES"));

        // Empty string test case
        assertNull(LocaleUtils.toLocale(""));

        // Invalid length test cases
        try {
            LocaleUtils.toLocale("en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_GB_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid format test cases
        try {
            LocaleUtils.toLocale("en_US_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_xx_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US__");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_xx__");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_xx__xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Empty country code test case
        try {
            LocaleUtils.toLocale("en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Empty language code test case
        try {
            LocaleUtils.toLocale("", "US");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Missing separator test case
        try {
            LocaleUtils.toLocale("enUS");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Null input test case
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        List list = LocaleUtils.localeLookupList(null);
        assertEquals(0, list.size());

        // Test default locale only
        Locale defaultLocale = new Locale("en", "US");
        list = LocaleUtils.localeLookupList(defaultLocale);
        assertEquals(1, list.size());
        assertEquals(defaultLocale, list.get(0));

        // Test locale with variant
        Locale localeWithVariant = new Locale("fr", "CA", "var");
        list = LocaleUtils.localeLookupList(localeWithVariant, defaultLocale);
        assertEquals(3, list.size());
        assertEquals(localeWithVariant, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(defaultLocale, list.get(2));

        // Test locale with country
        Locale localeWithCountry = new Locale("fr", "CA");
        list = LocaleUtils.localeLookupList(localeWithCountry, defaultLocale);
        assertEquals(2, list.size());
        assertEquals(localeWithCountry, list.get(0));
        assertEquals(defaultLocale, list.get(1));

        // Test locale with language
        Locale localeWithLanguage = new Locale("fr");
        list = LocaleUtils.localeLookupList(localeWithLanguage, defaultLocale);
        assertEquals(2, list.size());
        assertEquals(localeWithLanguage, list.get(0));
        assertEquals(defaultLocale, list.get(1));

        // Test locale with country and language
        Locale localeWithCountryAndLanguage = new Locale("fr", "CA", "var");
        list = LocaleUtils.localeLookupList(localeWithCountryAndLanguage, defaultLocale);
        assertEquals(4, list.size());
        assertEquals(localeWithCountryAndLanguage, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(localeWithCountry, list.get(2));
        assertEquals(defaultLocale, list.get(3));

        // Test locale with country, language, and variant
        Locale localeWithCountryLanguageAndVariant = new Locale("fr", "CA", "var_var");
        list = LocaleUtils.localeLookupList(localeWithCountryLanguageAndVariant, defaultLocale);
        assertEquals(5, list.size());
        assertEquals(localeWithCountryLanguageAndVariant, list.get(0));
        assertEquals(new Locale("fr", "CA", "var"), list.get(1));
        assertEquals(new Locale("fr", "CA"), list.get(2));
        assertEquals(localeWithCountry, list.get(3));
        assertEquals(defaultLocale, list.get(4));
    }

    @Test
    public void testAvailableLocaleList() {
        List list = LocaleUtils.availableLocaleList();
        assertTrue(list.size() > 0);

        // Test empty list
        List emptyList = Collections.emptyList();
        assertFalse(list.equals(emptyList));

        // Test contains test
        assertTrue(list.contains(new Locale("en", "US")));
        assertTrue(list.contains(new Locale("fr", "FR")));
        assertTrue(list.contains(new Locale("es", "ES")));

        // Test contains failed test
        assertFalse(list.contains(new Locale("en", "XX")));
        assertFalse(list.contains(new Locale("fr", "XX")));
        assertFalse(list.contains(new Locale("es", "XX")));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set set = LocaleUtils.availableLocaleSet();
        assertTrue(set.size() > 0);

        // Test empty set
        Set emptySet = Collections.emptySet();
        assertFalse(set.equals(emptySet));

        // Test contains test
        assertTrue(set.contains(new Locale("en", "US")));
        assertTrue(set.contains(new Locale("fr", "FR")));
        assertTrue(set.contains(new Locale("es", "ES")));

        // Test contains failed test
        assertFalse(set.contains(new Locale("en", "XX")));
        assertFalse(set.contains(new Locale("fr", "XX")));
        assertFalse(set.contains(new Locale("es", "XX")));
    }

    @Test
    public void testIsAvailableLocale() {
        List list = LocaleUtils.availableLocaleList();

        // Test known locale
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));

        // Test unknown locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "XX")));

        // Test null input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test empty list
        List emptyList = Collections.emptyList();
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "US"), emptyList));
    }

    @Test
    public void testLanguagesByCountry() {
        List languages = LocaleUtils.languagesByCountry("US");
        assertTrue(languages.size() > 0);

        // Test empty list
        List emptyList = Collections.emptyList();
        assertFalse(languages.equals(emptyList));

        // Test contains test
        assertTrue(languages.contains(new Locale("en", "US")));

        // Test contains failed test
        assertFalse(languages.contains(new Locale("es", "US")));

        // Test null input
        assertNull(LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List countries = LocaleUtils.countriesByLanguage("en");
        assertTrue(countries.size() > 0);

        // Test empty list
        List emptyList = Collections.emptyList();
        assertFalse(countries.equals(emptyList));

        // Test contains test
        assertTrue(countries.contains(new Locale("US", "en")));

        // Test contains failed test
        assertFalse(countries.contains(new Locale("CA", "en")));

        // Test null input
        assertNull(LocaleUtils.countriesByLanguage(null));
    }
}