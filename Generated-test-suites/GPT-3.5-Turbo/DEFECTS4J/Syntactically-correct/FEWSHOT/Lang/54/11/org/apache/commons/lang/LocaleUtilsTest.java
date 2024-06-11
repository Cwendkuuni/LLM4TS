package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // Test invalid input
        assertNull(LocaleUtils.toLocale("")); // Empty string
        assertNull(LocaleUtils.toLocale("e")); // Invalid language code
        assertNull(LocaleUtils.toLocale("enG")); // Invalid country code
        assertNull(LocaleUtils.toLocale("en_G")); // Invalid separator
        assertNull(LocaleUtils.toLocale("en_GB_xxxx")); // Invalid length
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(3, result.size());
        assertEquals(new Locale("fr", "CA", "xxx"), result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        
        result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, result.size());
        assertEquals(new Locale("fr", "CA", "xxx"), result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(new Locale("en"), result.get(3));
        
        // Test null input
        assertTrue(LocaleUtils.localeLookupList(null).isEmpty());
        assertTrue(LocaleUtils.localeLookupList(null, defaultLocale).isEmpty());
        assertTrue(LocaleUtils.localeLookupList(locale, null).isEmpty());
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(Locale.getAvailableLocales().length, result.size());
        
        // Verify that the list is unmodifiable
        try {
            result.add(new Locale("foo")); // Try adding a new locale
            assertTrue(false); // Fail if the list is modifiable
        } catch (UnsupportedOperationException e) {
            assertTrue(true); // Pass if the list is unmodifiable
        }
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertEquals(Locale.getAvailableLocales().length, result.size());
        
        // Verify that the set is unmodifiable
        try {
            result.add(new Locale("foo")); // Try adding a new locale
            assertTrue(false); // Fail if the set is modifiable
        } catch (UnsupportedOperationException e) {
            assertTrue(true); // Pass if the set is unmodifiable
        }
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRANCE));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("CA");
        assertTrue(result.contains(new Locale("fr", "CA")));
        
        result = LocaleUtils.languagesByCountry("ZZ"); // Invalid country code
        assertTrue(result.isEmpty());
        
        // Test null input
        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertTrue(result.contains(new Locale("en", "GB")));
        
        result = LocaleUtils.countriesByLanguage("zz"); // Invalid language code
        assertTrue(result.isEmpty());
        
        // Test null input
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }
}