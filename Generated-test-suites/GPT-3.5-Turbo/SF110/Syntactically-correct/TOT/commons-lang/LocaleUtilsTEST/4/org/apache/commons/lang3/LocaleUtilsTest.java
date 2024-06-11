import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // typical use case
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        
        // edge case: empty string
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        
        // edge case: locale with underscore
        try {
            LocaleUtils.toLocale("en_US");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_US", e.getMessage());
        }
        
        // error scenario: invalid locale format, single character
        try {
            LocaleUtils.toLocale("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: a", e.getMessage());
        }
        
        // error scenario: invalid locale format, lowercase language with 3 characters
        try {
            LocaleUtils.toLocale("eng");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: eng", e.getMessage());
        }

        // error scenario: invalid locale format, lowercase language with 4 characters 
        try {
            LocaleUtils.toLocale("engl");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: engl", e.getMessage());
        }
        
        // error scenario: invalid locale format, invalid language and country code combination
        try {
            LocaleUtils.toLocale("en_USA");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_USA", e.getMessage());
        }
        
        // error scenario: invalid locale format, invalid country code
        try {
            LocaleUtils.toLocale("_US");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: _US", e.getMessage());
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        
        // typical use case
        List<Locale> expected = Arrays.asList(locale, new Locale("en"));
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
        
        // edge case: null locale
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(new HashSet<Locale>(expected), LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
        
        locale = new Locale("xyz", "ABC");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testLanguagesByCountry() {
        // typical use case
        List<Locale> expected = Arrays.asList(new Locale("en", "US"));
        assertEquals(expected, LocaleUtils.languagesByCountry("US"));
        
        // edge case: null country code
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testCountriesByLanguage() {
        // typical use case
        List<Locale> expected = Arrays.asList(new Locale("en", "US"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
        
        // edge case: null language code
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}