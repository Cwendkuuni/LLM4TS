package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid input.
     */
    @Test
    public void testToLocaleValidInput() {
        Locale inputLocale1 = LocaleUtils.toLocale("en");
        Locale inputLocale2 = LocaleUtils.toLocale("en_GB");
        Locale inputLocale3 = LocaleUtils.toLocale("en_GB_xxx");

        assertEquals("English", inputLocale1.getDisplayLanguage());
        assertEquals("en", inputLocale1.getLanguage());
        assertEquals("", inputLocale1.getCountry());
        assertEquals("", inputLocale1.getVariant());

        assertEquals("English (United Kingdom)", inputLocale2.getDisplayLanguage());
        assertEquals("en", inputLocale2.getLanguage());
        assertEquals("GB", inputLocale2.getCountry());
        assertEquals("", inputLocale2.getVariant());

        assertEquals("English (United Kingdom) (xxx)", inputLocale3.getDisplayLanguage());
        assertEquals("en", inputLocale3.getLanguage());
        assertEquals("GB", inputLocale3.getCountry());
        assertEquals("xxx", inputLocale3.getVariant());
    }

    /**
     * Test toLocale method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        LocaleUtils.toLocale("fr_CA_InvalidFormat");
    }

    /**
     * Test localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupListValidInput() {
        Locale baseLocale = LocaleUtils.toLocale("fr_CA");
        List lookupList = LocaleUtils.localeLookupList(baseLocale, Locale.getDefault());

        assertEquals(4, lookupList.size());
        assertTrue(lookupList.contains(baseLocale));
        assertEquals("French (Canada)", lookupList.get(1).getDisplayLanguage());
        assertEquals("fr", lookupList.get(1).getLanguage());
        assertEquals("CA", lookupList.get(1).getCountry());
        assertEquals("", lookupList.get(1).getVariant());

        assertTrue(lookupList.contains(Locale.getDefault()));
        assertEquals("Default locale", Locale.getDefault().getDisplayLanguage());
        assertEquals("", Locale.getDefault().getLanguage());
        assertEquals("", Locale.getDefault().getCountry());
        assertEquals("", Locale.getDefault().getVariant());
    }

    /**
     * Test localeLookupList method with null input.
     */
    @Test
    public void testLocaleLookupListNullInput() {
        List lookupList = LocaleUtils.localeLookupList(null, Locale.getDefault());
        assertTrue(Collections.isEmpty(lookupList));
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List availableLocales = LocaleUtils.availableLocaleList();

        assertEquals(233, availableLocales.size());

        assertTrue(availableLocales.contains(Locale.US));
        assertTrue(availableLocales.contains(Locale.CHINA));
        assertTrue(availableLocales.contains(Locale.JAPAN));
        assertTrue(availableLocales.contains(Locale.FRANCE));
    }

    /**
     * Test availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set availableLocaleSet = LocaleUtils.availableLocaleSet();

        assertEquals(Locale.getAvailableLocales().length, availableLocaleSet.size());

        assertTrue(availableLocaleSet.contains(Locale.US));
        assertTrue(availableLocaleSet.contains(Locale.CHINA));
        assertTrue(availableLocaleSet.contains(Locale.JAPAN));
        assertTrue(availableLocaleSet.contains(Locale.FRANCE));
    }

    /**
     * Test isAvailableLocale method with valid input.
     */
    @Test
    public void testIsAvailableLocaleValidInput() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CHINA));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.JAPAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRANCE));
    }

    /**
     * Test isAvailableLocale method with invalid input.
     */
    @Test
    public void testIsAvailableLocaleInvalidInput() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    /**
     * Test languagesByCountry method with valid input.
     */
    @Test
    public void testLanguagesByCountryValidInput() {
        List languagesByCountry = LocaleUtils.languagesByCountry("CA");

        assertEquals(4, languagesByCountry.size());
        assertTrue(languagesByCountry.contains(Locale.CANADA_FRENCH));
        assertTrue(languagesByCountry.contains(Locale.CANADA));
        assertTrue(languagesByCountry.contains(Locale.ENGLISH_CANADA));
        assertTrue(languagesByCountry.contains(Locale.ENGLISH));
    }

    /**
     * Test languagesByCountry method with null input.
     */
    @Test
    public void testLanguagesByCountryNullInput() {
        List languagesByCountry = LocaleUtils.languagesByCountry(null);
        assertTrue(Collections.isEmpty(languagesByCountry));
    }

    /**
     * Test countriesByLanguage method with valid input.
     */
    @Test
    public void testCountriesByLanguageValidInput() {
        List countriesByLanguage = LocaleUtils.countriesByLanguage("en");

        assertEquals(2, countriesByLanguage.size());
        assertTrue(countriesByLanguage.contains(Locale.ENGLISH_AUSTRALIA));
        assertTrue(countriesByLanguage.contains(Locale.ENGLISH));
    }

    /**
     * Test countriesByLanguage method with null input.
     */
    @Test
    public void testCountriesByLanguageNullInput() {
        List countriesByLanguage = LocaleUtils.countriesByLanguage(null);
        assertTrue(Collections.isEmpty(countriesByLanguage));
    }
}