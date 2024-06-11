package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));

        // Test invalid inputs
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("x"));
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_xx"));
        assertNull(LocaleUtils.toLocale("en_xx_xx"));
        assertNull(LocaleUtils.toLocale("en_XX"));

        // Test variant casing
        assertEquals(Locale.ENGLISH, LoclocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_varies"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_varying"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_variant"));
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        List list = LocaleUtils.localeLookupList(null);
        assertTrue(list.isEmpty());

        // Test base locale
        List baseList = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(4, baseList.size());
        assertEquals(Locale.ENGLISH, baseList.get(0));
        assertEquals(Locale.ENGLISH, baseList.get(1));
        assertEquals(Locale.US, baseList.get(2));
        assertSame(Locale.ENGLISH, baseList.get(3));

        // Test specific locale
        List specificList = LocaleUtils.localeLookupList(Locale.FRENCH);
        assertEquals(5, specificList.size());
        assertEquals(Locale.FRENCH, specificList.get(0));
        assertEquals(Locale.FRENCH, specificList.get(1));
        assertEquals(Locale.FRENCH, specificList.get(2));
        assertEquals(Locale.FRENCH, specificList.get(3));
        assertEquals(Locale.FRENCH, specificList.get(4));

        // Test default locale
        List defaultList = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.ITALIAN);
        assertEquals(5, defaultList.size());
        assertEquals(Locale.ENGLISH, defaultList.get(0));
        assertEquals(Locale.ITALIAN, defaultList.get(1));
        assertEquals(Locale.ENGLISH, defaultList.get(2));
        assertEquals(Locale.ENGLISH, defaultList.get(3));
        assertEquals(Locale.ITALIAN, defaultList.get(4));
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocales = LocaleUtils.availableLocaleList();
        assertTrue(availableLocales.size() > 0);

        for (Object locale : availableLocales) {
            assertTrue(locale instanceof Locale);
        }

        List subList = new ArrayList(5);
        subList.addAll(Arrays.asList("en", "fr", "de", "es", "it"));
        List expectedLocales = new ArrayList(LocaleUtils.availableLocaleList());
        assertTrue(subList.containsAll(expectedLocales));
    }

    @Test
    public void testLanguagesByCountry() {
        List frenchLanguages = LocaleUtils.languagesByCountry("FR");
        assertFalse(frenchLanguages.isEmpty());
        assertEquals(Locale.FRENCH, frenchLanguages.get(0));

        List germanLanguages = LocaleUtils.languagesByCountry("DE");
        assertFalse(germanLanguages.isEmpty());
        assertEquals(Locale.GERMAN, germanLanguages.get(0));

        List spanishLanguages = LocaleUtils.languagesByCountry("ES");
        assertFalse(spanishLanguages.isEmpty());
        assertEquals(Locale.SPANISH, spanishLanguages.get(0));

        List italianLanguages = LocaleUtils.languagesByCountry("IT");
        assertFalse(italianLanguages.isEmpty());
        assertEquals(Locale.ITALIAN, italianLanguages.get(0));

        List emptyList = LocaleUtils.languagesByCountry(null);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List englishCountries = LocaleUtils.countriesByLanguage("en");
        assertFalse(englishCountries.isEmpty());
        assertEquals(Locale.ENGLISH, englishCountries.get(0));

        List frenchCountries = LocaleUtils.countriesByLanguage("fr");
        assertFalse(frenchCountries.isEmpty());
        assertEquals(Locale.FRENCH, frenchCountries.get(0));

        List germanCountries = LocaleUtils.countriesByLanguage("de");
        assertFalse(germanCountries.isEmpty());
        assertEquals(Locale.GERMAN, germanCountries.get(0));

        List spanishCountries = LocaleUtils.countriesByLanguage("es");
        assertFalse(spanishCountries.isEmpty());
        assertEquals(Locale.SPANISH, spanishCountries.get(0));

        List italianCountries = LocaleUtils.countriesByLanguage("it");
        assertFalse(italianCountries.isEmpty());
        assertEquals(Locale.ITALIAN, italianCountries.get(0));

        List emptyList = LocaleUtils.countriesByLanguage(null);
        assertTrue(emptyList.isEmpty());
    }
}