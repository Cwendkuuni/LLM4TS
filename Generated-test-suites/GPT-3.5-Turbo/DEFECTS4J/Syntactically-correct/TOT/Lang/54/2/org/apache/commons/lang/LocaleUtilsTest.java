import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class LocaleUtilsTest {
    
    //Test case for toLocale() method with valid input
    @Test
    public void testToLocaleValidInput() {
        Locale result = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("GB", result.getCountry());
    }
    
    //Test case for toLocale() method with null input
    @Test
    public void testToLocaleNullInput() {
        Locale result = LocaleUtils.toLocale(null);
        Assert.assertNull(result);
    }
    
    //Test case for toLocale() method with invalid input
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        Locale result = LocaleUtils.toLocale("en_USA");
    }
    
    //Test case for localeLookupList() method with valid input
    @Test
    public void testLocaleLookupListValidInput() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(locale, result.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), result.get(1));
        Assert.assertEquals(new Locale("fr"), result.get(2));
    }
    
    //Test case for localeLookupList() method with null input
    @Test
    public void testLocaleLookupListNullInput() {
        List result = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(0, result.size());
    }
    
    //Test case for availableLocaleList() method
    @Test
    public void testAvailableLocaleList() {
        List result = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);
    }
    
    //Test case for availableLocaleSet() method
    @Test
    public void testAvailableLocaleSet() {
        Set result = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);
    }
    
    //Test case for isAvailableLocale() method with available locale
    @Test
    public void testIsAvailableLocaleAvailableLocale() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }
    
    //Test case for isAvailableLocale() method with unavailable locale
    @Test
    public void testIsAvailableLocaleUnavailableLocale() {
        Locale locale = new Locale("xx", "YY");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(result);
    }
    
    //Test case for languagesByCountry() method with valid input
    @Test
    public void testLanguagesByCountryValidInput() {
        List result = LocaleUtils.languagesByCountry("CA");
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);
    }
    
    //Test case for languagesByCountry() method with null input
    @Test
    public void testLanguagesByCountryNullInput() {
        List result = LocaleUtils.languagesByCountry(null);
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }
    
    //Test case for countriesByLanguage() method with valid input
    @Test
    public void testCountriesByLanguageValidInput() {
        List result = LocaleUtils.countriesByLanguage("fr");
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);
    }
    
    //Test case for countriesByLanguage() method with null input
    @Test
    public void testCountriesByLanguageNullInput() {
        List result = LocaleUtils.countriesByLanguage(null);
       Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }
    
}