import org.junit.*;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnNull() {
        Locale result = LocaleUtils.toLocale(null);
        assertNull(result);
    }
    
    @Test
    public void testToLocale_EmptyInput_ReturnEmptyLocale() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHash_ThrowException() {
        LocaleUtils.toLocale("#en_US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatLengthLessThan2_ThrowException() {
        LocaleUtils.toLocale("e");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithMissingUnderscore_ThrowException() {
        LocaleUtils.toLocale("enUS");
    }
    
    @Test
    public void testToLocale_ValidFormatWithNoCountry_ReturnLocaleWithLanguage() {
        Locale result = LocaleUtils.toLocale("en");
        assertEquals("en", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test
    public void testToLocale_ValidFormatWithCountry_ReturnLocaleWithLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("en_US");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }
    
    @Test
    public void testToLocale_ValidFormatWithLanguageCountryAndVariant_ReturnLocaleWithLanguageCountryAndVariant() {
        Locale result = LocaleUtils.toLocale("en_US_var");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
        assertEquals("var", result.getVariant());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithIncorrectCaseForCountry_ThrowException() {
        LocaleUtils.toLocale("en_us");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithIncorrectCaseForLanguage_ThrowException() {
        LocaleUtils.toLocale("EN_US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithMissingCountry_ThrowException() {
        LocaleUtils.toLocale("en_");
    }
    
    @Test
    public void testLocaleLookupList_WithLocale_ReturnListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US", "var");
        Locale defaultLocale = new Locale("en", "UK", "var");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(defaultLocale));
    }
    
    @Test
    public void testLocaleLookupList_WithoutLocale_ReturnListWithDefaultLocale() {
        Locale defaultLocale = new Locale("en", "UK", "var");
        List<Locale> result = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(1, result.size());
        assertTrue(result.contains(defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList_ReturnNonNullList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnNonNullSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
    }
    
    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ReturnTrue() {
        Locale locale = new Locale("en", "US", "var");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertTrue(result);
    }
    
    @Test
    public void testIsAvailableLocale_WithUnavailableLocale_ReturnFalse() {
        Locale locale = new Locale("xx", "XX", "var");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertFalse(result);
    }
    
    @Test
    public void testLanguagesByCountry_WithValidCountryCode_ReturnListOfLanguages() {
        String countryCode = "US";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertNotNull(result);
    }
    
    @Test
    public void testLanguagesByCountry_WithInvalidCountryCode_ReturnEmptyList() {
        String countryCode = "XX";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testCountriesByLanguage_WithValidLanguageCode_ReturnListOfCountries() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertNotNull(result);
    }
    
    @Test
    public void testCountriesByLanguage_WithInvalidLanguageCode_ReturnEmptyList() {
        String languageCode = "xx";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}