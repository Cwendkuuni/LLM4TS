
import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String input = "en";
        Locale expected = new Locale("en", "");
        Locale result = LocaleUtils.toLocale(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToLocaleWithCountry() {
        String input = "en_GB";
        Locale expected = new Locale("en", "GB");
        Locale result = LocaleUtils.toLocale(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToLocaleWithCountryAndVariant() {
        String input = "en_GB_xxx";
        Locale expected = new Locale("en", "GB", "xxx");
        Locale result = LocaleUtils.toLocale(input);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidFormat() {
        String input = "en_GB_x";
        LocaleUtils.toLocale(input);
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = List.of(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = List.of(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList() {
        int expectedSize = Locale.getAvailableLocales().length;
        int resultSize = LocaleUtils.availableLocaleList().size();
        Assert.assertEquals(expectedSize, resultSize);
    }

    @Test
    public void testAvailableLocaleSet() {
        int expectedSize = Locale.getAvailableLocales().length;
        int resultSize = LocaleUtils.availableLocaleSet().size();
        Assert.assertEquals(expectedSize, resultSize);
    }

    @Test
    public void testIsAvailableLocaleWithAvailableLocale() {
        Locale locale = new Locale("en");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAvailableLocaleWithUnavailableLocale() {
        Locale locale = new Locale("zz");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "US";
        List<Locale> expected = List.of(new Locale("en", "US"));
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLanguagesByCountryWithInvalidCountryCode() {
        String countryCode = "ZZ";
        List<Locale> expected = List.of();
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> expected = List.of(new Locale("en", "US"), new Locale("en", "CA"));
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguageWithInvalidLanguageCode() {
        String languageCode = "zz";
        List<Locale> expected = List.of();
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expected, result);
    }

}
