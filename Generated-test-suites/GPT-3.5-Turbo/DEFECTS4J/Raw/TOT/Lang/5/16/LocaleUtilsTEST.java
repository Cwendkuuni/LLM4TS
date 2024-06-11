import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");

        Assert.assertEquals("en", locale1.getLanguage());
        Assert.assertEquals("", locale1.getCountry());

        Assert.assertEquals("en", locale2.getLanguage());
        Assert.assertEquals("GB", locale2.getCountry());

        Assert.assertEquals("en", locale3.getLanguage());
        Assert.assertEquals("GB", locale3.getCountry());
        Assert.assertEquals("xxx", locale3.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLanguageCode() {
        Locale locale = LocaleUtils.toLocale("EN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountryCode() {
        Locale locale = LocaleUtils.toLocale("en_gb");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        Locale locale = LocaleUtils.toLocale("en_gb_xxx_");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale);

        Assert.assertEquals(4, result.size());
        Assert.assertEquals(locale, result.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), result.get(1));
        Assert.assertEquals(new Locale("fr"), result.get(2));
        Assert.assertEquals(Locale.getDefault(), result.get(3));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.US;
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);

        Assert.assertEquals(4, result.size());
        Assert.assertEquals(locale, result.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), result.get(1));
        Assert.assertEquals(new Locale("fr"), result.get(2));
        Assert.assertEquals(defaultLocale, result.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();

        Assert.assertFalse(locales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();

        Assert.assertFalse(locales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        boolean result = LocaleUtils.isAvailableLocale(locale);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsNotAvailableLocale() {
        Locale locale = new Locale("xx", "XX");
        boolean result = LocaleUtils.isAvailableLocale(locale);

        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> languages = LocaleUtils.languagesByCountry(countryCode);

        Assert.assertEquals(2, languages.size());
        Assert.assertEquals(new Locale("en", "CA"), languages.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), languages.get(1));
    }

    @Test
    public void testLanguagesByCountryWithInvalidCountryCode() {
        String countryCode = "XX";
        List<Locale> languages = LocaleUtils.languagesByCountry(countryCode);

        Assert.assertTrue(languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> countries = LocaleUtils.countriesByLanguage(languageCode);

        Assert.assertEquals(1, countries.size());
        Assert.assertEquals(new Locale("fr", "CA"), countries.get(0));
    }

    @Test
    public void testCountriesByLanguageWithInvalidLanguageCode() {
        String languageCode = "xx";
        List<Locale> countries = LocaleUtils.countriesByLanguage(languageCode);

        Assert.assertTrue(countries.isEmpty());
    }

}