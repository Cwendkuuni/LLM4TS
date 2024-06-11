package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThrowsException() {
        LocaleUtils.toLocale("english_us");
    }
    
    @Test
    public void testToLocale_NullInput_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testLocaleLookupList_WithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testLocaleLookupList_NullInput_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        
        assertEquals(Collections.EMPTY_LIST, result);
    }
    
    @Test
    public void testLocaleLookupList_NullDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, null);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = new ArrayList<>(Arrays.asList(Locale.getAvailableLocales()));
        
        List<Locale> result = LocaleUtils.availableLocaleList();
        
        assertEquals(availableLocales, result);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        
        assertEquals(availableLocales, result);
    }
    
    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ReturnsTrue() {
        Locale locale = new Locale("en", "US");
        
        boolean result = LocaleUtils.isAvailableLocale(locale);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testIsAvailableLocale_WithUnavailableLocale_ReturnsFalse() {
        Locale locale = new Locale("zz", "ZZ");
        
        boolean result = LocaleUtils.isAvailableLocale(locale);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testLanguagesByCountry() {
        String countryCode = "US";
        List<Locale> expectedResult = new ArrayList<>();
        expectedResult.add(new Locale("en", "US"));
        
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testLanguagesByCountry_WithNullInput_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        
        assertEquals(Collections.EMPTY_LIST, result);
    }
    
    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> expectedResult = new ArrayList<>();
        expectedResult.add(new Locale("en", "US"));
        
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testCountriesByLanguage_WithNullInput_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        
        assertEquals(Collections.EMPTY_LIST, result);
    }
}