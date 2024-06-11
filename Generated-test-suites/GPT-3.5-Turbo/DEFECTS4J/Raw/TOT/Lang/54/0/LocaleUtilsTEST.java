import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_normalCase() {
        Locale locale = LocaleUtils.toLocale("en");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("", locale.getCountry());
    }

    @Test
    public void testToLocale_countrySpecified() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("GB", locale.getCountry());
    }

    @Test
    public void testToLocale_variantSpecified() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals("en", locale.getLanguage());
        Assert.assertEquals("GB", locale.getCountry());
        Assert.assertEquals("xxx", locale.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat_shortString() {
        LocaleUtils.toLocale("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat_wrongCase() {
        LocaleUtils.toLocale("EN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat_wrongSeparator() {
        LocaleUtils.toLocale("en-GB");
    }

    @Test
    public void testLocaleLookupList_singleLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);

        Assert.assertEquals(3, list.size());
        Assert.assertEquals(locale, list.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list.get(1));
        Assert.assertEquals(new Locale("fr"), list.get(2));
    }

    @Test
    public void testLocaleLookupList_withDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);

        Assert.assertEquals(4, list.size());
        Assert.assertEquals(locale, list.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), list.get(1));
        Assert.assertEquals(new Locale("fr"), list.get(2));
        Assert.assertEquals(defaultLocale, list.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        Assert.assertTrue(list.contains(Locale.ENGLISH));
        Assert.assertTrue(list.contains(Locale.FRENCH));
        // Additional checks...
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        Assert.assertTrue(set.contains(Locale.ENGLISH));
        Assert.assertTrue(set.contains(Locale.FRENCH));
        // Additional checks...
    }

    @Test
    public void testIsAvailableLocale_trueCase() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
    }

    @Test
    public void testIsAvailableLocale_falseCase() {
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry_validCountryCode() {
        List<Locale> langs = LocaleUtils.languagesByCountry("CA");
        Assert.assertTrue(langs.contains(new Locale("en", "CA")));
        Assert.assertTrue(langs.contains(new Locale("fr", "CA")));
        // Additional checks...
    }

    @Test
    public void testLanguagesByCountry_invalidCountryCode() {
        List<Locale> langs = LocaleUtils.languagesByCountry("xx");
        Assert.assertTrue(langs.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_validLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        Assert.assertTrue(countries.contains(new Locale("fr", "CA")));
        Assert.assertTrue(countries.contains(new Locale("fr", "FR")));
        // Additional checks...
    }

    @Test
    public void testCountriesByLanguage_invalidLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("xx");
        Assert.assertTrue(countries.isEmpty());
    }

}