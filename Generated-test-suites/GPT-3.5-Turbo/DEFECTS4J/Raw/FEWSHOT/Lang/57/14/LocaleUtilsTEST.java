package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("fr", ""), LocaleUtils.toLocale("fr"));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
        assertEquals(new Locale("fr", "CA", "xxx"), LocaleUtils.toLocale("fr_CA_xxx"));
        assertNull(LocaleUtils.toLocale(null));
        try {
            LocaleUtils.toLocale("invalid");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: invalid", e.getMessage());
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.ENGLISH;
        
        List expectedList = new ArrayList();
        expectedList.add(locale);
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr", ""));
        expectedList.add(defaultLocale);
        
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale));
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
        assertNull(LocaleUtils.localeLookupList(null));
        assertEquals(Collections.singletonList(defaultLocale), LocaleUtils.localeLookupList(null, defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List expectedList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedList, LocaleUtils.availableLocaleList());
        assertEquals(expectedList.size(), LocaleUtils.availableLocaleList().size());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set expectedSet = new HashSet();
        
        List availableLocales = LocaleUtils.availableLocaleList();
        for (Object obj : availableLocales) {
            expectedSet.add((Locale)obj);
        }
        
        assertEquals(expectedSet, LocaleUtils.availableLocaleSet());
        assertEquals(expectedSet.size(), LocaleUtils.availableLocaleSet().size());
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CANADA_FRENCH));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.CHINA));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List expectedList = new ArrayList();
        expectedList.add(new Locale("fr", "CA"));
        
        assertEquals(expectedList, LocaleUtils.languagesByCountry("CA"));
        assertEquals(Collections.singletonList(Locale.ENGLISH), LocaleUtils.languagesByCountry("US"));
        assertEquals(Collections.singletonList(Locale.ENGLISH), LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List expectedList = new ArrayList();
        expectedList.add(new Locale("fr", "CA"));
        
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("fr"));
        assertEquals(Collections.singletonList(Locale.CANADA), LocaleUtils.countriesByLanguage("en"));
        assertEquals(Collections.singletonList(Locale.CANADA), LocaleUtils.countriesByLanguage(null));
    }
}