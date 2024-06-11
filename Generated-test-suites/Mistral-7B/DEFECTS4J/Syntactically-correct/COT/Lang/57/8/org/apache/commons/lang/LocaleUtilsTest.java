import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr", LocaleUtils.toLocale("fr").getLanguage());
        assertEquals("CA", LocaleUtils.toLocale("fr_CA").getCountry());

        // Edge case: empty string
        assertNull(LocaleUtils.toLocale(""));

        // Edge case: invalid format
        try {
            LocaleUtils.toLocale("en_GB_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Edge case: null input
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        List localeList = LocaleUtils.localeLookupList(new Locale("fr", "CA", "XX"));
        assertEquals(3, localeList.size());
        assertEquals("fr_CA_XX", localeList.get(0).toString());
        assertEquals("fr_CA", localeList.get(1).toString());
        assertEquals("fr", localeList.get(2).toString());

        // Edge case: null input
        assertEquals(0, LocaleUtils.localeLookupList(null).size());

        // Edge case: empty list
        assertEquals(0, LocaleUtils.localeLookupList(Locale.ROOT).size());
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List localeList = LocaleUtils.availableLocaleList();
        assertEquals(Locale.SIMPLIFIED_CHINESE, localeList.get(0));
        assertEquals(Locale.ENGLISH, localeList.get(1));
        // Add more assertions for other available locales

        // Edge case: empty list
        assertEquals(0, LocaleUtils.availableLocaleList().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyAvailableLocaleList() {
        LocaleUtils.availableLocaleList().add(new Locale("XX", "XX"));
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SIMPLIFIED_CHINESE));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("XX", "XX")));

        // Edge case: null input
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List langList = LocaleUtils.languagesByCountry("US");
        assertEquals(2, langList.size());
        assertEquals("en", langList.get(0).getLanguage());
        assertEquals("es", langList.get(1).getLanguage());

        // Edge case: null input
        assertEquals(0, LocaleUtils.languagesByCountry(null).size());

        // Edge case: empty country
        assertEquals(0, LocaleUtils.languagesByCountry("").size());
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List countryList = LocaleUtils.countriesByLanguage("en");
        assertEquals(2, countryList.size());
        assertEquals("US", countryList.get(0).getCountry());
        assertEquals("GB", countryList.get(1).getCountry());

        // Edge case: null input
        assertEquals(0, LocaleUtils.countriesByLanguage(null).size());

        // Edge case: empty language
        assertEquals(0, LocaleUtils.countriesByLanguage("").size());
    }
}