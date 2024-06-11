import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {
    
    // Test the toLocale method
    
    @Test
    public void testToLocale_NullString_ReturnNull() {
        Locale result = LocaleUtils.toLocale(null);
        assertNull(result);
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnEmptyLocale() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("#en-US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLessThan2Length_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("e");
    }
    
    @Test
    public void testToLocale_ValidFormatWithLowerCaseLanguage_ReturnLocaleWithLowerCaseLanguage() {
        Locale result = LocaleUtils.toLocale("en");
        assertEquals("en", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test
    public void testToLocale_ValidFormatWithUpperCaseLanguage_ReturnLocaleWithUpperCaseLanguage() {
        Locale result = LocaleUtils.toLocale("EN");
        assertEquals("EN", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    // Add more test cases for toLocale method to cover all scenarios
    
    // Test the localeLookupList method
    
    @Test
    public void testLocaleLookupList_WithNullLocale_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLocaleLookupList_WithLocaleAndDefaultLocale_ReturnListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en", "GB");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale("en")));
        assertTrue(result.contains(new Locale("en", "")));
        assertTrue(result.contains(defaultLocale));
    }
    
    // Add more test cases for localeLookupList method to cover all scenarios
    
    // Test the availableLocaleList method
    
    @Test
    public void testAvailableLocaleList_ReturnListOfAvailableLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    // Add more test cases for availableLocaleList method to cover all scenarios
    
    // Test the availableLocaleSet method
    
    @Test
    public void testAvailableLocaleSet_ReturnSetOfAvailableLocales() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    
    // Add more test cases for availableLocaleSet method to cover all scenarios
    
    // Test the isAvailableLocale method
    
    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ReturnTrue() {
        boolean result = LocaleUtils.isAvailableLocale(new Locale("en", "US"));
        assertTrue(result);
    }
    
    @Test
    public void testIsAvailableLocale_WithUnavailableLocale_ReturnFalse() {
        boolean result = LocaleUtils.isAvailableLocale(new Locale("zz", "ZZ"));
        assertFalse(result);
    }
    
    // Add more test cases for isAvailableLocale method to cover all scenarios
    
    // Test the languagesByCountry method
    
    @Test
    public void testLanguagesByCountry_WithNullCountryCode_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_WithValidCountryCode_ReturnListWithMatchingLocales() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertNotNull(result);
        assertFalse(result.isEmpty());
        for (Locale locale : result) {
            assertEquals("US", locale.getCountry());
            assertTrue(locale.getVariant().isEmpty());
        }
    }
    
    // Add more test cases for languagesByCountry method to cover all scenarios
    
    // Test the countriesByLanguage method
    
    @Test
    public void testCountriesByLanguage_WithNullLanguageCode_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_WithValidLanguageCode_ReturnListWithMatchingLocales() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertNotNull(result);
        assertFalse(result.isEmpty());
        for (Locale locale : result) {
            assertEquals("en", locale.getLanguage());
            assertFalse(locale.getCountry().isEmpty());
            assertTrue(locale.getVariant().isEmpty());
        }
    }
    
    // Add more test cases for countriesByLanguage method to cover all scenarios
    
}