import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        Assert.assertNotNull(locale);
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("", locale.getCountry());
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(locale, result.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), result.get(1));
        Assert.assertEquals(new Locale("fr"), result.get(2));

        List<Locale> resultWithDefault = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertNotNull(resultWithDefault);
        Assert.assertEquals(4, resultWithDefault.size());
        Assert.assertEquals(locale, resultWithDefault.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), resultWithDefault.get(1));
        Assert.assertEquals(new Locale("fr"), resultWithDefault.get(2));
        Assert.assertEquals(defaultLocale, resultWithDefault.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("CA");
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("fr");
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);
    }

}