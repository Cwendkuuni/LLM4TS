

package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
        assertEquals(new Locale("fr", "CA", "yyy"), LocaleUtils.toLocale("fr_CA_yyy"));
        assertEquals(new Locale("es", ""), LocaleUtils.toLocale("es"));
        assertEquals(new Locale("es", "MX"), LocaleUtils.toLocale("es_MX"));
        assertEquals(new Locale("zh", "CN"), LocaleUtils.toLocale("zh_CN"));
        assertEquals(new Locale("zh", "TW"), LocaleUtils.toLocale("zh_TW"));
        assertEquals(new Locale("ko", ""), LocaleUtils.toLocale("ko"));
        assertEquals(new Locale("ko", "KR"), LocaleUtils.toLocale("ko_KR"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidFormat() {
        LocaleUtils.toLocale("randomStringFormat");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = new ArrayList<>(Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr")));
        
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = new ArrayList<>(Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale));
        
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actual = LocaleUtils.availableLocaleList();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertTrue(LocaleUtils.availableLocaleSet().containsAll(localeList));
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actual = LocaleUtils.languagesByCountry("US");
        
        assertTrue(expected.containsAll(actual));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        
        assertTrue(expected.containsAll(actual));
    }
}




