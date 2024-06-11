package org.apache.commons.lang3;
import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Assert.assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLanguageCode() {
        LocaleUtils.toLocale("EN_GB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountryCode() {
        LocaleUtils.toLocale("en_gb");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidSeparator() {
        LocaleUtils.toLocale("en-GB");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Assert.assertEquals(4, LocaleUtils.localeLookupList(locale).size());
        Assert.assertEquals(locale, LocaleUtils.localeLookupList(locale).get(0));
        Assert.assertEquals(new Locale("fr", "CA"), LocaleUtils.localeLookupList(locale).get(1));
        Assert.assertEquals(new Locale("fr"), LocaleUtils.localeLookupList(locale).get(2));
        Assert.assertEquals(Locale.getDefault(), LocaleUtils.localeLookupList(locale).get(3));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        Assert.assertEquals(4, LocaleUtils.localeLookupList(locale, defaultLocale).size());
        Assert.assertEquals(locale, LocaleUtils.localeLookupList(locale, defaultLocale).get(0));
        Assert.assertEquals(new Locale("fr", "CA"), LocaleUtils.localeLookupList(locale, defaultLocale).get(1));
        Assert.assertEquals(new Locale("fr"), LocaleUtils.localeLookupList(locale, defaultLocale).get(2));
        Assert.assertEquals(defaultLocale, LocaleUtils.localeLookupList(locale, defaultLocale).get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        Assert.assertEquals(LocaleUtils.availableLocaleList(), LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        Assert.assertEquals(LocaleUtils.availableLocaleSet(), LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        Assert.assertEquals(0, LocaleUtils.languagesByCountry(null).size());
        Assert.assertEquals(12, LocaleUtils.languagesByCountry("US").size());
    }

    @Test
    public void testCountriesByLanguage() {
        Assert.assertEquals(0, LocaleUtils.countriesByLanguage(null).size());
        Assert.assertEquals(246, LocaleUtils.countriesByLanguage("en").size());
    }
}