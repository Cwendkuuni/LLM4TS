package org.apache.commons.lang;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleValidInput() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        Assert.assertNotNull(locale);
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("GB", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        LocaleUtils.toLocale("invalid_locale");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(locale, list.get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Assert.assertNotNull(LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("CA");
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        Assert.assertNotNull(list);
        Assert.assertTrue(list.size() > 0);
    }

}