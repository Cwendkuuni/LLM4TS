import org.junit.Test;
import org.apache.commons.lang.LocaleUtils;

import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case
        Locale locale1 = LocaleUtils.toLocale("en_US");
        assertEquals("en", locale1.getLanguage());
        assertEquals("US", locale1.getCountry());

        // Edge case - null input
        Locale locale2 = LocaleUtils.toLocale(null);
        assertNull(locale2);

        // Error scenario - invalid locale format
        try {
            LocaleUtils.toLocale("en");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en", e.getMessage());
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale defaultLocale1 = new Locale("en");
        List<Locale> list1 = LocaleUtils.localeLookupList(locale1, defaultLocale1);
        assertEquals(4, list1.size());
        assertEquals(locale1, list1.get(0));
        assertEquals(new Locale("fr", "CA"), list1.get(1));
        assertEquals(new Locale("fr"), list1.get(2));
        assertEquals(defaultLocale1, list1.get(3));

        // Edge case - null locale
        Locale defaultLocale2 = new Locale("en");
        List<Locale> list2 = LocaleUtils.localeLookupList(null, defaultLocale2);
        assertEquals(1, list2.size());
        assertEquals(defaultLocale2, list2.get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        // Typical use case
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertTrue(list.size() > 0);

        // Edge case - empty list
        LocaleUtilsTest.clearAvailableLocales();
        List<Locale> emptyList = LocaleUtils.availableLocaleList();
        assertNotNull(emptyList);
        assertEquals(0, emptyList.size());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Typical use case
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        assertTrue(set.size() > 0);

        // Edge case - empty set
        LocaleUtilsTest.clearAvailableLocales();
        Set<Locale> emptySet = LocaleUtils.availableLocaleSet();
        assertNotNull(emptySet);
        assertEquals(0, emptySet.size());
    }

    @Test
    public void testIsAvailableLocale() {
        // Typical use case
        Locale locale1 = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));

        // Edge case - null locale
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Error scenario - locale not available
        Locale locale2 = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case
        List<Locale> list1 = LocaleUtils.languagesByCountry("FR");
        assertNotNull(list1);
        assertTrue(list1.size() > 0);

        // Edge case - null country code
        List<Locale> list2 = LocaleUtils.languagesByCountry(null);
        assertNotNull(list2);
        assertEquals(0, list2.size());

        // Error scenario - country code not found
        List<Locale> list3 = LocaleUtils.languagesByCountry("XX");
        assertNotNull(list3);
        assertEquals(0, list3.size());
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case
        List<Locale> list1 = LocaleUtils.countriesByLanguage("en");
        assertNotNull(list1);
        assertTrue(list1.size() > 0);

        // Edge case - null language code
        List<Locale> list2 = LocaleUtils.countriesByLanguage(null);
        assertNotNull(list2);
        assertEquals(0, list2.size());

        // Error scenario - language code not found
        List<Locale> list3 = LocaleUtils.countriesByLanguage("xx");
        assertNotNull(list3);
        assertEquals(0, list3.size());
    }

    // Helper method to clear available locales
    private static void clearAvailableLocales() {
        LocaleUtilsTest.MockLocale[] mocks = LocaleUtilsTest.MockLocale.values();
        for (LocaleUtilsTest.MockLocale mock : mocks) {
            mock.clear();
        }
    }

    // Helper enum for mocking available locales
    private enum MockLocale {
        EN_US("en", "US"),
        EN_CA("en", "CA"),
        FR_CA("fr", "CA");

        private static List<Locale> availableLocales = new ArrayList<>();

        MockLocale(String language, String country) {
            availableLocales.add(new Locale(language, country));
        }

        public static void clear() {
            availableLocales.clear();
        }
    }
}