import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_nullInput() {
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_validLocaleFormat() {
        Locale expected = new Locale("en", "GB", "xxx");
        Locale actual = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLocaleFormat() {
        LocaleUtils.toLocale("invalidLocaleFormat");
    }

    @Test
    public void testLocaleLookupList_localeNotNull() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                Locale.getDefault()
        );
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_localeNull() {
        List<Locale> expected = Arrays.asList(Locale.getDefault());
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_defaultLocale() {
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), defaultLocale);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actual = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actualList = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expectedList, actualList);

        List<Locale> expectedSet = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actualSet = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testIsAvailableLocale_true() {
        Locale locale = new Locale("en");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_false() {
        Locale locale = new Locale("invalid");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_validCountryCode() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actual = LocaleUtils.languagesByCountry("US");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry_nullCountryCode() {
        List<Locale> expected = Arrays.asList();
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage_validLanguageCode() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage_nullLanguageCode() {
        List<Locale> expected = Arrays.asList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(expected, actual);
    }
}