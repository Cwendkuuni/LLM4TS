import org.junit.Test;
import org.junit.Assert;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_basic() {
        Locale locale = LocaleUtils.toLocale("en");
        Assert.assertEquals(Locale.ENGLISH, locale);
    }

    @Test
    public void testToLocale_variant() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(new Locale("en", "GB", "xxx"), locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("xyz"); // Invalid format should throw IllegalArgumentException
    }

    @Test
    public void testLocaleLookupList_specificLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            locale
        ), list);
    }

    @Test
    public void testLocaleLookupList_defaultLocale() {
        Locale locale = new Locale("en");
        Locale defaultLocale = new Locale("de");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(Arrays.asList(
            new Locale("en"),
            new Locale("de")
        ), list);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(list);
        Assert.assertTrue(list.contains(Locale.ENGLISH));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(set);
        Assert.assertTrue(set.contains(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xyz")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("CA");
        Assert.assertNotNull(list);
        Assert.assertTrue(list.contains(new Locale("en", "CA")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        Assert.assertNotNull(list);
        Assert.assertTrue(list.contains(new Locale("en", "US")));
    }
}