
package org.apache.commons.lang3;

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
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat1() {
        LocaleUtils.toLocale("e");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat2() {
        LocaleUtils.toLocale("eng");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat3() {
        LocaleUtils.toLocale("en_G");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat4() {
        LocaleUtils.toLocale("en_GBx");
    }
    
    @Test
    public void testLocaleLookupList() {
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertEquals(3, localeList.size());
        assertEquals(new Locale("fr", "CA", "xxx"), localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
    }
    
    @Test
    public void testLocaleLookupList_DefaultLocale() {
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertEquals(4, localeList.size());
        assertEquals(new Locale("fr", "CA", "xxx"), localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
        assertEquals(new Locale("en"), localeList.get(3));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertFalse(availableLocales.isEmpty());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertFalse(availableLocales.isEmpty());
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesInCA = LocaleUtils.languagesByCountry("CA");
        assertFalse(languagesInCA.isEmpty());
        assertTrue(languagesInCA.contains(new Locale("en", "CA")));
    }
    
    @Test
    public void testLanguagesByCountry_NullInput() {
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        assertTrue(languages.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesWithEnglish = LocaleUtils.countriesByLanguage("en");
        assertFalse(countriesWithEnglish.isEmpty());
        assertTrue(countriesWithEnglish.contains(new Locale("en", "US")));
    }
    
    @Test
    public void testCountriesByLanguage_NullInput() {
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertTrue(countries.isEmpty());
    }
}
