package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Valid tests
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.GERMANY, LocaleUtils.toLocale("de_DE"));

        // Edge cases
        assertEquals(Locale.ROOT, LocaleUtils.toLocale(""));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale("xx"));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale("xx_XX"));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale("xx_xx"));

        // Error cases
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("AbCDeF");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_Ab");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_AbCd");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_AbCd_Ef");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_AbCd_EfGh");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_AbCd_EfGh_Ij");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_AbCd_EfGh_Ij_Kl");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.FRANCE);
        List<Locale> expectedList = Arrays.asList(Locale.FRANCE, Locale.FRENCH, Locale.ROOT);
        assertEquals(expectedList, localeList);

        localeList = LocaleUtils.localeLookupList(Locale.GERMANY, Locale.ENGLISH);
        expectedList = Arrays.asList(Locale.GERMANY, Locale.GERMAN, Locale.GERMANY, Locale.ENGLISH);
        assertEquals(expectedList, localeList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        List<Locale> expectedList = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.POLISH, Locale.RUSSIAN, Locale.SPANISH, Locale.SWEDISH);
        assertEquals(expectedList, localeList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.POLISH, Locale.RUSSIAN, Locale.SPANISH, Locale.SWEDISH));
        assertEquals(expectedSet, localeSet);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langByCountry = LocaleUtils.languagesByCountry("US");
        List<Locale> expectedList = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH, Locale.ENGLISH);
        assertEquals(expectedList, langByCountry);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countryByLanguage = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedList = Arrays.asList(Locale.ENGLAND, Locale.AUSTRALIA, Locale.CANADA);
        assertEquals(expectedList, countryByLanguage);
    }
}