import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_FR", LocaleUtils.toLocale("fr_FR").toString());
        assertEquals("de_DE", LocaleUtils.toLocale("de_DE").toString());

        // Test invalid inputs
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en");
            fail("Expected IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_invalid");
            fail("Expected IllegalArgumentException for invalid variant");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List lookupList = LocaleUtils.localeLookupList(locale);

        // Test with valid locale
        assertEquals(locale, lookupList.get(0));
        assertEquals("fr_CA", lookupList.get(1).toString());
        assertEquals("fr", lookupList.get(2).toString());
        assertEquals("en", lookupList.get(3).toString());

        // Test with null locale
        List emptyList = LocaleUtils.localeLookupList(null);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocales = LocaleUtils.availableLocaleList();

        // Test with valid input
        assertFalse(availableLocales.isEmpty());
        assertTrue(availableLocales.contains(Locale.US));
        assertTrue(availableLocales.contains(Locale.FRENCH));

        // Test with null input
        List emptyList = LocaleUtils.availableLocaleList(null);
        assertTrue(emptyList.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsAvailableLocaleWithInvalidLocale() {
        LocaleUtils.isAvailableLocale(null);
    }

    @Test
    public void testLanguagesByCountry() {
        List languagesByFrance = LocaleUtils.languagesByCountry("FR");

        // Test with valid input
        assertFalse(languagesByFrance.isEmpty());
        assertEquals("fr", languagesByFrance.get(0).getLanguage());
        assertEquals("fr_BE", languagesByFrance.get(1).toString());
        assertEquals("fr_CH", languagesByFrance.get(2).toString());

        // Test with null input
        List emptyList = LocaleUtils.languagesByCountry(null);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List countriesByEnglish = LocaleUtils.countriesByLanguage("en");

        // Test with valid input
        assertFalse(countriesByEnglish.isEmpty());
        assertEquals("US", countriesByEnglish.get(0).getCountry());
        assertEquals("GB", countriesByEnglish.get(1).getCountry());

        // Test with null input
        List emptyList = LocaleUtils.countriesByLanguage(null);
        assertTrue(emptyList.isEmpty());
    }
}