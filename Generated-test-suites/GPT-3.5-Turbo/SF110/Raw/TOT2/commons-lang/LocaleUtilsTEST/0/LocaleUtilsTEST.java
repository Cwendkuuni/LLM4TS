import org.junit.Test;
import org.junit.Assert;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en_US");
        Assert.assertEquals("en", locale1.getLanguage());
        Assert.assertEquals("US", locale1.getCountry());
        
        Locale locale2 = LocaleUtils.toLocale("fr");
        Assert.assertEquals("fr", locale2.getLanguage());
        Assert.assertEquals("", locale2.getCountry());
        
        Locale locale3 = LocaleUtils.toLocale("");
        Assert.assertEquals("", locale3.getLanguage());
        Assert.assertEquals("", locale3.getCountry());
        
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            LocaleUtils.toLocale("invalid");
        });
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(4, list.size());
        
        List<Locale> list2 = LocaleUtils.localeLookupList(null, locale);
        Assert.assertEquals(1, list2.size());
        
        List<Locale> list3 = LocaleUtils.localeLookupList(new Locale("fr"), locale);
        Assert.assertEquals(4, list3.size());
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        Assert.assertTrue(list.size() > 0);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        Assert.assertTrue(set.size() > 0);
    }
    
    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
        
        Locale invalidLocale = new Locale("xx", "XX");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        Assert.assertTrue(list.size() > 0);
        
        List<Locale> emptyList = LocaleUtils.languagesByCountry(null);
        Assert.assertTrue(emptyList.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        Assert.assertTrue(list.size() > 0);
        
        List<Locale> emptyList = LocaleUtils.countriesByLanguage(null);
        Assert.assertTrue(emptyList.isEmpty());
    }
}