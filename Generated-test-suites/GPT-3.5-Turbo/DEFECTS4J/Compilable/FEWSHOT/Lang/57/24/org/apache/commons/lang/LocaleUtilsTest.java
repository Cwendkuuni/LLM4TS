
/*
* Test cases for the LocaleUtils class
* */

package org.apache.commons.lang;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertNull(LocaleUtils.toLocale(null));
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("a"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("ab"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("abc"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("abcd"));
    }

    @Test
    public void testLocaleLookupList1() {
        List<Locale> expectedResult = new ArrayList<>();
        expectedResult.add(null);
        expectedResult.add(Locale.getDefault());
        assertEquals(expectedResult, LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList2() {
        List<Locale> expectedResult = new ArrayList<>();
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        expectedResult.add(locale);
        expectedResult.add(new Locale("fr", "CA"));
        expectedResult.add(new Locale("fr"));
        expectedResult.add(defaultLocale);
        assertEquals(expectedResult, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        assertNotNull(LocaleUtils.availableLocaleList());
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedResult = Arrays.asList(
                new Locale("de", "DE"),
                new Locale("en", "DE"),
                new Locale("en", "DE", "macintosh"),
                new Locale("de", "DE", "bavarian"),
                new Locale("de", "DE", "bavarian-x-lvariant-x-lvariant"),
                new Locale("en", "DE", "bavarian-x-lvariant-x-lvariant"),
                new Locale("es", "DE", "bavarian-x-lvariant-x-lvariant"),
                new Locale("es", "DE", "bavarian-x-lvariant-x-lvariant")
        );
        assertEquals(expectedResult, LocaleUtils.languagesByCountry("DE"));
        assertNotNull(LocaleUtils.languagesByCountry(null));
        assertTrue(LocaleUtils.languagesByCountry("").isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedResult = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("es", "CA"),
                new Locale("fr", "CA"),
                new Locale("pt", "CA"),
                new Locale("en", "CA", "labrador"),
                new Locale("eo", "CA"),
                new Locale("ro", "CA"),
                new Locale("pl", "CA"),
                new Locale("mt", "CA"),
                new Locale("in", "CA"),
                new Locale("te", "CA"),
                new Locale("mk", "CA"),
                new Locale("en", "CA", "sapir")
        );
        assertEquals(expectedResult, LocaleUtils.countriesByLanguage("CA"));
        assertNotNull(LocaleUtils.countriesByLanguage(null));
        assertTrue(LocaleUtils.countriesByLanguage("").isEmpty());
    }
}
