package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", "US", "variant"), LocaleUtils.toLocale("en_US_variant"));
        assertEquals(new Locale("en", "", "variant"), LocaleUtils.toLocale("en__variant"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en#US");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testToLocaleInvalidFormat2() {
        LocaleUtils.toLocale("e_US");
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", ""));
        expected.add(new Locale("", ""));
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
        
        locale = new Locale("en");
        expected.clear();
        expected.add(locale);
        expected.add(new Locale("en", ""));
        expected.add(new Locale("", ""));
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
        
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }
    
    @Test
    public void testAvailableLocaleList() {
        synchronized (SyncAvoid.class) {
            List<Locale> expected = new ArrayList<>(Arrays.asList(Locale.getAvailableLocales()));
            Collections.sort(expected, (l1, l2) -> l1.toString().compareTo(l2.toString()));
            assertEquals(expected, LocaleUtils.availableLocaleList());
        }
    }
    
    @Test
    public void testAvailableLocaleSet() {
        synchronized (SyncAvoid.class) {
            Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
            assertEquals(expected, LocaleUtils.availableLocaleSet());
        }
    }
    
    @Test
    public void testIsAvailableLocale() {
        synchronized (SyncAvoid.class) {
            assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
            assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
        }
    }
    
    @Test
    public void testLanguagesByCountry() {
        synchronized (LocaleUtils.cLanguagesByCountry) {
            LocaleUtils.cLanguagesByCountry.clear();
            List<Locale> expected = new ArrayList<>();
            expected.add(new Locale("en", "US"));
            expected.add(new Locale("en", "GB"));
            assertEquals(expected, LocaleUtils.languagesByCountry("US"));
        }
    }
    
    @Test
    public void testCountriesByLanguage() {
        synchronized (LocaleUtils.cCountriesByLanguage) {
            LocaleUtils.cCountriesByLanguage.clear();
            List<Locale> expected = new ArrayList<>();
            expected.add(new Locale("en", "US"));
            expected.add(new Locale("fr", "CA"));
            assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
        }
    }
}