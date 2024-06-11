import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleValidInput() {
        Locale expected = new Locale("en", "GB", "xxx");
        Locale actual = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        LocaleUtils.toLocale("invalid_input");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("en")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(locales);
        Assert.assertFalse(locales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(localeSet);
        Assert.assertFalse(localeSet.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Collections.singletonList(new Locale("en", "US"));
        List<Locale> actual = LocaleUtils.languagesByCountry("US");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Collections.singletonList(new Locale("en", "US"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(expected, actual);
    }
}