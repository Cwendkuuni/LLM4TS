import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString_ReturnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnLocaleWithEmptyLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("");
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("#en_US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLessThan2Characters_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("e");
    }
    
    @Test
    public void testToLocale_ValidFormatWithAllLowerCase2Characters_ReturnLocaleWithLanguageOnly() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }
    
    @Test
    public void testToLocale_ValidFormatWithAllLowerCase3Characters_ReturnLocaleWithLanguageOnly() {
        Locale locale = LocaleUtils.toLocale("eng");
        assertEquals("eng", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_ValidFormatWithLowerCaseLanguageAndUpperCaseCountry_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en_US");
    }
    
    @Test
    public void testToLocale_ValidFormatWithLowerCaseLanguageAndUpperCaseCountry_ReturnLocaleWithLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }
    
    @Test
    public void testToLocale_ValidFormatWithLowerCaseLanguageAndEmptyCountry_ReturnLocaleWithLanguageAndEmptyCountry() {
        Locale locale = LocaleUtils.toLocale("en_");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_ValidFormatWithLowerCaseLanguageAndLowerCaseCountryWithMoreThan2Characters_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en_abc");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_ValidFormatWithLowerCaseLanguageAndUpperCaseCountryAndEmptyVariant_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en_US_");
    }
    
    @Test
    public void testToLocale_ValidFormatWithUnderscoreAsFirstCharacter_ReturnLocaleWithEmptyLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("_US");
        assertEquals("", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_ValidFormatWithOnlyTwoCharacters_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("__");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithNonUpperCaseCharactersAfterUnderscore_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_us");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLessThan5Characters_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_USa");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithNoUnderscoreAfter3Characters_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_USa");
    }
    
    @Test
    public void testLocaleLookupList_NullLocale_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testLocaleLookupList_ValidLocale_ReturnListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = Locale.getDefault();
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        assertEquals(2, result.size());
        assertTrue(result.contains(locale));
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
    public void testIsAvailableLocale_ValidAvailableLocale_ReturnTrue() {
        Locale locale = Locale.getDefault();
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testIsAvailableLocale_InvalidAvailableLocale_ReturnFalse() {
        Locale locale = new Locale("xx", "YY");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnListWithLocales() {
        String countryCode = "US";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        
        assertTrue(result.size() > 0);
        for (Locale locale : result) {
            assertEquals(countryCode, locale.getCountry());
            assertTrue(locale.getVariant().isEmpty());
        }
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnListWithLocales() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        
        assertTrue(result.size() > 0);
        for (Locale locale : result) {
            assertEquals(languageCode, locale.getLanguage());
            assertFalse(locale.getCountry().isEmpty());
            assertTrue(locale.getVariant().isEmpty());
        }
    }
}