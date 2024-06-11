import org.junit.*;
import static org.junit.Assert.*;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_NullString_ReturnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnLocaleWithEmptyLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en#US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLengthLessThan2_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("x");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithFirstCharacterNotUnderscore_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("xy");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithTwoUnderscoresAndEmptyThirdPart_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_XY_");
    }
    
    @Test
    public void testToLocale_ValidLocaleWithLowerCase_ReturnLocaleWithLowerCaseLanguage() {
        Locale result = LocaleUtils.toLocale("en");
        assertEquals("en", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLowerCasePart1AndUpperCasePart2_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en_US");
    }
    
    @Test
    public void testToLocale_ValidFormatWithLowerCasePart1AndUpperCasePart2_ReturnLocaleWithProvidedLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("en_US");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLowerCasePart1AndEmptyPart2_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en_");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLowerCasePart1AndTwoUpperCasePart2_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en_US_XY");
    }
    
    @Test
    public void testToLocale_ValidFormatWithLowerCasePart1AndEmptyPart2_ReturnLocaleWithProvidedLanguage() {
        Locale result = LocaleUtils.toLocale("en_");
        assertEquals("en", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWith2Underscores_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("__");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithUpperCaseThirdCharacter_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_Ab");
    }
    
    @Test
    public void testToLocale_ValidFormatWithUpperCaseThirdCharacter_ReturnLocaleWithProvidedCountry() {
        Locale result = LocaleUtils.toLocale("_AB");
        assertEquals("", result.getLanguage());
        assertEquals("AB", result.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLengthLessThan3AndFourthCharacterNotUnderscore_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_ABCD");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLengthLessThan5_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_AB_");
    }
    
    @Test
    public void testToLocale_ValidFormat_ReturnLocaleWithProvidedLanguageCountryAndVariant() {
        Locale result = LocaleUtils.toLocale("_AB_CD");
        assertEquals("", result.getLanguage());
        assertEquals("AB", result.getCountry());
        assertEquals("CD", result.getVariant());
    }
    
    @Test
    public void testLocaleLookupList_NullLocale_ReturnEmptyList() {
        assertEquals(0, LocaleUtils.localeLookupList(null).size());
    }
    
    @Test
    public void testLocaleLookupList_ValidLocale_ReturnListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en", "GB");
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(2, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(defaultLocale));
    }
    
    @Test
    public void testLocaleLookupList_VariantInLocale_ReturnListWithLocaleAndLocaleWithEmptyVariant() {
        Locale locale = new Locale("en", "US", "variant");
        
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        
        assertEquals(2, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale("en", "US")));
    }
    
    @Test
    public void testLocaleLookupList_CountryInLocale_ReturnListWithLocaleAndLocaleWithEmptyCountry() {
        Locale locale = new Locale("en", "US");
        
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        
        assertEquals(2, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale("en", "")));
    }
    
    @Test
    public void testLocaleLookupList_DefaultLocaleNotInList_ReturnListWithLocaleDefaultAndDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("de", "DE");
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(3, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(defaultLocale));
        assertTrue(result.contains(Locale.getDefault()));
    }
    
    @Test
    public void testAvailableLocaleList_Always_ReturnListWithAvailableLocales() {
        assertEquals(Locale.getAvailableLocales().length, LocaleUtils.availableLocaleList().size());
    }
    
    @Test
    public void testAvailableLocaleSet_Always_ReturnSetWithAvailableLocales() {
        assertEquals(Locale.getAvailableLocales().length, LocaleUtils.availableLocaleSet().size());
    }
    
    @Test
    public void testIsAvailableLocale_IncludedLocale_ReturnTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testIsAvailableLocale_NotIncludedLocale_ReturnFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "YY")));
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
        assertEquals(0, LocaleUtils.languagesByCountry(null).size());
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnLanguagesList() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertTrue(result.size() > 0);
    }
    
    @Test
    public void testLanguagesByCountry_CacheHit_ReturnCachedLanguagesList() {
        List<Locale> result1 = LocaleUtils.languagesByCountry("US");
        List<Locale> result2 = LocaleUtils.languagesByCountry("US");
        
        assertEquals(result1, result2);
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
        assertEquals(0, LocaleUtils.countriesByLanguage(null).size());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnCountriesList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertTrue(result.size() > 0);
    }
    
    @Test
    public void testCountriesByLanguage_CacheHit_ReturnCachedCountriesList() {
        List<Locale> result1 = LocaleUtils.countriesByLanguage("en");
        List<Locale> result2 = LocaleUtils.countriesByLanguage("en");
        
        assertEquals(result1, result2);
    }
    
}