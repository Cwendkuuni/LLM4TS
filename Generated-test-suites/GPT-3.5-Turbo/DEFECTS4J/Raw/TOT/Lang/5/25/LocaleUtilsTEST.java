import org.junit.Test;
import org.junit.Assert;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {
    
    // Test toLocale method
    
    @Test
    public void testToLocale_ValidLocaleString() {
        Locale locale = LocaleUtils.toLocale("en_US");
        Assert.assertEquals(locale.getLanguage(), "en");
        Assert.assertEquals(locale.getCountry(), "US");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleString() {
        Locale locale = LocaleUtils.toLocale("english_US");
    }
    
    // Test localeLookupList method
    
    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(localeList.size(), 0);
    }
    
    @Test
    public void testLocaleLookupList_ValidLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(localeList.size(), 3);
        Assert.assertEquals(localeList.get(0), new Locale("fr", "CA", "xxx"));
        Assert.assertEquals(localeList.get(1), new Locale("fr", "CA"));
        Assert.assertEquals(localeList.get(2), new Locale("fr"));
    }
    
    // Test availableLocaleList method
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        Assert.assertTrue(localeList.size() > 0);
    }
    
    // Test availableLocaleSet method
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        Assert.assertTrue(localeSet.size() > 0);
    }
    
    // Test isAvailableLocale method
    
    @Test
    public void testIsAvailableLocale_ValidLocale() {
        Locale locale = new Locale("en", "US");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(isAvailable);
    }
    
    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale locale = new Locale("english", "US");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(isAvailable);
    }
    
    // Test languagesByCountry method
    
    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        List<Locale> languageList = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(languageList.size(), 0);
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        List<Locale> languageList = LocaleUtils.languagesByCountry("US");
        Assert.assertTrue(languageList.size() > 0);
    }
    
    // Test countriesByLanguage method
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        List<Locale> countryList = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(countryList.size(), 0);
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        List<Locale> countryList = LocaleUtils.countriesByLanguage("en");
        Assert.assertTrue(countryList.size() > 0);
    }
}