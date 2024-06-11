import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    // Test toLocale method

    @Test
    public void testToLocale() {
        // Typical use cases
        assertEquals(LocaleUtils.toLocale("en"), new Locale("en", ""));
        assertEquals(LocaleUtils.toLocale("en_GB"), new Locale("en", "GB"));
        assertEquals(LocaleUtils.toLocale("en_GB_xxx"), new Locale("en", "GB", "xxx"));

        // Edge cases
        assertEquals(LocaleUtils.toLocale("fr_CA"), new Locale("fr", "CA"));
        assertEquals(LocaleUtils.toLocale("fr_CA_xxx"), new Locale("fr", "CA", "xxx"));
        assertEquals(LocaleUtils.toLocale("zh"), new Locale("zh", ""));
    }

    // Test localeLookupList method

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        
        // Typical use cases
        List<Locale> expected1 = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        assertEquals(expected1, LocaleUtils.localeLookupList(locale, defaultLocale));

        // Edge case with null input
        List<Locale> expected2 = Arrays.asList(new Locale("en"));
        assertEquals(expected2, LocaleUtils.localeLookupList(null, null));
    }

    // Test availableLocaleList method

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }

    // Test availableLocaleSet method

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }

    // Test isAvailableLocale method

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    // Test languagesByCountry method

    @Test
    public void testLanguagesByCountry() {
        // Typical use case
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"));
        assertEquals(expected, LocaleUtils.languagesByCountry("US"));

        // Edge case with null input
        List<Locale> expected2 = Collections.EMPTY_LIST;
        assertEquals(expected2, LocaleUtils.languagesByCountry(null));
    }

    // Test countriesByLanguage method

    @Test
    public void testCountriesByLanguage() {
        // Typical use case
        List<Locale> expected = Arrays.asList(new Locale("US"), new Locale("GB"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));

        // Edge case with null input
        List<Locale> expected2 = Collections.EMPTY_LIST;
        assertEquals(expected2, LocaleUtils.countriesByLanguage(null));
    }
}