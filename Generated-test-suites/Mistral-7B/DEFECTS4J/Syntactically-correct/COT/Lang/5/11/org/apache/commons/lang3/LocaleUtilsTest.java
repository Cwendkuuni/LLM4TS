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
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale("en_US");
        Assert.assertEquals(expectedLocale, actualLocale);

        // Edge case testing
        // Test for empty string
        Assert.assertNull(LocaleUtils.toLocale(""));

        // Test for invalid format
        Assert.assertNull(LocaleUtils.toLocale("enUS")); // no separator
        Assert.assertNull(LocaleUtils.toLocale("en_US_")); // extra separator
        Assert.assertNull(LocaleUtils.toLocale("en_US_en")); // duplicate language
        Assert.assertNull(LocaleUtils.toLocale("en_US_US")); // duplicate country
        Assert.assertNull(LocaleUtils.toLocale("en_US-en")); // invalid separator
        Assert.assertNull(LocaleUtils.toLocale("en_US-US")); // invalid separator
        Assert.assertNull(LocaleUtils.toLocale("en_US_en_US")); // duplicate language and country
        Assert.assertNull(LocaleUtils.toLocale("en_US_en_GB")); // wrong country

        // Test for null input
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocaleList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Test for empty list
        Locale emptyLocale = null;
        List<Locale> emptyExpectedLocaleList = Collections.emptyList();
        List<Locale> emptyActualLocaleList = LocaleUtils.localeLookupList(emptyLocale);
        Assert.assertEquals(emptyExpectedLocaleList, emptyActualLocaleList);

        // Test for null input
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Collections.list(Locale.getAvailableLocales());
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Test for null result
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.availableLocaleList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocaleStringFormat() {
        LocaleUtils.toLocale("en_GB_en"); // wrong language
        LocaleUtils.toLocale("en_GB-en"); // wrong separator
        LocaleUtils.toLocale("en_GB_en-en"); // duplicate language and separator
        LocaleUtils.toLocale("en_GB_en_GB-en"); // duplicate country and separator
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        Locale emptyLocale = null;
        Assert.assertFalse(LocaleUtils.isAvailableLocale(emptyLocale));

        // Test for unknown locale
        Locale unknownLocale = new Locale("xy", "xy");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(unknownLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Test for null input
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "FR"));
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Test for null input
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }

}