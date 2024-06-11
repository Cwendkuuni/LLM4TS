package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LocaleUtilsTest {
    
    @Before
    public void setup() {
        LocaleUtils.availableLocaleSet().clear();
    }
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // Invalid formats
        try {
            LocaleUtils.toLocale("en_GB_");
            assertTrue("IllegalArgumentException not thrown.", false);
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            LocaleUtils.toLocale("en_GB_x");
            assertTrue("IllegalArgumentException not thrown.", false);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        
        assertEquals(3, list.size());
        assertEquals(new Locale("fr", "CA", "xxx"), list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
        
        list = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(4, list.size());
        assertEquals(new Locale("fr", "CA", "xxx"), list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
        assertEquals(new Locale ("en"), list.get(3));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertTrue(list.isEmpty());
        
        list.add(new Locale("en"));
        list.add(new Locale("fr", "CA"));
        list.add(new Locale("es", "ES"));
        
        list = LocaleUtils.availableLocaleList();
        
        assertEquals(3, list.size());
        assertEquals(new Locale("en"), list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("es", "ES"), list.get(2));
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertTrue(set.isEmpty());
        
        set.add(new Locale("en"));
        set.add(new Locale("fr", "CA"));
        set.add(new Locale("es", "ES"));
        
        set = LocaleUtils.availableLocaleSet();
        
        assertEquals(3, set.size());
        assertTrue(set.contains(new Locale("en")));
        assertTrue(set.contains(new Locale("fr", "CA")));
        assertTrue(set.contains(new Locale("es", "ES")));
    }
    
    @Test
    public void testIsAvailableLocale() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertTrue(!LocaleUtils.isAvailableLocale(new Locale("en")));
        
        set.add(new Locale("en"));
        set.add(new Locale("fr", "CA"));
        set.add(new Locale("es", "ES"));
        
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertTrue(!LocaleUtils.isAvailableLocale(new Locale("de")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("CA");
        assertTrue(list.isEmpty());
        
        list.add(new Locale("en"));
        list.add(new Locale("fr", "CA"));
        list.add(new Locale("es", "ES"));
        
        list = LocaleUtils.languagesByCountry("CA");
        
        assertEquals(2, list.size());
        assertEquals(new Locale("en"), list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("fr");
        assertTrue(list.isEmpty());
        
        list.add(new Locale("en"));
        list.add(new Locale("fr", "CA"));
        list.add(new Locale("es", "ES"));
        
        list = LocaleUtils.countriesByLanguage("fr");
        
        assertEquals(1, list.size());
        assertEquals(new Locale("fr", "CA"), list.get(0));
    }
}