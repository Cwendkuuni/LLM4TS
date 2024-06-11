package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertNotNull(LocaleUtils.toLocale("en_GB_"));
        assertNull(LocaleUtils.toLocale(""));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testToLocaleWithInvalidFormat() {
        LocaleUtils.toLocale("english");
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.ENGLISH;
        List<Locale> localeList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
        assertEquals(defaultLocale, localeList.get(3));
    }
    
    @Test
    public void testLocaleLookupListWithNullLocale() {
        Locale defaultLocale = Locale.ENGLISH;
        List<Locale> localeList = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(1, localeList.size());
        assertEquals(defaultLocale, localeList.get(0));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertNotNull(localeList);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        assertNotNull(LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }
    
    @Test
    public void testIsAvailableLocaleWithInvalidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("de", "CH")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assertNotNull(languages);
        assertTrue(languages.contains(new Locale("en", "CA")));
        assertTrue(languages.contains(new Locale("fr", "CA")));
    }
    
    @Test
    public void testLanguagesByCountryWithInvalidCountryCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry("ZZ");
        assertNotNull(languages);
        assertTrue(languages.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(countries);
        assertTrue(countries.contains(new Locale("fr", "CA")));
        assertTrue(countries.contains(new Locale("fr", "CH")));
        assertTrue(countries.contains(new Locale("fr", "FR")));
    }
    
    @Test
    public void testCountriesByLanguageWithInvalidLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("zz");
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }
}