import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
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
            // Expected, the language code must be lowercase
        }

        try {
            LocaleUtils.toLocale("en_aA");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, the country code must be uppercase
        }

        try {
            LocaleUtils.toLocale("en_aA_xyZ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, the separator must be an underscore
        }

        try {
            LocaleUtils.toLocale("en_aA_xyZ_abc");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, the length must be correct
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        assertEquals(expected, actual);

        // Edge cases and exception handling tests
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(Locale.ENGLISH));
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Locale.getAvailableLocales();
        assertEquals(expected, actual);

        // Edge cases and exception handling tests
        try {
            LocaleUtils.availableLocaleList();
            fail("Expected NPE");
        } catch (NullPointerException e) {
            // Expected, if list == null
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr", "CA", "xxx")));

        // Edge cases and exception handling tests
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List<Locale> expected = Arrays.asList(Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN);
        List<Locale> actual = LocaleUtils.languagesByCountry("IT");
        assertEquals(expected, actual);

        // Edge cases and exception handling tests
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List<Locale> expected = Arrays.asList(Locale.ITALIAN, Locale.FRENCH, Locale.GERMAN);
        List<Locale> actual = LocaleUtils.countriesByLanguage("FRE");
        assertEquals(expected, actual);

        // Edge cases and exception handling tests
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}