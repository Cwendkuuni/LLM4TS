import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        // Edge cases: invalid input
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_US_"));
        assertNull(LocaleUtils.toLocale("en_US_xx"));

        // Edge cases: invalid separator
        assertNull(LocaleUtils.toLocale("en_US_xx_"));
        assertNull(LocaleUtils.toLocale("en_US_xxxx"));
        assertNull(LocaleUtils.toLocale("en_US_xx_xx"));

        // Exception handling scenarios: language code not lowercase
        try {
            LocaleUtils.toLocale("EN_US");
            fail("Expected IllegalArgumentException for non-lowercase language code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Exception handling scenarios: country code not uppercase
        try {
            LocaleUtils.toLocale("en_Us");
            fail("Expected IllegalArgumentException for non-uppercase country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Exception handling scenarios: invalid separator
        try {
            LocaleUtils.toLocale("en_US_xy");
            fail("Expected IllegalArgumentException for invalid separator");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Exception handling scenarios: length < 2
        try {
            LocaleUtils.toLocale("en");
            fail("Expected IllegalArgumentException for length < 2");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Exception handling scenarios: length > 7
        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx");
            fail("Expected IllegalArgumentException for length > 7");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Edge cases: available locales
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        for (Locale locale : availableLocales) {
            Locale actual = LocaleUtils.toLocale(locale.getDisplayName());
            assertEquals(locale, actual);
        }

        // Edge cases: unavailable locale
        assertNull(LocaleUtils.toLocale("xx_XX"));
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA", "Vancouver");
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expected, localeLookupList);

        // Edge cases: null input
        Locale nullLocale = null;
        List<Locale> nullLocaleLookupList = LocaleUtils.localeLookupList(nullLocale);
        List<Locale> expectedEmptyList = Collections.emptyList();
        assertEquals(expectedEmptyList, nullLocaleLookupList);

        // Edge cases: empty list
        List<Locale> emptyLocaleList = Collections.emptyList();
        List<Locale> emptyLocaleLookupList = LocaleUtils.localeLookupList(emptyLocaleList.get(0));
        assertEquals(expectedEmptyList, emptyLocaleLookupList);

        // Edge cases: different locale object
        Locale differentLocale = new Locale("en", "US");
        List<Locale> differentLocaleLookupList = LocaleUtils.localeLookupList(differentLocale);
        List<Locale> expectedDifferentLocaleList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), differentLocale);
        assertEquals(expectedDifferentLocaleList, differentLocaleLookupList);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        int expectedSize = 61; // This number may change depending on the JRE being used
        assertEquals(expectedSize, availableLocaleList.size());

        // Edge cases: empty list
        List<Locale> emptyAvailableLocaleList = Collections.emptyList();
        List<Locale> emptyAvailableLocaleListResult = LocaleUtils.availableLocaleList();
        assertEquals(emptyAvailableLocaleList, emptyAvailableLocaleListResult);

        // Edge cases: null input
        List<Locale> nullAvailableLocaleList = null;
        List<Locale> nullAvailableLocaleListResult = LocaleUtils.availableLocaleList();
        assertEquals(availableLocaleList, nullAvailableLocaleListResult);
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Edge cases: null input
        Locale nullLocale = null;
        assertFalse(LocaleUtils.isAvailableLocale(nullLocale));

        // Edge cases: empty list
        List<Locale> emptyLocaleList = Collections.emptyList();
        assertFalse(LocaleUtils.isAvailableLocale(emptyLocaleList.get(0)));

        // Edge cases: unavailable locale
        Locale unavailableLocale = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(unavailableLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List<Locale> languagesByCountryFr = LocaleUtils.languagesByCountry("FR");
        List<Locale> expectedFrList = Arrays.asList(new Locale("fr", "FR"), new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"));
        assertEquals(expectedFrList, languagesByCountryFr);

        // Edge cases: null input
        String nullCountryCode = null;
        List<Locale> nullCountryCodeLanguagesByCountry = LocaleUtils.languagesByCountry(nullCountryCode);
        List<Locale> emptyList = Collections.emptyList();
        assertEquals(emptyList, nullCountryCodeLanguagesByCountry);

        // Edge cases: empty list
        List<Locale> emptyLocaleList = Collections.emptyList();
        List<Locale> emptyLocaleListLanguagesByCountry = LocaleUtils.languagesByCountry(emptyLocaleList.get(0));
        assertEquals(emptyList, emptyLocaleListLanguagesByCountry);

        // Edge cases: unsupported country code
        String unsupportedCountryCode = "XX";
        List<Locale> unsupportedCountryCodeLanguagesByCountry = LocaleUtils.languagesByCountry(unsupportedCountryCode);
        assertEquals(emptyList, unsupportedCountryCodeLanguagesByCountry);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List<Locale> countriesByLanguageEn = LocaleUtils.countriesByLanguage("EN");
        List<Locale> expectedEnList = Arrays.asList(new Locale("EN", "GB"), new Locale("EN", "US"), new Locale("EN", "CA"), new Locale("EN", "IE"), new Locale("EN", "AU"));
        assertEquals(expectedEnList, countriesByLanguageEn);

        // Edge cases: null input
        String nullLanguageCode = null;
        List<Locale> nullLanguageCodeCountriesByLanguage = LocaleUtils.countriesByLanguage(nullLanguageCode);
        List<Locale> emptyList = Collections.emptyList();
        assertEquals(emptyList, nullLanguageCodeCountriesByLanguage);

        // Edge cases: empty list
        List<Locale> emptyLocaleList = Collections.emptyList();
        List<Locale> emptyLocaleListCountriesByLanguage = LocaleUtils.countriesByLanguage(emptyLocaleList.get(0));
        assertEquals(emptyList, emptyLocaleListCountriesByLanguage);

        // Edge cases: unsupported language code
        String unsupportedLanguageCode = "XX";
        List<Locale> unsupportedLanguageCodeCountriesByLanguage = LocaleUtils.countriesByLanguage(unsupportedLanguageCode);
        assertEquals(emptyList, unsupportedLanguageCodeCountriesByLanguage);
    }
}