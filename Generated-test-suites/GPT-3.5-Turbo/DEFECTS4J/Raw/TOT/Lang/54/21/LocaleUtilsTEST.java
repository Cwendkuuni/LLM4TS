import org.junit.*;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleWithValidLocaleString() {
        // Typical use case: converting a valid locale string to a Locale object
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidLocaleString() {
        // Error scenario: converting an invalid locale string should throw an exception
        LocaleUtils.toLocale("invalid_locale");
    }
    
    @Test
    public void testToLocaleWithNullLocaleString() {
        // Edge case: passing a null locale string should return null
        Locale locale = LocaleUtils.toLocale(null);
        assertNull(locale);
    }
    
    @Test
    public void testLocaleLookupListWithValidLocale() {
        // Typical use case: generating a list of locales to search through
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(3, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
    }
    
    @Test
    public void testLocaleLookupListWithNullLocale() {
        // Edge case: passing a null locale should return an empty list
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
        assertTrue(localeList.isEmpty());
    }
    
    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        // Edge case: passing a null locale and a default locale should return a single-element list with the default locale
        Locale defaultLocale = new Locale("en");
        List<Locale> localeList = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(1, localeList.size());
        assertEquals(defaultLocale, localeList.get(0));
    }
    
    @Test
    public void testAvailableLocaleList() {
        // Typical use case: obtaining the list of available locales
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
        assertFalse(availableLocales.isEmpty());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        // Typical use case: obtaining the set of available locales
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocales);
        assertFalse(availableLocales.isEmpty());
    }
    
    @Test
    public void testIsAvailableLocaleWithAvailableLocale() {
        // Typical use case: checking if an available locale is known
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }
    
    @Test
    public void testIsAvailableLocaleWithUnavailableLocale() {
        // Typical use case: checking if an unavailable locale is known
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz")));
    }
    
    @Test
    public void testLanguagesByCountryWithValidCountryCode() {
        // Typical use case: obtaining the list of languages available for a given country
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assertNotNull(languages);
        assertFalse(languages.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountryWithInvalidCountryCode() {
        // Typical use case: obtaining the list of languages available for an invalid country
        List<Locale> languages = LocaleUtils.languagesByCountry("XX");
        assertNotNull(languages);
        assertTrue(languages.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountryWithNullCountryCode() {
        // Edge case: passing a null country code should return an empty list
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        assertNotNull(languages);
        assertTrue(languages.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguageWithValidLanguageCode() {
        // Typical use case: obtaining the list of countries available for a given language
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguageWithInvalidLanguageCode() {
        // Typical use case: obtaining the list of countries available for an invalid language
        List<Locale> countries = LocaleUtils.countriesByLanguage("xx");
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguageWithNullLanguageCode() {
        // Edge case: passing a null language code should return an empty list
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

}