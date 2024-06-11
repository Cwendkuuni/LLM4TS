import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case
        Locale locale1 = LocaleUtils.toLocale("en");
        Assert.assertEquals("en", locale1.getLanguage());
        Assert.assertEquals("", locale1.getCountry());

        // Edge case
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals("en", locale2.getLanguage());
        Assert.assertEquals("GB", locale2.getCountry());

        // Error scenario
        try {
            LocaleUtils.toLocale("invalid");
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> result1 = LocaleUtils.localeLookupList(locale1);
        Assert.assertEquals(3, result1.size());
        Assert.assertEquals(locale1, result1.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), result1.get(1));
        Assert.assertEquals(new Locale("fr"), result1.get(2));

        // Edge case
        Locale locale2 = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> result2 = LocaleUtils.localeLookupList(locale2, defaultLocale);
        Assert.assertEquals(4, result2.size());
        Assert.assertEquals(locale2, result2.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), result2.get(1));
        Assert.assertEquals(new Locale("fr"), result2.get(2));
        Assert.assertEquals(defaultLocale, result2.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(availableLocales);
        Assert.assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(availableLocales);
        Assert.assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale1));

        Locale locale2 = new Locale("invalid");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case
        List<Locale> expected1 = Arrays.asList(
            new Locale("en", "US"),
            new Locale("en", "GB")
        );
        List<Locale> result1 = LocaleUtils.languagesByCountry("US");
        Assert.assertEquals(expected1, result1);

        // Edge case
        List<Locale> expected2 = Arrays.asList(
            new Locale("fr", "CA")
        );
        List<Locale> result2 = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(expected2, result2);

        // Error scenario
        List<Locale> result3 = LocaleUtils.languagesByCountry("invalid");
        Assert.assertNotNull(result3);
        Assert.assertTrue(result3.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case
        List<Locale> expected1 = Arrays.asList(
            new Locale("en", "US"),
            new Locale("en", "GB")
        );
        List<Locale> result1 = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(expected1, result1);

        // Edge case
        List<Locale> expected2 = Arrays.asList(
            new Locale("fr", "CA")
        );
        List<Locale> result2 = LocaleUtils.countriesByLanguage("fr");
        Assert.assertEquals(expected2, result2);

        // Error scenario
        List<Locale> result3 = LocaleUtils.countriesByLanguage("invalid");
        Assert.assertNotNull(result3);
        Assert.assertTrue(result3.isEmpty());
    }

}