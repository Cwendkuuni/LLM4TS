import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        // Edge cases: empty input
        assertNull(LocaleUtils.toLocale(""));

        // Edge cases: invalid input (language code)
        try {
            LocaleUtils.toLocale("xx_US");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Edge cases: invalid input (country code)
        try {
            LocaleUtils.toLocale("en_US_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Edge cases: invalid separator
        try {
            LocaleUtils.toLocale("en_US_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Error cases: null input
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        // Basic test
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, actualList);

        // Edge cases: empty input
        List<Locale> actualListEmpty = LocaleUtils.localeLookupList(null);
        assertTrue(actualListEmpty.isEmpty());

        // Edge cases: invalid input
        Locale invalidLocale = null;
        try {
            invalidLocale = LocaleUtils.toLocale("xx_US_xxx");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        List<Locale> actualListInvalid = LocaleUtils.localeLookupList(invalidLocale);
        assertTrue(actualListInvalid.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic test
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> actualList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedList, actualList);

        // Edge cases: empty list
        List<Locale> emptyList = Collections.emptyList();
        assertTrue(LocaleUtils.availableLocaleList().equals(emptyList));
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic test
        Locale locale = new Locale("fr", "CA", "xxx");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Edge cases: empty input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Edge cases: invalid input
        Locale invalidLocale = null;
        try {
            invalidLocale = LocaleUtils.toLocale("xx_US_xxx");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic test
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA"));
        List<Locale> actualList = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedList, actualList);

        // Edge cases: empty country
        List<Locale> emptyList = Collections.emptyList();
        assertTrue(LocaleUtils.languagesByCountry(null).equals(emptyList));

        // Error cases: invalid country
        List<Locale> actualListInvalid = LocaleUtils.languagesByCountry("XX");
        assertTrue(actualListInvalid.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic test
        List<Locale> expectedList = Arrays.asList(new Locale("CA", "fr"));
        List<Locale> actualList = LocaleUtils.countriesByLanguage("fr");
        assertEquals(expectedList, actualList);

        // Edge cases: empty language
        List<Locale> emptyList = Collections.emptyList();
        assertTrue(LocaleUtils.countriesByLanguage(null).equals(emptyList));

        // Edge cases: invalid language
        List<Locale> actualListInvalid = LocaleUtils.countriesByLanguage("XX");
        assertTrue(actualListInvalid.isEmpty());
    }
}