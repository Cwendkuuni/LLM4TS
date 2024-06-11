import static org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_WithValidInput_ReturnsLocaleObject() {
        // Arrange
        String str = "en_GB";
        
        // Act
        Locale result = LocaleUtils.toLocale(str);
        
        // Assert
        assertNotNull(result);
        assertEquals("en", result.getLanguage());
        assertEquals("GB", result.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_WithInvalidInput_ThrowsException() {
        // Arrange
        String str = "invalid_locale";
        
        // Act
        LocaleUtils.toLocale(str);
        
        // Assert
        // Exception is thrown
    }
    
    @Test
    public void testLocaleLookupList_WithLocale_ReturnsListOfLocales() {
        // Arrange
        Locale locale = new Locale("fr", "CA");
        
        // Act
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        
        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
    }
    
    @Test
    public void testLocaleLookupList_WithLocaleAndDefaultLocale_ReturnsListOfLocalesIncludingDefaultLocale() {
        // Arrange
        Locale locale = new Locale("fr", "CA");
        Locale defaultLocale = Locale.ENGLISH;
        
        // Act
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        // Assert
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(defaultLocale, result.get(3));
    }
    
    @Test
    public void testAvailableLocaleList_ReturnsListOfAvailableLocales() {
        // Act
        List<Locale> result = LocaleUtils.availableLocaleList();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnsSetOfAvailableLocales() {
        // Act
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }
    
    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ReturnsTrue() {
        // Arrange
        Locale locale = Locale.ENGLISH;
        
        // Act
        boolean result = LocaleUtils.isAvailableLocale(locale);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsAvailableLocale_WithUnavailableLocale_ReturnsFalse() {
        // Arrange
        Locale locale = new Locale("invalid", "locale");
        
        // Act
        boolean result = LocaleUtils.isAvailableLocale(locale);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testLanguagesByCountry_WithValidCountryCode_ReturnsListOfLanguages() {
        // Arrange
        String countryCode = "CA";
        
        // Act
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }
    
    @Test
    public void testLanguagesByCountry_WithInvalidCountryCode_ReturnsEmptyList() {
        // Arrange
        String countryCode = "invalid";
        
        // Act
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_WithValidLanguageCode_ReturnsListOfCountries() {
        // Arrange
        String languageCode = "en";
        
        // Act
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }
    
    @Test
    public void testCountriesByLanguage_WithInvalidLanguageCode_ReturnsEmptyList() {
        // Arrange
        String languageCode = "invalid";
        
        // Act
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}