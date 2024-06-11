package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for {@link LocaleUtils} class.
 */
public class LocaleUtilsTest {

    /**
     * Test for {@code toLocale(String)} method.
     */
    @Test
    public void testToLocale() {
        // Test valid cases
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("en", LocaleUtils.toLocale("en").toString());

        // Test empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test null input
        assertNull(LocaleUtils.toLocale(null));

        // Test invalid format
        try {
            LocaleUtils.toLocale("en_GB_a");
            assertFalse("Expected IllegalArgumentException", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_aGB");
            assertFalse("Expected IllegalArgumentException", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_a");
            assertFalse("Expected IllegalArgumentException", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_a");
            assertFalse("Expected IllegalArgumentException", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_a_a");
            assertFalse("Expected IllegalArgumentException", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US__");
            assertFalse("Expected IllegalArgumentException", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en__US");
            assertFalse("Expected IllegalArgumentException", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US-");
            assertFalse("Expected IllegalArgumentException", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("-en_US");
            assertFalse("Expected IllegalArgumentException", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for {@code localeLookupList(Locale)} method.
     */
    @Test
    public void testLocaleLookupList() {
        // Test empty list
        List<Locale> list = LocaleUtils.localeLookupList(null);
        assertTrue(list.isEmpty());

        // Test valid locale
        Locale locale = new Locale("fr", "CA");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), Locale.getDefault());
        List<Locale> list1 = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, list1);

        // Test default locale
        List<Locale> list2 = LocaleUtils.localeLookupList(Locale.getDefault());
        assertTrue(list2.isEmpty());

        // Test invalid locale
        Locale invalidLocale = null;
        List<Locale> list3 = LocaleUtils.localeLookupList(invalidLocale);
        assertTrue(list3.isEmpty());
    }

    /**
     * Test for {@code availableLocaleList()} method.
     */
    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertTrue(list.isEmpty());

        // Test valid list
        List<Locale> expectedList = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.RUSSIAN, Locale.SPANISH);
        List<Locale> list1 = LocaleUtils.availableLocaleList();
        assertEquals(expectedList, list1);
    }

    /**
     * Test for {@code availableLocaleSet()} method.
     */
    @Test
    public void testAvailableLocaleSet() {
        // Test empty set
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertTrue(set.isEmpty());

        // Test valid set
        Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.RUSSIAN, Locale.SPANISH));
        Set<Locale> set1 = LocaleUtils.availableLocaleSet();
        assertEquals(expectedSet, set1);
    }

    /**
     * Test for {@code languagesByCountry(String)} method.
     */
    @Test
    public void testLanguagesByCountry() {
        // Test empty country
        List<Locale> list = LocaleUtils.languagesByCountry(null);
        assertTrue(list.isEmpty());

        // Test valid country
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"), new Locale("es", "ES"), new Locale("it", "IT"));
        List<Locale> list1 = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedList, list1);

        // Test invalid country
        List<Locale> list2 = LocaleUtils.languagesByCountry("ZZ");
        assertTrue(list2.isEmpty());
    }

    /**
     * Test for {@code countriesByLanguage(String)} method.
     */
    @Test
    public void testCountriesByLanguage() {
        // Test empty language
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        assertTrue(list.isEmpty());

        // Test valid language
        List<Locale> expectedList = Arrays.asList(new Locale("CN", "Zhongwen"), new Locale("FR", "France"), new Locale("DE", "Germany"), new Locale("IT", "Italy"), new Locale("JP", "Japan"), new Locale("KR", "Korea"), new Locale("RU", "Russia"), new Locale("ES", "Spain"));
        List<Locale> list1 = LocaleUtils.countriesByLanguage("en");
        assertEquals(expectedList, list1);

        // Test invalid language
        List<Locale> list2 = LocaleUtils.countriesByLanguage("ZZ");
        assertTrue(list2.isEmpty());
    }
}