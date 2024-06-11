import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic usage
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        // Edge case: empty input
        assertNull(LocaleUtils.toLocale(""));

        // Edge case: invalid input (missing language code)
        try {
            LocaleUtils.toLocale("_GB");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Edge case: invalid input (missing country code)
        try {
            LocaleUtils.toLocale("en_");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Edge case: invalid input (invalid separator)
        try {
            LocaleUtils.toLocale("en_GBx");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic usage
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                Locale.getDefault()
        );
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        // Edge case: null input
        assertEmpty(LocaleUtils.localeLookupList(null));

        // Edge case: defaultLocale is null
        Locale defaultLocale = null;
        List<Locale> actual2 = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEmpty(actual2);
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic usage
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);

        // Edge case: availableLocales is empty
        List<Locale> expected2 = Collections.emptyList();
        List<Locale> actual2 = LocaleUtils.availableLocaleList();
        assertEquals(expected2, actual2);
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic usage
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Edge case: null input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Edge case: unavailable locale
        Locale locale2 = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic usage
        List<Locale> expected = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA")
        );
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, actual);

        // Edge case: null input
        List<Locale> expected2 = Collections.emptyList();
        List<Locale> actual2 = LocaleUtils.languagesByCountry(null);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic usage
        List<Locale> expected = Arrays.asList(
                new Locale("CA", "en"),
                new Locale("CA", "fr")
        );
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, actual);

        // Edge case: null input
        List<Locale> expected3 = Collections.emptyList();
        List<Locale> actual3 = LocaleUtils.countriesByLanguage(null);
        assertEquals(expected3, actual3);
    }

    private void assertEquals(List expected, List actual) {
        if (expected.size() != actual.size()) {
            fail("Unexpected size: expected=" + expected.size() + ", actual=" + actual.size());
        }
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    private void assertEmpty(List list) {
        assertTrue(list.isEmpty());
    }
}