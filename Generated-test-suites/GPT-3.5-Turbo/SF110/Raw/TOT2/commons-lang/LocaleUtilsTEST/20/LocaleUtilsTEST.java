import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertEquals(Locale.ROOT, LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("invalid#format");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_ShortFormat() {
        LocaleUtils.toLocale("a");
    }

    // Add more test cases for edge cases and exceptions for the toLocale method

    @Test
    public void testLocaleLookupList() {
        // Test case for localeLookupList method
    }

    @Test
    public void testAvailableLocaleList() {
        // Test case for availableLocaleList method
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test case for availableLocaleSet method
    }

    @Test
    public void testIsAvailableLocale() {
        // Test case for isAvailableLocale method
    }

    @Test
    public void testLanguagesByCountry() {
        // Test case for languagesByCountry method
    }

    @Test
    public void testCountriesByLanguage() {
        // Test case for countriesByLanguage method
    }

    // Add more test cases for other methods in the LocaleUtils class

}