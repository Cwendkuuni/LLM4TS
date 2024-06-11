import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.lang.LocaleUtils;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("GB", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList() {
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
        Assert.assertNotNull(localeList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(localeSet);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = Locale.ENGLISH;
        boolean isAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(isAvailable);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("GB");
        Assert.assertNotNull(languages);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        Assert.assertNotNull(countries);
    }

}