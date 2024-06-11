package org.apache.commons.lang3;

import org.junit.Test;
import org.junit.Assert.*;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        String[] validInputs = new String[]{"en", "en_GB", "en_GB_xxx", "fr_CA", "fr_CA_xxx"};
        Locale[] expectedOutputs = new Locale[]{new Locale("en"), new Locale("en", "GB"), new Locale("en", "GB", "xxx"), new Locale("fr", "CA"), new Locale("fr", "CA", "xxx")};
        for (int i = 0; i < validInputs.length; i++) {
            Locale actualOutput = LocaleUtils.toLocale(validInputs[i]);
            assertEquals(expectedOutputs[i].getDisplayName(), actualOutput.getDisplayName());
        }

        // Test null input
        assertNull(LocaleUtils.toLocale(null));

        // Test invalid inputs
        String[] invalidInputs = new String[]{"en_GB_xxx_", "en_GBx"};
        for (String invalidInput : invalidInputs) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidInput));
            assertTrue(exception.getMessage().contains("Invalid locale format: " + invalidInput));
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty input
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedOutput = Collections.singletonList(defaultLocale);
        List<Locale> actualOutput = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(expectedOutput, actualOutput);

        // Test valid input with no variant
        Locale locale1 = new Locale("fr", "CA");
        List<Locale> expectedOutput1 = Arrays.asList(locale1, new Locale("fr"), defaultLocale);
        List<Locale> actualOutput1 = LocaleUtils.localeLookupList(locale1, defaultLocale);
        assertEquals(expectedOutput1, actualOutput1);

        // Test valid input with variant
        Locale locale2 = new Locale("fr", "CA", "xxx");
        List<Locale> expectedOutput2 = Arrays.asList(locale2, locale1, new Locale("fr"), defaultLocale);
        List<Locale> actualOutput2 = LocaleUtils.localeLookupList(locale2, defaultLocale);
        assertEquals(expectedOutput2, actualOutput2);

        // Test invalid input
        Locale invalidLocale = new Locale("fr", "XX"); // Invalid country code
        List<Locale> actualOutput3 = LocaleUtils.localeLookupList(invalidLocale, defaultLocale);
        assertTrue(actualOutput3.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> expectedOutput = Collections.emptyList();
        List<Locale> actualOutput = LocaleUtils.availableLocaleList();
        assertEquals(expectedOutput, actualOutput);

        // Test non-empty list
        List<Locale> locales = Arrays.asList(new Locale("en"), new Locale("fr"), new Locale("de"));
        ConcurrentMap<String, List<Locale>> cLanguagesByCountryMock = new ConcurrentHashMap<>();
        ConcurrentMap<String, List<Locale>> cCountriesByLanguageMock = new ConcurrentHashMap<>();
        when(cLanguagesByCountryMock.get("CA")).thenReturn(locales);
        when(cCountriesByLanguageMock.get("en")).thenReturn(locales);
        LocaleUtils localeUtilsMock = spy(new LocaleUtils());
        doReturn(cLanguagesByCountryMock).when(localeUtilsMock).getCCountriesByLanguage();
        doReturn(cLanguagesByCountryMock).when(localeUtilsMock).getCCountriesByLanguage();
        List<Locale> actualOutputMock = localeUtilsMock.availableLocaleList();
        assertEquals(locales, actualOutputMock);

        verify(localeUtilsMock, times(2)).getCCountriesByLanguage();
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        String countryCode = "XX"; // Invalid country code
        List<Locale> expectedOutput = Collections.emptyList();
        List<Locale> actualOutput = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedOutput, actualOutput);

        // Test non-empty list
        String languageCode = "en";
        List<Locale> locales = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"));
        ConcurrentMap<String, List<Locale>> cLanguagesByCountryMock = new ConcurrentHashMap<>();
        ConcurrentMap<String, List<Locale>> cCountriesByLanguageMock = new ConcurrentHashMap<>();
        when(cLanguagesByCountryMock.get("CA")).thenReturn(locales);
        LocaleUtils localeUtilsMock = spy(new LocaleUtils());
        doReturn(cLanguagesByCountryMock).when(localeUtilsMock).getCCountriesByLanguage();
        List<Locale> actualOutputMock = localeUtilsMock.languagesByCountry(countryCode);
        assertEquals(Collections.emptyList(), actualOutputMock);
    }

    // Add more test methods here for other LocaleUtils methods if needed
}