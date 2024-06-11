package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class LocaleUtilsTest {
    
    @Before
    public void setUp() {
        // Reset the cLanguagesByCountry and cCountriesByLanguage maps before each test
        LocaleUtils.cLanguagesByCountry.clear();
        LocaleUtils.cCountriesByLanguage.clear();
    }

    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_POSIX"));
        assertEquals(Locale.CHINESE, LocaleUtils.toLocale("zh_CN"));
        assertEquals(Locale.CHINESE, LocaleUtils.toLocale("zh_CH"));
        assertEquals(new Locale("en", "", ""), LocaleUtils.toLocale("en__"));
        assertEquals(new Locale("", "US", ""), LocaleUtils.toLocale("_US"));
        assertEquals(new Locale("", "", "POSIX"), LocaleUtils.toLocale("__POSIX"));

        assertNull(LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        try {
            LocaleUtils.toLocale("invalid#locale");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: invalid#locale", e.getMessage());
        }
        try {
            LocaleUtils.toLocale("x");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: x", e.getMessage());
        }
        try {
            LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_", e.getMessage());
        }
        try {
            LocaleUtils.toLocale("_US_");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: _US_", e.getMessage());
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US", "POSIX");
        Locale defaultLocale = new Locale("fr", "FR");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("en", "US"), new Locale("en", ""), defaultLocale);
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));

        locale = new Locale("fr");
        defaultLocale = new Locale("fr", "FR");
        expectedList = Arrays.asList(locale, new Locale("fr", ""), new Locale("fr", "FR"));
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));

        locale = Locale.FRENCH;
        defaultLocale = new Locale("fr", "FR");
        expectedList = Arrays.asList(locale, new Locale("fr", "FR"));
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));

        assertNull(LocaleUtils.localeLookupList(null));
        assertNull(LocaleUtils.localeLookupList(null, null));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, 
                                                 Locale.JAPANESE, Locale.KOREAN, Locale.CHINESE, Locale.SIMPLIFIED_CHINESE, 
                                                 Locale.TRADITIONAL_CHINESE);
        assertEquals(expectedList, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, 
                                                    Locale.JAPANESE, Locale.KOREAN, Locale.CHINESE, Locale.SIMPLIFIED_CHINESE, 
                                                    Locale.TRADITIONAL_CHINESE));
        assertEquals(expectedSet, LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CHINESE));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("de", "DE")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        // Test with "US" country code
        String countryCode = "US";
        List<Locale> expectedList = Arrays.asList(Locale.US);
        assertEquals(expectedList, LocaleUtils.languagesByCountry(countryCode));
        
        // Test with "CN" country code
        countryCode = "CN";
        expectedList = Arrays.asList(Locale.CHINESE, Locale.SIMPLIFIED_CHINESE);
        assertEquals(expectedList, LocaleUtils.languagesByCountry(countryCode));
        
        // Test with unknown country code
        countryCode = "ZZ";
        expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.languagesByCountry(countryCode));
        
        // Test with null country code
        countryCode = null;
        expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.languagesByCountry(countryCode));
    }
    
    @Test
    public void testCountriesByLanguage() {
        // Test with "en" language code
        String languageCode = "en";
        List<Locale> expectedList = Arrays.asList(Locale.US, Locale.CANADA, Locale.UK);
        assertEquals(expectedList, LocaleUtils.countriesByLanguage(languageCode));
        
        // Test with "zh" language code
        languageCode = "zh";
        expectedList = Arrays.asList(Locale.CHINA);
        assertEquals(expectedList, LocaleUtils.countriesByLanguage(languageCode));
        
        // Test with unknown language code
        languageCode = "xx";
        expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.countriesByLanguage(languageCode));
        
        // Test with null language code
        languageCode = null;
        expectedList = Collections.emptyList();
        assertEquals(expectedList, LocaleUtils.countriesByLanguage(languageCode));
    }
}