import static org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    // Test toLocale(String str)
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // Edge case: empty string
        assertNull(LocaleUtils.toLocale(""));
        
        // Exception handling: incorrect format
        try {
            LocaleUtils.toLocale("eng");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught, test passed
        }
    }

    // Test localeLookupList(Locale locale) and localeLookupList(Locale locale, Locale defaultLocale)
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = List.of(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en"));
        List<Locale> resultList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, resultList);
        
        resultList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expectedList, resultList);
        
        // Edge case: null input for locale
        resultList = LocaleUtils.localeLookupList(null);
        assertEquals(List.of(new Locale("en")), resultList);
        
        resultList = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(List.of(new Locale("en")), resultList);
    }

    // Test availableLocaleList()
    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertEquals(Locale.getAvailableLocales().length, locales.size());
        assertTrue(locales.contains(Locale.CANADA));
    }

    // Test availableLocaleSet()
    @Test
    public void testAvailableLocaleSet() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertEquals(locales.size(), LocaleUtils.availableLocaleSet().size());
    }

    // Test isAvailableLocale(Locale locale)
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CANADA));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz")));
    }

    // Test languagesByCountry(String countryCode)
    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assertEquals(3, languages.size());
        assertTrue(languages.contains(Locale.ENGLISH));
        
        // Edge case: null input for countryCode
        List<Locale> nullLanguages = LocaleUtils.languagesByCountry(null);
        assertEquals(0, nullLanguages.size());
    }

    // Test countriesByLanguage(String languageCode)
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertEquals(37, countries.size());
        assertTrue(countries.contains(Locale.CANADA_FRENCH));
        
        // Edge case: null input for languageCode
        List<Locale> nullCountries = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, nullCountries.size());
    }
}