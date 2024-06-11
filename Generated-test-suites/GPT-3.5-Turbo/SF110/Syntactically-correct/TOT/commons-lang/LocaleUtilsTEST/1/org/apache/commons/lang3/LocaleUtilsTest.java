import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString_ReturnsNull() {
        Locale result = LocaleUtils.toLocale(null);
        assertNull(result);
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnsEmptyLocale() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en#US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLengthLessThanTwo_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("a");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidLanguage_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("EN");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidLanguageAndValidCountry_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("ab_Cd");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithInvalidLanguageAndEmptyCountry_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("ab__ef");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLessThanThreeLengthAndNonUpperCaseSecondCharacter_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_ab");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLengthLessThanFive_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_en_");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithMissingUnderscore_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("_en_");
    }
    
    @Test
    public void testLocaleLookupList_WithNullLocale_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLocaleLookupList_WithNonNullLocale_ReturnsListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US", "variant");
        Locale defaultLocale = new Locale("en", "GB");
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(4, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(defaultLocale));
        assertTrue(result.contains(new Locale(locale.getLanguage(), locale.getCountry())));
        assertTrue(result.contains(new Locale(locale.getLanguage(), "")));
    }
    
    @Test
    public void testAvailableLocaleList_ReturnsAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        
        assertNotNull(result);
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnsAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        
        assertNotNull(result);
    }
    
    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ReturnsTrue() {
        Locale locale = Locale.US;
        
        boolean result = LocaleUtils.isAvailableLocale(locale);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsAvailableLocale_WithUnavailableLocale_ReturnsFalse() {
        Locale locale = new Locale("xx", "XX");
        
        boolean result = LocaleUtils.isAvailableLocale(locale);
        
        assertFalse(result);
    }
    
    @Test
    public void testLanguagesByCountry_WithNullCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_WithInvalidCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry("XX");
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_WithValidCountryCode_ReturnsListOfLanguages() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_WithNullLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_WithInvalidLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("XX");
        
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_WithValidLanguageCode_ReturnsListOfCountries() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}