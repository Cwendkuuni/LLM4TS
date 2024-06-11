package org.apache.commons.lang;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        Assert.assertEquals("en", locale1.getLanguage());
        Assert.assertEquals("", locale1.getCountry());
        
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals("en", locale2.getLanguage());
        Assert.assertEquals("GB", locale2.getCountry());
        
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals("en", locale3.getLanguage());
        Assert.assertEquals("GB", locale3.getCountry());
        Assert.assertEquals("xxx", locale3.getVariant());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidFormat() {
        LocaleUtils.toLocale("invalid");
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.ENGLISH;
        
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(locale, list.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list.get(1));
        Assert.assertEquals(new Locale("fr"), list.get(2));
        
        List<Locale> listWithDefault = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(4, listWithDefault.size());
        Assert.assertEquals(locale, listWithDefault.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), listWithDefault.get(1));
        Assert.assertEquals(new Locale("fr"), listWithDefault.get(2));
        Assert.assertEquals(defaultLocale, listWithDefault.get(3));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(list);
        Assert.assertFalse(list.isEmpty());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(set);
        Assert.assertFalse(set.isEmpty());
    }
    
    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale1));
        
        Locale locale2 = new Locale("xx");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("CA");
        Assert.assertNotNull(langs);
        Assert.assertFalse(langs.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        Assert.assertNotNull(countries);
        Assert.assertFalse(countries.isEmpty());
    }
}