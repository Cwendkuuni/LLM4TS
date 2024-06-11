package org.apache.commons.lang;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {
    
    // Test toLocale() method
    
    @Test
    public void testToLocaleWithLanguageOnly() {
        Locale expected = new Locale("en", "");
        Locale actual = LocaleUtils.toLocale("en");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToLocaleWithLanguageAndCountry() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToLocaleWithLanguageAndCountryAndVariant() {
        Locale expected = new Locale("en", "GB", "variant");
        Locale actual = LocaleUtils.toLocale("en_GB_variant");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToLocaleWithInvalidFormat() {
        assertNull(LocaleUtils.toLocale("invalid"));
    }
    
    // Test localeLookupList() method
    
    @Test
    public void testLocaleLookupListWithLocale() {
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLocaleLookupListWithLocaleAndDefaultLocale() {
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), Locale.getDefault());
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), Locale.getDefault());
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLocaleLookupListWithNullLocale() {
        List<Locale> expected = Arrays.asList(Locale.getDefault());
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertEquals(expected, actual);
    }
    
    // Test availableLocaleList() method
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);
    }
    
    // Test availableLocaleSet() method
    
    @Test
    public void testAvailableLocaleSet() {
        assertTrue(LocaleUtils.availableLocaleSet().containsAll(Arrays.asList(Locale.getAvailableLocales())));
    }
    
    // Test isAvailableLocale() method
    
    @Test
    public void testIsAvailableLocaleWithAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }
    
    @Test
    public void testIsAvailableLocaleWithUnavailableLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }
    
    // Test languagesByCountry() method
    
    @Test
    public void testLanguagesByCountryWithExistingCountry() {
        List<Locale> expected = Arrays.asList(new Locale("en", "GB"), new Locale("en"));
        List<Locale> actual = LocaleUtils.languagesByCountry("GB");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLanguagesByCountryWithNonExistingCountry() {
        List<Locale> expected = Arrays.asList();
        List<Locale> actual = LocaleUtils.languagesByCountry("XX");
        assertEquals(expected, actual);
    }
    
    // Test countriesByLanguage() method
    
    @Test
    public void testCountriesByLanguageWithExistingLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("en", "GB"), new Locale("fr", "CA"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCountriesByLanguageWithNonExistingLanguage() {
        List<Locale> expected = Arrays.asList();
        List<Locale> actual = LocaleUtils.countriesByLanguage("xx");
        assertEquals(expected, actual);
    }
}