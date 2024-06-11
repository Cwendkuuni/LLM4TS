package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test class for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Test method for {@link LocaleUtils#toLocale(String)}.
     */
    @Test
    public void testToLocale() {
        // Test valid locales
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr", "FR"), LocaleUtils.toLocale("fr_FR"));
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));

        // Test invalid locale formats
        try {
            LocaleUtils.toLocale("en_FR");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_FR");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_FR_v1.0");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_FR_v1.0_jdk1.3");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_FR_v1.0_jdk1.4");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_FR_v1.0_jdk1.5");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test null input
        assertNull(LocaleUtils.toLocale(null));
    }

    /**
     * Test method for {@link LocaleUtils#localeLookupList(Locale)}.
     */
    @Test
    public void testLocaleLookupList() {
        // Test empty input
        List emptyList = LocaleUtils.localeLookupList(null);
        assertEquals(Collections.emptyList(), emptyList);

        // Test valid locale
        List expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en"));
        List actualList = LocaleUtils.localeLookupList(new Locale("en", "US"));
        assertEquals(expectedList, actualList);

        // Test invalid locale
        expectedList = Collections.singletonList(new Locale("en"));
        actualList = LocaleUtils.localeLookupList(new Locale("fr"));
        assertEquals(expectedList, actualList);
    }

    /**
     * Test method for {@link LocaleUtils#availableLocaleList()}.
     */
    @Test
    public void testAvailableLocaleList() {
        List availableLocaleList = LocaleUtils.availableLocaleList();
        assertFalse(availableLocaleList.isEmpty());

        // Test empty list
        List emptyList = Collections.emptyList();
        List expectedList = Collections.singletonList(availableLocaleList);
        List actualList = LocaleUtils.localeLookupList(new Locale("", ""));
        assertEquals(expectedList, actualList);
    }

    /**
     * Test method for {@link LocaleUtils#isAvailableLocale(Locale)}.
     */
    @Test
    public void testIsAvailableLocale() {
        // Test available locale
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));

        // Test invalid locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "XX")));
    }

    /**
     * Test method for {@link LocaleUtils#languagesByCountry(String)}.
     */
    @Test
    public void testLanguagesByCountry() {
        // Test valid country code
        List expectedList = Arrays.asList(new Locale("nl", "BE"), new Locale("fr", "BE"), new Locale("de", "BE"));
        List actualList = LocaleUtils.languagesByCountry("BE");
        assertEquals(expectedList, actualList);

        // Test invalid country code
        List emptyList = Collections.emptyList();
        List actualList2 = LocaleUtils.languagesByCountry("ZZ");
        assertEquals(emptyList, actualList2);

        // Test null input
        List emptyList2 = Collections.emptyList();
        List actualList3 = LocaleUtils.languagesByCountry(null);
        assertEquals(emptyList2, actualList3);
    }

    /**
     * Test method for {@link LocaleUtils#countriesByLanguage(String)}.
     */
    @Test
    public void testCountriesByLanguage() {
        // Test valid language code
        List expectedList = Arrays.asList(new Locale("es", "ES"), new Locale("es", "MX"), new Locale("es", "AR"));
        List actualList = LocaleUtils.countriesByLanguage("es");
        assertEquals(expectedList, actualList);

        // Test invalid language code
        List emptyList = Collections.emptyList();
        List actualList2 = LocaleUtils.countriesByLanguage("ZZ");
        assertEquals(emptyList, actualList2);

        // Test null input
        List emptyList2 = Collections.emptyList();
        List actualList3 = LocaleUtils.countriesByLanguage(null);
        assertEquals(emptyList2, actualList3);
    }
}