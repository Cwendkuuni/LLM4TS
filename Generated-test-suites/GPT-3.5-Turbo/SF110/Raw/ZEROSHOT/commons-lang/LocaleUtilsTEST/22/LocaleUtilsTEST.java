import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_NullString_ReturnNull() {
        String str = null;
        Locale result = LocaleUtils.toLocale(str);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnLocaleWithEmptyLanguageAndCountry() {
        String str = "";
        Locale result = LocaleUtils.toLocale(str);
        Locale expected = new Locale("", "");
        
        Assert.assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_WithHashCharacter_ThrowIllegalArgumentException() {
        String str = "en#US";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_WithLengthLessThan2_ThrowIllegalArgumentException() {
        String str = "e";
        LocaleUtils.toLocale(str);
    }
    
    @Test
    public void testToLocale_ValidLocaleFormat_WithNoUnderscore_ReturnLocaleWithGivenLanguage() {
        String str = "en";
        Locale result = LocaleUtils.toLocale(str);
        Locale expected = new Locale("en");
        
        Assert.assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_WithLowerCaseLanguage_ThrowIllegalArgumentException() {
        String str = "EN";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_WithLengthNotEqual2Or3_ThrowIllegalArgumentException() {
        String str = "eng";
        LocaleUtils.toLocale(str);
    }
    
    @Test
    public void testToLocale_ValidLocaleFormat_WithLowerCaseLanguageAndUpperCaseCountry_ReturnLocaleWithGivenLanguageAndCountry() {
        String str = "en_US";
        Locale result = LocaleUtils.toLocale(str);
        Locale expected = new Locale("en", "US");
        
        Assert.assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_WithUpperCaseLanguageAndInvalidCountry_ThrowIllegalArgumentException() {
        String str = "EN_USA";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_WithLowerCaseLanguageAndUpperCaseCountryAndInvalidVariant_ThrowIllegalArgumentException() {
        String str = "en_US1";
        LocaleUtils.toLocale(str);
    }
    
    @Test
    public void testToLocale_ValidLocaleFormat_WithLowerCaseLanguageUpperCaseCountryAndVariant_ReturnLocaleWithGivenLanguageCountryAndVariant() {
        String str = "en_US_var";
        Locale result = LocaleUtils.toLocale(str);
        Locale expected = new Locale("en", "US", "var");
        
        Assert.assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_WithLengthLessThan3WithUnderscoreAsFirstCharacter_ThrowIllegalArgumentException() {
        String str = "_US";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_WithUpperCaseCharactersAsSecondAndThirdCharacter_ThrowIllegalArgumentException() {
        String str = "_AB";
        LocaleUtils.toLocale(str);
    }
    
    @Test
    public void testToLocale_ValidLocaleFormat_WithLengthEqual3_ReturnLocaleWithEmptyLanguageAndCountryAndGivenVariant() {
        String str = "_var";
        Locale result = LocaleUtils.toLocale(str);
        Locale expected = new Locale("", "var");
        
        Assert.assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_WithLengthLessThan5_ThrowIllegalArgumentException() {
        String str = "_USvar";
        LocaleUtils.toLocale(str);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_WithUnderscoreNotAsFourthCharacter_ThrowIllegalArgumentException() {
        String str = "_USv";
        LocaleUtils.toLocale(str);
    }
    
    @Test
    public void testLocaleLookupList_NullLocale_ReturnEmptyList() {
        Locale locale = null;
        
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        
        Assert.assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLocaleLookupList_ValidLocale_ReturnListWithGivenLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> expected = List.of(new Locale("en", "US"));
        
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLocaleLookupList_ValidLocaleWithVariant_ReturnListWithGivenAndModifiedLocale() {
        Locale locale = new Locale("en", "US", "var");
        List<Locale> expected = List.of(new Locale("en", "US"), new Locale("en"));
        
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLocaleLookupList_ValidLocaleWithCountry_ReturnListWithGivenAndModifiedLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = List.of(new Locale("en", "US"), new Locale("en"));
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLocaleLookupList_ValidLocaleWithCountryAndVariant_ReturnListWithGivenAndModifiedLocale() {
        Locale locale = new Locale("en", "US", "var");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = List.of(new Locale("en", "US", "var"), new Locale("en", "US"), new Locale("en"));
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testAvailableLocaleList_ReturnListWithAllAvailableLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnSetWithAllAvailableLocales() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
    }
    
    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnTrue() {
        Locale locale = new Locale("en", "US");
        
        boolean result = LocaleUtils.isAvailableLocale(locale);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsAvailableLocale_NonAvailableLocale_ReturnFalse() {
        Locale locale = new Locale("abc", "def");
        
        boolean result = LocaleUtils.isAvailableLocale(locale);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
        String countryCode = null;
        
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        
        Assert.assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnListWithLanguagesBelongsToCountry() {
        String countryCode = "DE";
        List<Locale> expected = List.of(new Locale("de"), new Locale("en"), new Locale("es"));
        
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
        String languageCode = null;
        
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        
        Assert.assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnListWithCountriesBelongsToLanguage() {
        String languageCode = "en";
        List<Locale> expected = List.of(new Locale("US"), new Locale("GB"), new Locale("CA"));
        
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        
        Assert.assertEquals(expected, result);
    }
}