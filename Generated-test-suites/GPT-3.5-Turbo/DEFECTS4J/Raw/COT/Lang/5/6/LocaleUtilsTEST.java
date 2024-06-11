import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        Assert.assertEquals(Locale.ENGLISH, locale);

        locale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(Locale.UK, locale);

        locale = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(new Locale("en", "GB", "xxx"), locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("abc"); // invalid format, should throw exception
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, localeList.size());
        Assert.assertEquals(locale, localeList.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), localeList.get(1));
        Assert.assertEquals(new Locale("fr"), localeList.get(2));
    }

    @Test
    public void testLocaleLookupList_withDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.ENGLISH;
        List<Locale> localeList = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(4, localeList.size());
        Assert.assertEquals(locale, localeList.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), localeList.get(1));
        Assert.assertEquals(new Locale("fr"), localeList.get(2));
        Assert.assertEquals(defaultLocale, localeList.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        Assert.assertTrue(localeList.contains(Locale.ENGLISH));
        Assert.assertTrue(localeList.contains(Locale.CHINESE));
        Assert.assertTrue(localeList.contains(Locale.JAPANESE));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        Assert.assertTrue(localeSet.contains(Locale.ENGLISH));
        Assert.assertTrue(localeSet.contains(Locale.CHINESE));
        Assert.assertTrue(localeSet.contains(Locale.JAPANESE));
    }

    @Test
    public void testIsAvailableLocale() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.CHINESE));
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.JAPANESE));

        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("abc"))); // not an available locale
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> localeList = LocaleUtils.languagesByCountry("CA");
        Assert.assertTrue(localeList.contains(new Locale("en", "CA")));
        Assert.assertTrue(localeList.contains(new Locale("fr", "CA")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> localeList = LocaleUtils.countriesByLanguage("fr");
        Assert.assertTrue(localeList.contains(new Locale("fr", "CA")));
        Assert.assertTrue(localeList.contains(new Locale("fr", "FR")));
    }
}