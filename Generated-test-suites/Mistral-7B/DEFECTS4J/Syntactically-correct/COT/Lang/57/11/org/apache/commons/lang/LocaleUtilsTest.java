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
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        assertEquals(expectedLocale, actualLocale);

        // Edge case tests
        String invalidLocaleString1 = null;
        try {
            LocaleUtils.toLocale(invalidLocaleString1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidLocaleString2 = "xx_YY";
        try {
            LocaleUtils.toLocale(invalidLocaleString2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidLocaleString3 = "en_AA";
        try {
            LocaleUtils.toLocale(invalidLocaleString3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidLocaleString4 = "en_GB_zz";
        try {
            LocaleUtils.toLocale(invalidLocaleString4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidLocaleString5 = "en_GB__";
        try {
            LocaleUtils.toLocale(invalidLocaleString5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Exception handling tests
        String missingLanguageLocaleString = "GB";
        try {
            LocaleUtils.toLocale(missingLanguageLocaleString);
            fail("Expected MissingResourceException");
        } catch (MissingResourceException e) {
            // Expected
        }

        String missingCountryLocaleString = "en_";
        try {
            LocaleUtils.toLocale(missingCountryLocaleString);
            fail("Expected MissingResourceException");
        } catch (MissingResourceException e) {
            // Expected
        }

        String missingBothLocaleString = "";
        try {
            LocaleUtils.toLocale(missingBothLocaleString);
            fail("Expected MissingResourceException");
        } catch (MissingResourceException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        String baseLocaleString = "fr_CA";
        Locale baseLocale = new Locale(baseLocaleString);
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedLocaleList = Arrays.asList(baseLocale, new Locale("fr_CA"), new Locale("fr"), defaultLocale);
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case tests
        String emptyLocaleString = "";
        List<Locale> emptyLocaleList = LocaleUtils.localeLookupList(emptyLocaleString, defaultLocale);
        assertEquals(Collections.singletonList(defaultLocale), emptyLocaleList);

        String noDefaultLocaleString = null;
        List<Locale> noDefaultLocaleList = LocaleUtils.localeLookupList(baseLocale, noDefaultLocaleString);
        assertEquals(Collections.singletonList(baseLocale), noDefaultLocaleList);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case tests
        LocaleUtils.availableLocaleList(); // no-op
        LocaleUtils.availableLocaleList(); // no-op
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        String availableLocaleString = "en_US";
        Locale availableLocale = new Locale(availableLocaleString);
        assertTrue(LocaleUtils.isAvailableLocale(availableLocale));

        String invalidLocaleString = "en_XX";
        Locale invalidLocale = new Locale(invalidLocaleString);
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        String countryCode = "US";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("en", countryCode), new Locale("en", ""));
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLocaleList, actualLocaleList);

        String emptyCountryCode = "";
        List<Locale> emptyLocaleList = LocaleUtils.languagesByCountry(emptyCountryCode);
        assertEquals(Collections.emptyList(), emptyLocaleList);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        String languageCode = "en";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale(languageCode, "US"), new Locale(languageCode, ""), new Locale("", languageCode));
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedLocaleList, actualLocaleList);

        String emptyLanguageCode = "";
        List<Locale> emptyLocaleList = LocaleUtils.countriesByLanguage(emptyLanguageCode);
        assertEquals(Collections.emptyList(), emptyLocaleList);
    }
}