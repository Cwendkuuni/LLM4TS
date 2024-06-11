package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Collections;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertNull(LocaleUtils.toLocale(null));

        Locale locale = LocaleUtils.toLocale("");
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
        assertEquals("", locale.getVariant());

        locale = LocaleUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
        assertEquals("", locale.getVariant());

        locale = LocaleUtils.toLocale("en_US");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("", locale.getVariant());

        locale = LocaleUtils.toLocale("en_US_POSIX");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("POSIX", locale.getVariant());

        boolean exceptionThrown = false;
        try {
            LocaleUtils.toLocale("en_");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = LocaleUtils.toLocale("en_US");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        assertEquals(4, lookupList.size());
        assertEquals(locale, lookupList.get(0));
        assertEquals(new Locale("en"), lookupList.get(1));
        assertEquals(new Locale("en", "US"), lookupList.get(2));
        assertEquals(new Locale("", ""), lookupList.get(3));
        
        lookupList = LocaleUtils.localeLookupList(locale, new Locale("es", "MX"));
        assertEquals(4, lookupList.size());
        assertEquals(locale, lookupList.get(0));
        assertEquals(new Locale("en"), lookupList.get(1));
        assertEquals(new Locale("en", "US"), lookupList.get(2));
        assertEquals(new Locale("es", "MX"), lookupList.get(3));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
        assertEquals(Arrays.asList(Locale.getAvailableLocales()), locales);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertNotNull(locales);
        assertEquals(new HashSet<>(Arrays.asList(Locale.getAvailableLocales())), locales);
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertNotNull(languages);
        assertEquals(Collections.singletonList(Locale.US), languages);
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countries);
        assertEquals(Collections.singletonList(Locale.US), countries);
    }
}