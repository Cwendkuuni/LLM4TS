import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

public class LocaleUtilsTest {
    
    // Testing toLocale() method
    
    @Test
    public void testToLocale() {
        // Typical use case
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale1);
        
        // Edge case with country code
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale2);
        
        // Edge case with language code, country code, and variant
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale3);
        
        // Testing null input
        assertNull(LocaleUtils.toLocale(null));
        
        // Testing invalid format with length less than 2
        try {
            LocaleUtils.toLocale("e");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }
        
        // Testing invalid format with invalid characters
        try {
            LocaleUtils.toLocale("en-GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }
    }
    
    // Testing localeLookupList() method
    
    @Test
    public void testLocaleLookupList() {
        // Typical use case with default locale
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocales1 = new ArrayList<Locale>();
        expectedLocales1.add(locale1);
        expectedLocales1.add(new Locale("fr", "CA"));
        expectedLocales1.add(new Locale("fr"));
        expectedLocales1.add(new Locale("en")); // Default locale
        assertEquals(expectedLocales1, LocaleUtils.localeLookupList(locale1));
        
        // Typical use case without default locale
        Locale locale2 = new Locale("en");
        List<Locale> expectedLocales2 = new ArrayList<Locale>();
        expectedLocales2.add(locale2);
        expectedLocales2.add(new Locale("en", "US"));
        expectedLocales2.add(new Locale("en"));
        assertEquals(expectedLocales2, LocaleUtils.localeLookupList(locale2, null));
        
        // Testing null input
        assertEquals(new ArrayList<Locale>(), LocaleUtils.localeLookupList(null));
        
        // Testing empty input
        Locale locale3 = new Locale("", "", "");
        List<Locale> expectedLocales3 = new ArrayList<Locale>();
        expectedLocales3.add(locale3);
        assertEquals(expectedLocales3, LocaleUtils.localeLookupList(locale3));
    }
    
    // Testing availableLocaleList() method
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
        assertTrue(locales.size() > 0);
    }
    
    // Testing availableLocaleSet() method
    
    @Test
    public void testAvailableLocaleSet() {
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertTrue(LocaleUtils.availableLocaleSet().size() > 0);
    }
    
    // Testing isAvailableLocale() method
    
    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));
        
        Locale locale2 = new Locale("zz", "ZZ");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }
    
    // Testing languagesByCountry() method
    
    @Test
    public void testLanguagesByCountry() {
        // Typical use case
        List<Locale> expectedLanguages1 = new ArrayList<Locale>();
        expectedLanguages1.add(new Locale("en", "US"));
        assertEquals(expectedLanguages1, LocaleUtils.languagesByCountry("US"));
        
        // Testing null input
        assertEquals(new ArrayList<Locale>(), LocaleUtils.languagesByCountry(null));
        
        // Testing invalid country code
        assertEquals(new ArrayList<Locale>(), LocaleUtils.languagesByCountry("ZZ"));
    }
    
    // Testing countriesByLanguage() method
    
    @Test
    public void testCountriesByLanguage() {
        // Typical use case
        List<Locale> expectedCountries1 = new ArrayList<Locale>();
        expectedCountries1.add(new Locale("en", "US"));
        assertEquals(expectedCountries1, LocaleUtils.countriesByLanguage("en"));
        
        // Testing null input
        assertEquals(new ArrayList<Locale>(), LocaleUtils.countriesByLanguage(null));
        
        // Testing invalid language code
        assertEquals(new ArrayList<Locale>(), LocaleUtils.countriesByLanguage("zz"));
    }
}