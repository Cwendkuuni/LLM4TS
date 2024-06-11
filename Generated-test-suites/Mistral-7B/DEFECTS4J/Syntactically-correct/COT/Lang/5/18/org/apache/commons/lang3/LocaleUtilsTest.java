import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        Assert.assertEquals(expectedLocale, actualLocale);

        // Edge case test with empty string
        String emptyString = "";
        Locale actualLocaleEmptyString = LocaleUtils.toLocale(emptyString);
        Assert.assertNull(actualLocaleEmptyString);

        // Edge case test with invalid format
        String invalidFormat = "en_invalid";
        try {
            LocaleUtils.toLocale(invalidFormat);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Edge case test with null input
        String nullInput = null;
        Locale actualLocaleNullInput = LocaleUtils.toLocale(nullInput);
        Assert.assertNull(actualLocaleNullInput);
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale, new Locale("en"));
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case test with empty list
        Locale emptyLocale = null;
        List<Locale> actualLocaleListEmpty = LocaleUtils.localeLookupList(emptyLocale, new Locale("en"));
        Assert.assertEquals(Collections.emptyList(), actualLocaleListEmpty);

        // Edge case test with null defaultLocale
        Locale nullDefaultLocale = null;
        try {
            List<Locale> actualLocaleListNullDefaultLocale = LocaleUtils.localeLookupList(baseLocale, nullDefaultLocale);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Exception handling test with duplicate locale in the list
        List<Locale> duplicateLocaleList = new ArrayList<>();
        duplicateLocaleList.add(baseLocale);
        duplicateLocaleList.add(baseLocale);
        try {
            LocaleUtils.localeLookupList(baseLocale, new Locale("en"));
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case test with empty list
        List<Locale> emptyLocaleList = Collections.emptyList();
        Assert.assertEquals(emptyLocaleList, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic functionality test
        Set<Locale> expectedLocaleSet = LocaleUtils.availableLocaleSet();
        Set<Locale> actualLocaleSet = new HashSet<>(Locale.getAvailableLocales());
        Assert.assertEquals(expectedLocaleSet, actualLocaleSet);

        // Edge case test with empty set
        Set<Locale> emptyLocaleSet = Collections.emptySet();
        Assert.assertEquals(emptyLocaleSet, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        Locale availableLocale = new Locale("en", "US");
        boolean expectedResult = true;
        boolean actualResult = LocaleUtils.isAvailableLocale(availableLocale);
        Assert.assertEquals(expectedResult, actualResult);

        // Edge case test with invalid locale
        Locale invalidLocale = new Locale("invalid", "invalid");
        boolean expectedResultInvalid = false;
        boolean actualResultInvalid = LocaleUtils.isAvailableLocale(invalidLocale);
        Assert.assertEquals(expectedResultInvalid, actualResultInvalid);

        // Edge case test with null locale
        Locale nullLocale = null;
        Assert.assertFalse(LocaleUtils.isAvailableLocale(nullLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        String countryCode = "CA";
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("en", "CA"),
                new Locale("es", "ES"),
                new Locale("it", "IT")
        );
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case test with null countryCode
        String nullCountryCode = null;
        List<Locale> actualLocaleListNullCountryCode = LocaleUtils.languagesByCountry(nullCountryCode);
        Assert.assertEquals(Collections.emptyList(), actualLocaleListNullCountryCode);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        String languageCode = "en";
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "GB"),
                new Locale("en", "IE")
        );
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case test with null languageCode
        String nullLanguageCode = null;
        List<Locale> actualLocaleListNullLanguageCode = LocaleUtils.countriesByLanguage(nullLanguageCode);
        Assert.assertEquals(Collections.emptyList(), actualLocaleListNullLanguageCode);
    }
}