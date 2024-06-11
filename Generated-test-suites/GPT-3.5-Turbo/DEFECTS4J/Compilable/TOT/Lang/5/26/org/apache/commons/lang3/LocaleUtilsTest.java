package org.apache.commons.lang3;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Locale;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale);

        locale = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale);

        locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("enGB");
    }

    @Test
    public void testToLocale_nullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_shortInput() {
        LocaleUtils.toLocale("e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_incorrectCase() {
        LocaleUtils.toLocale("En_GB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_incorrectSeparator() {
        LocaleUtils.toLocale("en-GB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_incorrectLength() {
        LocaleUtils.toLocale("en_GB_ERR");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_withDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_withNullLocale() {
        assertNull(LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_withNullLocaleAndDefaultLocale() {
        List<Locale> expected = Arrays.asList(Locale.getDefault());
        List<Locale> actual = LocaleUtils.localeLookupList(null, Locale.getDefault());
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertFalse(actual.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        assertFalse(actual.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_withAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testIsAvailableLocale_withUnavailableLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xyz")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> actual = LocaleUtils.languagesByCountry("US");
        assertFalse(actual.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_withNullCountryCode() {
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertFalse(actual.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_withNullLanguageCode() {
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        assertTrue(actual.isEmpty());
    }

}