package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    public void testToLocaleWithInvalidFormat() {
        LocaleUtils.toLocale("en_G");
    }
    
    @Test
    public void testToLocaleWithNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = new ArrayList<>(Arrays.asList(Locale.getDefault(), new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr")));
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupListWithNull() {
        List<Locale> expected = new ArrayList<>(Collections.singletonList(Locale.getDefault()));
        List<Locale> actual1 = LocaleUtils.localeLookupList(null);
        List<Locale> actual2 = LocaleUtils.localeLookupList(null, Locale.getDefault());
        assertEquals(expected, actual1);
        assertEquals(expected, actual2);
    }
    
    @Test
    public void testAvailableLocaleList() {
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<>(Arrays.asList(new Locale("en", "US"), new Locale("es", "US"), new Locale("fr", "US")));
        List<Locale> actual = LocaleUtils.languagesByCountry("US");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLanguagesByCountryWithNull() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<>(Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA")));
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCountriesByLanguageWithNull() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        assertEquals(expected, actual);
    }
}