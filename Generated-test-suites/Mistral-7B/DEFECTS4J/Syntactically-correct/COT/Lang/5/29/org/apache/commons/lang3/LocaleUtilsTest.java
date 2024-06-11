import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidLocale() {
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        Assert.assertEquals(expectedLocale, actualLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_EmptyLocaleString() {
        String localeString = "";
        Locale locale = LocaleUtils.toLocale(localeString);
        // An empty string should throw an IllegalArgumentException
        Assert.assertNull(locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_InvalidLanguageCode() {
        String localeString = "xx_US";
        Locale locale = LocaleUtils.toLocale(localeString);
        // An invalid language code should throw an IllegalArgumentException
        Assert.assertNull(locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_InvalidCountryCode() {
        String localeString = "en_xx";
        Locale locale = LocaleUtils.toLocale(localeString);
        // An invalid country code should throw an IllegalArgumentException
        Assert.assertNull(locale);
    }

    @Test
    public void testToLocale_ValidLocaleWithVariant() {
        String localeString = "en_US_foo";
        Locale expectedLocale = new Locale("en", "US", "foo");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        Assert.assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedLocaleLookupList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        Assert.assertEquals(expectedLocaleLookupList, localeLookupList);
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(null);
        Assert.assertTrue(Collections.isEmpty(localeLookupList));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        int expectedSize = 44; //The number of available locales may change, this is just an example.
        Assert.assertEquals(expectedSize, availableLocaleList.size());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        String countryCode = "CA";
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry(countryCode);
        int expectedSize = 12; //The number of languages by country may change, this is just an example.
        Assert.assertEquals(expectedSize, languagesByCountry.size());
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry(null);
        Assert.assertTrue(Collections.isEmpty(languagesByCountry));
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        String languageCode = "en";
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage(languageCode);
        int expectedSize = 6; //The number of countries by language may change, this is just an example.
        Assert.assertEquals(expectedSize, countriesByLanguage.size());
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage(null);
        Assert.assertTrue(Collections.isEmpty(countriesByLanguage));
    }
}