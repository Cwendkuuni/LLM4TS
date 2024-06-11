
package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertNull(LocaleUtils.toLocale(null));
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("fr", "CA", "xxx"), LocaleUtils.toLocale("fr_CA_xxx"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_");
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), Locale.getDefault());
        List<Locale> resultList = LocaleUtils.localeLookupList(locale);
        
        assertEquals(expectedList, resultList);
    }
    
    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        List<Locale> resultList = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(expectedList, resultList);
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        
        assertTrue(availableLocales.contains(new Locale("en")));
        assertTrue(availableLocales.contains(new Locale("fr")));
        assertTrue(availableLocales.contains(new Locale("es")));
    }
    
    @Test
    public void testAvailableLocaleSet() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        
        assertEquals(availableLocales.size(), availableLocaleSet.size());
        for (Locale locale : availableLocales) {
            assertTrue(availableLocaleSet.contains(locale));
        }
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("es")));
        assertTrue(!LocaleUtils.isAvailableLocale(new Locale("de")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = Arrays.asList(new Locale("en_CA"), new Locale("fr_CA"));
        List<Locale> resultList = LocaleUtils.languagesByCountry("CA");
        
        assertEquals(expectedList, resultList);
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = Arrays.asList(new Locale("en_US"), new Locale("en_GB"), new Locale("fr_CA"), new Locale("fr_CH"));
        List<Locale> resultList = LocaleUtils.countriesByLanguage("fr");
        
        assertEquals(expectedList, resultList);
    }
}
