package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        Locale expectedLocale1 = new Locale("en", "");
        Locale actualLocale1 = LocaleUtils.toLocale("en");
        assertEquals(expectedLocale1, actualLocale1);

        Locale expectedLocale2 = new Locale("en", "GB");
        Locale actualLocale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(expectedLocale2, actualLocale2);

        Locale expectedLocale3 = new Locale("en", "GB", "XXX");
        Locale actualLocale3 = LocaleUtils.toLocale("en_GB_XXX");
        assertEquals(expectedLocale3, actualLocale3);

        // Test invalid inputs
        try {
            Locale actualLocale4 = LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidLocale1 = "enX";
        try {
            Locale actualLocale5 = LocaleUtils.toLocale(invalidLocale1);
            fail("Expected IllegalArgumentException with message: " + invalidLocale1);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale1, e.getMessage());
        }

        String invalidLocale2 = "en_x";
        try {
            Locale actualLocale6 = LocaleUtils.toLocale(invalidLocale2);
            fail("Expected IllegalArgumentException with message: " + invalidLocale2);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale2, e.getMessage());
        }

        String invalidLocale3 = "en_GB_x";
        try {
            Locale actualLocale7 = LocaleUtils.toLocale(invalidLocale3);
            fail("Expected IllegalArgumentException with message: " + invalidLocale3);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale3, e.getMessage());
        }

        String invalidLocale4 = "en_GB_XXX_x";
        try {
            Locale actualLocale8 = LocaleUtils.toLocale(invalidLocale4);
            fail("Expected IllegalArgumentException with message: " + invalidLocale4);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale4, e.getMessage());
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        Locale defaultLocale = Locale.getDefault();
        List<Locale> actualLocaleList1 = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(Collections.singletonList(defaultLocale), actualLocaleList1);

        // Test single locale
        Locale locale = new Locale("fr", "CA", "XXX");
        List<Locale> actualLocaleList2 = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expectedLocaleList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expectedLocaleList, actualLocaleList2);

        // Test multiple locales with default locale
        List<Locale> expectedLocaleList2_2 = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        List<Locale> actualLocaleList3 = LocaleUtils.localeLookupList(locale, null);
        assertEquals(expectedLocaleList2_2, actualLocaleList3);

        // Test multiple locales without default locale
        List<Locale> expectedLocaleList3_2 = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualLocaleList4 = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(expectedLocaleList3_2, actualLocaleList4);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actualLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> expectedLocaleList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> actualLocaleSet = LocaleUtils.availableLocaleSet();
        Set<Locale> expectedLocaleSet = Collections.unmodifiableSet(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expectedLocaleSet, actualLocaleSet);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test available locale
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test invalid locale
        Locale invalidLocale = new Locale("en", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty country
        List<Locale> actualLocaleList1 = LocaleUtils.languagesByCountry(null);
        List<Locale> expectedLocaleList = Collections.emptyList();
        assertEquals(expectedLocaleList, actualLocaleList1);

        // Test valid country
        List<Locale> expectedLocaleList2 = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        List<Locale> actualLocaleList2 = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedLocaleList2, actualLocaleList2);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty language
        List<Locale> actualLocaleList1 = LocaleUtils.countriesByLanguage(null);
        List<Locale> expectedLocaleList = Collections.emptyList();
        assertEquals(expectedLocaleList, actualLocaleList1);

        // Test valid language
        List<Locale> expectedLocaleList2 = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"));
        List<Locale> actualLocaleList2 = LocaleUtils.countriesByLanguage("en");
        assertEquals(expectedLocaleList2, actualLocaleList2);
    }
}