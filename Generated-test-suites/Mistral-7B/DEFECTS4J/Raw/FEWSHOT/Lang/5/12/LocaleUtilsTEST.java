package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_EmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidFormat() {
        try {
            LocaleUtils.toLocale("en_GB_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB-");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB__");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GBxxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB-xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB---");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB----");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB---------");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testToLocale_ValidFormat() {
        assertEquals(new Locale("en", "EN"), LocaleUtils.toLocale("en_EN"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "XXX"), LocaleUtils.toLocale("en_GB_XXX"));
        assertEquals(new Locale("en", "GB", "XX"), LocaleUtils.toLocale("en_GB_XX"));
        assertEquals(new Locale("en", "GB", "X"), LocaleUtils.toLocale("en_GB_X"));
    }

    @Test
    public void testToLocale_UppercaseLanguageCode() {
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("EN", "GB"), LocaleUtils.toLocale("EN_GB"));
    }

    @Test
    public void testToLocale_UppercaseCountryCode() {
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
    }

    @Test
    public void testToLocale_EmptyCountryCode() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
    }

    @Test
    public void testLocaleLookupList_EmptyLocale() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_SingleLocale() {
        List<Locale> expected = Arrays.asList(new Locale("en", "GB"));
        List<Locale> actual = LocaleUtils.localeLookupList(LocaleUtils.toLocale("en_GB"));
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_MultipleLocales() {
        List<Locale> expected = Arrays.asList(new Locale("en", "GB"), new Locale("en"));
        List<Locale> actual = LocaleUtils.localeLookupList(LocaleUtils.toLocale("en_GB"), LocaleUtils.toLocale("en"));
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        List<Locale> expected = Arrays.asList(new Locale("en", "GB"), new Locale("en"));
        List<Locale> actual = LocaleUtils.localeLookupList(LocaleUtils.toLocale("en_GB"), null);
        assertEquals(expected, actual);

        expected = Arrays.asList(new Locale("en"));
        actual = LocaleUtils.localeLookupList(null, new Locale("en"));
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_EmptyDefaultLocale() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.localeLookupList(LocaleUtils.toLocale("en_GB"), null);
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList_Empty() {
        assertTrue(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleList_Size() {
        int size = LocaleUtils.availableLocaleList().size();
        assertTrue(size > 0);
    }

    @Test
    public void testAvailableLocaleSet_Empty() {
        assertTrue(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_Size() {
        int size = LocaleUtils.availableLocaleSet().size();
        assertTrue(size > 0);
    }

    @Test
    public void testAvailableLocaleList_Contains() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertTrue(list.contains(locale));
    }

    @Test
    public void testAvailableLocaleSet_Contains() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertTrue(set.contains(locale));
    }

    @Test
    public void testLanguagesByCountry_EmptyCountryCode() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry_CountryCodeWithLocales() {
        List<Locale> expected = Arrays.asList(new Locale("en", "GB"), new Locale("fr", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry("GB");
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry_CountryCodeEmpty() {
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.FRENCH);
        List<Locale> actual = LocaleUtils.languagesByCountry("");
        assertEquals(expected, actual);
    }
}