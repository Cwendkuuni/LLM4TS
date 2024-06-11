import org.apache.commons.lang.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic usage
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        Assert.assertEquals(expected, actual);

        // Edge cases with empty or null inputs
        Assert.assertNull(LocaleUtils.toLocale(null));
        Assert.assertNull(LocaleUtils.toLocale(""));

        // Edge cases with invalid input
        try {
            LocaleUtils.toLocale("xy_ZW");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: xy_ZW", e.getMessage());
        }

        try {
            LocaleUtils.toLocale("en_");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: en_", e.getMessage());
        }

        try {
            LocaleUtils.toLocale("en_US_");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: en_US_", e.getMessage());
        }

        try {
            LocaleUtils.toLocale("en_US_xxx");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: en_US_xxx", e.getMessage());
        }

        try {
            LocaleUtils.toLocale("en_US_xxx_yy");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid locale format: en_US_xxx_yy", e.getMessage());
        }

        // Test localeLookupList
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        Assert.assertArrayEquals(expectedList.toArray(), localeList.toArray());

        // Test isAvailableLocale
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xy", "ZW")));

        // Test languagesByCountry
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        List<Locale> expected = Arrays.asList(Locale.US, new Locale("en", "US"), new Locale("en"));
        Assert.assertArrayEquals(expected.toArray(), languages.toArray());

        // Test countriesByLanguage
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedCountries = Arrays.asList(new Locale("EN", "GB"), new Locale("EN", "US"), new Locale("EN"));
        Assert.assertArrayEquals(expectedCountries.toArray(), countries.toArray());
    }

    @Test
    public void testOtherMethods() {
        // Test other methods, if any
        // ...
    }

    @Test
    public void testExceptionHandling() {
        try {
            LocaleUtils.toLocale(null);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("xy_ZW");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPerformance() {
        // Test performance, if needed
        // ...
    }

    @Test
    public void testThreadSafety() {
        // Test thread safety, if needed
        // ...
    }
}