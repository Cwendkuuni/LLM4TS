package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertEquals(new Locale("en", "" , "xxx"), LocaleUtils.toLocale("en__xxx"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("he", ""), LocaleUtils.toLocale("he"));
        assertEquals(new Locale("he", "IL"), LocaleUtils.toLocale("he_IL"));
        assertEquals(new Locale("he", "IL", "xxx"), LocaleUtils.toLocale("he_IL_xxx"));
        assertEquals(new Locale("he", "", "xxx"), LocaleUtils.toLocale("he__xxx"));
        assertEquals(new Locale("zh", ""), LocaleUtils.toLocale("zh"));
        assertEquals(new Locale("zh", "CN"), LocaleUtils.toLocale("zh_CN"));
        assertEquals(new Locale("zh", "CN", "xxx"), LocaleUtils.toLocale("zh_CN_xxx"));
        assertEquals(new Locale("zh", "", "xxx"), LocaleUtils.toLocale("zh__xxx"));
        assertEquals(new Locale("ja", ""), LocaleUtils.toLocale("ja"));
        assertEquals(new Locale("ja", "JP"), LocaleUtils.toLocale("ja_JP"));
        assertEquals(new Locale("ja", "JP", "xxx"), LocaleUtils.toLocale("ja_JP_xxx"));
        assertEquals(new Locale("ja", "", "xxx"), LocaleUtils.toLocale("ja__xxx"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidShortString() {
        LocaleUtils.toLocale("e");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("en_GB_");
    }
    
    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = List.of(new Locale("fr","CA","xxx"), new Locale("fr","CA"), new Locale("fr"), new Locale("en"));
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
        assertTrue(locales.size() > 0);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertNotNull(locales);
        assertTrue(locales.size() > 0);
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CANADA_FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMANY));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("CA");
        assertNotNull(langs);
        assertTrue(langs.size() > 0);
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(countries);
        assertTrue(countries.size() > 0);
    }
}