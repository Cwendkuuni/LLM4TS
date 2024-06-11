import org.junit.Test;
import org.junit.Assert;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidLanguageCode_ShouldReturnLocale() {
        String languageCode = "en";
        Locale expectedLocale = new Locale("en", "");
        
        Locale result = LocaleUtils.toLocale(languageCode);
        
        Assert.assertEquals(expectedLocale, result);
    }
    
    @Test
    public void testToLocale_ValidLanguageAndCountryCode_ShouldReturnLocale() {
        String languageCode = "en_GB";
        Locale expectedLocale = new Locale("en", "GB");
        
        Locale result = LocaleUtils.toLocale(languageCode);
        
        Assert.assertEquals(expectedLocale, result);
    }
    
    @Test
    public void testToLocale_ValidLanguageCountryAndVariantCode_ShouldReturnLocale() {
        String languageCode = "en_GB_xxx";
        Locale expectedLocale = new Locale("en", "GB", "xxx");
        
        Locale result = LocaleUtils.toLocale(languageCode);
        
        Assert.assertEquals(expectedLocale, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ShouldThrowIllegalArgumentException() {
        String languageCode = "invalid";
        
        LocaleUtils.toLocale(languageCode);
    }
    
    // Additional test cases for localeLookupList() method
    
    @Test
    public void testLocaleLookupList_LocaleWithVariant_ShouldReturnExpectedList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        
        List<Locale> expectedList = List.of(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        Assert.assertEquals(expectedList, result);
    }
    
    @Test
    public void testLocaleLookupList_LocaleWithoutVariant_ShouldReturnExpectedList() {
        Locale locale = new Locale("fr", "CA");
        Locale defaultLocale = new Locale("en");
        
        List<Locale> expectedList = List.of(
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        Assert.assertEquals(expectedList, result);
    }
    
    @Test
    public void testLocaleLookupList_LocaleWithoutCountry_ShouldReturnExpectedList() {
        Locale locale = new Locale("fr");
        Locale defaultLocale = new Locale("en");
        
        List<Locale> expectedList = List.of(
                new Locale("fr"),
                new Locale("en")
        );
        
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        
        Assert.assertEquals(expectedList, result);
    }
    
    @Test
    public void testLocaleLookupList_NullLocale_ShouldReturnDefaultLocaleList() {
        Locale defaultLocale = new Locale("en");
        
        List<Locale> expectedList = List.of(
                new Locale("en")
        );
        
        List<Locale> result = LocaleUtils.localeLookupList(null, defaultLocale);
        
        Assert.assertEquals(expectedList, result);
    }
    
    // Additional test cases for availableLocaleList() method
    
    @Test
    public void testAvailableLocaleList_ListSize_ShouldReturnGreaterThanZero() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        
        Assert.assertTrue(localeList.size() > 0);
    }
    
    // Additional test cases for availableLocaleSet() method
    
    @Test
    public void testAvailableLocaleSet_SetSize_ShouldReturnGreaterThanZero() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        
        Assert.assertTrue(localeSet.size() > 0);
    }
    
    // Additional test cases for isAvailableLocale() method
    
    @Test
    public void testIsAvailableLocale_AvailableLocale_ShouldReturnTrue() {
        Locale locale = new Locale("fr", "CA");
        
        boolean result = LocaleUtils.isAvailableLocale(locale);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsAvailableLocale_NonAvailableLocale_ShouldReturnFalse() {
        Locale locale = new Locale("xx", "XX");
        
        boolean result = LocaleUtils.isAvailableLocale(locale);
        
        Assert.assertFalse(result);
    }
    
    // Additional test cases for languagesByCountry() method
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ShouldReturnLanguageList() {
        String countryCode = "CA";
        
        List<Locale> expectedList = List.of(
                new Locale("en", "CA"),
                new Locale("fr", "CA")
        );
        
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        
        Assert.assertEquals(expectedList, result);
    }
    
    @Test
    public void testLanguagesByCountry_InvalidCountryCode_ShouldReturnEmptyList() {
        String countryCode = "XX";
        
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        
        Assert.assertTrue(result.isEmpty());
    }
    
    // Additional test cases for countriesByLanguage() method
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ShouldReturnCountryList() {
        String languageCode = "fr";
        
        List<Locale> expectedList = List.of(
                new Locale("fr", "CA"),
                new Locale("fr", "CH"),
                new Locale("fr", "BE")
        );
        
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        
        Assert.assertEquals(expectedList, result);
    }
    
    @Test
    public void testCountriesByLanguage_InvalidLanguageCode_ShouldReturnEmptyList() {
        String languageCode = "xx";
        
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        
        Assert.assertTrue(result.isEmpty());
    }

}