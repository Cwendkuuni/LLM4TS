import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validInput() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testToLocale_invalidInput() {
        try {
            LocaleUtils.toLocale("invalid_input");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: invalid_input", e.getMessage());
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertNotNull(list);
        assertEquals(4, list.size());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        assertFalse(set.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_nullInput() {
        List<Locale> list = LocaleUtils.languagesByCountry(null);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_nullInput() {
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

}