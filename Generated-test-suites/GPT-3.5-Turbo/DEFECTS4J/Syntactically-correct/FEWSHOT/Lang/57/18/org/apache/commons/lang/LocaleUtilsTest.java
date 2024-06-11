package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("fr", "CA", "xxx"), LocaleUtils.toLocale("fr_CA_xxx"));
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("invalid");
    }
    
    @Test
    public void testLocaleLookupList_withLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(3, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
    }
    
    @Test
    public void testLocaleLookupList_withLocaleAndDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.US;
        List<Locale> localeList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
        assertEquals(defaultLocale, localeList.get(3));
    }
    
    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
        assertEquals(0, localeList.size());
    }
    
    @Test
    public void testLocaleLookupList_NullLocaleAndDefaultLocale() {
        Locale defaultLocale = Locale.US;
        List<Locale> localeList = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(1, localeList.size());
        assertEquals(defaultLocale, localeList.get(0));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertFalse(localeList.isEmpty());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertFalse(localeSet.isEmpty());
    }
    
    @Test
    public void testIsAvailableLocale_Exist() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testIsAvailableLocale_NotExist() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }
    
    @Test
    public void testLanguagesByCountry_Exist() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertFalse(languages.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_NotExist() {
        List<Locale> languages = LocaleUtils.languagesByCountry("XX");
        assertTrue(languages.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_Exist() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertFalse(countries.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_NotExist() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("xx");
        assertTrue(countries.isEmpty());
    }
}