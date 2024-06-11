import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        // Test with valid combinations
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", "", "variant"), LocaleUtils.toLocale("en__variant"));
        assertEquals(new Locale("", "US"), LocaleUtils.toLocale("_US"));
        assertEquals(new Locale("", "", "variant"), LocaleUtils.toLocale("__variant"));
        
        // Test with invalid combinations
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("en#US"));
        assertNull(LocaleUtils.toLocale("_"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_US_"));
        assertNull(LocaleUtils.toLocale("en_US_variant_"));
    }
    
    @Test
    public void testLocaleLookupList() {
        // Test with valid locales
        Locale locale = new Locale("en");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale("en", "US")));
        assertTrue(result.contains(new Locale("en", "")));
        
        locale = new Locale("en", "US");
        result = LocaleUtils.localeLookupList(locale);
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale("en", "")));
        
        // Test with null locale
        result = LocaleUtils.localeLookupList(null);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(Locale.getAvailableLocales().length, result.size());
        assertTrue(result.contains(new Locale("en")));
        assertTrue(result.contains(new Locale("en", "US")));
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertEquals(Locale.getAvailableLocales().length, result.size());
        assertTrue(result.contains(new Locale("en")));
        assertTrue(result.contains(new Locale("en", "US")));
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        // Test with valid country code
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertTrue(result.contains(new Locale("en", "US")));
        assertTrue(result.contains(new Locale("es", "US")));
        
        // Test with invalid country code
        result = LocaleUtils.languagesByCountry(null);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testCountriesByLanguage() {
        // Test with valid language code
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertTrue(result.contains(new Locale("en", "US")));
        assertTrue(result.contains(new Locale("en", "GB")));
        
        // Test with invalid language code
        result = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, result.size());
    }
}