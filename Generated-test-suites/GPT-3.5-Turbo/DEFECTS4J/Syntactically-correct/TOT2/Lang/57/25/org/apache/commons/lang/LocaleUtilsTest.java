import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NormalCase() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput() {
        LocaleUtils.toLocale("invalidLocale");
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("fr", "CA"), new Locale("en"));
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(new Locale("fr", "CA"), result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(new Locale("en"), result.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
        assertTrue(availableLocales.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocales);
        assertTrue(availableLocales.size() > 0);
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertNotNull(languages);
        assertTrue(languages.size() > 0);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countries);
        assertTrue(countries.size() > 0);
    }

}