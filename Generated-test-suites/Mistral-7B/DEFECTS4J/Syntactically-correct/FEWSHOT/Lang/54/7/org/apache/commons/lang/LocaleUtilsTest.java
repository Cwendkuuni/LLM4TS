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
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_xxx"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE_xxx"));

        // Invalid locale
        try {
            LocaleUtils.toLocale("xx_YY");
            assert false;
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid locale format"));
        }

        // Invalid language codes and country codes
        try {
            LocaleUtils.toLocale("aaa");
            assert false;
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid locale format"));
        }

        try {
            LocaleUtils.toLocale("en_AA");
            assert false;
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid locale format"));
        }

        try {
            LocaleUtils.toLocale("en_XX");
            assert false;
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid locale format"));
        }

        try {
            LocaleUtils.toLocale("en_XX_YY");
            assert false;
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid locale format"));
        }

        try {
            LocaleUtils.toLocale("en_XX_YY_ZZ");
            assert false;
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid locale format"));
        }

        try {
            LocaleUtils.toLocale("xx_YY_ZZ");
            assert false;
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid locale format"));
        }

        try {
            LocaleUtils.toLocale("aaa_BBB");
            assert false;
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid locale format"));
        }

        try {
            LocaleUtils.toLocale("aaa_XX");
            assert false;
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid locale format"));
        }

        try {
            LocaleUtils.toLocale("aaa_XX_YY");
            assert false;
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid locale format"));
        }

        try {
            LocaleUtils.toLocale("aaa_XX_YY_ZZ");
            assert false;
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid locale format"));
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List lookupList = LocaleUtils.localeLookupList(locale);
        List expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));

        assertEquals(expectedList, lookupList);

        locale = new Locale("fr");
        lookupList = LocaleUtils.localeLookupList(locale);
        expectedList = Arrays.asList(locale, new Locale("fr", ""), new Locale("en"));

        assertEquals(expectedList, lookupList);

        locale = null;
        lookupList = LocaleUtils.localeLookupList(locale);
        expectedList = Collections.emptyList();

        assertEquals(expectedList, lookupList);
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocaleList = LocaleUtils.availableLocaleList();

        // Check size
        int expectedSize = Locale.getAvailableLocale
                ().length;
        assertEquals(expectedSize, availableLocaleList.size());

        // Check first element
        Locale firstLocale = (Locale) availableLocaleList.get(0);
        assertEquals(Locale.ROOT, firstLocale);

        // Check last element
        Locale lastLocale = (Locale) availableLocaleList.get(expectedSize - 1);
        assertEquals(Locale.ENGLISH, lastLocale);

        // Check random element
        Locale randomLocale = (Locale) availableLocaleList.get(3);
        assertEquals(Locale.FRENCH, randomLocale);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set availableLocaleSet = LocaleUtils.availableLocaleSet();

        // Check size
        int expectedSize = Locale.getAvailableLocale
                ().length;
        assertEquals(expectedSize, availableLocaleSet.size());

        // Check first element
        Locale firstLocale = (Locale) availableLocaleSet.iterator().next();
        assertEquals(Locale.ROOT, firstLocale);

        // Check last element
        Locale lastLocale = (Locale) availableLocaleSet.iterator().next();
        assertEquals(Locale.ENGLISH, lastLocale);

        // Check random element
        Locale randomLocale = (Locale) availableLocaleSet.iterator().next();
        assertEquals(Locale.FRENCH, randomLocale);
    }

    @Test
    public void testIsAvailableLocale() {
        List availableLocaleList = LocaleUtils.availableLocaleList();
        Locale localeEN = Locale.ENGLISH;
        Locale localeFR = new Locale("fr", "");
        Locale localeFR_CA = new Locale("fr", "CA");
        Locale localeFR_CA_XX = new Locale("fr", "CA", "XX");
        Locale localeUnknown = new Locale("xx", "YY");

        // Available locale
        assertTrue(LocaleUtils.isAvailableLocale(localeEN));
        assertTrue(LocaleUtils.isAvailableLocale(localeFR));
        assertTrue(LocaleUtils.isAvailableLocale(localeFR_CA));
        assertTrue(LocaleUtils.isAvailableLocale(localeFR_CA_XX));

        // Unavailable locale
        assertFalse(LocaleUtils.isAvailableLocale(localeUnknown));

        // Check for null input
        assertNull(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List expectedList = Arrays.asList(new Locale("fr"), new Locale("en"));

        List languagesByCountry = LocaleUtils.languagesByCountry(countryCode);

        assertEquals(expectedList, languagesByCountry);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List expectedList = Arrays.asList(new Locale("CA"), new Locale("BE"), new Locale("CH"));

        List countriesByLanguage = LocaleUtils.countriesByLanguage(languageCode);

        assertEquals(expectedList, countriesByLanguage);
    }
}