package org.apache.commons.lang;
import org.junit.Test;
import org.junit.Assert;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        Locale result = LocaleUtils.toLocale(null);
        Assert.assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("abc");
    }

    // Add more test cases for the toLocale(String str) method to cover all possible edge cases and exception handling scenarios

    @Test
    public void testLocaleLookupList_NullLocale() {
        List result = LocaleUtils.localeLookupList(null);
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    // Add more test cases for the localeLookupList(Locale locale) method to cover all possible edge cases

    @Test
    public void testLocaleLookupList_NullLocale_DefaultLocale() {
        List result = LocaleUtils.localeLookupList(null, Locale.getDefault());
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(Locale.getDefault(), result.get(0));
    }

    // Add more test cases for the localeLookupList(Locale locale, Locale defaultLocale) method to cover all possible edge cases

    @Test
    public void testAvailableLocaleList() {
        List result = LocaleUtils.availableLocaleList();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set result = LocaleUtils.availableLocaleSet();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);
    }

    @Test
    public void testIsAvailableLocale_NullLocale() {
        boolean result = LocaleUtils.isAvailableLocale(null);
        Assert.assertFalse(result);
    }

    // Add more test cases for the isAvailableLocale(Locale locale) method to cover all possible edge cases

    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        List result = LocaleUtils.languagesByCountry(null);
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    // Add more test cases for the languagesByCountry(String countryCode) method to cover all possible edge cases

    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        List result = LocaleUtils.countriesByLanguage(null);
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());
    }

    // Add more test cases for the countriesByLanguage(String languageCode) method to cover all possible edge cases

}