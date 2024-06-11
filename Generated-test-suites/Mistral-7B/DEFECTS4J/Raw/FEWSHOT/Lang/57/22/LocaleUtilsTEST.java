package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_Empty() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_SingleLanguage() {
        Locale expected = new Locale("en");
        Locale actual = LocaleUtils.toLocale("en");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_SingleLanguageWithCountry() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LanguageNotLowercase() {
        LocaleUtils.toLocale("En");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_CountryNotUppercase() {
        LocaleUtils.toLocale("en_gb");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_SeparatorNotUnderscore() {
        LocaleUtils.toLocale("en_CA-xx");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LanguageLengthNotEqual2() {
        LocaleUtils.toLocale("español");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_CountryLengthNotEqual2() {
        LocaleUtils.toLocale("en_GERmanY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LengthNotCorrect() {
        LocaleUtils.toLocale("en_US_French");
    }

    @Test
    public void testLocaleLookupList_SingleLocale() {
        Locale input = new Locale("fr", "CA", "yy");
        List<Locale> output = LocaleUtils.localeLookupList(input);
        List<Locale> expected = Arrays.asList(input);
        assertEquals(expected, output);
    }

    @Test
    public void testLocaleLookupList_LocaleWithVariant() {
        Locale input = new Locale("fr", "CA", "yy_YY");
        List<Locale> output = LocaleUtils.localeLookupList(input);
        List<Locale> expected = Arrays.asList(input, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expected, output);
    }

    @Test
    public void testLocaleLookupList_LocaleWithCountry() {
        Locale input = new Locale("fr", "CA");
        List<Locale> output = LocaleUtils.localeLookupList(input);
        List<Locale> expected = Arrays.asList(input, new Locale("fr", ""), new Locale("fr"));
        assertEquals(expected, output);
    }

    @Test
    public void testLocaleLookupList_LocaleWithEmptyCountry() {
        Locale input = new Locale("fr");
        List<Locale> output = LocaleUtils.localeLookupList(input);
        List<Locale> expected = Collections.singletonList(input);
        assertEquals(expected, output);
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> output = LocaleUtils.localeLookupList(null);
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, output);
    }

    @Test
    public void testLocaleLookupList_NullDefaultLocale() {
        Locale input = new Locale("fr", "CA");
        List<Locale> output = LocaleUtils.localeLookupList(input, null);
        List<Locale> expected = Arrays.asList(input, new Locale("fr"), new Locale(""));
        assertEquals(expected, output);
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
        Set<Locale> expected = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        Locale locale = new Locale("en", "GB");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale locale = new Locale("es", "US");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_EmptyCountryCode() {
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage_EmptyLanguageCode() {
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> actual = LocaleUtils.countriesByLanguage("fr");
        List<Locale> expected = Arrays.asList(new Locale("CA"), new Locale("BE"));
        assertEquals(expected, actual);
    }
}