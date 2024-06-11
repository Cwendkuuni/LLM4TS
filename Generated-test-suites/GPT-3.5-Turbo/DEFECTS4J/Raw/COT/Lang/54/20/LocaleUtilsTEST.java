import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String str = "en";
        Locale locale = LocaleUtils.toLocale(str);
        Assert.assertEquals(new Locale("en", ""), locale);

        str = "en_GB";
        locale = LocaleUtils.toLocale(str);
        Assert.assertEquals(new Locale("en", "GB"), locale);

        str = "en_GB_xxx";
        locale = LocaleUtils.toLocale(str);
        Assert.assertEquals(new Locale("en", "GB", "xxx"), locale);

        str = "InvalidLocale";
        try {
            locale = LocaleUtils.toLocale(str);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Do nothing. Test passed.
        }

        str = "en_GB_xx";
        try {
            locale = LocaleUtils.toLocale(str);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Do nothing. Test passed.
        }
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
        list = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(4, list.size());
        Assert.assertEquals(defaultLocale, list.get(3));
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
        Locale locale = new Locale("en", "US");
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(isAvailable);

        locale = new Locale("xx", "YY");
        isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(isAvailable);
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "GB";
        List<Locale> languages = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertNotNull(languages);
        Assert.assertFalse(languages.isEmpty());

        countryCode = "XX";
        languages = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertNotNull(languages);
        Assert.assertTrue(languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> countries = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertNotNull(countries);
        Assert.assertFalse(countries.isEmpty());

        languageCode = "xx";
        countries = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertNotNull(countries);
        Assert.assertTrue(countries.isEmpty());
    }

}