import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");

        Assert.assertEquals(Locale.ENGLISH, locale1);
        Assert.assertEquals(new Locale("en", "GB"), locale2);
        Assert.assertEquals(new Locale("en", "GB", "xxx"), locale3);
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> locales = LocaleUtils.localeLookupList(locale);

        Assert.assertEquals(3, locales.size());
        Assert.assertEquals(locale, locales.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), locales.get(1));
        Assert.assertEquals(new Locale("fr"), locales.get(2));
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
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);

        Assert.assertTrue(isAvailable);
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