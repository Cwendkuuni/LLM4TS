package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class LocaleUtilsTest {

    private static List<Locale> availableLocaleList;
    private static List<String> availableLanguages;
    private static List<String> availableCountries;
    
    @BeforeClass
    public static void setUp() {
        availableLocaleList = LocaleUtils.availableLocaleList();
        
        availableLanguages = new ArrayList<String>();
        availableCountries = new ArrayList<String>();
        
        for (Locale locale : availableLocaleList) {
            if (!availableLanguages.contains(locale.getLanguage())) {
                availableLanguages.add(locale.getLanguage());
            }
            if (!availableCountries.contains(locale.getCountry())) {
                availableCountries.add(locale.getCountry());
            }
        }
    }
    
    @Test
    public void testToLocale_ValidInput() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals("en", locale1.getLanguage());
        assertEquals("", locale1.getCountry());
        assertEquals("", locale1.getVariant());
        
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale2.getLanguage());
        assertEquals("GB", locale2.getCountry());
        assertEquals("", locale2.getVariant());
        
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale3.getLanguage());
        assertEquals("GB", locale3.getCountry());
        assertEquals("xxx", locale3.getVariant());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("abc");
    }
    
    @Test
    public void testLocaleLookupList_SingleLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
        
        assertEquals(3, localeLookupList.size());
        assertTrue(localeLookupList.contains(locale));
        assertTrue(localeLookupList.contains(new Locale("fr", "CA")));
        assertTrue(localeLookupList.contains(new Locale("fr")));
    }
    
    @Test
    public void testLocaleLookupList_LocaleAndDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(4, localeLookupList.size());
        assertTrue(localeLookupList.contains(locale));
        assertTrue(localeLookupList.contains(new Locale("fr", "CA")));
        assertTrue(localeLookupList.contains(new Locale("fr")));
        assertTrue(localeLookupList.contains(defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        
        assertEquals(availableLocaleList.size(), availableLocales.size());
        
        for (Locale locale : availableLocales) {
            assertTrue(availableLocaleList.contains(locale));
        }
    }
    
    @Test
    public void testAvailableLocaleSet() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(availableLocales.size(), LocaleUtils.availableLocaleSet().size());
    }
    
    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testIsAvailableLocale_UnavailableLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("US");
        
        for (Locale language : languagesByCountry) {
            assertEquals("US", language.getCountry());
        }
    }
    
    @Test
    public void testLanguagesByCountry_InvalidCountryCode() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("XX");
        assertEquals(0, languagesByCountry.size());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        
        for (Locale country : countriesByLanguage) {
            assertEquals("en", country.getLanguage());
        }
    }
    
    @Test
    public void testCountriesByLanguage_InvalidLanguageCode() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("xx");
        assertEquals(0, countriesByLanguage.size());
    }
}