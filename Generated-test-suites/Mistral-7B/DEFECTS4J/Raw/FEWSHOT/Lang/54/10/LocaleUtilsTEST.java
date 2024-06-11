package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR_xxx"));
        assertEquals(Locale.GERMANY, LocaleUtils.toLocale("de_DE"));

        try {
            LocaleUtils.toLocale("en_US_xxx");
            throw new AssertionError();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_XX");
            throw new AssertionError();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_en");
            throw new AssertionError();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("");
            throw new AssertionError();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB");
            throw new AssertionError();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = Locale.FRANCE;
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, list.get(0));
        assertEquals(Locale.FRANCE, list.get(1));
        assertEquals(Locale.ITALY, list.get(2));
        assertEquals(Locale.ENGLISH, list.get(3));

        list = LocaleUtils.localeLookupList(null, Locale.ENGLISH);
        assertTrue(list.isEmpty());

        list = LocaleUtils.localeLookupList(Locale.ENGLISH, null);
        assertTrue(list.contains(Locale.ENGLISH));
        assertTrue(list.contains(Locale.US));
        assertTrue(list.contains(Locale.JAPAN));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertEquals(Locale.SIMPLIFIED_CHINESE, list.get(0));
        assertEquals(Locale.TRADITIONAL_CHINESE, list.get(1));
        assertEquals(Locale.ENGLISH, list.get(2));
        assertEquals(Locale.FRANCE, list.get(3));
        assertEquals(Locale.GERMANY, list.get(4));

        Set<String> set = new HashSet<String>();
        for (Locale locale : list) {
            set.add(locale.toString());
        }
        assertEquals(list.size(), set.size());
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertEquals(Locale.ENGLISH, list.get(0));
        assertEquals(Locale.SPANISH, list.get(1));

        list = LocaleUtils.languagesByCountry(null);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertEquals(Locale.US, list.get(0));
        assertEquals(Locale.UK, list.get(1));

        list = LocaleUtils.countriesByLanguage(null);
        assertTrue(list.isEmpty());
    }
}