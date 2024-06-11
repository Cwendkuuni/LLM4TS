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
        Locale expectedLocale = new Locale("en", "GB");
        Locale actualLocale = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(expectedLocale, actualLocale);

        // Edge case testing
        // Test for empty string
        Assert.assertNull(LocaleUtils.toLocale(""));

        // Test for invalid length
        Assert.assertNull(LocaleUtils.toLocale("en_GB_XXX"));

        // Test for invalid separator
        Assert.assertNull(LocaleUtils.toLocale("en_GB-XXX"));

        // Test for invalid language or country code
        Assert.assertNull(LocaleUtils.toLocale("en_XX"));
        Assert.assertNull(LocaleUtils.toLocale("XX_GB"));
        Assert.assertNull(LocaleUtils.toLocale("en_XX_XX"));

        // Test for null input
        Assert.assertNull(LocaleUtils.toLocale(null));

        // Exception handling testing
        try {
            LocaleUtils.toLocale(null);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test for invalid format
        try {
            LocaleUtils.toLocale("en_GB_XXX");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test for invalid language code
        try {
            LocaleUtils.toLocale("XX_GB");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test for invalid country code
        try {
            LocaleUtils.toLocale("en_XX");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale expectedLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocaleList = LocaleUtils.localeLookupList(expectedLocale);
        Assert.assertEquals(Arrays.asList(expectedLocale, new Locale("fr", "CA"), new Locale("fr")), expectedLocaleList);

        // Edge case testing
        // Test for empty list
        Locale emptyLocale = null;
        List<Locale> emptyLocaleList = LocaleUtils.localeLookupList(emptyLocale);
        Assert.assertEquals(Collections.emptyList(), emptyLocaleList);

        // Test for null input
        List<Locale> nullLocaleList = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(Collections.emptyList(), nullLocaleList);

        // Test for locale with empty variant
        Locale emptyVariantLocale = new Locale("fr", "CA");
        List<Locale> emptyVariantLocaleList = LocaleUtils.localeLookupList(emptyVariantLocale);
        Assert.assertEquals(Arrays.asList(emptyVariantLocale, new Locale("fr")), emptyVariantLocaleList);

        // Test for locale with non-empty variant
        Locale nonEmptyVariantLocale = new Locale("fr", "CA", "xxx");
        List<Locale> nonEmptyVariantLocaleList = LocaleUtils.localeLookupList(nonEmptyVariantLocale);
        Assert.assertEquals(Arrays.asList(nonEmptyVariantLocale, new Locale("fr", "CA"), new Locale("fr")), nonEmptyVariantLocaleList);

        // Test for default locale
        Locale defaultLocale = new Locale("en");
        List<Locale> defaultLocaleList = LocaleUtils.localeLookupList(defaultLocale);
        Assert.assertEquals(Collections.singletonList(defaultLocale), defaultLocaleList);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> expectedAvailableLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualAvailableLocaleList = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expectedAvailableLocaleList, actualAvailableLocaleList);

        // Edge case testing
        // Test for empty list
        List<Locale> emptyAvailableLocaleList = Collections.emptyList();
        Assert.assertEquals(emptyAvailableLocaleList, LocaleUtils.availableLocaleList());

        // Test for null input
        Assert.assertNotNull(LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic functionality test
        Set<Locale> expectedAvailableLocaleSet = LocaleUtils.availableLocaleSet();
        Set<Locale> actualAvailableLocaleSet = new HashSet<>(Locale.getAvailableLocales());
        Assert.assertEquals(expectedAvailableLocaleSet, actualAvailableLocaleSet);

        // Edge case testing
        // Test for empty set
        Set<Locale> emptyAvailableLocaleSet = Collections.emptySet();
        Assert.assertEquals(emptyAvailableLocaleSet, LocaleUtils.availableLocaleSet());

        // Test for null input
        Assert.assertNotNull(LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA", "xxx");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Edge case testing
        // Test for null input
        Assert.assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test for invalid locale
        Locale invalidLocale = new Locale("XX", "XX");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        String countryCode = "CA";
        List<Locale> expectedLanguagesByCountryList = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> actualLanguagesByCountryList = Arrays.asList(new Locale("fr"), new Locale("en"));
        Assert.assertEquals(expectedLanguagesByCountryList, actualLanguagesByCountryList);

        // Edge case testing
        // Test for null input
        String nullCountryCode = null;
        List<Locale> nullCountryCodeList = LocaleUtils.languagesByCountry(nullCountryCode);
        Assert.assertEquals(Collections.emptyList(), nullCountryCodeList);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        String languageCode = "fr";
        List<Locale> expectedCountriesByLanguageList = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> actualCountriesByLanguageList = Arrays.asList(new Locale("CA"));
        Assert.assertEquals(expectedCountriesByLanguageList, actualCountriesByLanguageList);

        // Edge case testing
        // Test for null input
        String nullLanguageCode = null;
        List<Locale> nullLanguageCodeList = LocaleUtils.countriesByLanguage(nullLanguageCode);
        Assert.assertEquals(Collections.emptyList(), nullLanguageCodeList);
    }
}