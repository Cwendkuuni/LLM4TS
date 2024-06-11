import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Assert.assertNull(LocaleUtils.toLocale(null));
        Assert.assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        Assert.assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        Assert.assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidFormat() {
        LocaleUtils.toLocale("invalid_locale_format");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.ENGLISH;
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(result.get(0), locale);
        Assert.assertEquals(result.get(1), new Locale("fr", "CA"));
        Assert.assertEquals(result.get(2), new Locale("fr"));
        Assert.assertEquals(result.get(3), defaultLocale);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(result);
    }

    @Test
    public void testIsAvailableLocale() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(result.get(0), new Locale("en", "CA"));
        Assert.assertEquals(result.get(1), new Locale("fr", "CA"));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("fr");
        Assert.assertEquals(result.get(0), new Locale("fr", "CA"));
        Assert.assertEquals(result.get(1), new Locale("fr", "CH"));
        Assert.assertEquals(result.get(2), new Locale("fr", "FR"));
    }
}