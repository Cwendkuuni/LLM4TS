package org.apache.commons.lang;
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
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> locales = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, locales.size());
        Assert.assertEquals(new Locale("fr", "CA", "xxx"), locales.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), locales.get(1));
        Assert.assertEquals(new Locale("fr"), locales.get(2));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        Assert.assertTrue(locales.contains(Locale.ENGLISH));
    }

    @Test
    public void testAvailableLocaleSet() {
        Assert.assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        Assert.assertTrue(languages.contains(new Locale("en", "US")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        Assert.assertTrue(countries.contains(new Locale("en", "US")));
    }
}