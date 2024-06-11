package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test valid locales
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE"));
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it_IT"));
        assertEquals(Locale.JAPANESE, LocaleUtils.toLocale("ja_JP"));
        assertEquals(Locale.SPANISH, LocaleUtils.toLocale("es_ES"));

        // Test invalid locales
        try {
            LocaleUtils.toLocale("en_caXXX"); // invalid country code
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_ca_XXX"); // invalid variant
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en-ca-XXX"); // invalid separator
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_CA_XXX"); // uppercase language
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_CA-XXX"); // lowercase country
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_CA__XXX"); // extra underscore
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Test valid locales
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.JAPANESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SPANISH));

        // Test invalid locales
        assertFalse(LocaleUtils.isAvailableLocale(Locale.CHINESE)); // not in list
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_CA", "XXX"))); // invalid country code
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_CA", "CA", "XXX"))); // invalid variant
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_CA-XXX"))); // invalid separator
    }

    @Test
    public void testLocaleLookupList() {
        Locale defaultLocale = Locale.ENGLISH;
        Locale locale = new Locale("fr_CA", "CA");

        List<Locale> result = LocaleUtils.localeLookupList(locale);

        assertEquals(5, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr_CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(defaultLocale, result.get(3));
        assertSame(defaultLocale, result.get(4));

        result = LocaleUtils.localeLookupList(locale, null);
        assertEquals(6, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr_CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(defaultLocale, result.get(3));
        assertEquals(Locale.ROOT, result.get(4));
        assertEquals(Locale.ROOT, result.get(5));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(146, result.size());

        assertNotNull(result.get(0)); // Locale[root, null]
        assertEquals(Locale.CHINESE, result.get(1));
        assertEquals(Locale.ENGLISH, result.get(2));
        assertEquals(Locale.FRENCH, result.get(3));
        assertEquals(Locale.GERMAN, result.get(4));
        assertEquals(Locale.ITALIAN, result.get(5));
        assertEquals(Locale.JAPANESE, result.get(6));
        assertEquals(Locale.SPANISH, result.get(7));
        // ...
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertEquals(11, result.size());
        assertEquals(Locale.ENGLISH, result.get(0));
        assertEquals(Locale.SPANISH, result.get(1));
        assertEquals(Locale.FRENCH, result.get(2));
        assertEquals(Locale.GERMAN, result.get(3));
        assertEquals(Locale.ITALIAN, result.get(4));
        assertEquals(Locale.PORTUGUESE, result.get(5));
        assertEquals(Locale.POLISH, result.get(6));
        assertEquals(Locale.DUTCH, result.get(7));
        assertEquals(Locale.SWEDISH, result.get(8));
        assertEquals(Locale.FINNISH, result.get(9));
        assertEquals(Locale.danish, result.get(10));

        result = LocaleUtils.languagesByCountry("FR");
        assertEquals(11, result.size());
        assertEquals(Locale.FRENCH, result.get(0));
        assertEquals(Locale.ENGLISH, result.get(1));
        assertEquals(Locale.SPANISH, result.get(2));
        assertEquals(Locale.GERMAN, result.get(3));
        assertEquals(Locale.ITALIAN, result.get(4));
        assertEquals(Locale.PORTUGUESE, result.get(5));
        assertEquals(Locale.POLISH, result.get(6));
        assertEquals(Locale.DUTCH, result.get(7));
        assertEquals(Locale.SWEDISH, result.get(8));
        assertEquals(Locale.FINNISH, result.get(9));
        assertEquals(Locale.NORWEGIAN, result.get(10));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(26, result.size());
        assertEquals(Locale.ENGLISH, result.get(0));
        assertEquals(Locale.ENGLISH, result.get(1));
        assertEquals(Locale.ENGLISH, result.get(2));
        assertEquals(Locale.ENGLISH, result.get(3));
        assertEquals(Locale.ENGLISH, result.get(4));
        assertEquals(Locale.ENGLISH, result.get(5));
        assertEquals(Locale.ENGLISH, result.get(6));
        assertEquals(Locale.ENGLISH, result.get(7));
        assertEquals(Locale.ENGLISH, result.get(8));
        assertEquals(Locale.ENGLISH, result.get(9));
        assertEquals(Locale.ENGLISH, result.get(10));
        assertEquals(Locale.ENGLISH, result.get(11));
        assertEquals(Locale.ENGLISH, result.get(12));
        assertEquals(Locale.ENGLISH, result.get(13));
        assertEquals(Locale.ENGLISH, result.get(14));
        assertEquals(Locale.ENGLISH, result.get(15));
        assertEquals(Locale.ENGLISH, result.get(16));
        assertEquals(Locale.ENGLISH, result.get(17));
        assertEquals(Locale.ENGLISH, result.get(18));
        assertEquals(Locale.ENGLISH, result.get(19));
        assertEquals(Locale.ENGLISH, result.get(20));
        assertEquals(Locale.ENGLISH, result.get(21));
        assertEquals(Locale.ENGLISH, result.get(22));
        assertEquals(Locale.ENGLISH, result.get(23));
        assertEquals(Locale.ENGLISH, result.get(24));
        assertEquals(Locale.ENGLISH, result.get(25));

        result = LocaleUtils.countriesByLanguage("fr");
        assertEquals(25, result.size());
        // ...
    }
}