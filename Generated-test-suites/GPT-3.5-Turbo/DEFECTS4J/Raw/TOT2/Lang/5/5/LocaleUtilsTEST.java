import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validInput() {
        Locale expected = new Locale("en", "GB");
        Locale result = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToLocale_invalidInput() {
        try {
            LocaleUtils.toLocale("invalid");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_withDefault() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList() {
        Assert.assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Assert.assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale_true() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale_false() {
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("en", "GB"));
        List<Locale> result = LocaleUtils.languagesByCountry("GB");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("en", "GB"));
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(expected, result);
    }
}