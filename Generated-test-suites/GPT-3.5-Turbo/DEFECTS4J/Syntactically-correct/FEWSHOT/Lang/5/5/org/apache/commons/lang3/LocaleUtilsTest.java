package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = null;
        
        // Test null String
        assertNull(LocaleUtils.toLocale(null));
        
        // Test valid locale formats
        locale = LocaleUtils.toLocale("en");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
        assertEquals("", locale.getVariant());
        
        locale = LocaleUtils.toLocale("en_GB");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("", locale.getVariant());
        
        locale = LocaleUtils.toLocale("en_GB_xxx");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("xxx", locale.getVariant());
        
        // Test invalid locale formats
        boolean exceptionThrown = false;
        try {
            LocaleUtils.toLocale("ENG");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
        
        exceptionThrown = false;
        try {
            LocaleUtils.toLocale("EN_GB");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        
        assertEquals(3, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
        
        Locale defaultLocale = new Locale("en");
        localeList = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(4, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
        assertEquals(defaultLocale, localeList.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        
        assertNotNull(localeList);
        assertFalse(localeList.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        
        assertNotNull(localeSet);
        assertFalse(localeSet.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale availableLocale = new Locale("en", "US");
        Locale unavailableLocale = new Locale("xx", "XX");
        
        assertTrue(LocaleUtils.isAvailableLocale(availableLocale));
        assertFalse(LocaleUtils.isAvailableLocale(unavailableLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> languages = LocaleUtils.languagesByCountry(countryCode);
        
        assertEquals(2, languages.size());
        assertEquals(new Locale("fr", "CA"), languages.get(0));
        assertEquals(new Locale("en", "CA"), languages.get(1));
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> countries = LocaleUtils.countriesByLanguage(languageCode);
        
        assertEquals(2, countries.size());
        assertEquals(new Locale("en", "CA"), countries.get(0));
        assertEquals(new Locale("en", ""), countries.get(1));
    }
}