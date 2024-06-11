package org.apache.commons.lang3;

import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_NullInput_ReturnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnLocaleWithEmptyValues() {
        Locale locale = LocaleUtils.toLocale("");
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en_US#");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithLessThanTwoCharacters_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("e");
    }
    
    // ...continue testing all the remaining methods
    
    @Test
    public void testLanguagesByCountry_NullInput_ReturnEmptyList() {
        List<Locale> list = LocaleUtils.languagesByCountry(null);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnListOfLanguages() {
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertEquals(1, list.size());
        assertEquals(new Locale("en"), list.get(0));
    }
    
    @Test
    public void testCountriesByLanguage_NullInput_ReturnEmptyList() {
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnListOfCountries() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertEquals(1, list.size());
        assertEquals(new Locale("en", "US"), list.get(0));
    }
}