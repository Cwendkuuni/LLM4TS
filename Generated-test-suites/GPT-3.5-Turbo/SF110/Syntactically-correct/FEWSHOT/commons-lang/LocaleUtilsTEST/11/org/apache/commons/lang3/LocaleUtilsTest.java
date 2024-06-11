package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en_"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", "US", "NY"), LocaleUtils.toLocale("en_US_NY"));
        assertEquals(new Locale("", "US"), LocaleUtils.toLocale("_US"));
        assertNull(LocaleUtils.toLocale("#"));
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US", "NY");
        
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(locale);
        expectedList.add(new Locale("en", "US"));
        expectedList.add(new Locale("en", ""));
        expectedList.add(new Locale("", ""));
        
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale));
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, new Locale("", "")));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = new ArrayList<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expectedList, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expectedSet, LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("en", "US"));
        expectedList.add(new Locale("en", "UK"));
        
        assertEquals(expectedList, LocaleUtils.languagesByCountry("US"));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("en", "US"));
        expectedList.add(new Locale("es", "US"));
        
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("en"));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}