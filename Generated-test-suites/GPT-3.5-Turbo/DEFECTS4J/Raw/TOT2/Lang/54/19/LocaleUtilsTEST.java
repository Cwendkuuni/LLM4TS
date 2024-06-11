import org.junit.Test;
import org.junit.Assert;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_ValidInput() {
        Assert.assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        Assert.assertEquals(expectedList, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Assert.assertTrue(availableLocales.contains(new Locale("en")));
        Assert.assertTrue(availableLocales.contains(new Locale("fr", "CA", "xxx")));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        Assert.assertTrue(availableLocales.contains(new Locale("en")));
        Assert.assertTrue(availableLocales.contains(new Locale("fr", "CA", "xxx")));
    }

    @Test
    public void testIsAvailableLocale() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesForCountry = LocaleUtils.languagesByCountry("CA");
        Assert.assertTrue(languagesForCountry.contains(new Locale("en", "CA")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesForLanguage = LocaleUtils.countriesByLanguage("en");
        Assert.assertTrue(countriesForLanguage.contains(new Locale("en", "CA")));
    }
}