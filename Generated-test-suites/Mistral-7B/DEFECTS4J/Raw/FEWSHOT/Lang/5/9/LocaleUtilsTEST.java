package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    public void testToLocale_InvalidFormat_LanguageCodeLowerCase() {
        assertNull(LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocale_InvalidFormat_LanguageCodeUppercase() {
        assertNull(LocaleUtils.toLocale("EN"));
    }

    @Test
    public void testToLocale_InvalidFormat_CountryCodeLowerCase() {
        assertNull(LocaleUtils.toLocale("en_gb"));
    }

    @Test
    public void testToLocale_InvalidFormat_CountryCodeUppercase() {
        assertNull(LocaleUtils.toLocale("en_GB"));
    }

    @Test
    public void testToLocale_InvalidFormat_SeparatorDash() {
        assertNull(LocaleUtils.toLocale("en-GB"));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageCountry() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
    }

    @Test
    public void testToLocale_ValidFormat_LanguageCountryVariant() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("xxx", locale.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_SeparatorUnderscore() {
        LocaleUtils.toLocale("en_GB_xxx"); // should throw an exception
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(baseLocale);

        assertEquals(4, localeList.size());
        assertEquals(baseLocale, localeList.get(0));
        Locale locale1 = new Locale("fr", "CA");
        assertEquals(locale1, localeList.get(1));
        Locale locale2 = new Locale("fr", "");
        assertEquals(locale2, localeList.get(2));
        Locale defaultLocale = new Locale("en");
        assertEquals(defaultLocale, localeList.get(3));
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
        assertEquals(0, localeList.size());
    }

    @Test
    public void testLocaleLookupList_DefaultLocaleInput() {
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("en"));
        assertEquals(1, localeList.size());
        assertEquals(new Locale("en"), localeList.get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertFalse(localeList.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertFalse(localeSet.isEmpty());

        Locale locale = new Locale("fr", "CA");
        assertTrue(localeSet.contains(locale));
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA-XXX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> frenchLocales = LocaleUtils.languagesByCountry("CA");
        assertFalse(frenchLocales.isEmpty());
        assertEquals(1, frenchLocales.size());
        assertEquals(new Locale("fr", "CA"), frenchLocales.get(0));

        List<Locale> englishLocales = LocaleUtils.languagesByCountry("GB");
        assertFalse(englishLocales.isEmpty());
        assertEquals(1, englishLocales.size());
        assertEquals(new Locale("en", "GB"), englishLocales.get(0));
    }

    @Test
    public void testLanguagesByCountry_EmptyInput() {
        List<Locale> languageList = LocaleUtils.languagesByCountry(null);
        assertEquals(0, languageList.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> frenchCanadaLocales = LocaleUtils.countriesByLanguage("fr");
        assertFalse(frenchCanadaLocales.isEmpty());
        assertEquals(1, frenchCanadaLocales.size());
        assertEquals(new Locale("fr", "CA"), frenchCanadaLocales.get(0));

        List<Locale> englishUKLocales = LocaleUtils.countriesByLanguage("en");
        assertFalse(englishUKLocales.isEmpty());
        assertEquals(1, englishUKLocales.size());
        assertEquals(new Locale("en", "GB"), englishUKLocales.get(0));
    }

    @Test
    public void testCountriesByLanguage_EmptyInput() {
        List<Locale> countryList = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, countryList.size());
    }

}