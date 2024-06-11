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
        assertEquals(new Locale("en", "GB", "XXX"), LocaleUtils.toLocale("en_GB_XXX"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_Xxx");
    }
    
    @Test
    public void testToLocaleNullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testLocaleLookupList() {
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertEquals(3, result.size());
        assertEquals(new Locale("fr", "CA", "xxx"), result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
    }
    
    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertEquals(4, result.size());
        assertEquals(new Locale("fr", "CA", "xxx"), result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(new Locale("en"), result.get(3));
    }
    
    @Test
    public void testLocaleLookupListNullInput() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLocaleLookupListWithDefaultLocaleNullInput() {
        List<Locale> result = LocaleUtils.localeLookupList(null, new Locale("en"));
        assertEquals(1, result.size());
        assertEquals(new Locale("en"), result.get(0));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertTrue(result.contains(Locale.US));
        assertTrue(result.contains(Locale.UK));
        assertTrue(result.contains(Locale.FRANCE));
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertTrue(result.contains(Locale.US));
        assertTrue(result.contains(Locale.UK));
        assertTrue(result.contains(Locale.FRANCE));
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertTrue(result.contains(Locale.ENGLISH));
        assertTrue(result.contains(Locale.FRENCH));
        assertFalse(result.contains(new Locale("xx")));
    }
    
    @Test
    public void testLanguagesByCountryNullInput() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertTrue(result.contains(Locale.US));
        assertTrue(result.contains(Locale.UK));
        assertFalse(result.contains(new Locale("xx")));
    }
    
    @Test
    public void testCountriesByLanguageNullInput() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }
}