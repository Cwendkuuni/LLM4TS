import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        Assert.assertEquals(new Locale("en", ""), locale);

        locale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(new Locale("en", "GB"), locale);

        locale = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(new Locale("en", "GB", "xxx"), locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("12345");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLocale() {
        LocaleUtils.toLocale("en1");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("en")
        );

        List<Locale> actualList = LocaleUtils.localeLookupList(locale, new Locale("en"));

        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(localeList);
        // Add more assertions if necessary
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(localeSet);
        // Add more assertions if necessary
    }

    @Test
    public void testIsAvailableLocale() {
        boolean isAvailable1 = LocaleUtils.isAvailableLocale(Locale.US);
        boolean isAvailable2 = LocaleUtils.isAvailableLocale(new Locale("fr", "CA"));

        Assert.assertTrue(isAvailable1);
        Assert.assertFalse(isAvailable2);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("CA");
        Assert.assertNotNull(langs);
        // Add more assertions if necessary
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        Assert.assertNotNull(countries);
        // Add more assertions if necessary
    }

}