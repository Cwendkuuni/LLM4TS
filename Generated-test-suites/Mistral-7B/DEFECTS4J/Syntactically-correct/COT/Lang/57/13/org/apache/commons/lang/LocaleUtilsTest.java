import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("en", LocaleUtils.toLocale("en").toString());

        // Invalid input
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("ab");
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
        // Basic functionality
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.FRENCH);
        List<Locale> expectedList = Arrays.asList(Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.ENGLISH);
        assertEquals(expectedList, localeList);

        // Input as null or empty list
        localeList = LocaleUtils.localeLookupList((Locale) null);
        assertEquals(Collections.emptyList(), localeList);
        localeList = LocaleUtils.localeLookupList(new ArrayList<>());
        assertEquals(Collections.emptyList(), localeList);

        // Input with defaultLocale as null
        localeList = LocaleUtils.localeLookupList(Locale.FRENCH, null);
        assertEquals(Collections.singletonList(Locale.FRENCH), localeList);

        // Input with defaultLocale not empty
        localeList = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        expectedList = Arrays.asList(Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.ENGLISH);
        assertEquals(expectedList, localeList);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        List<Locale> expectedAvailableLocales = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH);
        assertEquals(expectedAvailableLocales, availableLocales);

        // Input as null
        try {
            LocaleUtils.availableLocaleList(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic functionality
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        Set<Locale> expectedAvailableLocales = new HashSet<>(Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH));
        assertEquals(expectedAvailableLocales, availableLocales);

        // Input as null
        try {
            LocaleUtils.availableLocaleSet(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SIMPLIFIED_CHINESE));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("ab", "CD")));

        // Input as null
        try {
            LocaleUtils.isAvailableLocale(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        List<Locale> expectedLanguages = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH, Locale.ENGLISH);
        assertEquals(expectedLanguages, languages);

        // Input as null
        try {
            LocaleUtils.languagesByCountry(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedCountries = Arrays.asList(Locale.US, Locale.UK, Locale.CA);
        assertEquals(expectedCountries, countries);

        // Input as null
        try {
            LocaleUtils.countriesByLanguage(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}