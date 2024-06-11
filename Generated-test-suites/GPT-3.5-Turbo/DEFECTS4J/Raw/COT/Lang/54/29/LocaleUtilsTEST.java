import org.junit.Assert;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        String str = null;
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertNull(result);
    }

    @Test
    public void testToLocale_InvalidFormat() {
        String str = "abc";
        try {
            LocaleUtils.toLocale(str);
            Assert.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testToLocale_LanguageOnly() {
        String str = "en";
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("", result.getCountry());
        Assert.assertEquals("", result.getVariant());
    }

    @Test
    public void testToLocale_LanguageAndCountry() {
        String str = "en_GB";
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("GB", result.getCountry());
        Assert.assertEquals("", result.getVariant());
    }

    @Test
    public void testToLocale_LanguageCountryAndVariant() {
        String str = "en_GB_xxx";
        Locale result = LocaleUtils.toLocale(str);
        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("GB", result.getCountry());
        Assert.assertEquals("xxx", result.getVariant());
    }

    @Test
    public void testLocaleLookupList_LocaleNotNull() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(locale, result.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), result.get(1));
        Assert.assertEquals(new Locale("fr"), result.get(2));
    }

    @Test
    public void testLocaleLookupList_LocaleNull() {
        Locale locale = null;
        Locale defaultLocale = new Locale("en");
        List result = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(defaultLocale, result.get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        List result = LocaleUtils.availableLocaleList();
        // TODO: assert expected locales based on Locale.getAvailableLocales()
    }

    @Test
    public void testAvailableLocaleSet() {
        Set result = LocaleUtils.availableLocaleSet();
        // TODO: assert expected locales based on Locale.getAvailableLocales()
    }

    @Test
    public void testIsAvailableLocale_True() {
        Locale locale = Locale.GERMANY;
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_False() {
        Locale locale = new Locale("xx");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_CountryCodeNull() {
        String countryCode = null;
        List result = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(Collections.EMPTY_LIST, result);
    }

    @Test
    public void testLanguagesByCountry_CountryCodeValid() {
        String countryCode = "CA";
        List result = LocaleUtils.languagesByCountry(countryCode);
        // TODO: assert expected languages based on availableLocales
    }

    @Test
    public void testCountriesByLanguage_LanguageCodeNull() {
        String languageCode = null;
        List result = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(Collections.EMPTY_LIST, result);
    }

    @Test
    public void testCountriesByLanguage_LanguageCodeValid() {
        String languageCode = "fr";
        List result = LocaleUtils.countriesByLanguage(languageCode);
        // TODO: assert expected countries based on availableLocales
    }

}