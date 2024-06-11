package org.apache.commons.lang.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_FR", LocaleUtils.toLocale("fr_FR").toString());
        assertEquals("de_DE", LocaleUtils.toLocale("de_DE").toString());

        // Test empty input
        assertNull(LocaleUtils.toLocale(null));

        // Test invalid input - language code is empty
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid input - language code is not lowercase
        assertNull(LocaleUtils.toLocale("EN_US"));

        // Test invalid input - country code is empty
        assertNull(LocaleUtils.toLocale("en_"));

        // Test invalid input - country code is not uppercase
        assertNull(LocaleUtils.toLocale("en_gb"));

        // Test invalid input - separator is not underscore
        assertNull(LocaleUtils.toLocale("en:US"));

        // Test invalid input - length is incorrect
        assertNull(LocaleUtils.toLocale("en_US_"));
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));

        // Test single locale
        Locale locale = new Locale("en", "GB");
        List list = LocaleUtils.localeLookupList(locale);
        assertEquals(3, list.size());
        assertEquals(locale, list.get(0));
        assertEquals("en_GB", list.get(1).toString());
        assertEquals("en", list.get(2).toString());

        // Test multiple locales
        locale = new Locale("en", "GB", "London");
        list = LocaleUtils.localeLookupList(locale, new Locale("en", "US"));
        assertEquals(5, list.size());
        assertEquals(locale, list.get(0));
        assertEquals("en_GB_London", list.get(1).toString());
        assertEquals("en_GB", list.get(2).toString());
        assertEquals("en", list.get(3).toString());
        assertEquals("en_US", list.get(4).toString());

        // Test default locale
        list = LocaleUtils.localeLookupList(locale, null);
        assertEquals(6, list.size());
        assertEquals(locale, list.get(0));
        assertEquals("en_GB_London", list.get(1).toString());
        assertEquals("en_GB", list.get(2).toString());
        assertEquals("en", list.get(3).toString());
        assertEquals("en_US", list.get(4).toString());
        assertEquals("en", list.get(5).toString());
    }

    @Test
    public void testAvailableLocaleList() {
        List list = LocaleUtils.availableLocaleList();
        assertEquals(164, list.size());

        // Test specific locales
        assertTrue(list.contains(new Locale("en", "US")));
        assertTrue(list.contains(new Locale("fr", "FR")));
        assertTrue(list.contains(new Locale("de", "DE")));

        // Test empty locale
        assertFalse(list.contains(new Locale("", "")));

        // Test invalid locale
        assertFalse(list.contains(new Locale("en_US_xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List list = LocaleUtils.languagesByCountry("US");
        assertEquals(17, list.size());

        // Test empty country
        list = LocaleUtils.languagesByCountry(null);
        assertEquals(0, list.size());

        // Test invalid country
        list = LocaleUtils.languagesByCountry("");
        assertEquals(0, list.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List list = LocaleUtils.countriesByLanguage("en");
        assertEquals(25, list.size());

        // Test empty language
        list = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, list.size());

        // Test invalid language
        list = LocaleUtils.countriesByLanguage("");
        assertEquals(0, list.size());
    }
}