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
            fail("Expected IllegalArgumentException for empty string input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_aA"); // invalid language code
            fail("Expected IllegalArgumentException for invalid locale format");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_aB_aC"); // invalid country code
            fail("Expected IllegalArgumentException for invalid locale format");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_aa_AA"); // invalid variant
            fail("Expected IllegalArgumentException for invalid locale format");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_12"); // invalid separator
            fail("Expected IllegalArgumentException for invalid locale format");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("ja_JP_sh_JP"); // valid locale but not available
            fail("Expected IllegalArgumentException for unavailable locale");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(3, localeList.size());
        // Check the order of the list
        assertEquals("en_US", localeList.get(0).toString());
        assertEquals("en", localeList.get(1).toString());
        assertEquals("", localeList.get(2).toString());

        // Edge cases and exception handling tests
        try {
            LocaleUtils.localeLookupList(null);
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList("");
            fail("Expected IllegalArgumentException for empty string input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(Locale.ROOT);
            fail("Expected empty list for root locale");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(Locale.CHINESE);
            fail("Expected empty list for unavailable locale");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(Locale.TRADITIONAL_CHINESE, Locale.US);
            fail("Expected list to not contain the same locale twice");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertTrue(!localeList.isEmpty());

        // Edge cases and exception handling tests
        try {
            LocaleUtils.availableLocaleList(); // already initialized
            fail("Expected no exception for already initialized list");
        } catch (IllegalStateException e) {
            // Not expected
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic functionality test
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertTrue(!localeSet.isEmpty());

        // Edge cases and exception handling tests
        try {
            LocaleUtils.availableLocaleSet(); // already initialized
            fail("Expected no exception for already initialized set");
        } catch (IllegalStateException e) {
            // Not expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));

        // Edge cases and exception handling tests
        assertTrue(!LocaleUtils.isAvailableLocale(null));
        assertTrue(!LocaleUtils.isAvailableLocale(""));
        assertFalse(LocaleUtils.isAvailableLocale("en_aA"));
        assertFalse(LocaleUtils.isAvailableLocale("en_aB_aC"));
        assertFalse(LocaleUtils.isAvailableLocale("en_GB_aa_AA"));
        assertFalse(LocaleUtils.isAvailableLocale("en_12"));
        assertFalse(LocaleUtils.isAvailableLocale("ja_JP_sh_JP"));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List<Locale> langList = LocaleUtils.languagesByCountry("US");
        assertEquals(2, langList.size());
        assertEquals("en", langList.get(0).getLanguage());
        assertEquals("es", langList.get(1).getLanguage());

        // Edge cases and exception handling tests
        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
        assertTrue(LocaleUtils.languagesByCountry("").isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List<Locale> countryList = LocaleUtils.countriesByLanguage("en");
        assertEquals(4, countryList.size());
        assertEquals("US", countryList.get(0).getCountry());
        assertEquals("CA", countryList.get(1).getCountry());
        assertEquals("GB", countryList.get(2).getCountry());
        assertEquals("IE", countryList.get(3).getCountry());

        // Edge cases and exception handling tests
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
        assertTrue(LocaleUtils.countriesByLanguage("").isEmpty());
    }
}