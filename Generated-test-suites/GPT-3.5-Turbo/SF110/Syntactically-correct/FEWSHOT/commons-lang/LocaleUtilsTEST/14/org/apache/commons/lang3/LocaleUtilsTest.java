package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(null, LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", "US", "New York"), LocaleUtils.toLocale("en_US_New York"));
        assertEquals(new Locale("", "US"), LocaleUtils.toLocale("_US"));
        assertEquals(new Locale("", "US", "New York"), LocaleUtils.toLocale("_US_New York"));
		assertEquals(new Locale("", "", "New York"), LocaleUtils.toLocale("__New York"));
        assertEquals(new Locale("", "US", ""), LocaleUtils.toLocale("_US_"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("en#US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat2() {
        LocaleUtils.toLocale("e");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat3() {
        LocaleUtils.toLocale("__");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat4() {
        LocaleUtils.toLocale("_US__");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat5() {
        LocaleUtils.toLocale("__New York");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat6() {
        LocaleUtils.toLocale("__US_New York");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("fr");
        
        List<Locale> expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("en"));
        expected.add(new Locale("", "US"));
        expected.add(defaultLocale);
        
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = SyncAvoid.AVAILABLE_LOCALE_LIST;
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = SyncAvoid.AVAILABLE_LOCALE_SET;
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertEquals(true, LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testLanguagesByCountry() {
        String countryCode = "US";
        
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        
        assertEquals(expected, LocaleUtils.languagesByCountry(countryCode));
    }
    
    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        
        assertEquals(expected, LocaleUtils.countriesByLanguage(languageCode));
    }
}