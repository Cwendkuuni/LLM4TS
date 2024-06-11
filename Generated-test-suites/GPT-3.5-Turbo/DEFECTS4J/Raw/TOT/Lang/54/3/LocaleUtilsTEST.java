import org.junit.Test;
import org.junit.Assert;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidLanguageCode_ReturnsLocale() {
        String languageCode = "en";
        Locale expectedLocale = new Locale("en", "");
        
        Locale actualLocale = LocaleUtils.toLocale(languageCode);
        
        Assert.assertEquals(expectedLocale, actualLocale);
    }
    
    @Test
    public void testToLocale_ValidLanguageAndCountryCode_ReturnsLocale() {
        String languageCode = "en";
        String countryCode = "GB";
        Locale expectedLocale = new Locale("en", "GB");
        
        Locale actualLocale = LocaleUtils.toLocale(languageCode + "_" + countryCode);
        
        Assert.assertEquals(expectedLocale, actualLocale);
    }
    
    @Test
    public void testToLocale_ValidLanguageCountryAndVariantCode_ReturnsLocale() {
        String languageCode = "en";
        String countryCode = "GB";
        String variantCode = "xxx";
        Locale expectedLocale = new Locale("en", "GB", "xxx");
        
        Locale actualLocale = LocaleUtils.toLocale(languageCode + "_" + countryCode + "_" + variantCode);
        
        Assert.assertEquals(expectedLocale, actualLocale);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_NullInput_ThrowsIllegalArgumentException() {
        String languageCode = null;
        
        Locale actualLocale = LocaleUtils.toLocale(languageCode);
    }
    
    // More test cases for other methods can be added
    
}