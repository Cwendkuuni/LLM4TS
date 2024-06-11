package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidInput() {
        String[] testCases = new String[]{"en", "en_US", "fr_FR", "zh_CN", "es_ES"};
        for (String testCase : testCases) {
            Locale expected = new Locale(testCase.substring(0, 2), testCase.substring(3));
            Locale actual = LocaleUtils.toLocale(testCase);
            assertEquals(expected.getLanguage(), actual.getLanguage());
            assertEquals(expected.getCountry(), actual.getCountry());
        }
    }

    @Test
    public void testToLocale_InvalidInput_EmptyString() {
        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testToLocale_InvalidInput_InvalidLanguageCode() {
        try {
            LocaleUtils.toLocale("xyz_XY");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testToLocale_InvalidInput_InvalidCountryCode() {
        try {
            LocaleUtils.toLocale("en_xyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testToLocale_InvalidInput_Separator() {
        try {
            LocaleUtils.toLocale("en_GB-xyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_ValidInput_EmptyInput() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_ValidInput_DefaultLocale() {
        List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("fr"), new Locale("en"));
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList_ValidInput() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet_ValidInput() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale_ValidInput() {
        List<Locale> locales = new ArrayList<>();
        locales.add(new Locale("en", "US"));
        locales.add(new Locale("fr", "FR"));
        LocaleUtils.cAvailableLocaleList = Collections.unmodifiableList(locales);

        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "FR")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "YY")));
    }

    @Test
    public void testLanguagesByCountry_ValidInput() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("en", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry_EmptyInput() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage_ValidInput() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("ca", "CA"));
        expected.add(new Locale("us", "US"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage_EmptyInput() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        assertEquals(expected, actual);
    }

}