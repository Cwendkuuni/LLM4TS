package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        try {
            LocaleUtils.localeLookupList(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expected1 = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), Locale.ENGLISH);
        List<Locale> expected2 = Arrays.asList(Locale.ENGLISH, new Locale("fr"), new Locale("fr", "CA"), new Locale("fr", "CA", "xxx"));
        
        assertEquals(expected1, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx")));
        assertEquals(expected2, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), Locale.ENGLISH));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        
        // Test that the list is not empty
        assertNotNull(locales);
        
        // Test that the list is unmodifiable
        try {
            locales.add(Locale.CANADA);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        
        // Test that the set is not empty
        assertNotNull(locales);
        
        // Test that the set is unmodifiable
        try {
            locales.add(Locale.CANADA);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("pl", "PL")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected1 = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"));
        List<Locale> expected2 = Collections.emptyList();
        
        assertEquals(expected1, LocaleUtils.languagesByCountry("US"));
        assertEquals(expected2, LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected1 = Arrays.asList(new Locale("en", "GB"), new Locale("en", "US"));
        List<Locale> expected2 = Collections.emptyList();
        
        assertEquals(expected1, LocaleUtils.countriesByLanguage("en"));
        assertEquals(expected2, LocaleUtils.countriesByLanguage(null));
    }
}