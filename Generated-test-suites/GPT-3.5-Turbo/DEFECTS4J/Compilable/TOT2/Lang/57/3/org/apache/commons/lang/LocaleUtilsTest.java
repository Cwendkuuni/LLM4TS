package org.apache.commons.lang;
import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validInput() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(new Locale("en", "GB"), locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidInput() {
        LocaleUtils.toLocale("invalid_locale");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, localeList.size());
        Assert.assertEquals(localeList.get(0), locale);
        Assert.assertEquals(new Locale("fr", "CA"), localeList.get(1));
        Assert.assertEquals(new Locale("fr"), localeList.get(2));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(availableLocales);
        Assert.assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Assert.assertNotNull(LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale_true() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale_false() {
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "YY")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        Assert.assertNotNull(languages);
        Assert.assertTrue(languages.size() > 0);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        Assert.assertNotNull(countries);
        Assert.assertTrue(countries.size() > 0);
    }

}