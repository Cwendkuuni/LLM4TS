import org.junit.Test;
import org.junit.Assert;

import java.util.Locale;
import java.util.List;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        Assert.assertEquals(new Locale("en"), locale1);

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(new Locale("en", "GB"), locale2);

        Locale locale3 = LocaleUtils.toLocale("en_GB_xyz");
        Assert.assertEquals(new Locale("en", "GB", "xyz"), locale3);

        // Add more test cases for edge cases and exceptions
    }

    @Test
    public void testLocaleLookupList() {
        Locale inputLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = LocaleUtils.localeLookupList(inputLocale);
        // Add assertions to test the expected list against the actual list
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        // Add assertions to test the list of available locales
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocalesSet = LocaleUtils.availableLocaleSet();
        // Add assertions to test the set of available locales
    }

    @Test
    public void testIsAvailableLocale() {
        Locale testLocale = new Locale("en", "US");
        boolean isAvailable = LocaleUtils.isAvailableLocale(testLocale);
        Assert.assertTrue(isAvailable);

        // Add more test cases to cover different scenarios
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("US");
        // Add assertions to test the list of languages by country
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        // Add assertions to test the list of countries by language
    }

    // Add more test cases for other methods in the LocaleUtils class

}