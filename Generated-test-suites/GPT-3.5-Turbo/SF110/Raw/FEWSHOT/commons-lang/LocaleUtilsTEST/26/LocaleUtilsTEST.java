package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        //Test with null string
        assertNull(LocaleUtils.toLocale(null));
       
        //Test with empty string
        assertNotNull(LocaleUtils.toLocale(""));
       
        //Test with invalid locale format - contains #
        try {
            LocaleUtils.toLocale("en#US");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid locale format: en#US", ex.getMessage());
        }
       
        //Test with invalid locale format - length less than 2
        try {
            LocaleUtils.toLocale("a");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid locale format: a", ex.getMessage());
        }
       
        //Test with invalid locale format - first character not _
        try {
            LocaleUtils.toLocale("en");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid locale format: en", ex.getMessage());
        }
       
        //Test with invalid locale format - first character is _ and length less than 3
        try {
            LocaleUtils.toLocale("_a");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid locale format: _a", ex.getMessage());
        }
       
        //Test with invalid locale format - characters at indices 1 and 2 are not uppercase
        try {
            LocaleUtils.toLocale("_aa");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid locale format: _aa", ex.getMessage());
        }
       
        //Test with invalid locale format - characters at indices 3 is not _
        try {
            LocaleUtils.toLocale("_AAa");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid locale format: _AAa", ex.getMessage());
        }
       
        //Test with valid locale format - only language specified
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
        assertEquals("", locale.getVariant());
       
        //Test with valid locale format - language and country specified
        locale = LocaleUtils.toLocale("en_US");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("", locale.getVariant());
       
        //Test with valid locale format - language, country and variant specified
        locale = LocaleUtils.toLocale("en_US_variant");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("variant", locale.getVariant());
       
        //Test with valid locale format - language and variant specified
        locale = LocaleUtils.toLocale("en__variant");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
        assertEquals("variant", locale.getVariant());
    }
   
    @Test
    public void testLocaleLookupList() {
        //Test with null locale
        List<Locale> lookupList = LocaleUtils.localeLookupList(null);
        assertNotNull(lookupList);
        assertEquals(0, lookupList.size());
       
        lookupList = LocaleUtils.localeLookupList(new Locale("en", "US", "variant"));
        assertNotNull(lookupList);
        assertEquals(4, lookupList.size());
        assertEquals(new Locale("en", "US", "variant"), lookupList.get(0));
        assertEquals(new Locale("en", "US"), lookupList.get(1));
        assertEquals(new Locale("en", ""), lookupList.get(2));
        assertEquals(new Locale("", ""), lookupList.get(3));
    }
   
    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
        assertEquals(Arrays.asList(Locale.getAvailableLocales()), availableLocales);
    }
   
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocaleSet);
        assertEquals(new HashSet<Locale>(Arrays.asList(Locale.getAvailableLocales())), availableLocaleSet);
    }
   
    @Test
    public void testIsAvailableLocale() {
        //Test with null locale
        assertFalse(LocaleUtils.isAvailableLocale(null));
       
        assertTrue(LocaleUtils.isAvailableLocale(Locale.getDefault()));
    }
   
    @Test
    public void testLanguagesByCountry() {
        //Test with null country code
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        assertNotNull(languages);
        assertEquals(0, languages.size());
       
        //Test with valid country code
        languages = LocaleUtils.languagesByCountry("US");
        assertNotNull(languages);
        assertEquals(Arrays.asList(Locale.getAvailableLocales()), languages);
    }
   
    @Test
    public void testCountriesByLanguage() {
        //Test with null language code
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertNotNull(countries);
        assertEquals(0, countries.size());
       
        //Test with valid language code
        countries = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countries);
        assertEquals(Arrays.asList(Locale.getAvailableLocales()), countries);
    }
}