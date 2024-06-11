import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        assertEquals(expectedLocale, actualLocale);

        // Edge cases and exception handling
        // Null input
        String nullInput = null;
        Locale nullInputLocale = LocaleUtils.toLocale(nullInput);
        assertNull(nullInputLocale);

        // Empty string
        String emptyString = "";
        Locale emptyStringLocale = LocaleUtils.toLocale(emptyString);
        assertNull(emptyStringLocale);

        // Invalid format - wrong number of characters
        String invalidFormat1 = "en_US_";
        try {
            LocaleUtils.toLocale(invalidFormat1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidFormat2 = "en_US__";
        try {
            LocaleUtils.toLocale(invalidFormat2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid format - wrong format of language or country codes
        String invalidFormat3 = "en_aA";
        try {
            LocaleUtils.toLocale(invalidFormat3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidFormat4 = "en_US_aa";
        try {
            LocaleUtils.toLocale(invalidFormat4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidFormat5 = "en_Aa";
        try {
            LocaleUtils.toLocale(invalidFormat5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidFormat6 = "_US_";
        try {
            LocaleUtils.toLocale(invalidFormat6);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidFormat7 = "_US__";
        try {
            LocaleUtils.toLocale(invalidFormat7);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidFormat8 = "en___";
        try {
            LocaleUtils.toLocale(invalidFormat8);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidFormat9 = "en__";
        try {
            LocaleUtils.toLocale(invalidFormat9);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidFormat10 = "__";
        try {
            LocaleUtils.toLocale(invalidFormat10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality
        Locale locale = new Locale("fr", "CA", "XX");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, actualList);

        // Edge cases and exception handling
        // Null input
        Locale nullInputLocale = null;
        List<Locale> nullInputList = LocaleUtils.localeLookupList(nullInputLocale);
        assertNotNull(nullInputList);
        assertTrue(nullInputList.isEmpty());

        // Empty list
        Locale emptyLocale = new Locale("");
        List<Locale> emptyList = LocaleUtils.localeLookupList(emptyLocale);
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

        // Empty string input
        String emptyString = "";
        List<Locale> emptyStringList = LocaleUtils.localeLookupList(emptyString);
        assertNotNull(emptyStringList);
        assertTrue(emptyStringList.isEmpty());

        // Invalid locale
        Locale invalidLocale = new Locale("fr", "AA", "XX");
        List<Locale> invalidLocaleList = LocaleUtils.localeLookupList(invalidLocale);
        assertNotNull(invalidLocaleList);
        assertTrue(invalidLocaleList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> actualList = Locale.getAvailableLocalesList();
        assertEquals(expectedList, actualList);

        // Edge cases and exception handling
        // Null input
        List<Locale> nullInputList = LocaleUtils.availableLocaleList();
        assertNotNull(nullInputList);
        assertTrue(nullInputList.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic functionality
        Set<Locale> expectedSet = LocaleUtils.availableLocaleSet();
        Set<Locale> actualSet = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expectedSet, actualSet);

        // Edge cases and exception handling
        // Null input
        Set<Locale> nullInputSet = LocaleUtils.availableLocaleSet();
        assertNotNull(nullInputSet);
        assertTrue(nullInputSet.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality
        Locale locale1 = new Locale("en", "US");
        Locale locale2 = new Locale("fr", "FR");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));
        assertTrue(LocaleUtils.isAvailableLocale(locale2));

        // Edge cases and exception handling
        // Null input
        Locale nullInputLocale = null;
        assertFalse(LocaleUtils.isAvailableLocale(nullInputLocale));

        // Empty locale
        Locale emptyLocale = new Locale("");
        assertFalse(LocaleUtils.isAvailableLocale(emptyLocale));

        // Invalid locale
        Locale invalidLocale = new Locale("fr", "AA");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality
        String countryCode = "FR";
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "FR"), new Locale("fr", "BE"), new Locale("fr", "CA"));
        List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedList, actualList);

        // Edge cases and exception handling
        // Null input
        String nullCountryCode = null;
        List<Locale> nullCountryCodeList = LocaleUtils.languagesByCountry(nullCountryCode);
        assertNotNull(nullCountryCodeList);
        assertTrue(nullCountryCodeList.isEmpty());

        // Empty country code
        String emptyCountryCode = "";
        List<Locale> emptyCountryCodeList = LocaleUtils.languagesByCountry(emptyCountryCode);
        assertNotNull(emptyCountryCodeList);
        assertTrue(emptyCountryCodeList.isEmpty());

        // Invalid country code
        String invalidCountryCode = "AA";
        List<Locale> invalidCountryCodeList = LocaleUtils.languagesByCountry(invalidCountryCode);
        assertNotNull(invalidCountryCodeList);
        assertTrue(invalidCountryCodeList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality
        String languageCode = "fr";
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "FR"), new Locale("fr", "BE"), new Locale("fr", "CA"));
        List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedList, actualList);

        // Edge cases and exception handling
        // Null input
        String nullLanguageCode = null;
        List<Locale> nullLanguageCodeList = LocaleUtils.countriesByLanguage(nullLanguageCode);
        assertNotNull(nullLanguageCodeList);
        assertTrue(nullLanguageCodeList.isEmpty());

        // Empty language code
        String emptyLanguageCode = "";
        List<Locale> emptyLanguageCodeList = LocaleUtils.countriesByLanguage(emptyLanguageCode);
        assertNotNull(emptyLanguageCodeList);
        assertTrue(emptyLanguageCodeList.isEmpty());

        // Invalid language code
        String invalidLanguageCode = "AA";
        List<Locale> invalidLanguageCodeList = LocaleUtils.countriesByLanguage(invalidLanguageCode);
        assertNotNull(invalidLanguageCodeList);
        assertTrue(invalidLanguageCodeList.isEmpty());
    }
}