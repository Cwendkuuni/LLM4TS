package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // Test invalid inputs
        assertNull(LocaleUtils.toLocale(null));
        try {
            LocaleUtils.toLocale("");
            assertFalse("Expected IllegalArgumentException not thrown", true);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        try {
            LocaleUtils.toLocale("eng");
            assertFalse("Expected IllegalArgumentException not thrown", true);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        try {
            LocaleUtils.toLocale("en_");
            assertFalse("Expected IllegalArgumentException not thrown", true);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        try {
            LocaleUtils.toLocale("en_gb");
            assertFalse("Expected IllegalArgumentException not thrown", true);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        try {
            LocaleUtils.toLocale("en_gb_xxx");
            assertFalse("Expected IllegalArgumentException not thrown", true);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        
        // Test scenario 1
        assertEquals(4, LocaleUtils.localeLookupList(locale).size());
        assertEquals(locale, LocaleUtils.localeLookupList(locale).get(0));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.localeLookupList(locale).get(1));
        assertEquals(new Locale("fr"), LocaleUtils.localeLookupList(locale).get(2));
        assertEquals(defaultLocale, LocaleUtils.localeLookupList(locale).get(3));
        
        // Test scenario 2
        assertEquals(4, LocaleUtils.localeLookupList(locale, defaultLocale).size());
        assertEquals(locale, LocaleUtils.localeLookupList(locale, defaultLocale).get(0));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.localeLookupList(locale, defaultLocale).get(1));
        assertEquals(new Locale("fr"), LocaleUtils.localeLookupList(locale, defaultLocale).get(2));
        assertEquals(defaultLocale, LocaleUtils.localeLookupList(locale, defaultLocale).get(3));
    }
    
    @Test
    public void testAvailableLocaleList() {
        // Test that availableLocaleList() returns a non-empty list
        assertTrue(LocaleUtils.availableLocaleList().size() > 0);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        // Test that availableLocaleSet() returns a non-empty set
        assertTrue(LocaleUtils.availableLocaleSet().size() > 0);
    }
    
    @Test
    public void testIsAvailableLocale() {
        // Test that isAvailableLocale() returns true for a valid locale
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        
        // Test that isAvailableLocale() returns false for an invalid locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xyz")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        // Test with valid country code
        assertEquals(1, LocaleUtils.languagesByCountry("GB").size());
        assertEquals(new Locale("en", "GB"), LocaleUtils.languagesByCountry("GB").get(0));
        
        // Test with invalid country code
        assertEquals(0, LocaleUtils.languagesByCountry("ZZ").size());
    }
    
    @Test
    public void testCountriesByLanguage() {
        // Test with valid language code
        assertEquals(1, LocaleUtils.countriesByLanguage("en").size());
        assertEquals(new Locale("en", "GB"), LocaleUtils.countriesByLanguage("en").get(0));
        
        // Test with invalid language code
        assertEquals(0, LocaleUtils.countriesByLanguage("zz").size());
    }
}