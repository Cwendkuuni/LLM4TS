import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test case
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));

        // Test with empty input
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));

        // Test with invalid input
        try {
            LocaleUtils.toLocale("en_US_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_variant");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_variant_variant");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_US_variant_variant_variant");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic test case
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertEquals(3, localeList.size());
        assertEquals(new Locale("fr", "CA", "xxx"), localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));

        // Test with null input
        assertTrue(LocaleUtils.localeLookupList(null).isEmpty());

        // Test with defaultLocale
        List<Locale> localeListDefault = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertEquals(4, localeListDefault.size());
        assertEquals(new Locale("fr", "CA", "xxx"), localeListDefault.get(0));
        assertEquals(new Locale("fr", "CA"), localeListDefault.get(1));
        assertEquals(new Locale("fr"), localeListDefault.get(2));
        assertEquals(new Locale("en"), localeListDefault.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic test case
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertTrue(localeList.size() > 0);

        // Test with empty list
        List<Locale> emptyLocaleList = Collections.emptyList();
        assertEquals(LocaleUtils.availableLocaleList(), emptyLocaleList);
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic test case
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));

        // Test with null input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test with unavailable locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr", "CA", "xxx")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic test case
        List<Locale> languageList = LocaleUtils.languagesByCountry("CA");
        assertEquals(2, languageList.size());
        assertTrue(languageList.contains(new Locale("en", "CA")));
        assertTrue(languageList.contains(new Locale("fr", "CA")));

        // Test with null input
        assertFalse(LocaleUtils.languagesByCountry(null).isEmpty());

        // Test with empty list
        List<Locale> emptyLanguageList = Collections.emptyList();
        assertEquals(LocaleUtils.languagesByCountry(null), emptyLanguageList);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic test case
        List<Locale> countryList = LocaleUtils.countriesByLanguage("en");
        assertEquals(3, countryList.size());
        assertTrue(countryList.contains(new Locale("CA", "en")));
        assertTrue(countryList.contains(new Locale("GB", "en")));
        assertTrue(countryList.contains(new Locale("US", "en")));

        // Test with null input
        assertFalse(LocaleUtils.countriesByLanguage(null).isEmpty());

        // Test with empty list
        List<Locale> emptyCountryList = Collections.emptyList();
        assertEquals(LocaleUtils.countriesByLanguage(null), emptyCountryList);
    }
}