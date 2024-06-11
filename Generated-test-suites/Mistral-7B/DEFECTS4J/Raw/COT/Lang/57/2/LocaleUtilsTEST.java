import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        // Test with null input
        assertNull(LocaleUtils.toLocale(null));

        // Test invalid format
        try {
            LocaleUtils.toLocale("enGB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_aB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_aB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_aB_cD");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, actualList);

        // Test null input
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));

        // Test default locale
        Locale defaultLocale = new Locale("en");
        List<Locale> actualListDefault = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expectedListDefault = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        assertEquals(expectedListDefault, actualListDefault);
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA", "xxx");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test null input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test invalid locale
        Locale invalidLocale = new Locale("xy", "xy");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));

        // Test unavailable locale
        Locale unavailableLocale = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(unavailableLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        String countryCode = "CA";
        List<Locale> expectedList = Arrays.asList(new Locale("fr", countryCode), new Locale("en", countryCode));
        List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedList, actualList);

        // Test null input
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));

        // Test empty list
        String emptyCountryCode = "";
        List<Locale> actualListEmpty = LocaleUtils.languagesByCountry(emptyCountryCode);
        List<Locale> expectedListEmpty = Collections.emptyList();
        assertEquals(expectedListEmpty, actualListEmpty);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        String languageCode = "fr";
        List<Locale> expectedList = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "BE"));
        List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedList, actualList);

        // Test null input
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));

        // Test empty list
        String emptyLanguageCode = "";
        List<Locale> actualListEmpty = LocaleUtils.countriesByLanguage(emptyLanguageCode);
        List<Locale> expectedListEmpty = Collections.emptyList();
        assertEquals(expectedListEmpty, actualListEmpty);
    }
}