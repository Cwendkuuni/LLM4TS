package org.apache.commons.lang;
import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        assertEquals(expectedLocale, actualLocale);

        // Edge cases and exception handling tests
        // Test for null input
        String nullLocaleString = null;
        Locale nullLocale = LocaleUtils.toLocale(nullLocaleString);
        assertNull(nullLocale);

        // Test for invalid format
        String invalidLocaleString = "xx_xx_xx";
        try {
            LocaleUtils.toLocale(invalidLocaleString);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test for empty locale string
        String emptyLocaleString = "";
        Locale emptyLocale = LocaleUtils.toLocale(emptyLocaleString);
        assertEquals(Locale.ROOT, emptyLocale);

        // Test for language code with uppercase letter
        String uppercaseLanguageLocaleString = "en_US_EN";
        Locale uppercaseLanguageLocale = LocaleUtils.toLocale(uppercaseLanguageLocaleString);
        assertEquals(new Locale("en", "US"), uppercaseLanguageLocale);

        // Test for country code with lowercase letter
        String lowercaseCountryLocaleString = "en_US_us";
        Locale lowercaseCountryLocale = LocaleUtils.toLocale(lowercaseCountryLocaleString);
        assertEquals(new Locale("en", "US"), lowercaseCountryLocale);

        // Test for separator other than '_'
        String separatorOtherThanUnderscoreLocaleString = "en-US";
        Locale separatorOtherThanUnderscoreLocale = LocaleUtils.toLocale(separatorOtherThanUnderscoreLocaleString);
        assertEquals(new Locale("en", "US"), separatorOtherThanUnderscoreLocale);

        // Test for invalid separator
        String invalidSeparatorLocaleString = "en_US_xxx";
        try {
            LocaleUtils.toLocale(invalidSeparatorLocaleString);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test for length less than 2
        String lengthLessThanTwoLocaleString = "en";
        Locale lengthLessThanTwoLocale = LocaleUtils.toLocale(lengthLessThanTwoLocaleString);
        assertNull(lengthLessThanTwoLocale);

        // Test for length more than 7
        String lengthMoreThanSevenLocaleString = "en_US_XXXXXXX";
        try {
            LocaleUtils.toLocale(lengthMoreThanSevenLocaleString);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test for language code longer than 2 characters
        String languageCodeLongerThanTwoLocaleString = "en_US_en";
        try {
            LocaleUtils.toLocale(languageCodeLongerThanTwoLocaleString);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test for country code longer than 2 characters
        String countryCodeLongerThanTwoLocaleString = "en_US_US";
        try {
            LocaleUtils.toLocale(countryCodeLongerThanTwoLocaleString);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        String localeString = "fr_CA";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", ""), new Locale("fr"));
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(LocaleUtils.toLocale(localeString));
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge cases and exception handling tests
        // Test for null input
        Locale nullLocale = Locale.ROOT;
        List<Locale> nullLocaleList = LocaleUtils.localeLookupList(nullLocale);
        assertEquals(Collections.emptyList(), nullLocaleList);

        // Test for invalid locale
        String invalidLocaleString = "xx_xx";
        try {
            LocaleUtils.localeLookupList(LocaleUtils.toLocale(invalidLocaleString));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test for empty locale
        Locale emptyLocale = Locale.ROOT;
        List<Locale> emptyLocaleList = LocaleUtils.localeLookupList(emptyLocale);
        assertEquals(Collections.emptyList(), emptyLocaleList);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case test for empty list
        List<Locale> emptyLocaleList = Collections.emptyList();
        assertEquals(emptyLocaleList, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Edge case test for null input
        Locale nullLocale = null;
        assertFalse(LocaleUtils.isAvailableLocale(nullLocale));

        // Edge case test for invalid locale
        Locale invalidLocale = new Locale("xx", "xx");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        String countryCode = "US";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("en", "US"), new Locale("en", ""), new Locale("en"));
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge cases and exception handling tests
        // Test for null input
        String nullCountryCode = null;
        List<Locale> nullLocaleList = LocaleUtils.languagesByCountry(nullCountryCode);
        assertEquals(Collections.emptyList(), nullLocaleList);

        // Test for empty country code
        String emptyCountryCode = "";
        List<Locale> emptyLocaleList = LocaleUtils.languagesByCountry(emptyCountryCode);
        assertEquals(Collections.emptyList(), emptyLocaleList);

        // Test for invalid country code
        String invalidCountryCode = "xx";
        List<Locale> invalidLocaleList = LocaleUtils.languagesByCountry(invalidCountryCode);
        assertEquals(Collections.emptyList(), invalidLocaleList);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        String languageCode = "en";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", ""));
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge cases and exception handling tests
        // Test for null input
        String nullLanguageCode = null;
        List<Locale> nullLocaleList = LocaleUtils.countriesByLanguage(nullLanguageCode);
        assertEquals(Collections.emptyList(), nullLocaleList);

        // Test for empty language code
        String emptyLanguageCode = "";
        List<Locale> emptyLocaleList = LocaleUtils.countriesByLanguage(emptyLanguageCode);
        assertEquals(Collections.emptyList(), emptyLocaleList);

        // Test for invalid language code
        String invalidLanguageCode = "xx";
        List<Locale> invalidLocaleList = LocaleUtils.countriesByLanguage(invalidLanguageCode);
        assertEquals(Collections.emptyList(), invalidLocaleList);
    }
}