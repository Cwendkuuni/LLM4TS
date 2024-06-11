import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleValidInput() {
        Locale expectedLocale = new Locale("en", "GB", "xxx");
        Locale actualLocale = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(expectedLocale, actualLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        LocaleUtils.toLocale("invalid_locale");
    }

    @Test
    public void testLocaleLookupListSingleLocale() {
        Locale locale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(new Locale("en"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupListMultipleLocales() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("en")
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(locale, new Locale("en"));
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        Assert.assertTrue(!locales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        Assert.assertTrue(!locales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale usLocale = new Locale("en", "US");
        boolean isAvailable = LocaleUtils.isAvailableLocale(usLocale);
        Assert.assertTrue(isAvailable);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        Assert.assertTrue(!languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        Assert.assertTrue(!countries.isEmpty());
    }
}