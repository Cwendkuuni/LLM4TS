package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid input.
     */
    @Test
    public void testToLocale() {
        // valid test cases
        assertEquals("en", LocaleUtils.toLocale("en").getLanguage());
        assertEquals("en_GB", LocaleUtils.toLocale("en_GB").toString());
        assertEquals("en_GB_US", LocaleUtils.toLocale("en_GB_US").toString());

        // test cases with empty string
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale(null));

        // test cases with invalid input
        try {
            LocaleUtils.toLocale("AbCd");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_ab");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_ab");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_ab_US");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_ab_US_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_ab_US_x");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test toLocale method with various input scenarios.
     */
    @Test
    public void testToLocaleScenarios() {
        // test cases with empty string
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale(null));

        // test cases with invalid input
        assertNull(LocaleUtils.toLocale("AbCd"));
        try {
            LocaleUtils.toLocale("en_GB_ab");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_ab");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_ab_US");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_ab_US_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_ab_US_x");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // test cases with valid input
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR"));
    }

    /**
     * Test localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);

        // test list content
        assertEquals(3, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));

        // test list order
        List<Locale> list2 = LocaleUtils.localeLookupList(locale, Locale.ENGLISH);
        assertEquals(4, list2.size());
        assertEquals(locale, list2.get(0));
        assertEquals(new Locale("fr", "CA"), list2.get(1));
        assertEquals(new Locale("fr"), list2.get(2));
        assertEquals(Locale.ENGLISH, list2.get(3));

        // test empty list
        assertEquals(0, LocaleUtils.localeLookupList(Locale.ENGLISH).size());
    }

    /**
     * Test localeLookupList method with various input scenarios.
     */
    @Test
    public void testLocaleLookupListScenarios() {
        // test empty input
        assertEquals(0, LocaleUtils.localeLookupList(null).size());
        assertEquals(0, LocaleUtils.localeLookupList(Locale.ENGLISH).size());

        // test input with locale with variant
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(3, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));

        // test input with locale without variant
        Locale locale2 = new Locale("fr", "CA");
        List<Locale> list2 = LocaleUtils.localeLookupList(locale2);
        assertEquals(2, list2.size());
        assertEquals(locale2, list2.get(0));
        assertEquals(new Locale("fr"), list2.get(1));

        // test input with locale without country
        Locale locale3 = new Locale("fr");
        List<Locale> list3 = LocaleUtils.localeLookupList(locale3);
        assertEquals(1, list3.size());
        assertEquals(locale3, list3.get(0));

        // test input with default locale
        List<Locale> list4 = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.FRENCH);
        assertEquals(3, list4.size());
        assertEquals(Locale.ENGLISH, list4.get(0));
        assertEquals(Locale.FRENCH, list4.get(1));
        assertEquals(Locale.FRENCH, list4.get(2));
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(184, availableLocales.size());
    }

    /**
     * Test availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertEquals(184, availableLocales.size());
    }

    /**
     * Test isAvailableLocale method.
     */
    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();

        // test valid locale
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "GB")));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));

        // test invalid locale
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr", "CA", "xxx")));
    }

    /**
     * Test languagesByCountry method.
     */
    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertEquals(2, languages.size());
        assertEquals(Locale.ENGLISH, languages.get(0));
        assertEquals(Locale.Spanish, languages.get(1));

        languages = LocaleUtils.languagesByCountry("GB");
        assertEquals(3, languages.size());
        assertEquals(Locale.ENGLISH, languages.get(0));
        assertEquals(Locale.WELSH, languages.get(1));
        assertEquals(Locale.SCOTS_GAELIC, languages.get(2));

        List<Locale> languages2 = LocaleUtils.languagesByCountry("FR");
        assertEquals(27, languages2.size());
        // some assertions omitted for brevity
    }

    /**
     * Test countriesByLanguage method.
     */
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(25, countries.size());
        assertEquals(Locale.US, countries.get(0));
        assertEquals(Locale.UK, countries.get(1));
        assertEquals(Locale.CA, countries.get(2));
        assertEquals(Locale.IE, countries.get(3));
        // some assertions omitted for brevity

        countries = LocaleUtils.countriesByLanguage("fr");
        assertEquals(10, countries.size());
        assertEquals(Locale.FRANCE, countries.get(0));
        assertEquals(Locale.BELGIUM, countries.get(1));
        assertEquals(Locale.SWITZERLAND, countries.get(2));
        // some assertions omitted for brevity
    }
}