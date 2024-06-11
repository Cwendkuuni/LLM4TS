import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));

        // Edge cases and exception handling
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException for empty string");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en");
            fail("Expected IllegalArgumentException for incorrect format");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XXX");
            fail("Expected IllegalArgumentException for incorrect variant");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("es_AR_XXX_XXX");
            fail("Expected IllegalArgumentException for too many parts");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("fr_FR_aa");
            fail("Expected IllegalArgumentException for invalid language code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("fr_FR_AA");
            fail("Expected IllegalArgumentException for invalid country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("fr_FR_-_-");
            fail("Expected IllegalArgumentException for missing separator");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality
        List localeList = LocaleUtils.localeLookupList(new Locale("fr", "CA", "XXX"));
        List expectedList = Arrays.asList(new Locale("fr", "CA", "XXX"), new Locale("fr", "CA"), new Locale("fr"));
        assertEquals("Unexpected locale list", expectedList, localeList);

        localeList = LocaleUtils.localeLookupList(new Locale("fr", "CA"), new Locale("en"));
        expectedList = Arrays.asList(new Locale("fr", "CA", "XXX"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        assertEquals("Unexpected locale list", expectedList, localeList);

        // Edge cases and exception handling
        List localeListEmpty = LocaleUtils.localeLookupList(null);
        assertEquals("Unexpected locale list for null input", Collections.EMPTY_LIST, localeListEmpty);

        List localeListDefault = LocaleUtils.localeLookupList(new Locale("fr", "CA"), new Locale("en"), new Locale("es"));
        List expectedListDefault = Arrays.asList(new Locale("fr", "CA", "XXX"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"), new Locale("es"));
        assertEquals("Unexpected locale list", expectedListDefault, localeListDefault);

        List localeListSameLocale = LocaleUtils.localeLookupList(new Locale("fr", "CA"), new Locale("fr", "CA"));
        List expectedListSameLocale = Arrays.asList(new Locale("fr", "CA", "XXX"), new Locale("fr", "CA"), new Locale("fr"));
        assertEquals("Unexpected locale list", expectedListSameLocale, localeListSameLocale);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality
        List availableLocaleList = LocaleUtils.availableLocaleList();
        List expectedAvailableLocaleList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals("Unexpected available locale list", expectedAvailableLocaleList, availableLocaleList);

        // Edge cases and exception handling
        List availableLocaleListEmpty = LocaleUtils.availableLocaleList();
        assertEquals("Unexpected available locale list for empty list", Collections.EMPTY_LIST, availableLocaleListEmpty);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic functionality
        Set availableLocaleSet = LocaleUtils.availableLocaleSet();
        Set expectedAvailableLocaleSet = new HashSet(Locale.getAvailableLocales());
        assertEquals("Unexpected available locale set", expectedAvailableLocaleSet, availableLocaleSet);

        // Edge cases and exception handling
        Set availableLocaleSetEmpty = LocaleUtils.availableLocaleSet();
        assertEquals("Unexpected available locale set for empty set", Collections.EMPTY_SET, availableLocaleSetEmpty);
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "AR")));

        // Edge cases and exception handling
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality
        List languagesByCountryFr = LocaleUtils.languagesByCountry("FR");
        List expectedLanguagesByCountryFr = Arrays.asList(Locale.FRENCH);
        assertEquals("Unexpected languages by country for France", expectedLanguagesByCountryFr, languagesByCountryFr);

        // Edge cases and exception handling
        List languagesByCountryEmpty = LocaleUtils.languagesByCountry(null);
        assertEquals("Unexpected languages by country for null input", Collections.EMPTY_LIST, languagesByCountryEmpty);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality
        List countriesByLanguageEs = LocaleUtils.countriesByLanguage("ES");
        List expectedCountriesByLanguageEs = Arrays.asList(Locale.ARGENTINA, Locale.ESPANA);
        assertEquals("Unexpected countries by language for Spanish", expectedCountriesByLanguageEs, countriesByLanguageEs);

        // Edge cases and exception handling
        List countriesByLanguageEmpty = LocaleUtils.countriesByLanguage(null);
        assertEquals("Unexpected countries by language for null input", Collections.EMPTY_LIST, countriesByLanguageEmpty);
    }
}