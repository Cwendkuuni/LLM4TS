package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(null, LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", "US", "variant"), LocaleUtils.toLocale("en_US_variant"));
        assertEquals(new Locale("", "US"), LocaleUtils.toLocale("_US"));
        assertEquals(new Locale("", "US", "variant"), LocaleUtils.toLocale("_US_variant"));

        boolean exceptionThrown = false;
        try {
            LocaleUtils.toLocale("en#US");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
            assertEquals("Invalid locale format: en#US", e.getMessage());
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            LocaleUtils.toLocale("e");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
            assertEquals("Invalid locale format: e", e.getMessage());
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            LocaleUtils.toLocale("_");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
            assertEquals("Invalid locale format: _", e.getMessage());
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = new ArrayList<>(Arrays.asList(new Locale("en")));
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("en")));

        expected = new ArrayList<>(Arrays.asList(new Locale("en"), new Locale("en", "US")));
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("en", "US")));

        expected = new ArrayList<>(Arrays.asList(new Locale("en"), new Locale("en", "")));
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("en", "")));

        expected = new ArrayList<>(Arrays.asList(new Locale("en"), new Locale("en", "US"), new Locale("es")));
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("es"), new Locale("en", "US")));

        expected = new ArrayList<>(Collections.singletonList(new Locale("en")));
        assertEquals(expected, LocaleUtils.localeLookupList(null, new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = new SyncAvoid().AVAILABLE_LOCALE_LIST;
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = new SyncAvoid().AVAILABLE_LOCALE_SET;
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("es", "US"));

        List<Locale> actual = LocaleUtils.languagesByCountry("US");
        assertEquals(expected, actual);

        actual = LocaleUtils.languagesByCountry(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));

        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, actual);

        actual = LocaleUtils.countriesByLanguage(null);
        assertTrue(actual.isEmpty());
    }
}