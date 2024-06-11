import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    // Test cases for toLocale() method

    @Test
    public void testToLocale_WithValidLocaleString_ReturnsLocaleObject() {
        String localeStr = "en_GB";
        Locale expectedLocale = new Locale("en", "GB");
        Locale actualLocale = LocaleUtils.toLocale(localeStr);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocale_WithTwoLetterLanguageCode_ReturnsLocaleObjectWithEmptyCountry() {
        String localeStr = "en";
        Locale expectedLocale = new Locale("en", "");
        Locale actualLocale = LocaleUtils.toLocale(localeStr);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocale_WithFiveCharacterString_ReturnsLocaleObjectWithEmptyVariant() {
        String localeStr = "en_GB_xxx";
        Locale expectedLocale = new Locale("en", "GB", "");
        Locale actualLocale = LocaleUtils.toLocale(localeStr);
        assertEquals(expectedLocale, actualLocale);
    }

    // ... additional test cases for toLocale() method

    // Test cases for localeLookupList() method

    @Test
    public void testLocaleLookupList_WithValidLocale_ReturnsListOfLocalesStartingFromMostSpecificToDefault() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            Locale.getDefault()
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, actualList);
    }

    // ... additional test cases for localeLookupList() method

    // Test cases for availableLocaleList() method

    @Test
    public void testAvailableLocaleList_ReturnsUnmodifiableListOfInstalledLocales() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        try {
            locales.add(new Locale("test"));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Exception expected, test passes
        }
    }

    // ... additional test cases for availableLocaleList() method

    // Test cases for availableLocaleSet() method

    @Test
    public void testAvailableLocaleSet_ReturnsUnmodifiableSetOfInstalledLocales() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        try {
            locales.add(new Locale("test"));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Exception expected, test passes
        }
    }

    // ... additional test cases for availableLocaleSet() method

    // Test cases for isAvailableLocale() method

    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ReturnsTrue() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_WithUnavailableLocale_ReturnsFalse() {
        Locale locale = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    // ... additional test cases for isAvailableLocale() method

    // Test cases for languagesByCountry() method

    @Test
    public void testLanguagesByCountry_WithValidCountryCode_ReturnsListOfLanguagesSupported() {
        String countryCode = "US";
        List<Locale> expectedLanguages = Arrays.asList(
            new Locale("en", "US"),
            new Locale("es", "US")
        );
        List<Locale> actualLanguages = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testLanguagesByCountry_WithInvalidCountryCode_ReturnsEmptyList() {
        String countryCode = "XX";
        List<Locale> expectedLanguages = Collections.emptyList();
        List<Locale> actualLanguages = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLanguages, actualLanguages);
    }

    // ... additional test cases for languagesByCountry() method

    // Test cases for countriesByLanguage() method

    @Test
    public void testCountriesByLanguage_WithValidLanguageCode_ReturnsListOfCountriesSupported() {
        String languageCode = "en";
        List<Locale> expectedCountries = Arrays.asList(
            new Locale("en", "US"),
            new Locale("en", "GB"),
            new Locale("en", "CA")
        );
        List<Locale> actualCountries = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedCountries, actualCountries);
    }

    @Test
    public void testCountriesByLanguage_WithInvalidLanguageCode_ReturnsEmptyList() {
        String languageCode = "xx";
        List<Locale> expectedCountries = Collections.emptyList();
        List<Locale> actualCountries = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedCountries, actualCountries);
    }

    // ... additional test cases for countriesByLanguage() method

}