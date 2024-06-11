import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test typical case
        Locale expectedLocale = new Locale("en", "GB", "xxx");
        Locale result = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(expectedLocale, result);

        // Test edge case
        expectedLocale = new Locale("en", "GB");
        result = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(expectedLocale, result);

        // Test error case
        try {
            result = LocaleUtils.toLocale("123");
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test typical case
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(expectedList, result);

        // Test edge case
        locale = new Locale("fr");
        expectedList = Arrays.asList(
                new Locale("fr"),
                new Locale("en")
        );
        result = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(expectedList, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expectedList, result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(expectedSet, result);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "GB");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertTrue(result);

        locale = new Locale("xx");
        result = LocaleUtils.isAvailableLocale(locale);
        Assert.assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry() {
        // Test typical case
        String countryCode = "GB";
        List<Locale> expectedList = Arrays.asList(
                new Locale("en", "GB"),
                new Locale("gd", "GB"),
                new Locale("cy", "GB")
        );
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expectedList, result);

        // Test edge case
        countryCode = "XX";
        expectedList = Collections.emptyList();
        result = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expectedList, result);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test typical case
        String languageCode = "en";
        List<Locale> expectedList = Arrays.asList(
                new Locale("en", "AU"),
                new Locale("en", "CA"),
                new Locale("en", "GB"),
                new Locale("en", "IE"),
                new Locale("en", "IN"),
                new Locale("en", "MT"),
                new Locale("en", "NZ"),
                new Locale("en", "PH"),
                new Locale("en", "SG"),
                new Locale("en", "US"),
                new Locale("en", "ZA")
        );
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expectedList, result);

        // Test edge case
        languageCode = "xx";
        expectedList = Collections.emptyList();
        result = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expectedList, result);
    }
}