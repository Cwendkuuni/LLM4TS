import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        Assert.assertEquals("en", locale1.getLanguage());
        Assert.assertEquals("", locale1.getCountry());
        
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals("en", locale2.getLanguage());
        Assert.assertEquals("GB", locale2.getCountry());
        
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals("en", locale3.getLanguage());
        Assert.assertEquals("GB", locale3.getCountry());
        Assert.assertEquals("xxx", locale3.getVariant());
        
        try {
            LocaleUtils.toLocale("invalid_locale");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected exception
        }
        
        try {
            LocaleUtils.toLocale("en_gb");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testLocaleLookupList1() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale1);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(new Locale("fr", "CA", "xxx"), list.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list.get(1));
        Assert.assertEquals(new Locale("fr"), list.get(2));
    }

    @Test
    public void testLocaleLookupList2() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> list = LocaleUtils.localeLookupList(locale1, defaultLocale);
        Assert.assertEquals(4, list.size());
        Assert.assertEquals(new Locale("fr", "CA", "xxx"), list.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list.get(1));
        Assert.assertEquals(new Locale("fr"), list.get(2));
        Assert.assertEquals(new Locale("en"), list.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        Assert.assertEquals(194, list.size());
    }

    @Test
    public void testAvailableLocaleSet() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        int size = list.size();
        
        List<Locale> set = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(size, set.size());
        
        for (Locale locale : list) {
            Assert.assertTrue(set.contains(locale));
        }
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale1));
        
        Locale locale2 = new Locale("xx", "XX");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list1 = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(2, list1.size());
        Assert.assertEquals(new Locale("fr", "CA"), list1.get(0));
        Assert.assertEquals(new Locale("en", "CA"), list1.get(1));
        
        List<Locale> list2 = LocaleUtils.languagesByCountry("XX");
        Assert.assertTrue(list2.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list1 = LocaleUtils.countriesByLanguage("fr");
        Assert.assertEquals(42, list1.size());
        Assert.assertEquals(new Locale("fr", "CA"), list1.get(0));
        Assert.assertEquals(new Locale("fr", "CH"), list1.get(1));
        
        List<Locale> list2 = LocaleUtils.countriesByLanguage("xx");
        Assert.assertTrue(list2.isEmpty());
    }
}