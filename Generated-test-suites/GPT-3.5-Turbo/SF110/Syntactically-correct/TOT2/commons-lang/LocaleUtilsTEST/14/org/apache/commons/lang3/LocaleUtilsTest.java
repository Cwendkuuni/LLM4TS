package org.apache.commons.lang3;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {
    
    // Test for toLocale method
    @Test
    public void testToLocale_basic() {
        Locale result = LocaleUtils.toLocale("en_US");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }
    
    // Test for localeLookupList method
    @Test
    public void testLocaleLookupList_basic() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(4, result.size());
    }
    
    // Test for availableLocaleList method
    @Test
    public void testAvailableLocaleList_basic() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    // Test for availableLocaleSet method
    @Test
    public void testAvailableLocaleSet_basic() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    // Test for isAvailableLocale method
    @Test
    public void testIsAvailableLocale_basic() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }
    
    // Test for languagesByCountry method
    @Test
    public void testLanguagesByCountry_basic() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    // Test for countriesByLanguage method
    @Test
    public void testCountriesByLanguage_basic() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    // Additional tests for edge cases and exceptions can be added here
    
}