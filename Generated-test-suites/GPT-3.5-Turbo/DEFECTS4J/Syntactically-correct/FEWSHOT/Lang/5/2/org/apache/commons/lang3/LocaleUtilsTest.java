package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("fr", "FR", "xxx"), LocaleUtils.toLocale("fr_FR_xxx"));
        assertEquals(new Locale("ja", "JP"), LocaleUtils.toLocale("ja_JP"));
        
        try {
            LocaleUtils.toLocale("1");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: 1", e.getMessage());
        }
        try {
            LocaleUtils.toLocale("EN_GB");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: EN_GB", e.getMessage());
        }
        try {
            LocaleUtils.toLocale("en_GB.x");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_GB.x", e.getMessage());
        }
        try {
            LocaleUtils.toLocale("en_GB_");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_GB_", e.getMessage());
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        List<Locale> expectedList1 = Arrays.asList(new Locale("en", "US"), new Locale("en"), Locale.getDefault());
        assertEquals(expectedList1, LocaleUtils.localeLookupList(new Locale("en", "US")));
        
        List<Locale> expectedList2 = Arrays.asList(new Locale("en", "US"), new Locale("en"), Locale.getDefault());
        assertEquals(expectedList2, LocaleUtils.localeLookupList(new Locale("en", "US"), new Locale("fr")));
        
        List<Locale> expectedList3 = Arrays.asList(new Locale("fr", "FR"), new Locale("fr"), Locale.getDefault());
        assertEquals(expectedList3, LocaleUtils.localeLookupList(new Locale("fr", "FR")));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = new ArrayList<Locale>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expectedList, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = new HashSet<Locale>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expectedSet, LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("es", "US"));
        assertEquals(expectedList, LocaleUtils.languagesByCountry("US"));
        
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"));
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("en"));
        
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}