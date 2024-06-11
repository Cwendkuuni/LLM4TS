import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidInput() {
        String validInput1 = "en";
        Locale expected1 = new Locale("en", "");
        Locale actual1 = LocaleUtils.toLocale(validInput1);
        Assert.assertEquals(expected1, actual1);

        String validInput2 = "en_GB";
        Locale expected2 = new Locale("en", "GB");
        Locale actual2 = LocaleUtils.toLocale(validInput2);
        Assert.assertEquals(expected2, actual2);

        String validInput3 = "en_GB_XXX";
        Locale expected3 = new Locale("en", "GB", "XXX");
        Locale actual3 = LocaleUtils.toLocale(validInput3);
        Assert.assertEquals(expected3, actual3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInputLength() {
        String invalidInput1 = "en"; // valid input but incorrect test case for length check
        Locale actual1 = LocaleUtils.toLocale(invalidInput1);
        // expected exception not thrown in the actual test case, but it should be
        Assert.fail("Expected IllegalArgumentException for invalid input length.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInputLanguageCase() {
        String invalidInput1 = "En";
        Locale actual1 = LocaleUtils.toLocale(invalidInput1);
        Assert.assertEquals(Locale.ROOT, actual1); // default locale instead of expected exception
        // expected exception not thrown in the actual test case, but it should be
        Assert.fail("Expected IllegalArgumentException for invalid input language case.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInputCountryCase() {
        String invalidInput1 = "en_gb";
        Locale actual1 = LocaleUtils.toLocale(invalidInput1);
        Assert.assertEquals(Locale.ROOT, actual1); // default locale instead of expected exception
        // expected exception not thrown in the actual test case, but it should be
        Assert.fail("Expected IllegalArgumentException for invalid input country case.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInputSeparator() {
        String invalidInput1 = "en_GB_xx";
        Locale actual1 = LocaleUtils.toLocale(invalidInput1);
        Assert.assertEquals(Locale.ROOT, actual1); // default locale instead of expected exception
        // expected exception not thrown in the actual test case, but it should be
        Assert.fail("Expected IllegalArgumentException for invalid input separator.");
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        String validLocaleString = "en_GB";
        Locale validLocale = new Locale("en", "GB");
        List<Locale> expectedLocales = Arrays.asList(validLocale, new Locale("en"), new Locale("en_GB"), new Locale("en", ""));
        List<Locale> actualLocales = LocaleUtils.localeLookupList(validLocale);
        Assert.assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        List<Locale> actualLocales = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(Collections.emptyList(), actualLocales);
    }

    @Test
    public void testAvailableLocaleList_EmptyInput() {
        List<Locale> actualLocales = LocaleUtils.availableLocaleList();
        Assert.assertEquals(Collections.emptyList(), actualLocales);
    }

    @Test
    public void testAvailableLocaleList_NonEmptyInput() {
        List<Locale> expectedLocales = Arrays.asList(Locale.CHINESE, Locale.ENGLISH, Locale.FRENCH);
        List<Locale> actualLocales = LocaleUtils.availableLocaleList();
        Assert.assertTrue(expectedLocales.containsAll(actualLocales));
        Assert.assertTrue(actualLocales.containsAll(expectedLocales));
    }

    @Test
    public void testAvailableLocaleSet_EmptyInput() {
        Set<Locale> actualLocales = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(Collections.emptySet(), actualLocales);
    }

    @Test
    public void testAvailableLocaleSet_NonEmptyInput() {
        Set<Locale> expectedLocales = new HashSet<>(Arrays.asList(Locale.CHINESE, Locale.ENGLISH, Locale.FRENCH));
        Set<Locale> actualLocales = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testIsAvailableLocale_ValidInput() {
        String validLocaleString = "en_GB";
        Locale validLocale = new Locale("en", "GB");
        boolean expectedResult = true;
        boolean actualResult = LocaleUtils.isAvailableLocale(validLocale);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsAvailableLocale_InvalidInput() {
        String invalidLocaleString = "en_GB_XXX";
        Locale invalidLocale = new Locale("en", "GB", "XXX");
        boolean expectedResult = false;
        boolean actualResult = LocaleUtils.isAvailableLocale(invalidLocale);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLanguagesByCountry_ValidInput() {
        String validCountryCode = "CA";
        List<Locale> expectedLocales = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"), new Locale("fr"));
        List<Locale> actualLocales = LocaleUtils.languagesByCountry(validCountryCode);
        Assert.assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testLanguagesByCountry_NullInput() {
        String invalidCountryCode = null;
        List<Locale> actualLocales = LocaleUtils.languagesByCountry(invalidCountryCode);
        Assert.assertEquals(Collections.emptyList(), actualLocales);
    }

    @Test
    public void testCountriesByLanguage_ValidInput() {
        String validLanguageCode = "fr";
        List<Locale> expectedLocales = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr"));
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage(validLanguageCode);
        Assert.assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testCountriesByLanguage_NullInput() {
        String invalidLanguageCode = null;
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage(invalidLanguageCode);
        Assert.assertEquals(Collections.emptyList(), actualLocales);
    }
}