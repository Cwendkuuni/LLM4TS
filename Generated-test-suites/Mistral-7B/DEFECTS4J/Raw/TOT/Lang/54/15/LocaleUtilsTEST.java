package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests the toLocale method with valid input.
     */
    @Test
    public void testToLocale() {
        // Test with 2-character language code
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals("en", locale1.getLanguage());
        assertEquals("", locale1.getCountry());

        // Test with 5-character locale code
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale2.getLanguage());
        assertEquals("GB", locale2.getCountry());

        // Test with 7-character locale code
        Locale locale3 = LocaleUtils.toLocale("en_GB_XXX");
        assertEquals("en", locale3.getLanguage());
        assertEquals("GB", locale3.getCountry());
        assertEquals("XXX", locale3.getVariant());
    }

    /**
     * Tests the toLocale method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        // Test with empty string
        LocaleUtils.toLocale("");

        // Test with invalid language code
        LocaleUtils.toLocale("frAB");

        // Test with invalid country code
        LocaleUtils.toLocale("en_ab");

        // Test with separator other than underscore
        LocaleUtils.toLocale("en_DE_XXX:XXX");

        // Test with invalid length
        LocaleUtils.toLocale("en_XXX");

        // Test with invalid character in language code
        LocaleUtils.toLocale("en_ZX");

        // Test with invalid character in country code
        LocaleUtils.toLocale("en_xX");
    }

    /**
     * Tests the localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupList() {
        // Test with null locale
        List<Locale> list1 = LocaleUtils.localeLookupList(null);
        assertEquals(Collections.emptyList(), list1);

        // Test with valid locale and no default locale
        Locale locale1 = new Locale("fr", "CA", "XXX");
        List<Locale> list2 = LocaleUtils.localeLookupList(locale1);
        List<Locale> expectedList2 = Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expectedList2, list2);

        // Test with valid locale and default locale
        Locale defaultLocale = new Locale("en");
        List<Locale> list3 = LocaleUtils.localeLookupList(locale1, defaultLocale);
        List<Locale> expectedList3 = Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        assertEquals(expectedList3, list3);
    }

    /**
     * Tests the availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertFalse(list.isEmpty());
        for (Locale locale : list) {
            assertTrue(Locale.isValidLocale(locale));
        }
    }

    /**
     * Tests the isAvailableLocale method.
     */
    @Test
    public void testIsAvailableLocale() {
        // Test with valid locale
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));

        // Test with invalid locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr", "AB")));
    }

    /**
     * Tests the languagesByCountry method.
     */
    @Test
    public void testLanguagesByCountry() {
        // Test with null country code
        List<Locale> list1 = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.emptyList(), list1);

        // Test with valid country code
        List<Locale> list2 = LocaleUtils.languagesByCountry("CA");
        List<Locale> expectedList2 = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        assertEquals(expectedList2, list2);
    }

    /**
     * Tests the countriesByLanguage method.
     */
    @Test
    public void testCountriesByLanguage() {
        // Test with null language code
        List<Locale> list1 = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.emptyList(), list1);

        // Test with valid language code
        List<Locale> list2 = LocaleUtils.countriesByLanguage("fr");
        List<Locale> expectedList2 = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"));
        assertEquals(expectedList2, list2);
    }
}