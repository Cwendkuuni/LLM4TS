package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("es_ES", LocaleUtils.toLocale("es_ES").toString());

        // Test invalid inputs
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("en"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_US_"));
        assertNull(LocaleUtils.toLocale("en_US_xx"));

        // Test invalid country code
        assertNull(LocaleUtils.toLocale("en_xx"));
        assertNull(LocaleUtils.toLocale("en_XX"));

        // Test invalid language code
        assertNull(LocaleUtils.toLocale("xx_US"));
        assertNull(LocaleUtils.toLocale("XX_US"));

        // Test invalid separator
        assertNull(LocaleUtils.toLocale("en_US_xX"));
        assertNull(LocaleUtils.toLocale("en_US_Xx"));
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        assertEquals(0, LocaleUtils.localeLookupList(null).size());

        // Test basic usage
        List<Locale> localeList = LocaleUtils.localeLookupList(LocaleUtils.toLocale("fr_CA"));
        assertEquals("fr_CA", localeList.get(0).toString());
        assertEquals("fr_CA", localeList.get(1).toString());
        assertEquals("fr", localeList.get(2).toString());

        // Test default locale
        List<Locale> localeListDefault = LocaleUtils.localeLookupList(LocaleUtils.toLocale("fr_CA"), LocaleUtils.toLocale("en"));
        assertEquals("fr_CA", localeListDefault.get(0).toString());
        assertEquals("fr_CA", localeListDefault.get(1).toString());
        assertEquals("fr", localeListDefault.get(2).toString());
        assertEquals("en", localeListDefault.get(3).toString());

        // Test no match
        List<Locale> localeListNoMatch = LocaleUtils.localeLookupList(LocaleUtils.toLocale("en_US"), LocaleUtils.toLocale("fr"));
        assertEquals(0, localeListNoMatch.size());
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        assertEquals(0, LocaleUtils.availableLocaleList().size());

        // Test valid usage
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertEquals(Locale.SIMPLIFIED_CHINESE, localeList.get(0));
        assertEquals(Locale.TRADITIONAL_CHINESE, localeList.get(1));
        assertEquals(Locale.ENGLISH, localeList.get(2));
        // ... add more assertions for other available locales

        // Test no match
        assertFalse(localeList.contains(Locale.ITALIAN));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        assertEquals(0, LocaleUtils.languagesByCountry(null).size());

        // Test valid usage
        List<Locale> localeList = LocaleUtils.languagesByCountry("CA");
        assertEquals(Locale.FRENCH, localeList.get(0));
        assertEquals(Locale.ENGLISH, localeList.get(1));
        // ... add more assertions for other languages available for "CA"

        // Test no match
        assertEquals(0, LocaleUtils.languagesByCountry("ZZ").size());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty list
        assertEquals(0, LocaleUtils.countriesByLanguage(null).size());

        // Test valid usage
        List<Locale> localeList = LocaleUtils.countriesByLanguage("FR");
        assertEquals(Locale.CANADA, localeList.get(0));
        assertEquals(Locale.FRANCE, localeList.get(1));
        // ... add more assertions for other countries available for "FR"

        // Test no match
        assertEquals(0, LocaleUtils.countriesByLanguage("ZZ").size());
    }
}