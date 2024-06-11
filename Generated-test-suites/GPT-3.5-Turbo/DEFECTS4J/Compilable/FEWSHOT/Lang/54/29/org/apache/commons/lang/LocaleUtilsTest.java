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
        
        boolean exceptionThrown = false;
        try {
            LocaleUtils.toLocale("en_");
        } catch(IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertEquals(true, exceptionThrown);
        
        exceptionThrown = false;
        try {
            LocaleUtils.toLocale("en_GB_xx");
        } catch(IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertEquals(true, exceptionThrown);
        
        exceptionThrown = false;
        try {
            LocaleUtils.toLocale("");
        } catch(IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertEquals(true, exceptionThrown);
        
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList1() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), Locale.getDefault());
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLocaleLookupList2() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
    
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAvailableLocaleList() {
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        List<Locale> actual = LocaleUtils.availableLocaleList();
        
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.containsAll(actual), actual.containsAll(expected));
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> actual = LocaleUtils.availableLocaleSet();

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.containsAll(actual), actual.containsAll(expected));
    }
    
    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        boolean expected = true;
        boolean actual = LocaleUtils.isAvailableLocale(locale);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        expected = Collections.unmodifiableList(expected);
        
        List<Locale> actual = LocaleUtils.languagesByCountry("US");
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        expected = Collections.unmodifiableList(expected);
        
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        
        assertEquals(expected, actual);
    }
}