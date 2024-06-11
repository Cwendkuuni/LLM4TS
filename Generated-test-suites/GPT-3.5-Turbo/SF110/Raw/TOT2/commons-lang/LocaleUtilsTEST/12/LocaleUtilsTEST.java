import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_basic() {
        Locale result = LocaleUtils.toLocale("en_US");
        Assert.assertEquals("en_US", result.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("invalidLocale");
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(4, result.size());
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testIsAvailableLocale_true() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testLanguagesByCountry_null() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(0, result.size());
    }
    
    @Test
    public void testCountriesByLanguage_null() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(0, result.size());
    }

}