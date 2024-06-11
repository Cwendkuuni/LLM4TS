package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;
import java.util.List;
import java.util.Arrays;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        try {
            LocaleUtils.toLocale("e");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            LocaleUtils.toLocale("en GB");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            LocaleUtils.toLocale("en_gb");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actualList = LocaleUtils.availableLocaleList();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleSet() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertTrue(LocaleUtils.availableLocaleSet().containsAll(localeList));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zzz")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA")
        );
        List<Locale> actualList = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = Arrays.asList(
                new Locale("en", "GB"),
                new Locale("en", "US")
        );
        List<Locale> actualList = LocaleUtils.countriesByLanguage("en");
        assertEquals(expectedList, actualList);
    }

}