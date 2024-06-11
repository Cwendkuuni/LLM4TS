package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.Locale;
import java.util.List;
import java.util.Set;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertNull(LocaleUtils.toLocale(null));
        assertNotNull(LocaleUtils.toLocale(""));
        assertNotNull(LocaleUtils.toLocale("en"));
        assertNotNull(LocaleUtils.toLocale("en_US"));
        assertNotNull(LocaleUtils.toLocale("_US"));
        assertNotNull(LocaleUtils.toLocale("__US"));
        assertNull(LocaleUtils.toLocale("#"));
        assertNull(LocaleUtils.toLocale("_"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_US_"));
        assertNull(LocaleUtils.toLocale("en_US_1234"));
    }
    
    @Test
    public void testLocaleLookupList() {
        assertNull(LocaleUtils.localeLookupList(null));
        assertNull(LocaleUtils.localeLookupList(null, null));
        
        Locale locale = new Locale("en");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        assertNotNull(lookupList);
        assertEquals(1, lookupList.size());
        assertEquals(locale, lookupList.get(0));
        
        Locale defaultLocale = new Locale("en_US");
        lookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertNotNull(lookupList);
        assertEquals(2, lookupList.size());
        assertTrue(lookupList.contains(locale));
        assertTrue(lookupList.contains(defaultLocale));
        
        locale = new Locale("en_US", "NY");
        lookupList = LocaleUtils.localeLookupList(locale);
        assertNotNull(lookupList);
        assertEquals(3, lookupList.size());
        assertTrue(lookupList.contains(locale));
        assertTrue(lookupList.contains(new Locale("en", "US")));
        assertTrue(lookupList.contains(new Locale("en", "")));
        
        defaultLocale = new Locale("fr_FR");
        lookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertNotNull(lookupList);
        assertEquals(4, lookupList.size());
        assertTrue(lookupList.contains(locale));
        assertTrue(lookupList.contains(new Locale("en", "US")));
        assertTrue(lookupList.contains(new Locale("en", "")));
        assertTrue(lookupList.contains(defaultLocale));
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
        assertFalse(LocaleUtils.isAvailableLocale(null));
        
        Locale locale = new Locale("en");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        assertTrue(isAvailable);
        
        locale = new Locale("xx");
        isAvailable = LocaleUtils.isAvailableLocale(locale);
        assertFalse(isAvailable);
    }
    
    @Test
    public void testLanguagesByCountry() {
        assertNull(LocaleUtils.languagesByCountry(null));
        List<Locale> langList = LocaleUtils.languagesByCountry("US");
        assertNotNull(langList);
        assertFalse(langList.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage() {
        assertNull(LocaleUtils.countriesByLanguage(null));
        List<Locale> countryList = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countryList);
        assertFalse(countryList.isEmpty());
    }
    
}