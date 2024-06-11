import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("de", LocaleUtils.toLocale("de").toString());

        // Edge cases and exception handling tests
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(3, localeList.size());
        assertEquals(Locale.ENGLISH, localeList.get(0));
        assertEquals(Locale.ENGLISH, localeList.get(1));
        assertEquals(Locale.US, localeList.get(2));

        // Edge cases and exception handling tests
        try {
            LocaleUtils.localeLookupList(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList("en");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected, as the list is unmodifiable
        }

        try {
            LocaleUtils.localeLookupList(Locale.US, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ITALIAN));

        // Edge cases and exception handling tests
        try {
            LocaleUtils.isAvailableLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List<Locale> languagesByCountryFrance = LocaleUtils.languagesByCountry("FR");
        assertEquals(2, languagesByCountryFrance.size());
        assertEquals(Locale.FRENCH, languagesByCountryFrance.get(0));
        assertEquals(Locale.FRENCH_CANADA, languagesByCountryFrance.get(1));

        // Edge cases and exception handling tests
        try {
            LocaleUtils.languagesByCountry(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List<Locale> countriesByLanguageEnglish = LocaleUtils.countriesByLanguage("en");
        assertEquals(3, countriesByLanguageEnglish.size());
        assertEquals(Locale.US, countriesByLanguageEnglish.get(0));
        assertEquals(Locale.GB, countriesByLanguageEnglish.get(1));
        assertEquals(Locale.CA, countriesByLanguageEnglish.get(2));

        // Edge cases and exception handling tests
        try {
            LocaleUtils.countriesByLanguage(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}