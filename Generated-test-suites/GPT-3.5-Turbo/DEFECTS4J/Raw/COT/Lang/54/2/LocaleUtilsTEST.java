import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("", locale.getCountry());
        
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals("en", locale2.getLanguage());
        Assert.assertEquals("GB", locale2.getCountry());
        
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals("en", locale3.getLanguage());
        Assert.assertEquals("GB", locale3.getCountry());
        Assert.assertEquals("xxx", locale3.getVariant());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("en_GB_xxx_");
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(locale, list.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list.get(1));
        Assert.assertEquals(new Locale("fr"), list.get(2));
        
        Locale defaultLocale = new Locale("en");
        List<Locale> list2 = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(4, list2.size());
        Assert.assertEquals(locale, list2.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list2.get(1));
        Assert.assertEquals(new Locale("fr"), list2.get(2));
        Assert.assertEquals(defaultLocale, list2.get(3));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(list);
        Assert.assertFalse(list.isEmpty());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(set);
        Assert.assertFalse(set.isEmpty());
    }
    
    @Test
    public void testIsAvailableLocale() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(Locale.JAPANESE));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("CA");
        Assert.assertNotNull(list);
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.contains(Locale.ENGLISH));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        Assert.assertNotNull(list);
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.contains(Locale.CANADA));
    }
}