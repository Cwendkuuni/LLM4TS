package org.apache.commons.lang3;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(null, LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        Exception exception = null;
        try {
            LocaleUtils.toLocale("#");
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertEquals("Invalid locale format: #", exception.getMessage());
        
        exception = null;
        try {
            LocaleUtils.toLocale("a");
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertEquals("Invalid locale format: a", exception.getMessage());
        
        exception = null;
        try {
            LocaleUtils.toLocale("a_");
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertEquals("Invalid locale format: a_", exception.getMessage());
        
        exception = null;
        try {
            LocaleUtils.toLocale("a_0");
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertEquals("Invalid locale format: a_0", exception.getMessage());
        
        exception = null;
        try {
            LocaleUtils.toLocale("a_a_AB");
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertEquals("Invalid locale format: a_a_AB", exception.getMessage());
        
        exception = null;
        try {
            LocaleUtils.toLocale("ab_XX");
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertEquals("Invalid locale format: ab_XX", exception.getMessage());
        
        exception = null;
        try {
            LocaleUtils.toLocale("ab_XXa_");
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertEquals("Invalid locale format: ab_XXa_", exception.getMessage());
        
        exception = null;
        try {
            LocaleUtils.toLocale("_X!");
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertEquals("Invalid locale format: _X!", exception.getMessage());
        
        exception = null;
        try {
            LocaleUtils.toLocale("_X_A");
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertEquals("Invalid locale format: _X_A", exception.getMessage());
        
        exception = null;
        try {
            LocaleUtils.toLocale("_XA_");
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertEquals("Invalid locale format: _XA_", exception.getMessage());
        
        assertEquals(new Locale("", "", "A"), LocaleUtils.toLocale("_XA"));
        assertEquals(new Locale("", "A", "B"), LocaleUtils.toLocale("_AB"));
        assertEquals(new Locale("", "A"), LocaleUtils.toLocale("_A"));
        assertEquals(new Locale("", "AB"), LocaleUtils.toLocale("_AB"));
        assertEquals(new Locale("a", ""), LocaleUtils.toLocale("a_"));
        assertEquals(new Locale("a"), LocaleUtils.toLocale("a"));
        assertEquals(new Locale("ab"), LocaleUtils.toLocale("ab"));
        assertEquals(new Locale("ab", "", "AB"), LocaleUtils.toLocale("ab_XX_AB"));
        assertEquals(new Locale("ab", "", "ABC"), LocaleUtils.toLocale("ab_XX_ABC"));
        assertEquals(new Locale("ab"), LocaleUtils.toLocale("ab_XX_"));
        assertEquals(new Locale("", "XX"), LocaleUtils.toLocale("_XX"));
        assertEquals(new Locale("", "XX", "AB"), LocaleUtils.toLocale("_XX_AB"));
        assertEquals(new Locale("", "XX", "ABC"), LocaleUtils.toLocale("_XX_ABC"));
        assertEquals(new Locale("", "A"), LocaleUtils.toLocale("_A_"));
    }
    
    @Test
    public void testLocaleLookupList() {
        List<Locale> list = new ArrayList<Locale>();
        list.add(new Locale("en"));
        list.add(new Locale("en", "US"));
        assertEquals(list, LocaleUtils.localeLookupList(new Locale("en", "US")));
        
        list = new ArrayList<Locale>();
        list.add(new Locale("en"));
        list.add(new Locale("en", "US"));
        list.add(new Locale("en", ""));
        assertEquals(list, LocaleUtils.localeLookupList(new Locale("en", "")));
        
        list = new ArrayList<Locale>();
        list.add(new Locale("en"));
        list.add(new Locale("en", "GB"));
        list.add(new Locale("en", ""));
        list.add(new Locale("en", "US"));
        assertEquals(list, LocaleUtils.localeLookupList(new Locale("en", "GB")));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(list, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = new HashSet<Locale>();
        set.addAll(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(set, LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertEquals(true, LocaleUtils.isAvailableLocale(Locale.US));
        assertEquals(false, LocaleUtils.isAvailableLocale(new Locale("unknown")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = new ArrayList<Locale>();
        list.add(new Locale("en"));
        assertEquals(list, LocaleUtils.languagesByCountry("US"));
        
        list = new ArrayList<Locale>();
        list.add(new Locale("unknown"));
        assertEquals(list, LocaleUtils.languagesByCountry("unknown"));
        
        list = new ArrayList<Locale>();
        list.add(new Locale("en"));
        list.add(new Locale("en", "GB"));
        assertEquals(list, LocaleUtils.languagesByCountry("GB"));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = new ArrayList<Locale>();
        list.add(new Locale("", "US"));
        assertEquals(list, LocaleUtils.countriesByLanguage("en"));
        
        list = new ArrayList<Locale>();
        list.add(new Locale("unknown"));
        assertEquals(list, LocaleUtils.countriesByLanguage("unknown"));
        
        list = new ArrayList<Locale>();
        list.add(new Locale("", "QA"));
        list.add(new Locale("", "SA"));
        assertEquals(list, LocaleUtils.countriesByLanguage("ar"));
    }
}