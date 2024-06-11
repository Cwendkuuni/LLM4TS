package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        assertNull(LocaleUtils.toLocale(null));
        assertTrue(LocaleUtils.toLocale("").equals(new Locale("")));
        assertTrue(LocaleUtils.toLocale("a").equals(new Locale("a")));
        assertTrue(LocaleUtils.toLocale("aa").equals(new Locale("aa")));
        assertTrue(LocaleUtils.toLocale("aA").equals(new Locale("aA")));
        assertTrue(LocaleUtils.toLocale("aaa").equals(new Locale("aaa")));
        assertTrue(LocaleUtils.toLocale("a_aa").equals(new Locale("a_aa")));
        assertTrue(LocaleUtils.toLocale("aaa_bbb").equals(new Locale("aaa_bbb")));
        assertTrue(LocaleUtils.toLocale("aa_bb_cc").equals(new Locale("aa_bb_cc")));
        assertTrue(LocaleUtils.toLocale("__aa").equals(new Locale("__aa")));
        assertTrue(LocaleUtils.toLocale("aaa__").equals(new Locale("aaa__")));
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
        
        Locale defaultLocale = new Locale("en");
        list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
        assertEquals(defaultLocale, list.get(3));
        
        assertNull(LocaleUtils.localeLookupList(null));
        list = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(defaultLocale, list.get(0));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertTrue(list.contains(Locale.ENGLISH));
        assertTrue(list.contains(Locale.FRENCH));
        assertTrue(list.contains(Locale.GERMAN));
        assertTrue(list.contains(Locale.CHINESE));
        assertTrue(list.contains(Locale.JAPANESE));
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertTrue(set.contains(Locale.ENGLISH));
        assertTrue(set.contains(Locale.FRENCH));
        assertTrue(set.contains(Locale.GERMAN));
        assertTrue(set.contains(Locale.CHINESE));
        assertTrue(set.contains(Locale.JAPANESE));
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CHINESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.JAPANESE));
        
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.KOREAN));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("CA");
        assertTrue(list.contains(Locale.ENGLISH));
        assertTrue(list.contains(Locale.FRENCH));
        
        list = LocaleUtils.languagesByCountry("JP");
        assertTrue(list.contains(Locale.JAPANESE));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertTrue(list.contains(Locale.US));
        assertTrue(list.contains(Locale.UK));
        
        list = LocaleUtils.countriesByLanguage("fr");
        assertTrue(list.contains(Locale.CANADA_FRENCH));
        assertTrue(list.contains(Locale.FRANCE));
    }
}