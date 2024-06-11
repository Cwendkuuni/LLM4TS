import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    // Test cases for toLocale() method

    // Test case for a null input
    @Test
    public void testToLocaleNullInput() {
        String input = null;
        assertNull(LocaleUtils.toLocale(input));
    }

    // Test case for an empty input
    @Test
    public void testToLocaleEmptyInput() {
        String input = "";
        Locale expected = new Locale("", "");
        assertEquals(expected, LocaleUtils.toLocale(input));
    }

    // Test case for an input with invalid format
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        String input = "en#US";
        LocaleUtils.toLocale(input);
    }

    // Test case for a valid language code input
    @Test
    public void testToLocaleValidLanguageCode() {
        String input = "en";
        Locale expected = new Locale("en");
        assertEquals(expected, LocaleUtils.toLocale(input));
    }

    // Test case for a valid language and country code input
    @Test
    public void testToLocaleValidLanguageAndCountryCode() {
        String input = "en_US";
        Locale expected = new Locale("en", "US");
        assertEquals(expected, LocaleUtils.toLocale(input));
    }

    // Test case for a valid language, country and variant code input
    @Test
    public void testToLocaleValidLanguageCountryAndVariantCode() {
        String input = "en_US_VAR";
        Locale expected = new Locale("en", "US", "VAR");
        assertEquals(expected, LocaleUtils.toLocale(input));
    }

    // Test case for a valid underscore prefix followed by language and country code input
    @Test
    public void testToLocaleValidUnderscorePrefixLanguageAndCountryCode() {
        String input = "_en_US";
        Locale expected = new Locale("", "en", "US");
        assertEquals(expected, LocaleUtils.toLocale(input));
    }

    // Test case for a valid underscore prefix followed by language, country and variant code input
    @Test
    public void testToLocaleValidUnderscorePrefixLanguageCountryAndVariantCode() {
        String input = "_en_US_VAR";
        Locale expected = new Locale("", "en", "US_VAR");
        assertEquals(expected, LocaleUtils.toLocale(input));
    }

    // Test case for a lowercase language and uppercase country code input
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLowerCaseLanguageUpperCaseCountryCode() {
        String input = "en_USA";
        LocaleUtils.toLocale(input);
    }

    // Test case for invalid lowercase language code
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLowerCaseLanguageCode() {
        String input = "english";
        LocaleUtils.toLocale(input);
    }

    // Test case for invalid uppercase language code
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidUpperCaseLanguageCode() {
        String input = "ENG";
        LocaleUtils.toLocale(input);
    }

    // Test case for invalid length of language code
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLengthLanguageCode() {
        String input = "en_USA";
        LocaleUtils.toLocale(input);
    }

    // Test cases for localeLookupList() method

    // Test case for locale and defaultLocale being null
    @Test
    public void testLocaleLookupListNullInput() {
        Locale locale = null;
        Locale defaultLocale = null;
        List<Locale> expected = new ArrayList<Locale>();
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    // Test case for locale and defaultLocale both being valid
    @Test
    public void testLocaleLookupListValidInput() {
        Locale locale = new Locale("en_US");
        Locale defaultLocale = new Locale("fr_FR");
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(locale);
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", ""));
        expected.add(defaultLocale);
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    // Test case for locale and defaultLocale being the same
    @Test
    public void testLocaleLookupListSameInput() {
        Locale locale = new Locale("en_US");
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(locale);
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", ""));
        assertEquals(expected, LocaleUtils.localeLookupList(locale, locale));
    }

    // Test cases for availableLocaleList() method

    // Test case for availableLocaleList()
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }

    // Test cases for availableLocaleSet() method

    // Test case for availableLocaleSet()
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }

    // Test cases for isAvailableLocale() method

    // Test case for a null locale
    @Test
    public void testIsAvailableLocaleNullLocale() {
        Locale locale = null;
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    // Test case for an available locale
    @Test
    public void testIsAvailableLocaleAvailableLocale() {
        Locale locale = Locale.US;
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    // Test case for an unavailable locale
    @Test
    public void testIsAvailableLocaleUnavailableLocale() {
        Locale locale = new Locale("xx_XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    // Test cases for languagesByCountry() method

    // Test case for a null country code
    @Test
    public void testLanguagesByCountryNullCountryCode() {
        String countryCode = null;
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.languagesByCountry(countryCode));
    }

    // Test case for a valid country code
    @Test
    public void testLanguagesByCountryValidCountryCode() {
        String countryCode = "US";
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(Locale.US);
        assertEquals(expected, LocaleUtils.languagesByCountry(countryCode));
    }

    // Test case for an invalid country code
    @Test
    public void testLanguagesByCountryInvalidCountryCode() {
        String countryCode = "XX";
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.languagesByCountry(countryCode));
    }

    // Test cases for countriesByLanguage() method

    // Test case for a null language code
    @Test
    public void testCountriesByLanguageNullLanguageCode() {
        String languageCode = null;
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.countriesByLanguage(languageCode));
    }

    // Test case for a valid language code
    @Test
    public void testCountriesByLanguageValidLanguageCode() {
        String languageCode = "en";
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(Locale.US);
        assertEquals(expected, LocaleUtils.countriesByLanguage(languageCode));
    }

    // Test case for an invalid language code
    @Test
    public void testCountriesByLanguageInvalidLanguageCode() {
        String languageCode = "xx";
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.countriesByLanguage(languageCode));
    }

}