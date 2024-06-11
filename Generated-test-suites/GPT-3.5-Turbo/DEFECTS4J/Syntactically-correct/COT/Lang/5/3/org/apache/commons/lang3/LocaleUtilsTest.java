import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.Set;

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
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("eng");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(3, lookupList.size());
        Assert.assertEquals(locale, lookupList.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        Assert.assertEquals(new Locale("fr"), lookupList.get(2));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.ENGLISH;
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(4, lookupList.size());
        Assert.assertEquals(locale, lookupList.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        Assert.assertEquals(new Locale("fr"), lookupList.get(2));
        Assert.assertEquals(defaultLocale, lookupList.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(availableLocales);
        Assert.assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(availableLocales);
        Assert.assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(LocaleUtils.toLocale("xxx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("GB");
        Assert.assertEquals(1, languages.size());
        Assert.assertEquals(Locale.ENGLISH, languages.get(0));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(1, countries.size());
        Assert.assertEquals(Locale.UK, countries.get(0));
    }
}