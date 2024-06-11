package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertArrayEquals;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        expected = new Locale("fr", "");
        actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA");
        actual = LocaleUtils.toLocale("fr_CA");
        assertEquals(expected, actual);

        expected = new Locale("es", "ES", "Castilian");
        actual = LocaleUtils.toLocale("es_ES_Castilian");
        assertEquals(expected, actual);

        // Validate null input
        assertNull(LocaleUtils.toLocale(null));

        // Validate invalid format
        try {
            LocaleUtils.toLocale("en_GB_xxx");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB--");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_--");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_en");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_EN");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB___");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB___");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_...");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_...");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_-");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_-");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_-");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_-");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List expected = LocaleUtils.localeLookupList(locale);
        List actual = LocaleUtils.localeLookupList(locale);

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Locale eLocale = (Locale) expected.get(i);
            Locale aLocale = (Locale) actual.get(i);
            assertEquals(eLocale, aLocale);
        }

        locale = new Locale("en");
        expected = LocaleUtils.localeLookupList(locale, null);
        actual = LocaleUtils.localeLookupList(locale);

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Locale eLocale = (Locale) expected.get(i);
            Locale aLocale = (Locale) actual.get(i);
            assertEquals(eLocale, aLocale);
        }

        // Test null locale
        List emptyExpected = Collections.emptyList();
        List emptyActual = LocaleUtils.localeLookupList(null);

        assertEquals(emptyExpected.size(), emptyActual.size());
        assertArrayEquals(emptyExpected.toArray(), emptyActual.toArray());
    }

    @Test
    public void testAvailableLocaleList() {
        List expected = LocaleUtils.availableLocaleList();
        List actual = Locale.getAvailableLocales();

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Locale eLocale = (Locale) expected.get(i);
            Locale aLocale = (Locale) actual.get(i);
            assertEquals(eLocale, aLocale);
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        Set expected = LocaleUtils.availableLocaleSet();
        Set actual = new HashSet(Locale.getAvailableLocales());

        assertEquals(expected.size(), actual.size());
        for (Locale locale : expected) {
            assertTrue(actual.contains(locale));
        }
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "US");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA", "xxxx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List expected = LocaleUtils.languagesByCountry("CA");
        List actual = Arrays.asList(Locale.getLocales("ca"));

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Locale eLocale = (Locale) expected.get(i);
            Locale aLocale = (Locale) actual.get(i);
            assertEquals(eLocale.getCountry(), aLocale.getCountry());
            assertEquals(eLocale.getLanguage(), aLocale.getLanguage());
        }
    }

    @Test
    public void testCountriesByLanguage() {
        List expected = LocaleUtils.countriesByLanguage("en");
        List actual = Arrays.asList(Locale.getLocales("en"));

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Locale eLocale = (Locale) expected.get(i);
            Locale aLocale = (Locale) actual.get(i);
            assertEquals(eLocale.getCountry(), aLocale.getCountry());
            assertEquals(eLocale.getLanguage(), aLocale.getLanguage());
        }
    }
}