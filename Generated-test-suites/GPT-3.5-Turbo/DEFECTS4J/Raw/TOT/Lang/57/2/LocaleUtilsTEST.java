import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    private LocaleUtils localeUtils;

    @Before
    public void setUp() {
        localeUtils = new LocaleUtils();
    }

    // Test cases for toLocale(String str) method

    @Test
    public void testToLocale_ValidInput_ReturnsLocale() {
        // Typical use case
        Locale expected = new Locale("en", "GB");
        Locale actual = localeUtils.toLocale("en_GB");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLength_ThrowsIllegalArgumentException() {
        // Error scenario
        localeUtils.toLocale("en_GB_xxx");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageCode_ThrowsIllegalArgumentException() {
        // Error scenario
        localeUtils.toLocale("1a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidCountryCode_ThrowsIllegalArgumentException() {
        // Error scenario
        localeUtils.toLocale("en_gb");
    }

    // Test cases for localeLookupList(Locale locale) method

    @Test
    public void testLocaleLookupList_ValidLocale_ReturnsLocaleLookupList() {
        // Typical use case
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = List.of(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = localeUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        // Error scenario
        List<Locale> expected = List.of();
        List<Locale> actual = localeUtils.localeLookupList(null);
        assertEquals(expected, actual);
    }

    // Additional test cases for localeLookupList(Locale locale, Locale defaultLocale) method

    @Test
    public void testLocaleLookupList_ValidLocales_ReturnsLocaleLookupList() {
        // Typical use case
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = List.of(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        List<Locale> actual = localeUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);
    }

    // Additional test cases for availableLocaleList() method

    @Test
    public void testAvailableLocaleList_ReturnsListOfAvailableLocales() {
        // Typical use case
        List<Locale> availableLocales = localeUtils.availableLocaleList();
        assertNotNull(availableLocales);
        assertTrue(availableLocales.size() > 0);
        // Add more assertions if needed
    }

    // Additional test cases for availableLocaleSet() method

    @Test
    public void testAvailableLocaleSet_ReturnsSetOfAvailableLocales() {
        // Typical use case
        Set<Locale> availableLocales = localeUtils.availableLocaleSet();
        assertNotNull(availableLocales);
        assertTrue(availableLocales.size() > 0);
        // Add more assertions if needed
    }

    // Additional test cases for isAvailableLocale(Locale locale) method

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
        // Typical use case
        Locale locale = new Locale("en", "US");
        assertTrue(localeUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_UnavailableLocale_ReturnsFalse() {
        // Typical use case
        Locale locale = new Locale("xyz");
        assertFalse(localeUtils.isAvailableLocale(locale));
    }

    // Additional test cases for languagesByCountry(String countryCode) method

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsListOfLanguages() {
        // Typical use case
        String countryCode = "US";
        List<Locale> expected = List.of(new Locale("en"), new Locale("es"), new Locale("fr"));
        List<Locale> actual = localeUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        // Error scenario
        List<Locale> expected = List.of();
        List<Locale> actual = localeUtils.languagesByCountry(null);
        assertEquals(expected, actual);
    }

    // Additional test cases for countriesByLanguage(String languageCode) method

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsListOfCountries() {
        // Typical use case
        String languageCode = "en";
        List<Locale> expected = List.of(new Locale("US"), new Locale("GB"));
        List<Locale> actual = localeUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        // Error scenario
        List<Locale> expected = List.of();
        List<Locale> actual = localeUtils.countriesByLanguage(null);
        assertEquals(expected, actual);
    }

}