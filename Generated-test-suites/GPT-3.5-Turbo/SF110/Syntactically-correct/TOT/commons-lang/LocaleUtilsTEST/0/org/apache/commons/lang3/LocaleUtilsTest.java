import org.junit.Test;
import org.junit.Assert;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest{
    
    @Test
    public void testToLocaleNullString(){
        String str = null;
        Locale result = LocaleUtils.toLocale(str);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testToLocaleEmptyString(){
        String str = "";
        Locale result = LocaleUtils.toLocale(str);
        
        Assert.assertEquals(new Locale("", ""), result);
    }
    
    @Test
    public void testToLocaleInvalidFormatWithSharpSymbol(){
        String str = "en#US";
        
        try {
            LocaleUtils.toLocale(str);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: en#US", e.getMessage());
        }
    }
    
    @Test
    public void testToLocaleInvalidFormatLengthLessThan2(){
        String str = "e";
        
        try {
            LocaleUtils.toLocale(str);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: e", e.getMessage());
        }
    }
    
    @Test
    public void testToLocaleValidFormatWithAllLowerCaseLength2(){
        String str = "en";
        Locale expected = new Locale("en");
        
        Locale result = LocaleUtils.toLocale(str);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testToLocaleValidFormatWithAllLowerCaseLength3(){
        String str = "eng";
        Locale expected = new Locale("eng");
        
        Locale result = LocaleUtils.toLocale(str);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testToLocaleInvalidFormatLength2WithMixedCase(){
        String str = "En";
        
        try {
            LocaleUtils.toLocale(str);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: En", e.getMessage());
        }
    }
    
    @Test
    public void testToLocaleValidFormatWithLowerCaseLanguageCodeAndUpperCaseCountryCode(){
        String str = "en_US";
        Locale expected = new Locale("en", "US");
        
        Locale result = LocaleUtils.toLocale(str);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testToLocaleInvalidFormatWithLowerCaseLanguageCodeAndCountryCodeLessThan2Characters(){
        String str = "en_u";
        
        try {
            LocaleUtils.toLocale(str);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: en_u", e.getMessage());
        }
    }
    
    @Test
    public void testToLocaleInvalidFormatWithLowerCaseLanguageCodeAndInvalidUpperCaseCountryCode(){
        String str = "en_USA";
        
        try {
            LocaleUtils.toLocale(str);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: en_USA", e.getMessage());
        }
    }
    
    @Test
    public void testToLocaleValidFormatWithLowerCaseLanguageCodeAndEmptyCountryCodeAndNonEmptyVariant(){
        String str = "en__variant";
        Locale expected = new Locale("en", "", "variant");
        
        Locale result = LocaleUtils.toLocale(str);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testToLocaleInvalidFormatWithLowerCaseLanguageCodeAndEmptyCountryCodeAndEmptyVariant(){
        String str = "en__";
        
        try {
            LocaleUtils.toLocale(str);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: en__", e.getMessage());
        }
    }
    
    @Test
    public void testToLocaleInvalidFormatWithUnderscoreAsFirstCharacter(){
        String str = "_en_US";
        
        try {
            LocaleUtils.toLocale(str);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: _en_US", e.getMessage());
        }
    }
    
    @Test
    public void testToLocaleInvalidFormatWithLengthLessThan3AndUpperCasedCharacters(){
        String str = "A_";
        
        try {
            LocaleUtils.toLocale(str);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: A_", e.getMessage());
        }
    }
    
    @Test
    public void testToLocaleValidFormatWithUpperCaseLetterLanguageCode(){
        String str = "_EN";
        Locale expected = new Locale("", "EN");
        
        Locale result = LocaleUtils.toLocale(str);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLocaleLookupListWithNullLocale(){
        Locale locale = null;
        
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testLocaleLookupListWithNullDefaultLocale(){
        Locale locale = new Locale("en", "US");
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, null);
        
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(locale, result.get(0));
    }
    
    @Test
    public void testLocaleLookupListWithEmptyDefaultLocale(){
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("", "");
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(locale, result.get(0));
        Assert.assertEquals(defaultLocale, result.get(1));
    }
    
    @Test
    public void testLocaleLookupListWithVariantInLocale(){
        Locale locale = new Locale("en", "US", "variant");
        Locale defaultLocale = new Locale("", "");
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(locale, result.get(0));
        Assert.assertEquals(new Locale("en", "US"), result.get(1));
        Assert.assertEquals(defaultLocale, result.get(2));
    }
    
    @Test
    public void testAvailableLocaleList(){
        List<Locale> result = LocaleUtils.availableLocaleList();
        
        Assert.assertFalse(result.isEmpty());
    }
    
    @Test
    public void testAvailableLocaleSet(){
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        
        Assert.assertFalse(result.isEmpty());
    }
    
    @Test
    public void testIsAvailableLocaleWithAvailableLocale(){
        Locale locale = new Locale("en", "US");
        
        boolean result = LocaleUtils.isAvailableLocale(locale);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsAvailableLocaleWithUnavailableLocale(){
        Locale locale = new Locale("zz", "ZZ");
        
        boolean result = LocaleUtils.isAvailableLocale(locale);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testLanguagesByCountryWithNullCountryCode(){
        String countryCode = null;
        
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        
        Assert.assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountryWithAvailableCountryCode(){
        String countryCode = "US";
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(new Locale("en", "US"));
        
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLanguagesByCountryWithUnavailableCountryCode(){
        String countryCode = "ZZ";
        
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        
        Assert.assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguageWithNullLanguageCode(){
        String languageCode = null;
        
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        
        Assert.assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguageWithAvailableLanguageCode(){
        String languageCode = "en";
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(new Locale("en", "US"));
        
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testCountriesByLanguageWithUnavailableLanguageCode(){
        String languageCode = "zz";
        
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        
        Assert.assertTrue(result.isEmpty());
    }
}