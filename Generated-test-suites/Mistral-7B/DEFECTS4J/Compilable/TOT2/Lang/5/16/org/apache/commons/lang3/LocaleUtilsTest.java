package org.apache.commons.lang3;
import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidInput() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("en_GB_XXX");
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertFalse(actual.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidInput() {
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertFalse(actual.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_EmptyInput() {
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidInput() {
        List<Locale> actual = LocaleUtils.countriesByLanguage("fr");
        assertFalse(actual.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_EmptyInput() {
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_ValidInput() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidInput() {
        Locale locale = new Locale("fr", "XXX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test(expected = NullPointerException.class)
    public void testIsAvailableLocale_NullInput() {
        LocaleUtils.isAvailableLocale(null);
    }
}