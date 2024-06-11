import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale("en_US");
        assertEquals(expectedLocale, actualLocale);

        // Edge cases and exception handling tests
        assertNull(LocaleUtils.toLocale(null));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(""));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("aa_ZZ")); // Invalid language code
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_AA")); // Invalid country code
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_ZZ_XX")); // Invalid separator
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_XX_XX")); // Country code longer than 2 characters
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_XX_XX_XX")); // Variant longer than empty
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocaleList = LocaleUtils.localeLookupList(locale);
        List<Locale> actualLocaleList = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(actualLocaleList, expectedLocaleList);

        // Edge cases and exception handling tests
        assertNotNull(LocaleUtils.localeLookupList(null));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.localeLookupList(new Locale("", "")));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.localeLookupList(new Locale("", "CA", "xxx")));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.localeLookupList(new Locale("fr", "", "xxx")));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), null));
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(actualLocaleList, expectedLocaleList);

        // Edge cases and exception handling tests
        assertNotNull(LocaleUtils.availableLocaleList());
        assertThrows(NullPointerException.class, () -> LocaleUtils.availableLocaleList(null));
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic functionality test
        Set<Locale> expectedLocaleSet = LocaleUtils.availableLocaleSet();
        Set<Locale> actualLocaleSet = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(actualLocaleSet, expectedLocaleSet);

        // Edge cases and exception handling tests
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertThrows(NullPointerException.class, () -> LocaleUtils.availableLocaleSet(null));
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Edge cases and exception handling tests
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "CA")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr", "")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr", "AA")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr", "AA_XX")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr", "AA_XX_XX")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        String countryCode = "CA";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(actualLocaleList, expectedLocaleList);

        // Edge cases and exception handling tests
        assertNotNull(LocaleUtils.languagesByCountry(null));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.languagesByCountry(""));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.languagesByCountry("ZZ"));
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        String languageCode = "en";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"));
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(actualLocaleList, expectedLocaleList);

        // Edge cases and exception handling tests
        assertNotNull(LocaleUtils.countriesByLanguage(null));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.countriesByLanguage(""));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.countriesByLanguage("XX"));
    }

}