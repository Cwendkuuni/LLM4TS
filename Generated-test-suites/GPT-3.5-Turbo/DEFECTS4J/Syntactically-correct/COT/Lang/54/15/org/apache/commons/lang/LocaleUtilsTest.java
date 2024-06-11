
import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Assert.assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(locale, result.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), result.get(1));
        Assert.assertEquals(new Locale("fr"), result.get(2));

        result = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(4, result.size());
        Assert.assertEquals(locale, result.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), result.get(1));
        Assert.assertEquals(new Locale("fr"), result.get(2));
        Assert.assertEquals(defaultLocale, result.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("CA");
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
    }

}
