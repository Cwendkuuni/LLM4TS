import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case
        String str1 = "en";
        Locale expected1 = new Locale("en", "");
        Locale result1 = LocaleUtils.toLocale(str1);
        Assert.assertEquals(expected1, result1);

        // Edge case
        String str2 = "en_GB";
        Locale expected2 = new Locale("en", "GB");
        Locale result2 = LocaleUtils.toLocale(str2);
        Assert.assertEquals(expected2, result2);

        // Error scenario
        String str3 = "en_US_xxx";
        try {
            LocaleUtils.toLocale(str3);
            Assert.fail("IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale defaultLocale1 = new Locale("en");
        List<Locale> expected1 = List.of(
                new Locale("fr","CA","xxx"),
                new Locale("fr","CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> result1 = LocaleUtils.localeLookupList(locale1, defaultLocale1);
        Assert.assertEquals(expected1, result1);

        // Edge case
        Locale locale2 = new Locale("en");
        Locale defaultLocale2 = new Locale("en");
        List<Locale> expected2 = List.of(
                new Locale("en")
        );
        List<Locale> result2 = LocaleUtils.localeLookupList(locale2, defaultLocale2);
        Assert.assertEquals(expected2, result2);

        // Error scenario
        Locale locale3 = null;
        Locale defaultLocale3 = new Locale("en");
        List<Locale> expected3 = List.of(
                new Locale("en")
        );
        List<Locale> result3 = LocaleUtils.localeLookupList(locale3, defaultLocale3);
        Assert.assertEquals(expected3, result3);
    }

    @Test
    public void testAvailableLocaleList() {
        // Typical use case
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Typical use case
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale() {
        // Typical use case
        Locale locale1 = new Locale("fr", "CA", "xxx");
        boolean result1 = LocaleUtils.isAvailableLocale(locale1);
        Assert.assertTrue(result1);

        // Edge case
        Locale locale2 = new Locale("xyz");
        boolean result2 = LocaleUtils.isAvailableLocale(locale2);
        Assert.assertFalse(result2);
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case
        String countryCode1 = "CA";
        List<Locale> expected1 = List.of(
                new Locale("en","CA"),
                new Locale("fr","CA")
        );
        List<Locale> result1 = LocaleUtils.languagesByCountry(countryCode1);
        Assert.assertEquals(expected1, result1);

        // Edge case
        String countryCode2 = "US";
        List<Locale> expected2 = List.of();
        List<Locale> result2 = LocaleUtils.languagesByCountry(countryCode2);
        Assert.assertEquals(expected2, result2);

        // Error scenario
        String countryCode3 = null;
        List<Locale> expected3 = List.of();
        List<Locale> result3 = LocaleUtils.languagesByCountry(countryCode3);
        Assert.assertEquals(expected3, result3);
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case
        String languageCode1 = "fr";
        List<Locale> expected1 = List.of(
                new Locale("fr","CA"),
                new Locale("fr","BE"),
                new Locale("fr","CH")
        );
        List<Locale> result1 = LocaleUtils.countriesByLanguage(languageCode1);
        Assert.assertEquals(expected1, result1);

        // Edge case
        String languageCode2 = "es";
        List<Locale> expected2 = List.of();
        List<Locale> result2 = LocaleUtils.countriesByLanguage(languageCode2);
        Assert.assertEquals(expected2, result2);

        // Error scenario
        String languageCode3 = null;
        List<Locale> expected3 = List.of();
        List<Locale> result3 = LocaleUtils.countriesByLanguage(languageCode3);
        Assert.assertEquals(expected3, result3);
    }
}