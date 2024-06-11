import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        Assert.assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        Assert.assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Edge cases and exception handling tests
        try {
            LocaleUtils.toLocale(null);
            Assert.fail("Expected IllegalArgumentException when input is null");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("");
            Assert.fail("Expected IllegalArgumentException when input is empty string");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("  ");
            Assert.fail("Expected IllegalArgumentException when input contains only whitespaces");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("fr_CA_XXX");
            Assert.fail("Expected IllegalArgumentException when input contains invalid character '_' in the wrong position");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("fr_CA_XXX_XXX");
            Assert.fail("Expected IllegalArgumentException when input length is greater than 5");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("fr_CA_XXX_XXX_XXX");
            Assert.fail("Expected IllegalArgumentException when input length is greater than 5");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("fr_CA_en");
            Assert.fail("Expected IllegalArgumentException when language code is not lowercase");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_FR");
            Assert.fail("Expected IllegalArgumentException when country code is not uppercase");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_FR_XXX");
            Assert.fail("Expected IllegalArgumentException when input contains invalid character '_' in the wrong position");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_FR_XXX_XXX");
            Assert.fail("Expected IllegalArgumentException when input length is greater than 5");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_FR_XXX_XXX_XXX");
            Assert.fail("Expected IllegalArgumentException when input length is greater than 5");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_FR_XXX_XXX_XXX_XXX");
            Assert.fail("Expected IllegalArgumentException when input length is greater than 5");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedLocaleLookupList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        Assert.assertEquals(expectedLocaleLookupList, localeLookupList);

        // Edge cases and exception handling tests
        locale = new Locale("fr");
        localeLookupList = LocaleUtils.localeLookupList(locale, null);
        expectedLocaleLookupList = Arrays.asList(locale, new Locale("fr"), new Locale(""));
        Assert.assertEquals(expectedLocaleLookupList, localeLookupList);

        locale = null;
        try {
            localeLookupList = LocaleUtils.localeLookupList(locale, new Locale("en"));
            Assert.fail("Expected NullPointerException when input is null");
        } catch (NullPointerException e) {
            // Expected
        }

        locale = new Locale("en");
        try {
            localeLookupList = LocaleUtils.localeLookupList(locale, null);
            Assert.fail("Expected NullPointerException when defaultLocale is null");
        } catch (NullPointerException e) {
            // Expected
        }

        locale = new Locale("fr", "CA", "xxx");
        try {
            localeLookupList = LocaleUtils.localeLookupList(locale, new Locale("fr", "CA", "yyy"));
            Assert.fail("Expected IllegalArgumentException when locale and defaultLocale are the same");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> expectedAvailableLocaleList = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expectedAvailableLocaleList, availableLocaleList);

        // Edge cases and exception handling tests
        try {
            LocaleUtils.availableLocaleList();
            Assert.fail("Expected UnsupportedOperationException when the list cannot be modified");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic functionality test
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        Set<Locale> expectedAvailableLocaleSet = new HashSet<>(Locale.getAvailableLocales());
        Assert.assertEquals(expectedAvailableLocaleSet, availableLocaleSet);

        // Edge cases and exception handling tests
        try {
            LocaleUtils.availableLocaleSet();
            Assert.fail("Expected UnsupportedOperationException when the set cannot be modified");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        Locale locale = new Locale("en");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA", "xxx");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("", "");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("", "US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "GB");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "GB", "xxx");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en_US_XXX");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en_US_XXX_XXX");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en_US_XXX_XXX_XXX");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Edge cases and exception handling tests
        locale = new Locale("fr_CA_en");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en_FR");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en_FR_en");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en_FR_en_en");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en_FR_en_en_en");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr_CA_en_en");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en_FR_en_en_en");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en_FR_en_en_en_en");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
        List<Locale> expectedLanguagesByCountry = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        Assert.assertEquals(expectedLanguagesByCountry, languagesByCountry);

        // Edge cases and exception handling tests
        try {
            languagesByCountry = LocaleUtils.languagesByCountry(null);
            Assert.assertNotNull(languagesByCountry);
            Assert.assertEquals(Collections.emptyList(), languagesByCountry);
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            languagesByCountry = LocaleUtils.languagesByCountry("");
            Assert.assertNotNull(languagesByCountry);
            Assert.assertEquals(Collections.emptyList(), languagesByCountry);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            languagesByCountry = LocaleUtils.languagesByCountry("XX");
            Assert.assertNotNull(languagesByCountry);
            Assert.assertEquals(Collections.emptyList(), languagesByCountry);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedCountriesByLanguage = Arrays.asList(new Locale("CA"), new Locale("GB"), new Locale("US"));
        Assert.assertEquals(expectedCountriesByLanguage, countriesByLanguage);

        // Edge cases and exception handling tests
        try {
            countriesByLanguage = LocaleUtils.countriesByLanguage(null);
            Assert.assertNotNull(countriesByLanguage);
            Assert.assertEquals(Collections.emptyList(), countriesByLanguage);
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            countriesByLanguage = LocaleUtils.countriesByLanguage("");
            Assert.assertNotNull(countriesByLanguage);
            Assert.assertEquals(Collections.emptyList(), countriesByLanguage);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            countriesByLanguage = LocaleUtils.countriesByLanguage("XX");
            Assert.assertNotNull(countriesByLanguage);
            Assert.assertEquals(Collections.emptyList(), countriesByLanguage);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}