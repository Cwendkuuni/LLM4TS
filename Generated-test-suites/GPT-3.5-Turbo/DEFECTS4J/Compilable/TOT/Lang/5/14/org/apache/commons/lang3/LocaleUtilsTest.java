package org.apache.commons.lang3;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validInput() {
        // Typical input: "en_GB"
        Locale result = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), result);
        
        // Edge case: Language code has only two characters (minimum valid length)
        result = LocaleUtils.toLocale("fr");
        assertEquals(new Locale("fr", ""), result);
        
        // Edge case: Language code has more than two characters
        result = LocaleUtils.toLocale("en_US_xxx");
        assertEquals(new Locale("en", "US", "xxx"), result);
        
        // Error scenario: Invalid format, input is null
        result = LocaleUtils.toLocale(null);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        // Error scenario: Invalid format, input has length less than 2
        LocaleUtils.toLocale("e");
        
        // Error scenario: Invalid format, language code is not lowercase
        LocaleUtils.toLocale("DE");
        
        // Error scenario: Invalid format, country code is not uppercase
        LocaleUtils.toLocale("en_gb");
        
        // Error scenario: Invalid format, separator is not underscore
        LocaleUtils.toLocale("en-GB");
    }
    
    @Test
    public void testLocaleLookupList() {
        // Typical input: locale = Locale("fr", "CA", "xxx"), defaultLocale = Locale("en")
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertEquals(4, result.size());
        assertEquals(new Locale("fr", "CA", "xxx"), result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(new Locale("en"), result.get(3));
        
        // Error scenario: locale is null, defaultLocale is not null
        result = LocaleUtils.localeLookupList(null, new Locale("en"));
        assertEquals(1, result.size());
        assertEquals(new Locale("en"), result.get(0));
        
        // Error scenario: locale and defaultLocale are null
        result = LocaleUtils.localeLookupList(null, null);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testIsAvailableLocale() {
        // Typical input: locale is available
        boolean result = LocaleUtils.isAvailableLocale(Locale.CANADA);
        assertTrue(result);
        
        // Typical input: locale is not available
        result = LocaleUtils.isAvailableLocale(new Locale("zz"));
        assertFalse(result);
    }
    
    @Test
    public void testLanguagesByCountry() {
        // Typical input: countryCode = "CA"
        List<Locale> result = LocaleUtils.languagesByCountry("CA");
        assertEquals(2, result.size());
        assertTrue(result.contains(new Locale("en", "CA")));
        assertTrue(result.contains(new Locale("fr", "CA")));
        
        // Edge case: countryCode is null
        result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage() {
        // Typical input: languageCode = "en"
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(261, result.size());
        assertTrue(result.contains(new Locale("en", "AU")));
        assertTrue(result.contains(new Locale("en", "US")));
        
        // Edge case: languageCode is null
        result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }
    
}