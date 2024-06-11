
package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.Locale;
import java.util.List;
import java.util.ArrayList;


public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertNull(LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", "US", "variant"), LocaleUtils.toLocale("en_US_variant"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidFormat() {
        LocaleUtils.toLocale("en_US_");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");

        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(locale);
        expectedList.add(new Locale("en"));

        assertEquals(expectedList, LocaleUtils.localeLookupList(locale));

        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, new Locale("de")));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
        assertTrue(availableLocales.contains(new Locale("en", "US")));
        assertTrue(availableLocales.contains(Locale.JAPAN));
        assertTrue(availableLocales.contains(new Locale("de")));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocalesSet = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocalesSet);
        assertTrue(availableLocalesSet.contains(new Locale("en", "US")));
        assertTrue(availableLocalesSet.contains(Locale.JAPAN));
        assertTrue(availableLocalesSet.contains(new Locale("de")));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.JAPAN));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("de")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("en", "US"));
        expectedList.add(new Locale("en", "GB"));

        assertEquals(expectedList, LocaleUtils.languagesByCountry("US"));

        expectedList.clear();
        expectedList.add(new Locale("fr", "FR"));
        expectedList.add(new Locale("fr", "CA"));

        assertEquals(expectedList, LocaleUtils.languagesByCountry("FR"));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("en", "US"));
        expectedList.add(new Locale("es", "US"));

        assertEquals(expectedList, LocaleUtils.countriesByLanguage("en"));

        expectedList.clear();
        expectedList.add(new Locale("en", "GB"));
        expectedList.add(new Locale("es", "MX"));

        assertEquals(expectedList, LocaleUtils.countriesByLanguage("es"));
    }
}
