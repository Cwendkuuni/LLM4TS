import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
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
        // Test null input
        String nullInput = null;
        assertNull(LocaleUtils.toLocale(nullInput));

        // Test empty string
        String emptyString = "";
        assertNull(LocaleUtils.toLocale(emptyString));

        // Test invalid format - language code is not lowercase
        String invalidLanguage = "EN_US";
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLanguage));

        // Test invalid format - country code is not uppercase
        String invalidCountry = "en_us";
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidCountry));

        // Test invalid format - separator is not an underscore
        String invalidSeparator = "en_US=";
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidSeparator));

        // Test invalid format - length is not correct
        String invalidLength = "en_US_XXX";
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLength));
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale baseLocale = new Locale("fr", "CA", "XX");
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("fr", "CA", "XX"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge cases and exception handling tests
        // Test null input
        Locale nullInputLocale = null;
        assertNull(LocaleUtils.localeLookupList(nullInputLocale));

        // Test empty list
        Locale emptyLocale = new Locale("", "");
        assertEmpty(LocaleUtils.localeLookupList(emptyLocale));

        // Test invalid locale
        Locale invalidLocale = new Locale("xx", "XX");
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.localeLookupList(invalidLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge cases and exception handling tests
        // Test empty list
        assertEmpty(LocaleUtils.availableLocaleList());
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        Locale availableLocale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(availableLocale));

        Locale unavailableLocale = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(unavailableLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        String countryCode = "US";
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "GB"),
                new Locale("en", "IE")
        );
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge cases and exception handling tests
        // Test null input
        String nullInput = null;
        assertEmpty(LocaleUtils.languagesByCountry(nullInput));

        // Test empty country code
        String emptyCountryCode = "";
        assertEmpty(LocaleUtils.languagesByCountry(emptyCountryCode));

        // Test invalid country code
        String invalidCountryCode = "XX";
        assertEmpty(LocaleUtils.languagesByCountry(invalidCountryCode));
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        String languageCode = "en";
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "GB"),
                new Locale("en", "IE")
        );
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge cases and exception handling tests
        // Test null input
        String nullInput = null;
        assertEmpty(LocaleUtils.countriesByLanguage(nullInput));

        // Test empty language code
        String emptyLanguageCode = "";
        assertEmpty(LocaleUtils.countriesByLanguage(emptyLanguageCode));

        // Test invalid language code
        String invalidLanguageCode = "XX";
        assertEmpty(LocaleUtils.countriesByLanguage(invalidLanguageCode));
    }
}