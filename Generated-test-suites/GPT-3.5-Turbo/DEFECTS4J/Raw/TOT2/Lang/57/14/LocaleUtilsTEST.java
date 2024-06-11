import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidInput() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
    }

    @Test
    public void testToLocale_InvalidInput() {
        try {
            LocaleUtils.toLocale("invalid");
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        assertNotNull(lookupList);
        assertFalse(lookupList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
        assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocaleSet);
        assertFalse(availableLocaleSet.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_ValidInput() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertNotNull(languages);
        assertFalse(languages.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_InvalidInput() {
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        assertNotNull(languages);
        assertTrue(languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidInput() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_InvalidInput() {
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }
}