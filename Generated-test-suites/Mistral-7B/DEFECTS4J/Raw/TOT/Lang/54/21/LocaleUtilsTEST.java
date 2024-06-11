package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locales
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("es", LocaleUtils.toLocale("es").toString());

        // Test empty locale
        assertNull(LocaleUtils.toLocale(null));

        // Test invalid locale
        try {
            LocaleUtils.toLocale("xx_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty locale and default locale
        List lookupList = LocaleUtils.localeLookupList(null, Locale.getDefault());
        assertEquals(1, lookupList.size());
        assertEquals(Locale.getDefault(), lookupList.get(0));

        // Test valid locale with no variant
        Locale locale = new Locale("en", "GB");
        lookupList = LocaleUtils.localeLookupList(locale, Locale.getDefault());
        assertEquals(3, lookupList.size());
        assertEquals(locale, lookupList.get(0));
        assertEquals(new Locale("en", "GB"), lookupList.get(1));
        assertEquals(Locale.getDefault(), lookupList.get(2));

        // Test valid locale with variant
        locale = new Locale("en", "GB", "X");
        lookupList = LocaleUtils.localeLookupList(locale, Locale.getDefault());
        assertEquals(4, lookupList.size());
        assertEquals(locale, lookupList.get(0));
        assertEquals(new Locale("en", "GB"), lookupList.get(1));
        assertEquals(Locale.getDefault(), lookupList.get(2));
        assertEquals(Locale.getDefault(), lookupList.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(0, availableLocales.size());

        // Test available locales
        List locales = Locale.getAvailableLocales();
        List availableLocaleList = LocaleUtils.availableLocaleList();
        assertEquals(locales.size(), availableLocaleList.size());
        for (int i = 0; i < locales.size(); i++) {
            Locale locale = (Locale) locales.get(i);
            assertEquals(locale.toString(), availableLocaleList.get(i).toString());
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Test available locale
        Locale locale = Locale.US;
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test invalid locale
        Locale invalidLocale = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty country
        List languages = LocaleUtils.languagesByCountry(null);
        assertEquals(0, languages.size());

        // Test valid country
        List languagesByCountry = LocaleUtils.languagesByCountry("CA");
        assertEquals(2, languagesByCountry.size());
        assertEquals("en", languagesByCountry.get(0).toString());
        assertEquals("fr", languagesByCountry.get(1).toString());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty language
        List countries = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, countries.size());

        // Test valid language
        List countriesByLanguage = LocaleUtils.countriesByLanguage("es");
        assertEquals(2, countriesByLanguage.size());
        assertEquals("ES", countriesByLanguage.get(0).toString());
        assertEquals("MX", countriesByLanguage.get(1).toString());
    }
}