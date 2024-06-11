package org.apache.commons.lang;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        String[] validInputs = new String[]{"en", "en_GB", "en_GB_US", "fr_FR", "fr_FR_CA"};
        Locale[] expectedOutputs = new Locale[] {new Locale("en", ""), new Locale("en", "GB"),
                new Locale("en", "GB", "US"), new Locale("fr", "FR"), new Locale("fr", "FR", "CA")};
        for (int i = 0; i < validInputs.length; i++) {
            Locale actualOutput = LocaleUtils.toLocale(validInputs[i]);
            assertEquals(expectedOutputs[i].getDisplayName(), actualOutput.getDisplayName());
        }

        // Test invalid inputs
        String[] invalidInputs = new String[]{"en_aB", "en_GB_aB", "en_aB_US", "fr_aF", "fr_FR_aF"};
        for (String invalidInput : invalidInputs) {
            try {
                LocaleUtils.toLocale(invalidInput);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }

        // Test empty string
        assertEquals(Locale.ROOT, LocaleUtils.toLocale(""));
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("en", "CA", "AB");
        Locale defaultLocale = new Locale("fr", "FR");

        List<Locale> expectedOutput = Arrays.asList(new Locale("en", "CA", "AB"),
                new Locale("en", "CA"), new Locale("en", ""), new Locale("fr", "FR"));
        List<Locale> actualOutput = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        assertEquals(expectedOutput, actualOutput);

        expectedOutput = Arrays.asList(new Locale("en", ""), new Locale("en", "CA"), new Locale("fr", "FR"));
        actualOutput = LocaleUtils.localeLookupList(baseLocale);
        assertEquals(expectedOutput, actualOutput);

        // Test null inputs
        Locale nullLocale = null;
        List<Locale> nullList = LocaleUtils.localeLookupList(nullLocale);
        assertEquals(Collections.emptyList(), nullList);
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(nullLocale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedOutput = LocaleUtils.availableLocaleList();
        Set<String> expectedCountrySet = new HashSet<>();
        Set<String> expectedLanguageSet = new HashSet<>();
        for (Locale locale : expectedOutput) {
            expectedCountrySet.add(locale.getCountry());
            expectedLanguageSet.add(locale.getLanguage());
        }

        List<Locale> actualOutput = LocaleUtils.availableLocaleList();
        Set<String> actualCountrySet = new HashSet<>();
        Set<String> actualLanguageSet = new HashSet<>();
        for (Locale locale : actualOutput) {
            actualCountrySet.add(locale.getCountry());
            actualLanguageSet.add(locale.getLanguage());
        }

        assertEquals(expectedOutput.size(), actualOutput.size());
        assertEquals(expectedCountrySet, actualCountrySet);
        assertEquals(expectedLanguageSet, actualLanguageSet);
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        for (Locale locale : availableLocales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        Locale invalidLocale = new Locale("non-existent", "non-existent");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> englishLocales = LocaleUtils.languagesByCountry("CA");
        List<String> expectedOutput = Arrays.asList("en");
        assertEquals(expectedOutput, Arrays.asList(englishLocales.get(0).getLanguage()));

        List<Locale> emptyList = Collections.emptyList();
        assertEquals(emptyList, LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> frenchCanadianLocales = LocaleUtils.countriesByLanguage("fr");
        List<String> expectedOutput = Arrays.asList("CA");
        assertEquals(expectedOutput, Arrays.asList(frenchCanadianLocales.get(0).getCountry()));

        List<Locale> emptyList = Collections.emptyList();
        assertEquals(emptyList, LocaleUtils.countriesByLanguage(null));
    }
}