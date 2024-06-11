package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import org.apache.commons.lang3.LocaleUtils;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case
        Locale validLocale = LocaleUtils.toLocale("en_US");
        assertEquals("en", validLocale.getLanguage());
        assertEquals("US", validLocale.getCountry());
        
        // Edge cases
        Locale emptyLocale = LocaleUtils.toLocale("");
        assertNull(emptyLocale);
        
        // Error scenario
        try {
            LocaleUtils.toLocale("invalid_locale_format");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case
        Locale locale = new Locale("en", "US");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        assertEquals(4, lookupList.size());
        
        // Edge cases
        List<Locale> emptyLookupList = LocaleUtils.localeLookupList(null);
        assertEquals(0, emptyLookupList.size());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocales);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale validLocale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(validLocale));
        
        Locale invalidLocale = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertTrue(languages.size() > 0);
        
        // Edge case
        List<Locale> emptyLanguages = LocaleUtils.languagesByCountry(null);
        assertEquals(0, emptyLanguages.size());
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertTrue(countries.size() > 0);
        
        // Edge case
        List<Locale> emptyCountries = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, emptyCountries.size());
    }
}