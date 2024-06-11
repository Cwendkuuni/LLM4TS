package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_basic() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testToLocale_emptyString() {
        Locale locale = LocaleUtils.toLocale("");
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("#invalid");
    }

    // Add more test cases for other scenarios

    @Test
    public void testLocaleLookupList_basic() {
        Locale locale = new Locale("en", "US");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(4, list.size());
        assertTrue(list.contains(locale));
        assertTrue(list.contains(new Locale("en", "")));
    }

    // Add more test cases for other methods
    
    @Test
    public void testCountriesByLanguage_null() {
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, list.size());
    }

    // Add more edge cases and error scenarios for thorough testing
  
}