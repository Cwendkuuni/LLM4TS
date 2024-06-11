import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        Assert.assertEquals(new Locale("en", ""), locale1);

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(new Locale("en", "GB"), locale2);

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(new Locale("en", "GB", "xxx"), locale3);
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        List<Locale> expected = List.of(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(availableLocales);
        Assert.assertTrue(availableLocales.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(availableLocales);
        Assert.assertTrue(availableLocales.size() > 0);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        Locale unsupportedLocale = new Locale("xx", "XX");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(unsupportedLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        Assert.assertNotNull(languages);
        Assert.assertTrue(languages.size() > 0);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        Assert.assertNotNull(countries);
        Assert.assertTrue(countries.size() > 0);
    }
}