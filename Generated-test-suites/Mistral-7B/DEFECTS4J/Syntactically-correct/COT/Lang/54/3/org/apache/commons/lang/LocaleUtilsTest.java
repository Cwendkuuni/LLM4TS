import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));

        // Edge cases and exception handling tests
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, language code must be lowercase
        }

        try {
            LocaleUtils.toLocale("en_X");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, invalid country code
        }

        try {
            LocaleUtils.toLocale("en_GB_X");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, invalid variant
        }

        try {
            LocaleUtils.toLocale("xx_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, invalid language code
        }

        try {
            LocaleUtils.toLocale("en_GB_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, invalid country code
        }

        try {
            LocaleUtils.toLocale("en_GB_XX_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, invalid variant
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        List availableLocales = LocaleUtils.availableLocaleList();
        List expectedLocales = Arrays.asList(new Locale("en", "US"), new Locale("fr", "FR"), new Locale("de", "DE"));
        assertEquals(expectedLocales, LocaleUtils.localeLookupList(new Locale("en", "US", "XXX"), new Locale("fr", "FR")));

        // Edge cases and exception handling tests
        try {
            LocaleUtils.localeLookupList(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("en", "US"));
            fail("Expected empty list");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("en", "US"), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List availableLocales = LocaleUtils.availableLocaleList();
        List expectedLocales = Arrays.asList(new Locale("ar"), new Locale("de"), new Locale("en"), new Locale("es"), new Locale("fr"), new Locale("it"), new Locale("ja"), new Locale("ko"), new Locale("pt"), new Locale("ru"), new Locale("zh"));
        assertEquals(expectedLocales, availableLocales);

        // Edge cases and exception handling tests
        try {
            LocaleUtils.availableLocaleList();
            fail("Expected empty list");
        } catch (Exception e) {
            // Expected, method should not throw an exception
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "FR")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));

        // Edge cases and exception handling tests
        try {
            LocaleUtils.isAvailableLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.isAvailableLocale(new Locale("en", "US"));
            fail("Expected true");
        } catch (AssertionError e) {
            // Expected
        }

        try {
            LocaleUtils.isAvailableLocale(new Locale("en", "US"), new Locale("fr", "FR"));
            fail("Expected true");
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List languages = LocaleUtils.languagesByCountry("US");
        List expectedLanguages = Arrays.asList(new Locale("en"));
        assertEquals(expectedLanguages, languages);

        // Edge cases and exception handling tests
        try {
            LocaleUtils.languagesByCountry(null);
            fail("Expected empty list");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.languagesByCountry("XX");
            fail("Expected empty list");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.languagesByCountry("");
            fail("Expected empty list");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List countries = LocaleUtils.countriesByLanguage("en");
        List expectedCountries = Arrays.asList(new Locale("US"), new Locale("CA"), new Locale("GB"), new Locale("AU"));
        assertEquals(expectedCountries, countries);

        // Edge cases and exception handling tests
        try {
            LocaleUtils.countriesByLanguage(null);
            fail("Expected empty list");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.countriesByLanguage("XX");
            fail("Expected empty list");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.countriesByLanguage("");
            fail("Expected empty list");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}