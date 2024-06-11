package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;

public class LocaleUtilsTest {
  
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            LocaleUtils.toLocale("eN");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            LocaleUtils.toLocale("EN_GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            LocaleUtils.toLocale("en_gb");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
        
        Locale defaultLocale = new Locale("en");
        expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList() {
        assertTrue(LocaleUtils.availableLocaleList().contains(Locale.ENGLISH));
        assertTrue(LocaleUtils.availableLocaleList().contains(Locale.FRENCH));
        assertTrue(LocaleUtils.availableLocaleList().contains(Locale.GERMAN));
    }
    
    @Test
    public void testAvailableLocaleSet() {
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.ENGLISH));
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.FRENCH));
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.GERMAN));
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(Locale.CANADA_FRENCH);
        assertEquals(expected, LocaleUtils.languagesByCountry("CA"));
        
        expected = Arrays.asList();
        assertEquals(expected, LocaleUtils.languagesByCountry("US"));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(Locale.CANADA, Locale.CANADA_FRENCH);
        assertEquals(expected, LocaleUtils.countriesByLanguage("fr"));
        
        expected = Arrays.asList(Locale.ENGLISH);
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
    }
}