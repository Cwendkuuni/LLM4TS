package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleValidFormat() {
        Locale locale = LocaleUtils.toLocale("en_US_POSIX");
        assertEquals("en_US_POSIX", locale.toLanguageTag());

        locale = LocaleUtils.toLocale("en_GB");
        assertEquals("en_GB", locale.toLanguageTag());

        locale = LocaleUtils.toLocale("en_GB_London");
        assertEquals("en_GB_London", locale.toLanguageTag());

        locale = LocaleUtils.toLocale("fr_FR");
        assertEquals("fr_FR", locale.toLanguageTag());

        locale = LocaleUtils.toLocale("fr_FR_Paris");
        assertEquals("fr_FR_Paris", locale.toLanguageTag());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatLanguage() {
        LocaleUtils.toLocale("En_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatCountry() {
        LocaleUtils.toLocale("en_us");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatSeparator() {
        LocaleUtils.toLocale("en_US:");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatLength() {
        LocaleUtils.toLocale("en_US____");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "CA");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);

        assertEquals(3, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("en", "CA"), localeList.get(1));
        assertEquals(new Locale("en"), localeList.get(2));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("en", "CA");
        Locale defaultLocale = Locale.getDefault();
        List<Locale> localeList = LocaleUtils.localeLookupList(locale, defaultLocale);

        assertEquals(4, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("en", "CA"), localeList.get(1));
        assertEquals(new Locale("en"), localeList.get(2));
        assertEquals(defaultLocale, localeList.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertEquals(145, localeList.size());

        assertTrue(localeList.contains(Locale.ENGLISH));
        assertTrue(localeList.contains(Locale.FRENCH));
        assertTrue(localeList.contains(Locale.GERMAN));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languageList = LocaleUtils.languagesByCountry("CA");
        assertEquals(11, languageList.size());

        assertTrue(languageList.contains(new Locale("en", "CA")));
        assertTrue(languageList.contains(new Locale("fr", "CA")));
        assertTrue(languageList.contains(new Locale("fa", "CA")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countryList = LocaleUtils.countriesByLanguage("en");
        assertEquals(26, countryList.size());

        assertTrue(countryList.contains(new Locale("CA")));
        assertTrue(countryList.contains(new Locale("GB")));
        assertTrue(countryList.contains(new Locale("US")));
    }
}