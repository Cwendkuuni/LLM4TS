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

import org.junit.Before;
import org.junit.Test;

public class LocaleUtilsTest {
    
    @Before
    public void setUp(){
        Locale.setDefault(new Locale("en", "US"));
    }
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat(){
        LocaleUtils.toLocale("ent");
    }
    
    @Test
    public void testLocaleLookupList(){
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("fr", "CA", "xxx"));
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        expected.add(new Locale("en"));
        
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
    }
    
    @Test
    public void testAvailableLocaleList(){
        List<Locale> expected = new ArrayList<>(Arrays.asList(Locale.getAvailableLocales()));
        Collections.sort(expected, (a, b) -> a.toString().compareTo(b.toString()));
        
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet(){
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale(){
        assertEquals(true, LocaleUtils.isAvailableLocale(new Locale("en", "US")));
    }
    
    @Test
    public void testIsNotAvailableLocale(){
        assertEquals(false, LocaleUtils.isAvailableLocale(new Locale("fr", "CA")));
    }
    
    @Test
    public void testLanguagesByCountry(){
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", ""));
        
        assertEquals(expected, LocaleUtils.languagesByCountry("US"));
    }
    
    @Test
    public void testCountriesByLanguage(){
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("US"));
        
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
    }
}