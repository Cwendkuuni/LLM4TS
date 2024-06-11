package org.apache.commons.lang.test;

import static org.junit.Assert.*;

import org.apache.commons.lang.LocaleUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @org.junit.Test
    public void testToLocale() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        expected = new Locale("fr", "");
        actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA", "xxx");
        actual = LocaleUtils.toLocale("fr_CA_xxx");
        assertEquals(expected, actual);

        String invalidFormat = "invalid_locale";
        try {
            Locale actualInvalid = LocaleUtils.toLocale(invalidFormat);
            fail("Expected IllegalArgumentException for invalid format: " + invalidFormat);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");

        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr", ""),
                new Locale("en")
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        assertEquals(expectedList, actualList);

        expectedList = Arrays.asList(new Locale("fr"));
        actualList = LocaleUtils.localeLookupList(baseLocale);
        assertEquals(expectedList, actualList);
    }

    @org.junit.Test
    public void testAvailableLocaleList() {
        Collection<Locale> expected = LocaleUtils.availableLocaleSet();
        Collection<Locale> actual = Locale.getAvailableLocales();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr_FR");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr_CA_xxx");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en_US_xxx");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("unknown_locale");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @org.junit.Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("en", "CA")
        );
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        countryCode = "";
        expected = Collections.emptyList();
        actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("fr", "BE"),
                new Locale("fr", "CH")
        );
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        languageCode = "";
        expected = Collections.emptyList();
        actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }
}