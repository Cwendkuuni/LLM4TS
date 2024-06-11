
package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {
  
    @Test
    public void testToLocale() {
        // valid locales
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // invalid locales
        try {
            LocaleUtils.toLocale(null);
        } catch (IllegalArgumentException e) {
            assertNull(e.getMessage());
        }
        
        try {
            LocaleUtils.toLocale("en_GB_");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_GB_", e.getMessage());
        }
    }
  
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", ""), result.get(1));
        assertEquals(new Locale("fr", "CA"), result.get(2));
        
        locale = new Locale("fr", "CA", "xxx");
        result = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(4, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(new Locale("en"), result.get(3));
    }
  
    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertTrue(result.contains(Locale.ENGLISH));
        assertTrue(result.contains(Locale.FRENCH));
    }
  
    @Test
    public void testAvailableLocaleSet() {
        assertEquals(new Locale("en"), LocaleUtils.availableLocaleSet().iterator().next());
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("GB");
        assertEquals(1, result.size());
        assertEquals(Locale.ENGLISH, result.get(0));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("fr");
        assertEquals(1, result.size());
        assertEquals(Locale.CANADA_FRENCH, result.get(0));
    }

}
