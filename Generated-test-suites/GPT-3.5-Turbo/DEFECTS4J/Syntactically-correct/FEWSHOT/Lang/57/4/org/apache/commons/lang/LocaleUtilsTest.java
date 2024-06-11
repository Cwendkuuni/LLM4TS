
package org.apache.commons.lang;

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

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // Invalid Formats
        assertNull(LocaleUtils.toLocale(null));
        try {
            LocaleUtils.toLocale("e");
            assertTrue(false); // Should not reach this
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            LocaleUtils.toLocale("EN_GB");
            assertTrue(false); // Should not reach this
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = new ArrayList<>(Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        ));
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx")));
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
        
        expected = new ArrayList<>(Arrays.asList(
                new Locale("en")
        ));
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("en")));
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("en"), new Locale("en")));
        
        expected = new ArrayList<>();
        assertEquals(expected, LocaleUtils.localeLookupList(null));
        assertEquals(expected, LocaleUtils.localeLookupList(null, new Locale("en")));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xxx")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("fr")
        );
        assertEquals(expected, LocaleUtils.languagesByCountry("CA"));
        
        expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.languagesByCountry("XXX"));
        assertEquals(expected, LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA")
        );
        assertEquals(expected, LocaleUtils.countriesByLanguage("fr"));
        
        expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.countriesByLanguage("XXX"));
        assertEquals(expected, LocaleUtils.countriesByLanguage(null));
    }
}
