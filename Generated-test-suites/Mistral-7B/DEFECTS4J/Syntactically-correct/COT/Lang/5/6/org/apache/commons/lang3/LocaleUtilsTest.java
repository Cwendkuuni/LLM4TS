import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());

        // Edge cases and exceptions
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
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_xx_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic test
        assertEquals(Arrays.asList(new Locale("en", "US"), new Locale("en"), new Locale("en_US")),
                LocaleUtils.localeLookupList(new Locale("en", "US", "US")));

        // Edge cases and exceptions
        try {
            LocaleUtils.localeLookupList(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("en", "US"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("en", "US", "US"), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic test
        assertEquals(LocaleUtils.availableLocaleList(), Arrays.asList(Locale.getAvailableLocales()));

        // Edge cases and exceptions
        try {
            LocaleUtils.availableLocaleList();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic test
        assertEquals(LocaleUtils.availableLocaleSet(), new HashSet<>(Locale.getAvailableLocales()));

        // Edge cases and exceptions
        try {
            LocaleUtils.availableLocaleSet();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic test
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));

        // Edge cases and exceptions
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(""));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "USA")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "US", "US_US")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic test
        assertEquals(Arrays.asList(new Locale("fr", "CA"), new Locale("fr")),
                LocaleUtils.languagesByCountry("CA"));

        // Edge cases and exceptions
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(""));
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic test
        assertEquals(Arrays.asList(new Locale("fr", "CA"), new Locale("fr")),
                LocaleUtils.countriesByLanguage("fr"));

        // Edge cases and exceptions
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(""));
    }

}