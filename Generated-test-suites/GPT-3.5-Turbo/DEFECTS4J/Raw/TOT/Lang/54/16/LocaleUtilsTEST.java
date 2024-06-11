import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    // Test case for toLocale() method
    @Test
    public void testToLocale() {
        // Typical use case
        String str1 = "en";
        Locale expected1 = new Locale("en", "");
        Locale actual1 = LocaleUtils.toLocale(str1);
        Assert.assertEquals(expected1, actual1);

        // Edge case
        String str2 = "en_GB";
        Locale expected2 = new Locale("en", "GB");
        Locale actual2 = LocaleUtils.toLocale(str2);
        Assert.assertEquals(expected2, actual2);

        // Error scenario - invalid format
        String str3 = "en_USA";
        try {
            LocaleUtils.toLocale(str3);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }

        // Error scenario - null input
        String str4 = null;
        Locale expected4 = null;
        Locale actual4 = LocaleUtils.toLocale(str4);
        Assert.assertEquals(expected4, actual4);
    }

    // Test case for localeLookupList() method
    @Test
    public void testLocaleLookupList() {
        // Typical use case
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale defaultLocale1 = new Locale("en");
        List<Locale> expected1 = List.of(locale1, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> actual1 = LocaleUtils.localeLookupList(locale1, defaultLocale1);
        Assert.assertEquals(expected1, actual1);

        // Error scenario - null input
        Locale locale2 = null;
        Locale defaultLocale2 = new Locale("en");
        List<Locale> expected2 = List.of(new Locale("en"));
        List<Locale> actual2 = LocaleUtils.localeLookupList(locale2, defaultLocale2);
        Assert.assertEquals(expected2, actual2);
    }

    // Test case for availableLocaleList() method
    @Test
    public void testAvailableLocaleList() {
        // Typical use case
        int expectedSize = Locale.getAvailableLocales().length;
        int actualSize = LocaleUtils.availableLocaleList().size();
        Assert.assertEquals(expectedSize, actualSize);
    }

    // Test case for availableLocaleSet() method
    @Test
    public void testAvailableLocaleSet() {
        // Typical use case
        int expectedSize = Locale.getAvailableLocales().length;
        int actualSize = LocaleUtils.availableLocaleSet().size();
        Assert.assertEquals(expectedSize, actualSize);
    }

    // Test case for isAvailableLocale() method
    @Test
    public void testIsAvailableLocale() {
        // Typical use case
        Locale locale1 = new Locale("en", "US");
        boolean actual1 = LocaleUtils.isAvailableLocale(locale1);
        Assert.assertEquals(true, actual1);

        // Edge case
        Locale locale2 = new Locale("xyz");
        boolean actual2 = LocaleUtils.isAvailableLocale(locale2);
        Assert.assertEquals(false, actual2);

        // Error scenario - null input
        Locale locale3 = null;
        boolean actual3 = LocaleUtils.isAvailableLocale(locale3);
        Assert.assertEquals(false, actual3);
    }

    // Test case for languagesByCountry() method
    @Test
    public void testLanguagesByCountry() {
        // Typical use case
        String countryCode1 = "US";
        List<Locale> expected1 = List.of(new Locale("en", "US"));
        List<Locale> actual1 = LocaleUtils.languagesByCountry(countryCode1);
        Assert.assertEquals(expected1, actual1);

        // Edge case
        String countryCode2 = "ZZ";
        List<Locale> expected2 = List.of();
        List<Locale> actual2 = LocaleUtils.languagesByCountry(countryCode2);
        Assert.assertEquals(expected2, actual2);

        // Error scenario - null input
        String countryCode3 = null;
        List<Locale> expected3 = List.of();
        List<Locale> actual3 = LocaleUtils.languagesByCountry(countryCode3);
        Assert.assertEquals(expected3, actual3);
    }

    // Test case for countriesByLanguage() method
    @Test
    public void testCountriesByLanguage() {
        // Typical use case
        String languageCode1 = "en";
        List<Locale> expected1 = List.of(new Locale("en", "US"));
        List<Locale> actual1 = LocaleUtils.countriesByLanguage(languageCode1);
        Assert.assertEquals(expected1, actual1);

        // Edge case
        String languageCode2 = "xyz";
        List<Locale> expected2 = List.of();
        List<Locale> actual2 = LocaleUtils.countriesByLanguage(languageCode2);
        Assert.assertEquals(expected2, actual2);

        // Error scenario - null input
        String languageCode3 = null;
        List<Locale> expected3 = List.of();
        List<Locale> actual3 = LocaleUtils.countriesByLanguage(languageCode3);
        Assert.assertEquals(expected3, actual3);
    }
}