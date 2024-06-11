import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // test invalid format
        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("e");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_GB_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = List.of(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
        
        // test null input
        assertEquals(List.of(defaultLocale), LocaleUtils.localeLookupList(null));
        assertEquals(List.of(defaultLocale), LocaleUtils.localeLookupList(null, defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = List.of(Locale.getAvailableLocales());
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        assertEquals(new HashSet<>(List.of(Locale.getAvailableLocales())), LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.UK));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = List.of(Locale.FRANCE, Locale.CANADA_FRENCH);
        
        assertEquals(expected, LocaleUtils.languagesByCountry("FR"));
        
        // test null input
        assertEquals(List.of(), LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = List.of(Locale.CANADA, Locale.CANADA_FRENCH);
        
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
        
        // test null input
        assertEquals(List.of(), LocaleUtils.countriesByLanguage(null));
    }
}