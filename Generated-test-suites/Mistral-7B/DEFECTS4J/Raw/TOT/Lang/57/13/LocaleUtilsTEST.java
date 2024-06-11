package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test case for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Test method for {@link LocaleUtils#toLocale(String)}.
     */
    @Test
    public void testToLocale() {
        // Test valid input
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr", "FR"), LocaleUtils.toLocale("fr_FR"));
        assertEquals(new Locale("de", ""), LocaleUtils.toLocale("de"));
        assertEquals(new Locale("es", "ES"), LocaleUtils.toLocale("es_ES"));
        assertEquals(new Locale("it", "IT"), LocaleUtils.toLocale("it_IT"));

        // Test empty input
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale(null));

        // Test invalid input
        try {
            LocaleUtils.toLocale("es_ES_XXX");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XXX");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("enZZ");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_XXX");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_en");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_en_US");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link LocaleUtils#localeLookupList(Locale, Locale)}.
     */
    @Test
    public void testLocaleLookupList() {
        // Test empty list
        List<Locale> lookupList = LocaleUtils.localeLookupList(null);
        assertTrue(lookupList.isEmpty());
        assertEquals(Collections.EMPTY_LIST, lookupList);

        // Test default locale
        Locale defaultLocale = Locale.getDefault();
        List<Locale> lookupList1 = LocaleUtils.localeLookupList(Locale.ENGLISH);
        List<Locale> lookupList2 = LocaleUtils.localeLookupList(Locale.FRENCH, defaultLocale);
        List<Locale> lookupList3 = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);

        assertEquals(2, lookupList1.size());
        assertTrue(lookupList1.contains(Locale.ENGLISH));
        assertTrue(lookupList1.contains(defaultLocale));

        assertEquals(3, lookupList2.size());
        assertTrue(lookupList2.contains(Locale.FRENCH));
        assertTrue(lookupList2.contains(Locale.FRENCH));
        assertTrue(lookupList2.contains(defaultLocale));

        assertEquals(3, lookupList3.size());
        assertTrue(lookupList3.contains(Locale.FRENCH));
        assertTrue(lookupList3.contains(Locale.ENGLISH));
        assertTrue(lookupList3.contains(defaultLocale));

        // Test specific locale
        Locale locale = new Locale("es", "ES");
        List<Locale> lookupList4 = LocaleUtils.localeLookupList(locale);
        assertEquals(4, lookupList4.size());
        assertTrue(lookupList4.contains(locale));
        assertTrue(lookupList4.contains(new Locale("es", "ES")));
        assertTrue(lookupList4.contains(new Locale("es", "")));
        assertTrue(lookupList4.contains(defaultLocale));
    }

    /**
     * Test method for {@link LocaleUtils#availableLocaleList()}.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertTrue(locales.size() > 0);
        assertFalse(locales.isEmpty());
    }

    /**
     * Test method for {@link LocaleUtils#availableLocaleSet()}.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertTrue(locales.size() > 0);
        assertFalse(locales.isEmpty());
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

        // Test empty locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));

        // Test null locale
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    /**
     * Test method for {@link LocaleUtils#languagesByCountry(String)}.
     */
    @Test
    public void testLanguagesByCountry() {
        // Test empty country
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        assertTrue(languages.isEmpty());
        assertEquals(Collections.EMPTY_LIST, languages);

        // Test valid country
        List<Locale> languages1 = LocaleUtils.languagesByCountry("US");
        assertEquals(2, languages1.size());
        assertTrue(languages1.contains(new Locale("en", "US")));
        assertTrue(languages1.contains(new Locale("es", "US")));

        // Test invalid country
        List<Locale> languages2 = LocaleUtils.languagesByCountry("XX");
        assertTrue(languages2.isEmpty());
        assertEquals(Collections.EMPTY_LIST, languages2);
    }

    /**
     * Test method for {@link LocaleUtils#countriesByLanguage(String)}.
     */
    @Test
    public void testCountriesByLanguage() {
        // Test empty language
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertTrue(countries.isEmpty());
        assertEquals(Collections.EMPTY_LIST, countries);

        // Test valid language
        List<Locale> countries1 = LocaleUtils.countriesByLanguage("en");
        assertEquals(3, countries1.size());
        assertTrue(countries1.contains(new Locale("en", "US")));
        assertTrue(countries1.contains(new Locale("en", "GB")));
        assertTrue(countries1.contains(new Locale("en", "CA")));

        // Test invalid language
        List<Locale> countries2 = LocaleUtils.countriesByLanguage("XX");
        assertTrue(countries2.isEmpty());
        assertEquals(Collections.EMPTY_LIST, countries2);
    }
}