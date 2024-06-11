package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_emptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_null() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLanguageCode() {
        LocaleUtils.toLocale("AB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidCountryCode() {
        LocaleUtils.toLocale("enA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_separatorNotUnderScore() {
        LocaleUtils.toLocale("en_--GB");
    }

    @Test
    public void testToLocale_emptyCountryCode() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocale_simpleLocale() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocale_localeWithLanguageAndCountry() {
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
    }

    @Test
    public void testToLocale_localeWithLanguageCountryAndVariant() {
        assertEquals(new Locale("en", "GB", "en_GB"), LocaleUtils.toLocale("en_GB_en_GB"));
    }

    @Test
    public void testLocaleLookupList_defaultLocale() {
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
        assertEquals(Locale.getDefault(), localeList.get(0));
    }

    @Test
    public void testLocaleLookupList_localeWithVariant() {
        Locale locale = new Locale("en", "GB", "en_GB");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("en", "GB"), localeList.get(1));
        assertEquals(new Locale("en"), localeList.get(2));
        assertEquals(Locale.getDefault(), localeList.get(3));
    }

    @Test
    public void testLocaleLookupList_localeWithLanguageOnly() {
        Locale locale = new Locale("en");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("en"), localeList.get(1));
        assertEquals(Locale.getDefault(), localeList.get(2));
    }

    @Test
    public void testLocaleLookupList_localeWithCountryOnly() {
        Locale locale = new Locale("en", "GB");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("en"), localeList.get(1));
        assertEquals(new Locale("en", ""), localeList.get(2));
        assertEquals(Locale.getDefault(), localeList.get(3));
    }

    @Test
    public void testLocaleLookupList_localeWithLanguageAndCountry() {
        Locale locale = new Locale("en", "GB");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale, Locale.getDefault());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("en"), localeList.get(1));
        assertEquals(new Locale("en", ""), localeList.get(2));
        assertEquals(Locale.getDefault(), localeList.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertTrue(localeList.size() > 0);
        for (Locale locale : localeList) {
            assertNotNull(locale);
        }
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> localeList = LocaleUtils.countriesByLanguage("en");
        assertFalse(localeList.isEmpty());
        for (Locale locale : localeList) {
            assertNotNull(locale);
        }
        Locale expected = new Locale("en", "CA");
        assertSame(expected, localeList.get(0));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> localeList = LocaleUtils.languagesByCountry("CA");
        assertFalse(localeList.isEmpty());
        for (Locale locale : localeList) {
            assertNotNull(locale);
        }
        Locale expected = new Locale("fr", "CA");
        assertSame(expected, localeList.get(0));
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA", "en_CA");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }
}