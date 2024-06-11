package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Test invalid input - empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid input - invalid length
        try {
            LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_GB_xxx_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid input - non-lowercase language code
        try {
            LocaleUtils.toLocale("En_GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid input - non-uppercase country code
        try {
            LocaleUtils.toLocale("en_gb");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid input - separator other than '_'
        try {
            LocaleUtils.toLocale("en_GB-xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        locale = new Locale("fr");
        expected = Arrays.asList(locale, new Locale("fr", ""), new Locale(""));
        actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        locale = new Locale("");
        expected = Collections.emptyList();
        actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        locale = null;
        expected = Collections.emptyList();
        actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Locale.getAvailableLocales();
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = new HashSet<Locale>(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en_US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr_CA");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("fr"), new Locale("en"));
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("CA"), new Locale("US"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, actual);
    }
}