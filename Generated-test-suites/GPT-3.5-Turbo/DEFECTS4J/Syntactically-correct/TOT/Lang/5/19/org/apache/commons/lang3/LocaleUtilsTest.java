import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnsNull() {
        // Arrange
        String input = null;
        
        // Act
        Locale result = LocaleUtils.toLocale(input);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testToLocale_ValidInput_ReturnsLocaleObject() {
        // Arrange
        String input = "en_GB";
        
        // Act
        Locale result = LocaleUtils.toLocale(input);
        
        // Assert
        assertNotNull(result);
        assertEquals("en", result.getLanguage());
        assertEquals("GB", result.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThrowsIllegalArgumentException() {
        // Arrange
        String input = "invalid";
        
        // Act
        LocaleUtils.toLocale(input);
        
        // Assert (Exception expected)
    }
    
    @Test
    public void testLocaleLookupList_ValidLocale_ReturnsListInOrder() {
        // Arrange
        Locale locale = new Locale("fr", "CA", "xxx");
        
        // Act
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        
        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr","CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
    }
    
    @Test
    public void testLocaleLookupList_ValidLocaleAndDefaultLocale_ReturnsListInOrder() {
        // Arrange
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.ENGLISH;
        
        // Act
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        // Assert
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr","CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(defaultLocale, result.get(3));
    }
    
    @Test
    public void testAvailableLocaleList_ReturnsListOfAvailableLocales() {
        // Arrange
        
        // Act
        List<Locale> result = LocaleUtils.availableLocaleList();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertTrue(result.contains(Locale.ENGLISH));
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnsSetOfAvailableLocales() {
        // Arrange
        
        // Act
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertTrue(result.contains(Locale.ENGLISH));
    }
    
    @Test
    public void testIsAvailableLocale_ValidLocale_ReturnsTrue() {
        // Arrange
        Locale locale = Locale.ENGLISH;
        
        // Act
        boolean result = LocaleUtils.isAvailableLocale(locale);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testIsAvailableLocale_InvalidLocale_ReturnsFalse() {
        // Arrange
        Locale locale = new Locale("invalid");
        
        // Act
        boolean result = LocaleUtils.isAvailableLocale(locale);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsListOfLanguages() {
        // Arrange
        String countryCode = "CA";
        
        // Act
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }
    
    @Test
    public void testLanguagesByCountry_InvalidCountryCode_ReturnsEmptyList() {
        // Arrange
        String countryCode = "invalid";
        
        // Act
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsListOfCountries() {
        // Arrange
        String languageCode = "fr";
        
        // Act
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }
    
    @Test
    public void testCountriesByLanguage_InvalidLanguageCode_ReturnsEmptyList() {
        // Arrange
        String languageCode = "invalid";
        
        // Act
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}