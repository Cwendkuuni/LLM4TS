package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validInput() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_emptyInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_invalidInput_length() {
        String invalidInput = "en";
        try {
            LocaleUtils.toLocale(invalidInput);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testToLocale_invalidInput_invalidFormat() {
        String invalidInput = "en_Aa";
        try {
            LocaleUtils.toLocale(invalidInput);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testLocaleLookupList_validInput() {
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_emptyInput() {
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testLocaleLookupList_defaultLocale() {
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_availableLocales() {
        List<Locale> actualLocales = LocaleUtils.availableLocaleList();
        List<Locale> expectedLocales = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testIsAvailableLocale_validInput() {
        Locale validLocale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(validLocale));
    }

    @Test
    public void testIsAvailableLocale_invalidInput() {
        Locale invalidLocale = new Locale("en", "US_XXX");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry_validInput() {
        List<Locale> expected = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA")
        );
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry_emptyInput() {
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_validInput() {
        List<Locale> expected = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("en", "US"),
                new Locale("fr", "FR")
        );
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage_emptyInput() {
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        assertTrue(actual.isEmpty());
    }
}