import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        Assert.assertEquals(locale1.getLanguage(), "en");
        Assert.assertEquals(locale1.getCountry(), "");

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(locale2.getLanguage(), "en");
        Assert.assertEquals(locale2.getCountry(), "GB");

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(locale3.getLanguage(), "en");
        Assert.assertEquals(locale3.getCountry(), "GB");
        Assert.assertEquals(locale3.getVariant(), "xxx");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("invalid_locale");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(localeList.size(), 3);
        Assert.assertEquals(localeList.get(0).toString(), "fr_CA_xxx");
        Assert.assertEquals(localeList.get(1).toString(), "fr_CA");
        Assert.assertEquals(localeList.get(2).toString(), "fr");

        List<Locale> localeListWithDefault = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(localeListWithDefault.size(), 4);
        Assert.assertEquals(localeListWithDefault.get(0).toString(), "fr_CA_xxx");
        Assert.assertEquals(localeListWithDefault.get(1).toString(), "fr_CA");
        Assert.assertEquals(localeListWithDefault.get(2).toString(), "fr");
        Assert.assertEquals(localeListWithDefault.get(3).toString(), "en");
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

        Locale locale2 = new Locale("xx");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> languages = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertNotNull(languages);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> countries = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertNotNull(countries);
    }
}