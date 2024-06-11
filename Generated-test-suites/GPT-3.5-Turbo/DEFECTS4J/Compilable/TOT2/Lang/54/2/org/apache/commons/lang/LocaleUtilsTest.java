package org.apache.commons.lang;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        
        // Edge case: invalid locale format
        try {
            LocaleUtils.toLocale("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        
        // Error scenario: null input
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case
        Locale locale = Locale.US;
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(4, result.size());
        
        // Edge case: null input
        assertEquals(1, LocaleUtils.localeLookupList(null).size());
    }

    @Test
    public void testAvailableLocaleList() {
        // Typical use case
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
        
        // Edge case: unmodifiable list
        try {
            locales.add(Locale.FRANCE);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Typical use case
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.US));
        
        // Error scenario: unknown locale
        assertFalse(LocaleUtils.availableLocaleSet().contains(new Locale("xx")));
    }

    @Test
    public void testIsAvailableLocale() {
        // Typical use case
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        
        // Error scenario: unknown locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertNotNull(languages);
        
        // Error scenario: null input
        assertNotNull(LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countries);
        
        // Error scenario: null input
        assertNotNull(LocaleUtils.countriesByLanguage(null));
    }

}