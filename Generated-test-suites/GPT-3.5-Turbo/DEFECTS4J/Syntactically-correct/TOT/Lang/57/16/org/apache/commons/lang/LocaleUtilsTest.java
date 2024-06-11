import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnsNull() {
        Locale result = LocaleUtils.toLocale(null);
        assertEquals(null, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("INVALID_FORMAT");
    }
    
    @Test
    public void testToLocale_ValidFormats_ReturnsLocaleObject() {
        Locale result1 = LocaleUtils.toLocale("en");
        assertEquals("en", result1.getLanguage());
        assertEquals("", result1.getCountry());
        
        Locale result2 = LocaleUtils.toLocale("en_GB");
        assertEquals("en", result2.getLanguage());
        assertEquals("GB", result2.getCountry());
        
        Locale result3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", result3.getLanguage());
        assertEquals("GB", result3.getCountry());
        assertEquals("xxx", result3.getVariant());
    }
    
    @Test
    public void testLocaleLookupList_LocaleIsNull_ReturnsEmptyList() {
        List result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLocaleLookupList_WithLocale_ReturnsListOfLocales() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        
        List result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(4, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(defaultLocale, result.get(3));
    }
    
    @Test
    public void testAvailableLocaleList_ReturnsListOfAvailableLocales() {
        List result = LocaleUtils.availableLocaleList();
        assertFalse(result.isEmpty());
        // Test some expected locales
        assertTrue(result.contains(Locale.US));
        assertTrue(result.contains(Locale.GERMANY));
        assertTrue(result.contains(Locale.CHINA));
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnsSetOfAvailableLocales() {
        List result = LocaleUtils.availableLocaleSet();
        assertFalse(result.isEmpty());
        // Test some expected locales
        assertTrue(result.contains(Locale.US));
        assertTrue(result.contains(Locale.GERMANY));
        assertTrue(result.contains(Locale.CHINA));
    }
    
    @Test
    public void testIsAvailableLocale_KnownLocale_ReturnsTrue() {
        Locale locale = Locale.US;
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertTrue(result);
    }
    
    @Test
    public void testIsAvailableLocale_UnknownLocale_ReturnsFalse() {
        Locale locale = new Locale("xyz");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertFalse(result);
    }
    
    @Test
    public void testLanguagesByCountry_NullInput_ReturnsEmptyList() {
        List result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_KnownCountryCode_ReturnsListOfLanguages() {
        List result = LocaleUtils.languagesByCountry("US");
        assertFalse(result.isEmpty());
        assertTrue(result.contains(Locale.ENGLISH));
        assertTrue(result.contains(Locale.SPANISH));
    }
    
    @Test
    public void testLanguagesByCountry_UnknownCountryCode_ReturnsEmptyList() {
        List result = LocaleUtils.languagesByCountry("XYZ");
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_NullInput_ReturnsEmptyList() {
        List result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_KnownLanguageCode_ReturnsListOfCountries() {
        List result = LocaleUtils.countriesByLanguage("en");
        assertFalse(result.isEmpty());
        assertTrue(result.contains(Locale.US));
        assertTrue(result.contains(Locale.CANADA));
    }
    
    @Test
    public void testCountriesByLanguage_UnknownLanguageCode_ReturnsEmptyList() {
        List result = LocaleUtils.countriesByLanguage("xyz");
        assertTrue(result.isEmpty());
    }
}