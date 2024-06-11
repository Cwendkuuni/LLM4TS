import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {
    
    // Test case for toLocale(String) method
    
    @Test
    public void testToLocale_ValidString_ReturnsLocaleObject() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("", locale.getVariant());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidString_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("english");
    }
    
    @Test
    public void testToLocale_NullString_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    // Test case for localeLookupList(Locale) method
    
    @Test
    public void testLocaleLookupList_ValidLocale_ReturnsCorrectList() {
        Locale locale = LocaleUtils.toLocale("fr_CA_xxx");
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(locale);
        expectedList.add(LocaleUtils.toLocale("fr_CA"));
        expectedList.add(LocaleUtils.toLocale("fr"));
        
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        
        assertEquals(expectedList, result);
    }
    
    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }
    
    // Test case for availableLocaleList() method
    
    @Test
    public void testAvailableLocaleList_ReturnsListOfAvailableLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        
        // Check if the result is not null and contains some common locales
        assertNotNull(result);
        assertTrue(result.contains(Locale.ENGLISH));
        assertTrue(result.contains(Locale.FRENCH));
    }
    
    // Test case for availableLocaleSet() method
    
    @Test
    public void testAvailableLocaleSet_ReturnsSetOfAvailableLocales() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        
        // Check if the result is not null and contains some common locales
        assertNotNull(result);
        assertTrue(result.contains(Locale.ENGLISH));
        assertTrue(result.contains(Locale.FRENCH));
    }
    
    // Test case for isAvailableLocale(Locale) method
    
    @Test
    public void testIsAvailableLocale_ValidLocale_ReturnsTrue() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testIsAvailableLocale_InvalidLocale_ReturnsFalse() {
        Locale locale = LocaleUtils.toLocale("xyz");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }
    
    // Test case for languagesByCountry(String) method
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsListOfLanguages() {
        String countryCode = "GB";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        
        // Check if the result is not null and contains some common languages for the country
        assertNotNull(result);
        assertTrue(result.contains(Locale.ENGLISH));
    }
    
    @Test
    public void testLanguagesByCountry_InvalidCountryCode_ReturnsEmptyList() {
        String countryCode = "XYZ";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(result.isEmpty());
    }
    
    // Test case for countriesByLanguage(String) method
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsListOfCountries() {
        String languageCode = "fr";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        
        // Check if the result is not null and contains some common countries for the language
        assertNotNull(result);
        assertTrue(result.contains(Locale.CANADA));
    }
    
    @Test
    public void testCountriesByLanguage_InvalidLanguageCode_ReturnsEmptyList() {
        String languageCode = "xyz";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(result.isEmpty());
    }
}