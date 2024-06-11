package org.apache.commons.lang;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB_xxx"));
        assertNull(LocaleUtils.toLocale(null));
        //Edge Case: Invalid format
        try {
            LocaleUtils.toLocale("invalid_locale");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertEquals(4, LocaleUtils.localeLookupList(locale).size());
        //Edge Case: Default locale is null
        assertEquals(1, LocaleUtils.localeLookupList(null, null).size());
    }

    @Test
    public void testAvailableLocaleList() {
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "YY")));
    }

    @Test
    public void testLanguagesByCountry() {
        assertFalse(LocaleUtils.languagesByCountry("US").isEmpty());
        //Edge Case: Country code is null
        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        assertFalse(LocaleUtils.countriesByLanguage("en").isEmpty());
        //Edge Case: Language code is null
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }

}