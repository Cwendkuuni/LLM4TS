package org.apache.commons.lang;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("abcd");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat2() {
        LocaleUtils.toLocale("en_");
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        assertEquals(4, LocaleUtils.localeLookupList(locale).size());
        assertEquals(4, LocaleUtils.localeLookupList(locale, defaultLocale).size());
    }
    
    @Test
    public void testAvailableLocaleList() {
        assertEquals(217, LocaleUtils.availableLocaleList().size());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        assertEquals(217, LocaleUtils.availableLocaleSet().size());
    }
    
    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "");
        assertEquals(true, LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testLanguagesByCountry() {
        assertEquals(0, LocaleUtils.languagesByCountry(null).size());
        assertEquals(2, LocaleUtils.languagesByCountry("FR").size());
    }
    
    @Test
    public void testCountriesByLanguage() {
        assertEquals(0, LocaleUtils.countriesByLanguage(null).size());
        assertEquals(81, LocaleUtils.countriesByLanguage("fr").size());
    }
}