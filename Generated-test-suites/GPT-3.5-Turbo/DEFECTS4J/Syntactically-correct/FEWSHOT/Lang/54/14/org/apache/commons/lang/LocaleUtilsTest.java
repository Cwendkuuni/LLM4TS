package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR_xxx"));
        assertEquals(Locale.CANADA_FRENCH, LocaleUtils.toLocale("fr_CA"));
        assertEquals(Locale.CANADA_FRENCH, LocaleUtils.toLocale("fr_CA_xxx"));
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLanguage() {
        LocaleUtils.toLocale("EN");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountry() {
        LocaleUtils.toLocale("en_gB");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidSeparator() {
        LocaleUtils.toLocale("en-GB");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLength() {
        LocaleUtils.toLocale("en_GB_XXX");
    }
    
    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(Locale.US);
        expected.add(Locale.ENGLISH);
        expected.add(Locale.ROOT);
        
        List<Locale> result = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(expected, result);
        
        expected.add(Locale.FRENCH);
        result = LocaleUtils.localeLookupList(Locale.US, Locale.FRENCH);
        assertEquals(expected, result);
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        
        assertTrue(locales.contains(Locale.US));
        assertTrue(locales.contains(Locale.ENGLISH));
        assertTrue(locales.contains(Locale.FRENCH));
        
        assertFalse(locales.contains(new Locale("xx", "YY")));
        assertFalse(locales.contains(Locale.ROOT));
    }
    
    @Test
    public void testAvailableLocaleSet() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertEquals(new HashSet<Locale>(locales), new HashSet<Locale>(LocaleUtils.availableLocaleSet()));
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRANCE));
        
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "YY")));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ROOT));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(Locale.ENGLISH);
        expected.add(Locale.FRENCH);
        
        List<Locale> result = LocaleUtils.languagesByCountry("GB");
        assertEquals(expected, result);
        
        expected.add(Locale.US);
        result = LocaleUtils.languagesByCountry("US");
        assertEquals(expected, result);
        
        expected.clear();
        expected.add(Locale.ENGLISH);
        expected.add(Locale.FRENCH);
        result = LocaleUtils.languagesByCountry(null);
        assertEquals(expected, result);
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(Locale.UK);
        expected.add(Locale.US);
        
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, result);
        
        expected.add(Locale.CANADA);
        result = LocaleUtils.countriesByLanguage("en_US");
        assertEquals(expected, result);
        
        expected.clear();
        expected.add(Locale.FRANCE);
        expected.add(Locale.CANADA);
        result = LocaleUtils.countriesByLanguage("fr");
        assertEquals(expected, result);
        
        expected.clear();
        result = LocaleUtils.countriesByLanguage(null);
        assertEquals(expected, result);
    }
}