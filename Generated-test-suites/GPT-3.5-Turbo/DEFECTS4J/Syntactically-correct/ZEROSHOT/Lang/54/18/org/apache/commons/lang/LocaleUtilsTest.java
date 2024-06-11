import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        Assert.assertNotNull(locale1);
        Assert.assertEquals("en", locale1.getLanguage());
        Assert.assertEquals("", locale1.getCountry());
        Assert.assertEquals("", locale1.getVariant());

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Assert.assertNotNull(locale2);
        Assert.assertEquals("en", locale2.getLanguage());
        Assert.assertEquals("GB", locale2.getCountry());
        Assert.assertEquals("", locale2.getVariant());

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertNotNull(locale3);
        Assert.assertEquals("en", locale3.getLanguage());
        Assert.assertEquals("GB", locale3.getCountry());
        Assert.assertEquals("xxx", locale3.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("abc");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> locales = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, locales.size());
        Assert.assertEquals(locale, locales.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), locales.get(1));
        Assert.assertEquals(new Locale("fr"), locales.get(2));

        List<Locale> localesWithDefault = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(4, localesWithDefault.size());
        Assert.assertEquals(locale, localesWithDefault.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), localesWithDefault.get(1));
        Assert.assertEquals(new Locale("fr"), localesWithDefault.get(2));
        Assert.assertEquals(defaultLocale, localesWithDefault.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(locales);
        Assert.assertFalse(locales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(locales);
        Assert.assertFalse(locales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale1));

        Locale locale2 = new Locale("abc");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode1 = "US";
        List<Locale> locales1 = LocaleUtils.languagesByCountry(countryCode1);
        Assert.assertNotNull(locales1);
        Assert.assertFalse(locales1.isEmpty());

        String countryCode2 = "";
        List<Locale> locales2 = LocaleUtils.languagesByCountry(countryCode2);
        Assert.assertNotNull(locales2);
        Assert.assertTrue(locales2.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode1 = "en";
        List<Locale> locales1 = LocaleUtils.countriesByLanguage(languageCode1);
        Assert.assertNotNull(locales1);
        Assert.assertFalse(locales1.isEmpty());

        String languageCode2 = "abc";
        List<Locale> locales2 = LocaleUtils.countriesByLanguage(languageCode2);
        Assert.assertNotNull(locales2);
        Assert.assertTrue(locales2.isEmpty());
    }

}