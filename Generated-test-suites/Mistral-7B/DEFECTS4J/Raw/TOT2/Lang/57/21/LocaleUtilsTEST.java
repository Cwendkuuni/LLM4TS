import org.apache.commons.lang.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality
        Assert.assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        Assert.assertEquals(new Locale("fr"), LocaleUtils.toLocale("fr"));

        // Edge cases and exception handling
        try {
            LocaleUtils.toLocale(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_GB_xxx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_xxx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_GB_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_GB_x");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_GB_xxxx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_GB_xxxx_x");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid language and country codes
        try {
            LocaleUtils.toLocale("xx_XX");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_XX");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_XX_YY");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_XX_YY_ZZ");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test empty strings
        try {
            LocaleUtils.toLocale("");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("_");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("_XX");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("XX_");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("_XX_");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("XX__");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("__");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("en", "US"));
        Assert.assertEquals(3, localeList.size());
        Assert.assertEquals(new Locale("en", "US"), localeList.get(0));
        Assert.assertEquals(new Locale("en", "US"), localeList.get(1));
        Assert.assertEquals(new Locale("en"), localeList.get(2));

        // Edge cases and exception handling
        List<Locale> localeListEmpty = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(0, localeListEmpty.size());

        List<Locale> localeListDefault = LocaleUtils.localeLookupList(null, new Locale("en", "US"));
        Assert.assertEquals(2, localeListDefault.size());
        Assert.assertEquals(new Locale("en", "US"), localeListDefault.get(0));
        Assert.assertEquals(new Locale("en"), localeListDefault.get(1));

        List<Locale> localeListInvalid = LocaleUtils.localeLookupList(new Locale("en", "XX"));
        Assert.assertEquals(1, localeListInvalid.size());
        Assert.assertEquals(new Locale("en"), localeListInvalid.get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        Assert.assertEquals(173, localeList.size());
        Assert.assertTrue(localeList.contains(new Locale("en", "US")));
        Assert.assertTrue(localeList.contains(new Locale("fr", "FR")));

        // Test empty list
        List<Locale> localeListEmpty = LocaleUtils.availableLocaleList();
        Assert.assertEquals(0, localeListEmpty.size());
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality
        Assert.assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        Assert.assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "FR")));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "ES")));

        // Test null input
        Assert.assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality
        List<Locale> frenchLocales = LocaleUtils.languagesByCountry("FR");
        Assert.assertEquals(1, frenchLocales.size());
        Assert.assertEquals(new Locale("fr"), frenchLocales.get(0));

        List<Locale> spanishLocales = LocaleUtils.languagesByCountry("ES");
        Assert.assertEquals(1, spanishLocales.size());
        Assert.assertEquals(new Locale("es"), spanishLocales.get(0));

        List<Locale> englishLocales = LocaleUtils.languagesByCountry("EN");
        Assert.assertEquals(5, englishLocales.size());
        Assert.assertTrue(englishLocales.contains(new Locale("en", "US")));
        Assert.assertTrue(englishLocales.contains(new Locale("en", "CA")));
        Assert.assertTrue(englishLocales.contains(new Locale("en", "GB")));
        Assert.assertTrue(englishLocales.contains(new Locale("en", "AU")));
        Assert.assertTrue(englishLocales.contains(new Locale("en", "IE")));

        // Test empty list
        List<Locale> englishEmptyLocales = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(0, englishEmptyLocales.size());

        // Test invalid country code
        List<Locale> invalidCountryCodeLocales = LocaleUtils.languagesByCountry("XX");
        Assert.assertEquals(0, invalidCountryCodeLocales.size());
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality
        List<Locale> englishLocales = LocaleUtils.languagesByCountry("EN");
        List<Locale> usLocales = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(1, usLocales.size());
        Assert.assertEquals(new Locale("US"), usLocales.get(0));

        List<Locale> frenchLocales = LocaleUtils.languagesByCountry("FR");
        List<Locale> frLocales = LocaleUtils.countriesByLanguage("fr");
        Assert.assertEquals(1, frLocales.size());
        Assert.assertEquals(new Locale("FR"), frLocales.get(0));

        List<Locale> spanishLocales = LocaleUtils.languagesByCountry("ES");
        List<Locale> esLocales = LocaleUtils.countriesByLanguage("es");
        Assert.assertEquals(1, esLocales.size());
        Assert.assertEquals(new Locale("ES"), esLocales.get(0));

        List<Locale> dutchLocales = LocaleUtils.languagesByCountry("NL");
        List<Locale> nlLocales = LocaleUtils.countriesByLanguage("nl");
        Assert.assertEquals(1, nlLocales.size());
        Assert.assertEquals(new Locale("NL"), nlLocales.get(0));

        // Test empty list
        List<Locale> englishEmptyLocales = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(0, englishEmptyLocales.size());

        // Test invalid language code
        List<Locale> invalidLanguageCodeLocales = LocaleUtils.countriesByLanguage("XX");
        Assert.assertEquals(0, invalidLanguageCodeLocales.size());
    }
}