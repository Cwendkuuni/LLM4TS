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
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException for empty input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("xx_XX");
            fail("Expected IllegalArgumentException for invalid length");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_aA");
            fail("Expected IllegalArgumentException for invalid country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_invalid");
            fail("Expected IllegalArgumentException for invalid variant");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xyzzy");
            fail("Expected IllegalArgumentException for invalid variant");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.US);
        List<Locale> expectedLocaleList = Arrays.asList(Locale.US, Locale.ENGLISH);
        assertEquals(expectedLocaleList, localeList);

        localeList = LocaleUtils.localeLookupList(Locale.US, Locale.FRENCH);
        List<Locale> expectedLocaleList2 = Arrays.asList(Locale.US, Locale.ENGLISH, Locale.FRENCH);
        assertEquals(expectedLocaleList2, localeList);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> expectedAvailableLocaleList = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPAN, Locale.KOREAN, Locale.RUSSIAN, Locale.SPANISH);
        assertEquals(expectedAvailableLocaleList, availableLocaleList);
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));

        // Edge cases and exception handling tests
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(""));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.CHINESE));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List<Locale> languagesByCountryEN = LocaleUtils.languagesByCountry("EN");
        List<Locale> expectedLanguagesByCountryEN = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH_AU, Locale.ENGLISH_CA, Locale.ENGLISH_GB, Locale.ENGLISH_IE, Locale.ENGLISH_US);
        assertEquals(expectedLanguagesByCountryEN, languagesByCountryEN);

        // Edge cases and exception handling tests
        List<Locale> languagesByCountryEmpty = LocaleUtils.languagesByCountry(null);
        assertTrue(languagesByCountryEmpty.isEmpty());

        List<Locale> languagesByCountryInvalidCountry = LocaleUtils.languagesByCountry("XX");
        assertTrue(languagesByCountryInvalidCountry.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List<Locale> countriesByLanguageEN = LocaleUtils.countriesByLanguage("EN");
        List<Locale> expectedCountriesByLanguageEN = Arrays.asList(Locale.ENGLISH_AU, Locale.ENGLISH_BW, Locale.ENGLISH_CA, Locale.ENGLISH_IE, Locale.ENGLISH_IN, Locale.ENGLISH_KE, Locale.ENGLISH_NG, Locale.ENGLISH_NZ, Locale.ENGLISH_PK, Locale.ENGLISH_SG, Locale.ENGLISH_TZ, Locale.ENGLISH_US, Locale.ENGLISH_ZA);
        assertEquals(expectedCountriesByLanguageEN, countriesByLanguageEN);

        // Edge cases and exception handling tests
        List<Locale> countriesByLanguageEmpty = LocaleUtils.countriesByLanguage(null);
        assertTrue(countriesByLanguageEmpty.isEmpty());

        List<Locale> countriesByLanguageInvalidLanguage = LocaleUtils.countriesByLanguage("XX");
        assertTrue(countriesByLanguageInvalidLanguage.isEmpty());
    }
}