import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        // Test with empty string as argument
        expected = null;
        actual = LocaleUtils.toLocale("");
        assertEquals(expected, actual);

        // Test with invalid locale format
        String invalidLocale = "fr_CA_abcd";
        try {
            actual = LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException: Invalid locale format: " + invalidLocale);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic test
        Locale locale = new Locale("fr", "CA", "XX");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        // Test with null locale
        expected = Collections.emptyList();
        actual = LocaleUtils.localeLookupList(null);
        assertEquals(expected, actual);

        // Test with defaultLocale
        expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic test
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);

        // Test with empty list
        expected = Collections.emptyList();
        actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic test
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test with null locale
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test with invalid locale
        Locale invalidLocale = new Locale("fr", "CA", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic test
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(new Locale("fr", countryCode), new Locale("en", countryCode));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        // Test with null countryCode
        expected = Collections.emptyList();
        actual = LocaleUtils.languagesByCountry(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic test
        String languageCode = "fr";
        List<Locale> expected = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "BE"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        // Test with null languageCode
        expected = Collections.emptyList();
        actual = LocaleUtils.countriesByLanguage(null);
        assertEquals(expected, actual);
    }
}