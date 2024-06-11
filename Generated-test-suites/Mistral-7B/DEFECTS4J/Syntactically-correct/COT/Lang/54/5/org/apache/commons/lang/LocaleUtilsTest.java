import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        // Edge cases and exception handling
        // Test null input
        assertNull(LocaleUtils.toLocale(null));

        // Test empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid format - missing language code
        try {
            LocaleUtils.toLocale("GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid format - missing separator
        try {
            LocaleUtils.toLocale("enGB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid format - missing country code
        try {
            LocaleUtils.toLocale("en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid format - invalid language code
        try {
            LocaleUtils.toLocale("xyz_GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid format - invalid country code
        try {
            LocaleUtils.toLocale("en_XYZ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid format - length is incorrect
        try {
            LocaleUtils.toLocale("en_GB_xyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid format - invalid separator
        try {
            LocaleUtils.toLocale("en_GB_xyz_XYZ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale expected = new Locale("fr", "CA");
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual.get(0));
        assertEquals(expected, actual.get(1));
        assertEquals(new Locale("fr", "CA"), actual.get(2));
        assertEquals(new Locale("fr"), actual.get(3));

        // Test empty input
        Locale emptyLocale = null;
        actual = LocaleUtils.localeLookupList(emptyLocale);
        assertTrue(actual.isEmpty());

        // Test defaultLocale is not in the list
        Locale defaultLocale = new Locale("en");
        actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertFalse(actual.contains(defaultLocale));

        // Test defaultLocale is the first in the list
        actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(defaultLocale, actual.get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> actual = LocaleUtils.availableLocaleList();
        // Perform some checks on the list to ensure it's not empty
        // and contains a known locale
        assertTrue(actual.size() > 0);
        assertTrue(actual.contains(Locale.ENGLISH));

        // Test empty list
        List<Locale> emptyList = Collections.emptyList();
        assertEquals(emptyList, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test null input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test empty locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("")));

        // Test invalid locale
        Locale invalidLocale = new Locale("xyz", "XYZ");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        // Perform some checks on the list to ensure it's not empty
        // and contains a known locale
        assertTrue(actual.size() > 0);
        assertTrue(actual.contains(new Locale("en", "CA")));

        // Test empty list
        List<Locale> emptyList = Collections.emptyList();
        assertEquals(emptyList, LocaleUtils.languagesByCountry(null));

        // Test empty country code
        assertEquals(emptyList, LocaleUtils.languagesByCountry(""));
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        // Perform some checks on the list to ensure it's not empty
        // and contains a known locale
        assertTrue(actual.size() > 0);
        assertTrue(actual.contains(Locale.CANADA));

        // Test empty list
        List<Locale> emptyList = Collections.emptyList();
        assertEquals(emptyList, LocaleUtils.countriesByLanguage(null));

        // Test empty language code
        assertEquals(emptyList, LocaleUtils.countriesByLanguage(""));
    }
}