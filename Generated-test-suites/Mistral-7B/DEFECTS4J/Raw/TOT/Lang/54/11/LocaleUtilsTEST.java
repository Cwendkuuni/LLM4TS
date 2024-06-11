package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test cases for LocaleUtils class.
 */
public class LocaleUtilsTest {

    /**
     * Tests the toLocale method with valid inputs.
     */
    @Test
    public void testToLocaleSuccess() {
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale("en_US");
        assertEquals(expectedLocale, actualLocale);

        expectedLocale = new Locale("fr", "FR");
        actualLocale = LocaleUtils.toLocale("fr_FR");
        assertEquals(expectedLocale, actualLocale);

        expectedLocale = new Locale("es", "ES");
        actualLocale = LocaleUtils.toLocale("es_ES");
        assertEquals(expectedLocale, actualLocale);
    }

    /**
     * Tests the toLocale method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        LocaleUtils.toLocale(null);

        LocaleUtils.toLocale("en");

        LocaleUtils.toLocale("en_GB_");

        LocaleUtils.toLocale("en_GB_abc");
    }

    /**
     * Tests the localeLookupList method with valid inputs.
     */
    @Test
    public void testLocaleLookupListSuccess() {
        List<Locale> expectedList = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "GB"),
                new Locale("en"),
                new Locale("fr")
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);

        assertEquals(expectedList.size(), actualList.size());
        assertContentEquals(expectedList.toArray(new Locale[expectedList.size()]), actualList.toArray());
    }

    /**
     * Tests the localeLookupList method with empty input.
     */
    @Test
    public void testLocaleLookupListEmptyInput() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.localeLookupList(null);

        assertEquals(expectedList, actualList);
    }

    /**
     * Tests the availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> actualList = Arrays.asList(Locale.getAvailableLocales());

        assertEquals(expectedList, actualList);
    }

    /**
     * Tests the availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = LocaleUtils.availableLocaleSet();
        Set<Locale> actualSet = Collections.newSetFromMap(new HashMap<>());
        actualSet.addAll(Locale.getAvailableLocales());

        assertEquals(expectedSet, actualSet);
    }

    /**
     * Tests the isAvailableLocale method with valid inputs.
     */
    @Test
    public void testIsAvailableLocaleSuccess() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "FR");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Tests the isAvailableLocale method with invalid inputs.
     */
    @Test
    public void testIsAvailableLocaleInvalidInput() {
        assertFalse(LocaleUtils.isAvailableLocale(null));

        Locale locale = new Locale("en");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "US", "ABC");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Tests the languagesByCountry method with valid inputs.
     */
    @Test
    public void testLanguagesByCountrySuccess() {
        Collection<Locale> expectedList = Arrays.asList(
                new Locale("es", "ES"),
                new Locale("ca", "ES"),
                new Locale("fr", "CA"),
                new Locale("en", "US")
        );
        Collection<Locale> actualList = LocaleUtils.languagesByCountry("ES");

        assertEquals(expectedList.size(), actualList.size());
        assertContentEquals(expectedList.toArray(new Locale[expectedList.size()]), actualList.toArray());
    }

    /**
     * Tests the languagesByCountry method with empty input.
     */
    @Test
    public void testLanguagesByCountryEmptyInput() {
        Collection<Locale> expectedList = Collections.emptyList();
        Collection<Locale> actualList = LocaleUtils.languagesByCountry(null);

        assertEquals(expectedList, actualList);
    }

    /**
     * Tests the countriesByLanguage method with valid inputs.
     */
    @Test
    public void testCountriesByLanguageSuccess() {
        Collection<Locale> expectedList = Arrays.asList(
                new Locale("CA", "es"),
                new Locale("MX", "es"),
                new Locale("US", "en"),
                new Locale("GB", "en")
        );
        Collection<Locale> actualList = LocaleUtils.countriesByLanguage("es");

        assertEquals(expectedList.size(), actualList.size());
        assertContentEquals(expectedList.toArray(new Locale[expectedList.size()]), actualList.toArray());
    }

    /**
     * Tests the countriesByLanguage method with empty input.
     */
    @Test
    public void testCountriesByLanguageEmptyInput() {
        Collection<Locale> expectedList = Collections.emptyList();
        Collection<Locale> actualList = LocaleUtils.countriesByLanguage(null);

        assertEquals(expectedList, actualList);
    }

    /**
     * Helper method for asserting that two collections are equal.
     *
     * @param expected the expected collection
     * @param actual the actual collection
     */
    private void assertContentEquals(Object[] expected, Collection<?> actual) {
        assertEquals(expected.length, actual.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual.iterator().next());
        }
    }
}