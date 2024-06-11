import org.junit.*;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    private static final String TEST_LOCALE_STRING = "en_US";
    private static final Locale TEST_LOCALE = new Locale("en", "US");


    // Test for toLocale() method
    @Test
    public void testToLocale() {
        // Test null input
        assertNull(LocaleUtils.toLocale(null));

        // Test empty input
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));

        // Test invalid format with "#"
        try {
            LocaleUtils.toLocale("en#US");
            fail();
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Test invalid format with length < 2
        try {
            LocaleUtils.toLocale("e");
            fail();
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Test valid format with occurrences = 0 and length = 2
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));

        // Test invalid format with occurrences = 0 and lowercase characters
        try {
            LocaleUtils.toLocale("us");
            fail();
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Test invalid format with occurrences = 0 and length = 3
        try {
            LocaleUtils.toLocale("eng");
            fail();
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Test valid format with occurrences = 1, lowercase language code, uppercase country code, and length = 5
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));

        // Test invalid format with occurrences = 1 and invalid language code
        try {
            LocaleUtils.toLocale("eng_US");
            fail();
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Test invalid format with occurrences = 1, lowercase language code, uppercase country code, and invalid length
        try {
            LocaleUtils.toLocale("en_USA");
            fail();
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Test valid format with occurrences = 2, lowercase language code, uppercase country code, and empty variant
        assertEquals(new Locale("en", "US", ""), LocaleUtils.toLocale("en_US_"));

        // Test invalid format with occurrences = 2 and lowercase language code
        try {
            LocaleUtils.toLocale("eng_US_");
            fail();
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Test invalid format with occurrences = 2 and invalid length
        try {
            LocaleUtils.toLocale("en_US_A");
            fail();
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }


    // Test for localeLookupList() method
    @Test
    public void testLocaleLookupList() {
        // Test with null locale
        List<Locale> result1 = LocaleUtils.localeLookupList(null);
        assertEquals(Collections.emptyList(), result1);

        // Test with null locale and default locale
        List<Locale> result2 = LocaleUtils.localeLookupList(null, TEST_LOCALE);
        assertEquals(Arrays.asList(TEST_LOCALE), result2);

        // Test with valid locale, empty variant
        Locale locale = new Locale("en", "US");
        List<Locale> result3 = LocaleUtils.localeLookupList(locale);
        List<Locale> expected3 = Arrays.asList(locale, new Locale("en", ""));
        assertEquals(expected3, result3);

        // Test with valid locale, non-empty variant
        Locale locale2 = new Locale("en", "US", "variant");
        List<Locale> result4 = LocaleUtils.localeLookupList(locale2);
        List<Locale> expected4 = Arrays.asList(locale2, new Locale("en", "US"));
        assertEquals(expected4, result4);

        // Test with valid locale, empty variant, and default locale
        List<Locale> result5 = LocaleUtils.localeLookupList(locale, TEST_LOCALE);
        List<Locale> expected5 = Arrays.asList(locale, new Locale("en", ""), TEST_LOCALE);
        assertEquals(expected5, result5);
    }


    // Test for availableLocaleList() method
    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertTrue(locales.contains(Locale.US));
        assertTrue(locales.contains(Locale.UK));
        // Add more assertions for the rest of the available locales
    }


    // Test for availableLocaleSet() method
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertTrue(locales.contains(Locale.US));
        assertTrue(locales.contains(Locale.UK));
        // Add more assertions for the rest of the available locales
    }


    // Test for isAvailableLocale() method
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.UK));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }


    // Test for languagesByCountry() method
    @Test
    public void testLanguagesByCountry() {
        // Test with null country
        List<Locale> result1 = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.emptyList(), result1);

        // Test with valid country
        List<Locale> result2 = LocaleUtils.languagesByCountry("US");
        assertEquals(Arrays.asList(Locale.ENGLISH), result2);
    }


    // Test for countriesByLanguage() method
    @Test
    public void testCountriesByLanguage() {
        // Test with null language
        List<Locale> result1 = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.emptyList(), result1);

        // Test with valid language
        List<Locale> result2 = LocaleUtils.countriesByLanguage("en");
        assertEquals(Arrays.asList(Locale.US), result2);
    }
}