import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr"), LocaleUtils.toLocale("fr"));
        assertEquals(new Locale("es", "ES"), LocaleUtils.toLocale("es_ES"));

        // Edge cases and exception handling tests
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidLocale1 = "en_GB_invalid";
        try {
            LocaleUtils.toLocale(invalidLocale1);
            fail("Expected IllegalArgumentException: Invalid locale format: " + invalidLocale1);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale1, e.getMessage());
        }

        String invalidLocale2 = "en_GB_XXX";
        try {
            LocaleUtils.toLocale(invalidLocale2);
            fail("Expected IllegalArgumentException: Invalid locale format: " + invalidLocale2);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale2, e.getMessage());
        }

        String invalidLocale3 = "en_GB_xx";
        try {
            LocaleUtils.toLocale(invalidLocale3);
            fail("Expected IllegalArgumentException: Invalid locale format: " + invalidLocale3);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale3, e.getMessage());
        }

        String invalidLocale4 = "en_aa";
        try {
            LocaleUtils.toLocale(invalidLocale4);
            fail("Expected IllegalArgumentException: Invalid locale format: " + invalidLocale4);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale4, e.getMessage());
        }

        String invalidLocale5 = "en_AA";
        try {
            LocaleUtils.toLocale(invalidLocale5);
            fail("Expected IllegalArgumentException: Invalid locale format: " + invalidLocale5);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale5, e.getMessage());
        }

        String invalidLocale6 = "en_GB_en";
        try {
            LocaleUtils.toLocale(invalidLocale6);
            fail("Expected IllegalArgumentException: Invalid locale format: " + invalidLocale6);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale6, e.getMessage());
        }

        String invalidLocale7 = "en_GB_EN";
        try {
            LocaleUtils.toLocale(invalidLocale7);
            fail("Expected IllegalArgumentException: Invalid locale format: " + invalidLocale7);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale7, e.getMessage());
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale expectedLocale = new Locale("fr", "CA", "XXX");
        List<Locale> localeList = LocaleUtils.localeLookupList(expectedLocale);
        assertEquals(Arrays.asList(expectedLocale, new Locale("fr", "CA"), new Locale("fr")), localeList);

        // Edge cases and exception handling tests
        Locale locale = null;
        List<Locale> localeListNull = LocaleUtils.localeLookupList(locale);
        assertTrue(localeListNull.isEmpty());

        Locale invalidLocale = new Locale("fr", "AA");
        List<Locale> localeListInvalidLocale = LocaleUtils.localeLookupList(invalidLocale);
        assertTrue(localeListInvalidLocale.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        // Basic functionality test
        assertTrue(availableLocaleList.size() > 0);

        // Edge cases and exception handling tests
        List<Locale> emptyAvailableLocaleList = Collections.emptyList();
        try {
            LocaleUtils.availableLocaleList();
            fail("Expected empty list");
        } catch (UnsupportedOperationException e) {
            // Expected
            assertEquals(emptyAvailableLocaleList, LocaleUtils.availableLocaleList());
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        // Basic functionality test
        assertTrue(availableLocaleSet.size() > 0);

        // Edge cases and exception handling tests
        Set<Locale> emptyAvailableLocaleSet = Collections.emptySet();
        try {
            LocaleUtils.availableLocaleSet();
            fail("Expected empty set");
        } catch (UnsupportedOperationException e) {
            // Expected
            assertEquals(emptyAvailableLocaleSet, LocaleUtils.availableLocaleSet());
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "XX")));

        // Edge cases and exception handling tests
        Locale invalidLocale = new Locale("en", "AA");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        List<Locale> localeList = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedLocaleList, localeList);

        // Edge cases and exception handling tests
        List<Locale> emptyLocaleList = Collections.emptyList();
        assertEquals(emptyLocaleList, LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("ca", "CA"), new Locale("es", "ES"), new Locale("pt", "PT"));
        List<Locale> localeList = LocaleUtils.countriesByLanguage("es");
        assertEquals(expectedLocaleList, localeList);

        // Edge cases and exception handling tests
        List<Locale> emptyLocaleList = Collections.emptyList();
        assertEquals(emptyLocaleList, LocaleUtils.countriesByLanguage(null));
    }
}