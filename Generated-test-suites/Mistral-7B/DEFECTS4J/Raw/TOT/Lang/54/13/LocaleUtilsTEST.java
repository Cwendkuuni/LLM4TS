package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locales
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        expected = new Locale("fr", "");
        actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA");
        actual = LocaleUtils.toLocale("fr_CA");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA", "xxx");
        actual = LocaleUtils.toLocale("fr_CA_xxx");
        assertEquals(expected, actual);

        // Test invalid locales
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xxx_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xxx_xxx_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xxx_xxx_xxx_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale expected = new Locale("en", "US");
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.FRENCH, expected);

        // Test empty list
        List<Locale> emptyList = LocaleUtils.localeLookupList(null);
        assertTrue(emptyList.isEmpty());

        // Test no match
        List<Locale> noMatchList = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        assertTrue(noMatchList.isEmpty());

        // Test one match
        List<Locale> oneMatchList = LocaleUtils.localeLookupList(Locale.FRENCH, expected);
        assertEquals(1, oneMatchList.size());
        assertEquals(expected, oneMatchList.get(0));

        // Test multiple matches
        List<Locale> multipleMatchesList = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.US);
        assertEquals(2, multipleMatchesList.size());
        assertTrue(multipleMatchesList.contains(new Locale("fr", "CA")));
        assertTrue(multipleMatchesList.contains(expected));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH);
        List<Locale> actual = LocaleUtils.availableLocaleList();

        // Test empty list
        List<Locale> emptyList = LocaleUtils.availableLocaleList();
        assertTrue(emptyList.isEmpty());

        // Test same list
        List<Locale> sameList = LocaleUtils.availableLocaleList();
        assertEquals(expected, sameList);

        // Test different order
        List<Locale> differentOrderList = LocaleUtils.availableLocaleList();
        Collections.shuffle(differentOrderList);
        assertTrue(expected.equals(differentOrderList));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        List<Locale> emptyList = LocaleUtils.languagesByCountry(null);
        assertTrue(emptyList.isEmpty());

        // Test available languages for a country
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, actual);

        // Test empty list for an unsupported country
        List<Locale> emptyList2 = LocaleUtils.languagesByCountry("ZW");
        assertTrue(emptyList2.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty list
        List<Locale> emptyList = LocaleUtils.countriesByLanguage(null);
        assertTrue(emptyList.isEmpty());

        // Test available countries for a language
        List<Locale> expected = Arrays.asList(Locale.CANADA, Locale.SWITZERLAND, Locale.AUSTRIA);
        List<Locale> actual = LocaleUtils.countriesByLanguage("ENGLISH");
        assertEquals(expected, actual);

        // Test empty list for an unsupported language
        List<Locale> emptyList2 = LocaleUtils.countriesByLanguage("ZW");
        assertTrue(emptyList2.isEmpty());
    }
}