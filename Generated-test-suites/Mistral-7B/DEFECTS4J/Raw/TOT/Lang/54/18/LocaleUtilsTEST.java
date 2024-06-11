package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toLanguageTag());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toLanguageTag());
        assertEquals("es_ES", LocaleUtils.toLocale("es_ES").toLanguageTag());

        // Test invalid input - language code
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }

        try {
            LocaleUtils.toLocale("XY");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }

        try {
            LocaleUtils.toLocale("en_XY");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }

        try {
            LocaleUtils.toLocale("en_AB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }

        // Test invalid input - country code
        try {
            LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }

        try {
            LocaleUtils.toLocale("en_AB_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }

        try {
            LocaleUtils.toLocale("en_ABC");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }

        // Test valid input - variant
        assertEquals("en_US_POSIX", LocaleUtils.toLocale("en_US_POSIX").toLanguageTag());
        assertEquals("fr_CA_frCA", LocaleUtils.toLocale("fr_CA_frCA").toLanguageTag());
        assertEquals("es_ES_esES", LocaleUtils.toLocale("es_ES_esES").toLanguageTag());

        // Test invalid input - variant length
        try {
            LocaleUtils.toLocale("en_US_POSIX_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }

        try {
            LocaleUtils.toLocale("en_US_POSIX_AB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }

        // Test valid input - empty
        assertEquals("", LocaleUtils.toLocale("").toLanguageTag());
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en"));
        List<Locale> lookupList = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(expectedList, lookupList);

        expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        lookupList = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.US);
        assertEquals(expectedList, lookupList);

        expectedList = Collections.singletonList(Locale.US);
        lookupList = LocaleUtils.localeLookupList(Locale.US, Locale.US);
        assertEquals(expectedList, lookupList);

        expectedList = Collections.emptyList();
        lookupList = LocaleUtils.localeLookupList(null);
        assertEquals(expectedList, lookupList);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test available locales
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SPANISH));

        // Test unavailable locales
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "XX")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr", "XX")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "XX")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("en", "CA"));
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("en", "US"));
        expectedList.add(new Locale("fr", "FR"));

        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedList, languagesByCountry);

        expectedList.clear();
        expectedList.add(new Locale("en"));
        expectedList.add(new Locale("de"));

        languagesByCountry = LocaleUtils.languagesByCountry("US");
        assertEquals(expectedList, languagesByCountry);

        expectedList.clear();
        languagesByCountry = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.EMPTY_LIST, languagesByCountry);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("CA", "en"));
        expectedList.add(new Locale("CA", "fr"));
        expectedList.add(new Locale("MX", "es"));
        expectedList.add(new Locale("ES", "es"));

        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        assertEquals(expectedList, countriesByLanguage);

        expectedList.clear();
        countriesByLanguage = LocaleUtils.countriesByLanguage("de");
        assertEquals(Collections.EMPTY_LIST, countriesByLanguage);

        expectedList.clear();
        countriesByLanguage = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.EMPTY_LIST, countriesByLanguage);
    }
}