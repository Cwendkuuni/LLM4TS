import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validString() {
        Locale locale = LocaleUtils.toLocale("en_US");
        Assert.assertNotNull(locale);
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("US", locale.getCountry());
        Assert.assertEquals("", locale.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("invalid_locale");
    }

    @Test
    public void testToLocale_languageOnly() {
        Locale locale = LocaleUtils.toLocale("fr");
        Assert.assertNotNull(locale);
        Assert.assertEquals("fr", locale.getLanguage());
        Assert.assertEquals("", locale.getCountry());
        Assert.assertEquals("", locale.getVariant());
    }

    @Test
    public void testLocaleLookupList_singleLocale() {
        Locale locale = new Locale("en_US");
        List<Locale> locales = LocaleUtils.localeLookupList(locale);
        Assert.assertNotNull(locales);
        Assert.assertEquals(3, locales.size());
        Assert.assertEquals(locale, locales.get(0));
        Assert.assertEquals(new Locale("en", "US"), locales.get(1));
        Assert.assertEquals(new Locale("en", ""), locales.get(2));
    }

    @Test
    public void testLocaleLookupList_multipleLocales() {
        Locale locale = new Locale("fr_CA");
        Locale defaultLocale = new Locale("en");
        List<Locale> locales = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertNotNull(locales);
        Assert.assertEquals(4, locales.size());
        Assert.assertEquals(locale, locales.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), locales.get(1));
        Assert.assertEquals(new Locale("fr", ""), locales.get(2));
        Assert.assertEquals(defaultLocale, locales.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(locales);
        Assert.assertTrue(locales.contains(new Locale("en", "US")));
        Assert.assertTrue(locales.contains(new Locale("fr", "CA")));
        Assert.assertTrue(locales.contains(new Locale("es", "ES")));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(locales);
        Assert.assertTrue(locales.contains(new Locale("en", "US")));
        Assert.assertTrue(locales.contains(new Locale("fr", "CA")));
        Assert.assertTrue(locales.contains(new Locale("es", "ES")));
    }

    @Test
    public void testIsAvailableLocale_true() {
        Locale locale = new Locale("en", "US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_false() {
        Locale locale = new Locale("de", "DE");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_validCountryCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        Assert.assertNotNull(languages);
        Assert.assertTrue(languages.contains(new Locale("en", "US")));
        Assert.assertTrue(languages.contains(new Locale("es", "US")));
    }

    @Test
    public void testLanguagesByCountry_invalidCountryCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry("XX");
        Assert.assertNotNull(languages);
        Assert.assertEquals(0, languages.size());
    }

    @Test
    public void testCountriesByLanguage_validLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        Assert.assertNotNull(countries);
        Assert.assertTrue(countries.contains(new Locale("fr", "CA")));
        Assert.assertTrue(countries.contains(new Locale("fr", "CH")));
    }

    @Test
    public void testCountriesByLanguage_invalidLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("xx");
        Assert.assertNotNull(countries);
        Assert.assertEquals(0, countries.size());
    }

}