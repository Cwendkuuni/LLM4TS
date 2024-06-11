import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_withNullInput() {
        Locale locale = LocaleUtils.toLocale(null);
        Assert.assertNull(locale);
    }

    @Test
    public void testToLocale_withInvalidFormat() {
        try {
            Locale locale = LocaleUtils.toLocale("abc");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testToLocale_withValidLanguageCode() {
        Locale locale = LocaleUtils.toLocale("en");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("", locale.getCountry());
    }

    @Test
    public void testToLocale_withValidLanguageAndCountryCode() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("GB", locale.getCountry());
    }

    @Test
    public void testToLocale_withValidLanguageAndCountryCodeAndVariant() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("GB", locale.getCountry());
        Assert.assertEquals("xxx", locale.getVariant());
    }

    @Test
    public void testLocaleLookupList_withNullLocale() {
        List<Locale> list = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void testLocaleLookupList_withLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(locale, list.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list.get(1));
        Assert.assertEquals(new Locale("fr"), list.get(2));
    }

    @Test
    public void testLocaleLookupList_withLocaleAndDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(4, list.size());
        Assert.assertEquals(locale, list.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list.get(1));
        Assert.assertEquals(new Locale("fr"), list.get(2));
        Assert.assertEquals(defaultLocale, list.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(list);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(set);
    }

    @Test
    public void testIsAvailableLocale_withAvailableLocale() {
        Locale locale = new Locale("en", "US");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(isAvailable);
    }

    @Test
    public void testIsAvailableLocale_withUnavailableLocale() {
        Locale locale = new Locale("zz");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(isAvailable);
    }

    @Test
    public void testLanguagesByCountry_withNullCountryCode() {
        List<Locale> list = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void testLanguagesByCountry_withValidCountryCode() {
        List<Locale> list = LocaleUtils.languagesByCountry("CA");
        Assert.assertTrue(list.contains(new Locale("fr", "CA")));
        Assert.assertTrue(list.contains(new Locale("en", "CA")));
    }

    @Test
    public void testCountriesByLanguage_withNullLanguageCode() {
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void testCountriesByLanguage_withValidLanguageCode() {
        List<Locale> list = LocaleUtils.countriesByLanguage("fr");
        Assert.assertTrue(list.contains(new Locale("fr", "CA")));
        Assert.assertTrue(list.contains(new Locale("fr", "FR")));
    }

}