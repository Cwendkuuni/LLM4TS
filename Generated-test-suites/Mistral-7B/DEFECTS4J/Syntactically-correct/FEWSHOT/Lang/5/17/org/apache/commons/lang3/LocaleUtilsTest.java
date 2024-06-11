package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en_GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en_GB_XXX"), LocaleUtils.toLocale("en_GB_XXX"));

        // Test invalid inputs: null, empty string, invalid format
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("en_GB_XXX_"));
        assertNull(LocaleUtils.toLocale("en_GB-XXX"));
        assertNull(LocaleUtils.toLocale("en_GBxXXX"));

        // Test language is empty or uppercase
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("EN"));
        assertNull(LocaleUtils.toLocale("EN_GB"));

        // Test country is empty or lowercase
        assertNull(LocaleUtils.toLocale("en"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_gb"));

        // Test separator is not an underscore
        assertNull(LocaleUtils.toLocale("en_GB-"));
        assertNull(LocaleUtils.toLocale("en_GB_XXX_"));

        // Test length is incorrect
        assertNull(LocaleUtils.toLocale("en_GB"));
        assertNull(LocaleUtils.toLocale("en_GB_XXX_XXX"));

        // Test duplicate locale
        assertEquals(new Locale("en_US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en_GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en_GB"), LocaleUtils.toLocale("en_GB"));
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
        assertTrue(localeList.isEmpty());

        // Test single locale
        Locale locale = new Locale("en_GB");
        List<Locale> localeList1 = LocaleUtils.localeLookupList(locale);
        assertEquals(1, localeList1.size());
        assertEquals(locale, localeList1.get(0));

        // Test multiple locales
        Locale locale2 = new Locale("es");
        List<Locale> localeList2 = LocaleUtils.localeLookupList(locale, locale);
        assertEquals(2, localeList2.size());
        assertEquals(locale, localeList2.get(0));
        assertEquals(locale2, localeList2.get(1));

        // Test default locale
        List<Locale> localeList3 = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(3, localeList3.size());
        assertEquals(locale, localeList3.get(0));
        assertEquals(locale2, localeList3.get(1));
        assertEquals(new Locale("en"), localeList3.get(2));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test getting available locales
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocaleList);
        assertEquals(35, availableLocaleList.size());

        // Test getting available locales set
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocaleSet);
        assertEquals(35, availableLocaleSet.size());

        // Test checking if a locale is available
        Locale locale = new Locale("en_GB");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test checking if an invalid locale is available
        Locale invalidLocale = new Locale("xyz_XYZ");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty country list
        List<Locale> languagesByCountry1 = LocaleUtils.languagesByCountry(null);
        assertTrue(languagesByCountry1.isEmpty());

        // Test empty language list for a country
        List<Locale> languagesByCountry2 = LocaleUtils.languagesByCountry("ZZ");
        assertTrue(languagesByCountry2.isEmpty());

        // Test valid country and language list
        List<Locale> languagesByCountry3 = LocaleUtils.languagesByCountry("US");
        assertEquals(14, languagesByCountry3.size());

        // Test checking if a language is available for a country
        Locale language = new Locale("en");
        assertTrue(LocaleUtils.isAvailableLocale(language));
        assertTrue(LocaleUtils.languagesByCountry("US").contains(language));

        // Test checking if an invalid language is available for a country
        Locale invalidLanguage = new Locale("xyz");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLanguage));
        assertFalse(LocaleUtils.languagesByCountry("US").contains(invalidLanguage));
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty language list
        List<Locale> countriesByLanguage1 = LocaleUtils.countriesByLanguage(null);
        assertTrue(countriesByLanguage1.isEmpty());

        // Test empty country list for a language
        List<Locale> countriesByLanguage2 = LocaleUtils.countriesByLanguage("ZZ");
        assertTrue(countriesByLanguage2.isEmpty());

        // Test valid language and country list
        List<Locale> countriesByLanguage3 = LocaleUtils.countriesByLanguage("en");
        assertEquals(12, countriesByLanguage3.size());

        // Test checking if a country is available for a language
        Locale country = new Locale("US");
        assertTrue(LocaleUtils.isAvailableLocale(country));
        assertTrue(LocaleUtils.countriesByLanguage("en").contains(country));

        // Test checking if an invalid country is available for a language
        Locale invalidCountry = new Locale("XYZ");
        assertFalse(LocaleUtils.isAvailableLocale(invalidCountry));
        assertFalse(LocaleUtils.countriesByLanguage("en").contains(invalidCountry));
    }

}