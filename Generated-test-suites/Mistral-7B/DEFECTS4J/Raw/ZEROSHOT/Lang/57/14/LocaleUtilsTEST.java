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
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA");
        actual = LocaleUtils.toLocale("fr_CA");
        assertEquals(expected, actual);

        expected = new Locale("en");
        actual = LocaleUtils.toLocale("en");
        assertEquals(expected, actual);

        expected = new Locale("es", "");
        actual = LocaleUtils.toLocale("es");
        assertEquals(expected, actual);

        String invalidFormat = "fr_US_CA";
        try {
            actual = LocaleUtils.toLocale(invalidFormat);
            fail("Expected InvalidLocaleFormatException but got " + actual);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");

        List<Locale> expected = Arrays.asList(
                baseLocale,
                new Locale("fr", "CA"),
                new Locale("fr"),
                defaultLocale
        );
        List<Locale> actual = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        assertEquals(expected, actual);

        expected = Arrays.asList(defaultLocale);
        actual = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = Collections.unmodifiableSet(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("es", "ES");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("es", "NY");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        List<Locale> expected = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA")
        );
        assertEquals(expected, actual);

        actual = LocaleUtils.languagesByCountry(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "GB"),
                new Locale("en", "CA")
        );
        assertEquals(expected, actual);

        actual = LocaleUtils.countriesByLanguage(null);
        assertTrue(actual.isEmpty());
    }
}