import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        // Edge case: empty string
        assertNull(LocaleUtils.toLocale(""));
        // Edge case: invalid format
        assertNull(LocaleUtils.toLocale("en_UK_London"));
        // Error handling: null input
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx")));
        // Edge case: empty list
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
        // Error handling: null input
        assertNull(LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> expected = LocaleUtils.availableLocaleList();
        assertEquals(expected.size(), Locale.getAvailableLocales().length);
        // Edge case: empty list
        assertEmpty(LocaleUtils.availableLocaleList());
        // Error handling: illegal input
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.availableLocaleList(""));
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        // Edge case: invalid input
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        List<Locale> expected = Arrays.asList(new Locale("fr"), new Locale("en"));
        assertEquals(expected, LocaleUtils.languagesByCountry("CA"));
        // Edge case: empty list
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
        // Error handling: invalid input
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.languagesByCountry(""));
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        List<Locale> expected = Arrays.asList(new Locale("CA"), new Locale("US"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
        // Edge case: empty list
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
        // Error handling: invalid input
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.countriesByLanguage(""));
    }
}