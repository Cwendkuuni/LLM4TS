import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch(IllegalArgumentException e) {
            assertEquals("Invalid locale format: null", e.getMessage());
        }
        
        try {
            LocaleUtils.toLocale("e");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: e", e.getMessage());
        }
        
        // Testing the uppercase/lowercase exception mentioned in the source code
        Locale locale = LocaleUtils.toLocale("en_GB_XXX");
        assertTrue(locale.getVariant().equals("XXX") || locale.getVariant().equals("xxx"));
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        
        Locale defaultLocale = new Locale("en");
        result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(defaultLocale, result.get(3));
        
        result = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(1, result.size());
        assertEquals(defaultLocale, result.get(0));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertEquals(Locale.getAvailableLocales().length, locales.size());
        assertTrue(locales.contains(Locale.US));
        assertTrue(locales.contains(Locale.ENGLISH));
        assertTrue(locales.contains(Locale.FRENCH));
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertEquals(Locale.getAvailableLocales().length, localeSet.size());
        assertTrue(localeSet.contains(Locale.US));
        assertTrue(localeSet.contains(Locale.ENGLISH));
        assertTrue(localeSet.contains(Locale.FRENCH));
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("de", "DE")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> locales = LocaleUtils.languagesByCountry("US");
        assertEquals(1, locales.size());
        assertEquals(Locale.US, locales.get(0));
        
        locales = LocaleUtils.languagesByCountry("CA");
        assertEquals(2, locales.size());
        assertTrue(locales.contains(new Locale("en", "CA")));
        assertTrue(locales.contains(new Locale("fr", "CA")));
        
        locales = LocaleUtils.languagesByCountry("DE");
        assertEquals(0, locales.size());
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> locales = LocaleUtils.countriesByLanguage("en");
        assertEquals(2, locales.size());
        assertTrue(locales.contains(new Locale("en", "US")));
        assertTrue(locales.contains(new Locale("en", "CA"))); 
        
        locales = LocaleUtils.countriesByLanguage("fr");
        assertEquals(1, locales.size());
        assertTrue(locales.contains(new Locale("fr", "CA")));
        
        locales = LocaleUtils.countriesByLanguage("de");
        assertEquals(0, locales.size());
    }
}