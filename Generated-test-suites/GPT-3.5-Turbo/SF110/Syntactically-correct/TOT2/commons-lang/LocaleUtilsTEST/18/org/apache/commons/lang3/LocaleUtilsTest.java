package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyString() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("#");
    }

    // Add more test cases for edge cases and exceptions

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        Locale defaultLocale = Locale.ENGLISH;
        assertEquals(4, LocaleUtils.localeLookupList(defaultLocale).size());
    }

    // Add more test cases for different scenarios

    @Test
    public void testAvailableLocaleList_NotEmpty() {
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_NotEmpty() {
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    // Add more test cases for edge cases and exceptions

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    // Add more test cases for different scenarios

    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
    }

    // Add more test cases for different scenarios

    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }

    // Add more test cases for different scenarios
    
}