import static org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleValidString() {
        String validLocaleString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(validLocaleString);
        assertEquals(expectedLocale, actualLocale);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidString() {
        String invalidLocaleString = "invalid";
        LocaleUtils.toLocale(invalidLocaleString);
    }
    
    @Test
    public void testToLocaleNullString() {
        String nullLocaleString = null;
        Locale expectedLocale = null;
        Locale actualLocale = LocaleUtils.toLocale(nullLocaleString);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = List.of(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("en")
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expectedList, actualList);
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        assertNotNull(expectedList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = LocaleUtils.availableLocaleSet();
        assertNotNull(expectedSet);
    }
    
    @Test
    public void testIsAvailableLocaleValidLocale() {
        Locale validLocale = new Locale("fr", "CA");
        boolean isAvailable = LocaleUtils.isAvailableLocale(validLocale);
        assertTrue(isAvailable);
    }
    
    @Test
    public void testIsAvailableLocaleInvalidLocale() {
        Locale invalidLocale = new Locale("xx", "XX");
        boolean isAvailable = LocaleUtils.isAvailableLocale(invalidLocale);
        assertFalse(isAvailable);
    }

    @Test
    public void testLanguagesByCountryValidCountry() {
        String validCountryCode = "US";
        List<Locale> expectedList = List.of(
            new Locale("en", "US"),
            new Locale("es", "US")
        );
        List<Locale> actualList = LocaleUtils.languagesByCountry(validCountryCode);
        assertEquals(expectedList, actualList);
    }
    
    @Test
    public void testLanguagesByCountryInvalidCountry() {
        String invalidCountryCode = "XX";
        List<Locale> expectedList = List.of();
        List<Locale> actualList = LocaleUtils.languagesByCountry(invalidCountryCode);
        assertEquals(expectedList, actualList);
    }
    
    @Test
    public void testCountriesByLanguageValidLanguage() {
        String validLanguageCode = "es";
        List<Locale> expectedList = List.of(
            new Locale("es", "AR"),
            new Locale("es", "ES"),
            new Locale("es", "MX")
        );
        List<Locale> actualList = LocaleUtils.countriesByLanguage(validLanguageCode);
        assertEquals(expectedList, actualList);
    }
    
    @Test
    public void testCountriesByLanguageInvalidLanguage() {
        String invalidLanguageCode = "xx";
        List<Locale> expectedList = List.of();
        List<Locale> actualList = LocaleUtils.countriesByLanguage(invalidLanguageCode);
        assertEquals(expectedList, actualList);
    }
}