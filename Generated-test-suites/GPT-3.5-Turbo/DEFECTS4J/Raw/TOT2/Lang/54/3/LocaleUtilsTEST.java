import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en_GB");
        Locale locale2 = LocaleUtils.toLocale("en");

        Assert.assertEquals("en", locale2.getLanguage());
        Assert.assertEquals("GB", locale1.getCountry());
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        
        Assert.assertEquals(3, localeList.size());
        Assert.assertEquals("en", localeList.get(0).getLanguage());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();

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