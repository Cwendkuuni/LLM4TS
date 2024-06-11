package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("fr_CA"), LocaleUtils.toLocale("fr_CA"));
        assertEquals(new Locale("en_GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en_GB_XXX"), LocaleUtils.toLocale("en_GB_XXX"));
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xyz"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_XX"));
        assertNull(LocaleUtils.toLocale("en_XX_"));
        assertNull(LocaleUtils.toLocale("en_XX_XX"));
        assertNull(LocaleUtils.toLocale("en_XX_XX_"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, lookupList.get(0));
        assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        assertEquals(new Locale("fr"), lookupList.get(2));

        locale = new Locale("en");
        lookupList = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, lookupList.get(0));
        assertEquals(Locale.ENGLISH, lookupList.get(1));

        locale = null;
        lookupList = LocaleUtils.localeLookupList(locale);
        assertTrue(lookupList.isEmpty());

        lookupList = LocaleUtils.localeLookupList(locale, Locale.ENGLISH);
        assertTrue(lookupList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertFalse(availableLocaleList.isEmpty());

        Locale locale = new Locale("xyz");
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
        assertFalse(localeLookupList.isEmpty());
        assertFalse(availableLocaleList.contains(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
        assertFalse(languagesByCountry.isEmpty());
        assertEquals(Locale.CANADA, languagesByCountry.get(0).getCountry());

        languagesByCountry = LocaleUtils.languagesByCountry(null);
        assertTrue(languagesByCountry.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        assertFalse(countriesByLanguage.isEmpty());
        assertEquals(Locale.US, countriesByLanguage.get(0));

        countriesByLanguage = LocaleUtils.countriesByLanguage(null);
        assertTrue(countriesByLanguage.isEmpty());
    }
}