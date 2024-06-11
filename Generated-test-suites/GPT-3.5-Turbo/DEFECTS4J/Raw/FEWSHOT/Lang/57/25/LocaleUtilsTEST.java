package org.apache.commons.lang;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale formats
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Test invalid locale formats
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("e");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_G");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_gb");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_GB_");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_GB_xxx_");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test null input
        List<Locale> expected = Collections.emptyList();
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertEquals(expected, result);

        // Test language-only locale
        Locale locale1 = new Locale("fr");
        expected = Arrays.asList(locale1, new Locale("fr", ""));
        result = LocaleUtils.localeLookupList(locale1);
        assertEquals(expected, result);

        // Test language and country locale
        Locale locale2 = new Locale("fr", "CA");
        expected = Arrays.asList(locale2, new Locale("fr"), new Locale("en"));
        result = LocaleUtils.localeLookupList(locale2, new Locale("en"));
        assertEquals(expected, result);

        // Test language, country, and variant locale
        Locale locale3 = new Locale("fr", "CA", "xxx");
        expected = Arrays.asList(locale3, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        result = LocaleUtils.localeLookupList(locale3, new Locale("en"));
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));

        Locale locale2 = new Locale("zz", "ZZ");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", "GB"));
        expected.add(new Locale("en", ""));
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertEquals(expected, result);

        expected = Collections.emptyList();
        result = LocaleUtils.languagesByCountry(null);
        assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", "GB"));
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, result);

        expected = Collections.emptyList();
        result = LocaleUtils.countriesByLanguage(null);
        assertEquals(expected, result);
    }
}