import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleValidFormat() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test
    public void testToLocaleInvalidFormat() {
        try {
            LocaleUtils.toLocale("123");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupListNotNull() {
        List<Locale> list = LocaleUtils.localeLookupList(Locale.US);
        assertNotNull(list);
    }

    @Test
    public void testAvailableLocaleListNotEmpty() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertFalse(list.isEmpty());
    }

    @Test
    public void testAvailableLocaleSetContainsLocale() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertTrue(set.contains(Locale.US));
    }

    @Test
    public void testIsAvailableLocaleValidLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.getDefault()));
    }

    @Test
    public void testIsAvailableLocaleInvalidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }

    @Test
    public void testLanguagesByCountryValidCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("US");
        assertFalse(langs.isEmpty());
    }

    @Test
    public void testLanguagesByCountryInvalidCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("XX");
        assertTrue(langs.isEmpty());
    }

    @Test
    public void testCountriesByLanguageValidLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertFalse(countries.isEmpty());
    }

    @Test
    public void testCountriesByLanguageInvalidLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("xx");
        assertTrue(countries.isEmpty());
    }

}