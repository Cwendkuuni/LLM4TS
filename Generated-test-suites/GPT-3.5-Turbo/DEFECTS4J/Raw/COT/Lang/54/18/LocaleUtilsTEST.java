import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String str = "en";
        Locale expected = new Locale("en", "");
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToLocaleWithCountry() {
        String str = "en_GB";
        Locale expected = new Locale("en", "GB");
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToLocaleWithCountryAndVariant() {
        String str = "en_GB_xxx";
        Locale expected = new Locale("en", "GB", "xxx");
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidFormat() {
        String str = "invalid_locale_format";
        LocaleUtils.toLocale(str);
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
        List<Locale> expected = List.of(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(expected, result);
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
    public void testIsAvailableLocaleWithAvailableLocale() {
        Locale locale = new Locale("en");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAvailableLocaleWithUnavailableLocale() {
        Locale locale = new Locale("xx");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "GB";
        List<Locale> expected = List.of(new Locale("en", "GB"), new Locale("cy", "GB"));
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLanguagesByCountryWithInvalidCountryCode() {
        String countryCode = "XX";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> expected = List.of(new Locale("en", "AU"), new Locale("en", "BZ"), new Locale("en", "CA"),
                new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "JM"), new Locale("en", "NZ"),
                new Locale("en", "PH"), new Locale("en", "TT"), new Locale("en", "US"), new Locale("en", "ZA"));
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguageWithInvalidLanguageCode() {
        String languageCode = "xx";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

}