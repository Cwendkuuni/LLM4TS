package org.apache.commons.lang3;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("#en"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("e"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_e"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en#"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US_"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_US"));
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("EN"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US_"));
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en__"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_US_"));
        assertEquals(new Locale("", "US"), LocaleUtils.toLocale("_US"));
        assertEquals(new Locale("", "US"), LocaleUtils.toLocale("_US_"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("__US"));
        assertEquals(new Locale("", "US", "variant"), LocaleUtils.toLocale("_US_variant"));
        assertEquals(new Locale("", "US", "variant"), LocaleUtils.toLocale("_US_variant_"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("___variant"));
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        assertEquals(Arrays.asList(locale), LocaleUtils.localeLookupList(locale));
        assertEquals(Arrays.asList(locale, new Locale("en")), LocaleUtils.localeLookupList(locale, new Locale("en")));
        assertEquals(Collections.singletonList(locale), LocaleUtils.localeLookupList(locale, locale));
        locale = new Locale("en");
        assertEquals(Arrays.asList(locale), LocaleUtils.localeLookupList(locale));
        assertEquals(Arrays.asList(locale, new Locale("")), LocaleUtils.localeLookupList(locale, new Locale("")));
        assertEquals(Collections.singletonList(locale), LocaleUtils.localeLookupList(locale, locale));
        locale = new Locale("");
        assertEquals(Arrays.asList(locale), LocaleUtils.localeLookupList(locale));
        assertEquals(Arrays.asList(locale, new Locale("en")), LocaleUtils.localeLookupList(locale, new Locale("en")));
        assertEquals(Collections.singletonList(locale), LocaleUtils.localeLookupList(locale, locale));
        assertNull(LocaleUtils.localeLookupList(null));
        assertNull(LocaleUtils.localeLookupList(null, null));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actualList = LocaleUtils.availableLocaleList();
        assertEquals(expectedList.size(), actualList.size());
        assertTrue(actualList.containsAll(expectedList));
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> actualSet = LocaleUtils.availableLocaleSet();
        assertEquals(expectedSet.size(), actualSet.size());
        assertTrue(actualSet.containsAll(expectedSet));
    }
    
    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
        locale = new Locale("xx", "YY");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }
    
    @Test
    public void testLanguagesByCountry() {
        String countryCode = "US";
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales())
                .stream()
                .filter(l -> countryCode.equals(l.getCountry()) && l.getVariant().isEmpty())
                .collect(Collectors.toList());
        List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedList.size(), actualList.size());
        assertTrue(actualList.containsAll(expectedList));
        
        expectedList = Collections.emptyList();
        actualList = LocaleUtils.languagesByCountry(null);
        assertEquals(expectedList.size(), actualList.size());
        assertTrue(actualList.containsAll(expectedList));
    }
    
    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales())
                .stream()
                .filter(l -> languageCode.equals(l.getLanguage()) && l.getCountry().length() != 0 && l.getVariant().isEmpty())
                .collect(Collectors.toList());
        List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedList.size(), actualList.size());
        assertTrue(actualList.containsAll(expectedList));
        
        expectedList = Collections.emptyList();
        actualList = LocaleUtils.countriesByLanguage(null);
        assertEquals(expectedList.size(), actualList.size());
        assertTrue(actualList.containsAll(expectedList));
    }
}