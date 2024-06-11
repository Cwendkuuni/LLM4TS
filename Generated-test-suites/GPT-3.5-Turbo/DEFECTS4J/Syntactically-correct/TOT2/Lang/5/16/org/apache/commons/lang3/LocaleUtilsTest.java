import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;

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
    public void testToLocaleInvalidFormat() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            LocaleUtils.toLocale("invalid");
        });
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(new Locale("fr", "CA", "xxx"), list.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list.get(1));
        Assert.assertEquals(new Locale("fr"), list.get(2));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(4, list.size());
        Assert.assertEquals(new Locale("fr", "CA", "xxx"), list.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list.get(1));
        Assert.assertEquals(new Locale("fr"), list.get(2));
        Assert.assertEquals(defaultLocale, list.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(availableLocales);
        Assert.assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocalesSet = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(availableLocalesSet);
        Assert.assertFalse(availableLocalesSet.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        Assert.assertNotNull(languages);
        Assert.assertFalse(languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        Assert.assertNotNull(countries);
        Assert.assertFalse(countries.isEmpty());
    }
}