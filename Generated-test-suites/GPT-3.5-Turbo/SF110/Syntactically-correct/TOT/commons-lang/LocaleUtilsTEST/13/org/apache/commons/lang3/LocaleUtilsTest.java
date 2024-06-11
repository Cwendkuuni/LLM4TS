package org.apache.commons.lang3;

import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    // Test cases for toLocale method
    @Test
    public void testToLocale_NullString_ReturnsNull() {
        Locale result = LocaleUtils.toLocale(null);
        assertNull(result);
    }

    @Test
    public void testToLocale_EmptyString_ReturnsLocaleWithEmptyLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_ContainsHash_ShouldThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en#US");
    }

    // Add more test cases for the toLocale method

    // Test cases for localeLookupList method
    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        Locale locale = null;
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLocaleLookupList_LocaleWithVariant_ContainsLocaleWithoutVariant() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertTrue(result.contains(new Locale("en", "US")));
    }

    // Add more test cases for the localeLookupList method

    // Test cases for availableLocaleList method
    // ... (similar structure to the previous methods)

    // Test cases for availableLocaleSet method
    // ... (similar structure to the previous methods)

    // Test cases for isAvailableLocale method
    // ... (similar structure to the previous methods)

    // Test cases for languagesByCountry method
    // ... (similar structure to the previous methods)

    // Test cases for countriesByLanguage method
    // ... (similar structure to the previous methods)

}