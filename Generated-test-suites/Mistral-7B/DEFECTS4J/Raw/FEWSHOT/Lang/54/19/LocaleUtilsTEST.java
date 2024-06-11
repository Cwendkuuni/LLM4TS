package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB_xxx"));

        try {
            LocaleUtils.toLocale("fr_CA_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("fr_CA__xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("fr_CA_xxx_foo");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("fr_CA");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("fr");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(4, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
        assertEquals(Locale.getDefault(), list.get(3));

        List<Locale> list2 = LocaleUtils.localeLookupList(locale, Locale.ENGLISH);
        assertEquals(5, list2.size());
        assertEquals(locale, list2.get(0));
        assertEquals(new Locale("fr", "CA"), list2.get(1));
        assertEquals(new Locale("fr"), list2.get(2));
        assertEquals(Locale.ENGLISH, list2.get(4));
    }

    @Test
    public void testAvailableLocales() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(251, availableLocales.size());

        assertTrue(availableLocales.contains(Locale.ENGLISH));
        assertTrue(availableLocales.contains(new Locale("fr", "FR")));
        assertTrue(availableLocales.contains(new Locale("de", "DE")));
        assertTrue(availableLocales.contains(new Locale("es", "ES")));
        // ...
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assertEquals(2, languages.size());
        assertEquals(new Locale("en", "CA"), languages.get(0));
        assertEquals(new Locale("fr", "CA"), languages.get(1));

        List<Locale> languages2 = LocaleUtils.languagesByCountry("FR");
        assertEquals(0, languages2.size());

        List<Locale> languages3 = LocaleUtils.languagesByCountry(null);
        assertEquals(0, languages3.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(4, countries.size());
        assertEquals(new Locale("en", "CA"), countries.get(0));
        assertEquals(new Locale("en", "US"), countries.get(1));
        assertEquals(new Locale("en", "GB"), countries.get(2));
        assertEquals(new Locale("en", "IE"), countries.get(3));

        List<Locale> countries2 = LocaleUtils.countriesByLanguage("fr");
        assertEquals(1, countries2.size());
        assertEquals(new Locale("fr", "FR"), countries2.get(0));

        List<Locale> countries3 = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, countries3.size());
    }
}