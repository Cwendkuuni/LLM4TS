
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_WithNullString_ShouldReturnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_WithEmptyString_ShouldReturnLocaleWithEmptyLanguageAndCountry() {
        Locale expected = new Locale("", "");
        assertEquals(expected, LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_WithInvalidFormatString_ShouldThrowIllegalArgumentException() {
        String invalidFormat = "invalid#format";
        try {
            LocaleUtils.toLocale(invalidFormat);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidFormat, e.getMessage());
        }
    }

    // Edge cases and exception handling scenarios for toLocale method can be added here

    @Test
    public void testLocaleLookupList_WithNullLocale_ShouldReturnEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_WithLocale_ShouldReturnListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale(locale.getLanguage(), locale.getCountry())));
        assertTrue(result.contains(new Locale(locale.getLanguage(), "")));
        assertTrue(result.contains(defaultLocale));
    }

    // Edge cases and exception handling scenarios for localeLookupList method can be added here

    @Test
    public void testAvailableLocaleList_ShouldReturnNonEmptyList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertFalse(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_ShouldReturnNonEmptySet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertFalse(result.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ShouldReturnTrue() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_WithUnavailableLocale_ShouldReturnFalse() {
        Locale locale = new Locale("xx", "YY");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    // Edge cases and exception handling scenarios for isAvailableLocale method can be added here

    @Test
    public void testLanguagesByCountry_WithNullCountryCode_ShouldReturnEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_WithExistingCountryCode_ShouldReturnListOfLanguages() {
        String countryCode = "US";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertFalse(result.isEmpty());
    }

    // Edge cases and exception handling scenarios for languagesByCountry method can be added here

    @Test
    public void testCountriesByLanguage_WithNullLanguageCode_ShouldReturnEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_WithExistingLanguageCode_ShouldReturnListOfCountries() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertFalse(result.isEmpty());
    }

    // Edge cases and exception handling scenarios for countriesByLanguage method can be added here

}
