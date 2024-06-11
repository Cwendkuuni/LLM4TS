package org.apache.commons.lang;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    // Test for toLocale(String str) method
    @Test
    public void testToLocale() {
        // Typical use case
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        
        // Edge cases
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // Error scenarios
        assertThrows(IllegalArgumentException.class, () -> {
            LocaleUtils.toLocale(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            LocaleUtils.toLocale("EN_GB"); // Country code must be uppercase
        });
    }

    // Test for localeLookupList(Locale locale) method
    @Test
    public void testLocaleLookupList() {
        // Typical use case
        assertEquals("[en_GB_xxx, en_GB, en]", LocaleUtils.localeLookupList(new Locale("en", "GB", "xxx")).toString());
        
        // Edge cases
        assertEquals("[en_GB, en]", LocaleUtils.localeLookupList(new Locale("en", "GB")).toString());
        
        // Error scenarios
        assertEquals("[]", LocaleUtils.localeLookupList(null).toString());
    }

    // Test for localeLookupList(Locale locale, Locale defaultLocale) method
    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        // Typical use case
        assertEquals("[fr_CA_xxx, fr_CA, fr, en]", LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")).toString());
        
        // Edge cases
        assertEquals("[en_GB_xxx, en_GB, en]", LocaleUtils.localeLookupList(new Locale("en", "GB", "xxx"), Locale.ENGLISH).toString());
        
        // Error scenarios
        assertEquals("[]", LocaleUtils.localeLookupList(null, Locale.ENGLISH).toString());
    }

    // Test for availableLocaleList() method
    @Test
    public void testAvailableLocaleList() {
        // Typical use case
        assertEquals(LocaleUtils.availableLocaleList().size(), Locale.getAvailableLocales().length);
    }

    // Test for availableLocaleSet() method
    @Test
    public void testAvailableLocaleSet() {
        // Typical use case
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.ENGLISH));
    }

    // Test for isAvailableLocale(Locale locale) method
    @Test
    public void testIsAvailableLocale() {
        // Typical use case
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    // Test for languagesByCountry(String countryCode) method
    @Test
    public void testLanguagesByCountry() {
        // Typical use case
        assertEquals("[en_GB, en, fr]", LocaleUtils.languagesByCountry("GB").toString());
        
        // Edge cases
        assertEquals("[]", LocaleUtils.languagesByCountry("XXX").toString());
    }

    // Test for countriesByLanguage(String languageCode) method
    @Test
    public void testCountriesByLanguage() {
        // Typical use case
        assertEquals("[en_GB, en, fr]", LocaleUtils.countriesByLanguage("en").toString());
        
        // Edge cases
        assertEquals("[]", LocaleUtils.countriesByLanguage("xxx").toString());
    }

}