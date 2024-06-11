package org.apache.commons.lang3;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Arrange
        String str1 = "en";
        String str2 = "en_GB";
        String str3 = "en_GB_xxx";

        // Act
        Locale result1 = LocaleUtils.toLocale(str1);
        Locale result2 = LocaleUtils.toLocale(str2);
        Locale result3 = LocaleUtils.toLocale(str3);

        // Assert
        assertEquals(new Locale("en", ""), result1);
        assertEquals(new Locale("en", "GB"), result2);
        assertEquals(new Locale("en", "GB", "xxx"), result3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        // Arrange
        String str = "invalid_locale_format";

        // Act
        LocaleUtils.toLocale(str);
    }

    @Test
    public void testLocaleLookupList() {
        // Arrange
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale locale2 = new Locale("en");

        // Act
        List<Locale> result1 = LocaleUtils.localeLookupList(locale1);
        List<Locale> result2 = LocaleUtils.localeLookupList(locale1, locale2);

        // Assert
        assertEquals(Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr")), result1);
        assertEquals(Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr"), locale2), result2);
    }

    @Test
    public void testAvailableLocaleList() {
        // Act
        List<Locale> result = LocaleUtils.availableLocaleList();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Act
        Set<Locale> result = LocaleUtils.availableLocaleSet();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Set);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        // Arrange
        Locale locale = new Locale("en");

        // Act
        boolean result = LocaleUtils.isAvailableLocale(locale);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        // Arrange
        Locale locale = new Locale("xx");

        // Act
        boolean result = LocaleUtils.isAvailableLocale(locale);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry() {
        // Arrange
        String countryCode = "GB";

        // Act
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);

        // Assert
        assertEquals(Arrays.asList(new Locale("en", "GB")), result);
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        // Arrange
        String countryCode = null;

        // Act
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Arrange
        String languageCode = "en";

        // Act
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);

        // Assert
        assertEquals(Arrays.asList(new Locale("en", "GB")), result);
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        // Arrange
        String languageCode = null;

        // Act
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}