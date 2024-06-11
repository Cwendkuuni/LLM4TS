import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        // Test null input
        assertNull(LocaleUtils.toLocale(null));

        // Test empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid format - wrong length
        try {
            LocaleUtils.toLocale("en_GB_XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid format - wrong char
        try {
            LocaleUtils.toLocale("en_GB_XXX_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid format - wrong case
        try {
            LocaleUtils.toLocale("en_GB_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic test
        Locale locale = new Locale("fr", "CA", "XXX");
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "XXX"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(expected, actual);

        // Test null input
        Locale defaultLocale = new Locale("en");
        List<Locale> actual1 = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(Collections.singletonList(defaultLocale), actual1);

        // Test empty list
        List<Locale> actual2 = LocaleUtils.localeLookupList(new Locale(""), defaultLocale);
        assertEquals(Collections.singletonList(defaultLocale), actual2);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertTrue(actual.isEmpty());

        // Test available locales
        List<Locale> expected = Arrays.asList(
                Locale.SIMPLIFIED_CHINESE,
                Locale.TRADITIONAL_CHINESE,
                // ... other available locales ...
                Locale.ENGLISH,
                Locale.FRENCH,
                // ... other available locales ...
                Locale.GERMAN,
                Locale.ITALIAN,
                // ... other available locales ...
                Locale.JAPANESE,
                Locale.KOREAN,
                // ... other available locales ...
                Locale.RUSSIAN,
                Locale.SPANISH,
                // ... other available locales ...
                Locale.TRADITIONAL_TURKISH,
                Locale.SIMPLIFIED_TURKISH,
                // ... other available locales ...
                Locale.UNSPECIFIED
        );
        List<Locale> actual1 = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual1);
    }

    @Test(expected = IllegalStateException.class)
    public void testIsAvailableLocaleWithUnavailableLocale() {
        Locale locale = new Locale("xx", "XX");
        boolean actual = LocaleUtils.isAvailableLocale(locale);
        assertFalse(actual);
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic test
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA"),
                // ... other languages for Canada ...
                new Locale("es", "ES") // just for testing that it returns the Spanish locale for other countries
        );
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        // Test null input
        List<Locale> actual1 = LocaleUtils.languagesByCountry(null);
        assertTrue(actual1.isEmpty());

        // Test empty country code
        List<Locale> actual2 = LocaleUtils.languagesByCountry("");
        assertTrue(actual2.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic test
        String languageCode = "en";
        List<Locale> expected = Arrays.asList(
                new Locale("EN", "AG"),
                new Locale("EN", "AE"),
                new Locale("EN", "BW"),
                // ... other countries for English language ...
                new Locale("EN", "US"),
                new Locale("EN", "GB")
        );
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        // Test null input
        List<Locale> actual1 = LocaleUtils.countriesByLanguage(null);
        assertTrue(actual1.isEmpty());

        // Test empty language code
        List<Locale> actual2 = LocaleUtils.countriesByLanguage("");
        assertTrue(actual2.isEmpty());
    }
}