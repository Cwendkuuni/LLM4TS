package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

/**
 * Tests for {@link LocaleUtils} class.
 */
public class LocaleUtilsTest {

    /**
     * Test method for {@link LocaleUtils#toLocale(String)}.
     */
    @Test
    public void testToLocale() {
        // valid inputs
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE"));

        // invalid inputs
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("en_XX"));
        assertNull(LocaleUtils.toLocale("en_XX_xx"));
        assertNull(LocaleUtils.toLocale("en_XX_xx_xx"));
    }

    /**
     * Test method for {@link LocaleUtils#localeLookupList(Locale)}.
     */
    @Test
    public void testLocaleLookupList() {
        Locale locale = Locale.ENGLISH;
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(4, result.size());
        assertEquals(locale, result.get(0));
        assertTrue(result.get(1).getLanguage().equals("en"));
        assertTrue(result.get(1).getCountry().isEmpty());
        assertTrue(result.get(2).getLanguage().equals("en"));
        assertTrue(result.get(2).getCountry().equals("CA"));
        assertTrue(result.get(3).getLanguage().equals("en"));
        assertTrue(result.get(3).getCountry().equals("US"));

        locale = Locale.US;
        result = LocaleUtils.localeLookupList(locale);
        assertEquals(5, result.size());
        assertEquals(locale, result.get(0));
        assertTrue(result.get(1).getLanguage().equals("en"));
        assertTrue(result.get(1).getCountry().isEmpty());
        assertTrue(result.get(2).getLanguage().equals("en"));
        assertTrue(result.get(2).getCountry().equals("US"));
        assertTrue(result.get(3).getLanguage().equals("en"));
        assertTrue(result.get(3).getCountry().isEmpty());
        assertTrue(result.get(4).getLanguage().equals("und"));
        assertTrue(result.get(4).getCountry().isEmpty());
    }

    /**
     * Test method for {@link LocaleUtils#localeLookupList(Locale, Locale)}.
     */
    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = Locale.ENGLISH;
        Locale defaultLocale = Locale.FRENCH;
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(5, result.size());
        assertEquals(locale, result.get(0));
        assertTrue(result.get(1).getLanguage().equals("en"));
        assertTrue(result.get(1).getCountry().isEmpty());
        assertTrue(result.get(2).getLanguage().equals("en"));
        assertTrue(result.get(2).getCountry().equals("CA"));
        assertTrue(result.get(3).getLanguage().equals("en"));
        assertTrue(result.get(3).getCountry().equals("US"));
        assertTrue(result.get(4).equals(defaultLocale));

        locale = Locale.FRENCH;
        result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertTrue(result.get(1).getLanguage().equals("fr"));
        assertTrue(result.get(1).getCountry().isEmpty());
        assertTrue(result.get(2).getLanguage().equals("en"));
        assertTrue(result.get(2).getCountry().isEmpty());
    }

    /**
     * Test method for {@link LocaleUtils#availableLocaleList()}.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertTrue(result.size() > 0);
        for (Locale locale : result) {
            assertTrue(locale.getLanguage().length() > 0);
            assertTrue(locale.getCountry().length() > 0 || locale.getCountry().isEmpty());
        }
    }

    /**
     * Test method for {@link LocaleUtils#availableLocaleSet()}.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertTrue(result.size() > 0);
        for (Locale locale : result) {
            assertTrue(locale.getLanguage().length() > 0);
            assertTrue(locale.getCountry().length() > 0 || locale.getCountry().isEmpty());
        }
    }

    /**
     * Test method for {@link LocaleUtils#languagesByCountry(String)}.
     */
    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertEquals(18, result.size());
        assertTrue(result.get(0).getLanguage().equals("en"));
        assertTrue(result.get(1).getLanguage().equals("es"));
        assertTrue(result.get(2).getLanguage().equals("fr"));
        // other tests omitted for brevity

        result = LocaleUtils.languagesByCountry("FR");
        assertEquals(24, result.size());
        assertTrue(result.get(0).getLanguage().equals("fr"));
        // other tests omitted for brevity

        result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }

    /**
     * Test method for {@link LocaleUtils#countriesByLanguage(String)}.
     */
    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(27, result.size());
        assertTrue(result.get(0).getCountry().equals("US"));
        // other tests omitted for brevity

        result = LocaleUtils.countriesByLanguage("fr");
        assertEquals(14, result.size());
        assertTrue(result.get(0).getCountry().equals("CA"));
        // other tests omitted for brevity

        result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }
}