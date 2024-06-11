package org.apache.commons.lang;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    // Test cases for toLocale method

    @Test
    public void testToLocale_validInput() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidInput() {
        LocaleUtils.toLocale("invalid");
    }

    // Test cases for localeLookupList method

    @Test
    public void testLocaleLookupList_singleLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertEquals(3, LocaleUtils.localeLookupList(locale).size());
        assertTrue(LocaleUtils.localeLookupList(locale).contains(locale));
        assertTrue(LocaleUtils.localeLookupList(locale).contains(new Locale("fr", "CA")));
        assertTrue(LocaleUtils.localeLookupList(locale).contains(new Locale("fr")));
    }

    @Test
    public void testLocaleLookupList_multipleLocales() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        assertEquals(4, LocaleUtils.localeLookupList(locale, defaultLocale).size());
        assertTrue(LocaleUtils.localeLookupList(locale, defaultLocale).contains(locale));
        assertTrue(LocaleUtils.localeLookupList(locale, defaultLocale).contains(new Locale("fr", "CA")));
        assertTrue(LocaleUtils.localeLookupList(locale, defaultLocale).contains(new Locale("fr")));
        assertTrue(LocaleUtils.localeLookupList(locale, defaultLocale).contains(defaultLocale));
    }

    // Test cases for availableLocaleList method

    @Test
    public void testAvailableLocaleList_notEmpty() {
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    // Test cases for availableLocaleSet method

    @Test
    public void testAvailableLocaleSet_notEmpty() {
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    // Test cases for isAvailableLocale method

    @Test
    public void testIsAvailableLocale_availableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testIsAvailableLocale_unavailableLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid")));
    }

    // Test cases for languagesByCountry method

    @Test
    public void testLanguagesByCountry_validCountryCode() {
        assertEquals(2, LocaleUtils.languagesByCountry("US").size());
    }

    @Test
    public void testLanguagesByCountry_invalidCountryCode() {
        assertEquals(0, LocaleUtils.languagesByCountry("invalid").size());
    }

    // Test cases for countriesByLanguage method

    @Test
    public void testCountriesByLanguage_validLanguageCode() {
        assertEquals(1, LocaleUtils.countriesByLanguage("en").size());
    }

    @Test
    public void testCountriesByLanguage_invalidLanguageCode() {
        assertEquals(0, LocaleUtils.countriesByLanguage("invalid").size());
    }
}