import org.apache.commons.lang.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        String input1 = "en";
        Locale expected1 = new Locale("en", "");
        Locale actual1 = LocaleUtils.toLocale(input1);
        Assert.assertEquals(expected1, actual1);

        String input2 = "en_GB";
        Locale expected2 = new Locale("en", "GB");
        Locale actual2 = LocaleUtils.toLocale(input2);
        Assert.assertEquals(expected2, actual2);

        String input3 = "en_GB_XXX";
        Locale expected3 = new Locale("en", "GB", "XXX");
        Locale actual3 = LocaleUtils.toLocale(input3);
        Assert.assertEquals(expected3, actual3);

        // Test invalid input
        String input4 = null;
        Locale actual4 = LocaleUtils.toLocale(input4);
        Assert.assertNull(actual4);

        String input5 = "fr";
        String errorMessage5 = "Invalid locale format: fr";
        try {
            Locale actual5 = LocaleUtils.toLocale(input5);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(errorMessage5, e.getMessage());
        }

        String input6 = "fr_CA";
        String errorMessage6 = "Invalid locale format: fr_CA";
        try {
            Locale actual6 = LocaleUtils.toLocale(input6);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(errorMessage6, e.getMessage());
        }

        String input7 = "fr_CA_XXX";
        String errorMessage7 = "Invalid locale format: fr_CA_XXX";
        try {
            Locale actual7 = LocaleUtils.toLocale(input7);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(errorMessage7, e.getMessage());
        }

        String input8 = "fr_CA_XXX_YYY";
        String errorMessage8 = "Invalid locale format: fr_CA_XXX_YYY";
        try {
            Locale actual8 = LocaleUtils.toLocale(input8);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(errorMessage8, e.getMessage());
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty input
        Locale locale1 = null;
        List<Locale> expected1 = Collections.emptyList();
        List<Locale> actual1 = LocaleUtils.localeLookupList(locale1);
        Assert.assertEquals(expected1, actual1);

        // Test valid input
        String countryCode = "CA";
        Locale locale2 = new Locale("fr", countryCode, "xxx");
        List<Locale> expected2 = new ArrayList<>();
        expected2.add(locale2);
        expected2.add(new Locale("fr", countryCode));
        expected2.add(new Locale("fr"));
        List<Locale> actual2 = LocaleUtils.localeLookupList(locale2);
        Assert.assertEquals(expected2, actual2);

        // Test default locale
        Locale defaultLocale = new Locale("en");
        List<Locale> expected3 = new ArrayList<>();
        expected3.add(locale2);
        expected3.add(new Locale("fr", countryCode));
        expected3.add(new Locale("fr"));
        expected3.add(defaultLocale);
        List<Locale> actual3 = LocaleUtils.localeLookupList(locale2, defaultLocale);
        Assert.assertEquals(expected3, actual3);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expected, actual);

        // Test non-empty list
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> expected2 = new ArrayList<>();
        expected2.add(locale1);
        expected2.add(new Locale("fr", "CA"));
        expected2.add(new Locale("fr"));
        List<Locale> actual2 = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expected2, actual2);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test valid locale
        Locale locale1 = new Locale("fr", "CA");
        boolean expected = true;
        boolean actual = LocaleUtils.isAvailableLocale(locale1);
        Assert.assertEquals(expected, actual);

        // Test invalid locale
        Locale locale2 = new Locale("fr", "XX");
        boolean expected2 = false;
        boolean actual2 = LocaleUtils.isAvailableLocale(locale2);
        Assert.assertEquals(expected2, actual2);
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        String countryCode = null;
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expected, actual);

        // Test valid input
        String countryCode1 = "CA";
        List<Locale> expected1 = new ArrayList<>();
        expected1.add(new Locale("fr", countryCode1));
        expected1.add(new Locale("en", countryCode1));
        List<Locale> actual1 = LocaleUtils.languagesByCountry(countryCode1);
        Assert.assertEquals(expected1, actual1);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty list
        String languageCode = null;
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expected, actual);

        // Test valid input
        String languageCode1 = "fr";
        List<Locale> expected1 = new ArrayList<>();
        expected1.add(new Locale("fr", "CA"));
        expected1.add(new Locale("fr", "FR"));
        List<Locale> actual1 = LocaleUtils.countriesByLanguage(languageCode1);
        Assert.assertEquals(expected1, actual1);
    }
}