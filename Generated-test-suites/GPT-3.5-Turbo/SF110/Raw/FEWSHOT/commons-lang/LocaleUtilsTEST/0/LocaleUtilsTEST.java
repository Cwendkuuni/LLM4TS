package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Locale;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        // Test null input
        assertNull(LocaleUtils.toLocale(null));
        
        // Test empty input
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        
        // Test input with #
        try {
            LocaleUtils.toLocale("en#us");
            assertTrue("IllegalArgumentException not thrown for input with #", false);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en#us", e.getMessage());
        }
        
        // Test invalid format
        try {
            LocaleUtils.toLocale("e");
            assertTrue("IllegalArgumentException not thrown for invalid locale format", false);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: e", e.getMessage());
        }
        
        // Test valid locale format
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en__"));
        assertEquals(new Locale("en", "US", "NY"), LocaleUtils.toLocale("en_US_NY"));
        assertEquals(new Locale("", "US", "NY"), LocaleUtils.toLocale("_US_NY"));
    }
    
    @Test
    public void testLocaleLookupList() {
        // Test null locale
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
        
        // Test locale with variant
        Locale locale1 = new Locale("en", "US", "NY");
        List<Locale> expected1 = Arrays.asList(locale1, new Locale("en", "US"));
        List<Locale> actual1 = LocaleUtils.localeLookupList(locale1);
        assertEquals(expected1, actual1);
        
        // Test locale with country
        Locale locale2 = new Locale("en", "US");
        List<Locale> expected2 = Arrays.asList(locale2, new Locale("en", ""));
        List<Locale> actual2 = LocaleUtils.localeLookupList(locale2);
        assertEquals(expected2, actual2);
        
        // Test locale without variant or country
        Locale locale3 = new Locale("en");
        List<Locale> expected3 = Arrays.asList(locale3);
        List<Locale> actual3 = LocaleUtils.localeLookupList(locale3);
        assertEquals(expected3, actual3);
        
        // Test locale with default locale not present in list
        Locale locale4 = new Locale("fr");
        Locale defaultLocale4 = new Locale("en", "US");
        List<Locale> expected4 = Arrays.asList(locale4, defaultLocale4);
        List<Locale> actual4 = LocaleUtils.localeLookupList(locale4, defaultLocale4);
        assertEquals(expected4, actual4);
    }
    
    @Test
    public void testAvailableLocaleList() {
        // Test available locale list
        List<Locale> availableLocales = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(availableLocales, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        // Test available locale set
        Set<Locale> availableLocales = new HashSet<Locale>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(availableLocales, LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        // Test null locale
        assertFalse(LocaleUtils.isAvailableLocale(null));
        
        // Test available locale
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        
        // Test unavailable locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz", "ZZ")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        // Test null country code
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
        
        // Test languages by country
        List<Locale> expected1 = Arrays.asList(Locale.ENGLISH, Locale.ITALIAN, Locale.JAPANESE);
        List<Locale> actual1 = LocaleUtils.languagesByCountry("US");
        assertEquals(expected1, actual1);
        
        // Test country with no languages
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("ZZ"));
    }
    
    @Test
    public void testCountriesByLanguage() {
        // Test null language code
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
        
        // Test countries by language
        List<Locale> expected1 = Arrays.asList(Locale.US, Locale.CANADA, Locale.UK);
        List<Locale> actual1 = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected1, actual1);
        
        // Test language with no countries
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("zz"));
    }
}