package org.apache.commons.lang;

import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {
  
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidFormat() {
        LocaleUtils.toLocale("invalid_locale_format");
    }
    
    @Test
    public void testLocaleLookupList() {
        List<Locale> list1 = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            Locale.ENGLISH
        );
        assertEquals(list1, LocaleUtils.localeLookupList(new Locale("fr","CA","xxx")));
        
        List<Locale> list2 = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            Locale.ENGLISH
        );
        assertEquals(list2, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), Locale.ENGLISH));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertNotNull(locales);
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "YY")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("CA");
        assertNotNull(langs);
    }
    
    @Test
    public void testLanguagesByCountryWithInvalidCountryCode() {
        List<Locale> langs = LocaleUtils.languagesByCountry("XX");
        assertNotNull(langs);
        assertEquals(0, langs.size());
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(countries);
    }
    
    @Test
    public void testCountriesByLanguageWithInvalidLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("XX");
        assertNotNull(countries);
        assertEquals(0, countries.size());
    }
}