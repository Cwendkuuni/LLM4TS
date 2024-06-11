package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locales
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE"));

        // Test invalid locales
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("en_UK")); // missing country code
        assertNull(LocaleUtils.toLocale("en_GB_abc")); // invalid variant
        assertNull(LocaleUtils.toLocale("en_GB-abc")); // invalid separator
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> lookupList = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, lookupList.get(0));
        assertEquals(Locale.ENGLISH, lookupList.get(1));
        assertEquals(Locale.US, lookupList.get(2));
        assertSame(Locale.ENGLISH, lookupList.get(0).getLanguage());
        assertSame(Locale.US, lookupList.get(2).getLanguage());

        lookupList = LocaleUtils.localeLookupList(Locale.US, Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, lookupList.get(0));
        assertEquals(Locale.ENGLISH, lookupList.get(1));
        assertEquals(Locale.US, lookupList.get(2));
        assertSame(Locale.ENGLISH, lookupList.get(0).getLanguage());
        assertSame(Locale.US, lookupList.get(2).getLanguage());

        lookupList = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        assertEquals(Locale.FRENCH, lookupList.get(0));
        assertEquals(Locale.FRENCH, lookupList.get(1));
        assertEquals(Locale.FRENCH, lookupList.get(2));
        assertEquals(Locale.US, lookupList.get(3));
        assertSame(Locale.FRENCH, lookupList.get(0).getLanguage());
        assertSame(Locale.FRENCH, lookupList.get(1).getLanguage());
        assertSame(Locale.FRENCH, lookupList.get(2).getLanguage());
        assertSame(Locale.US, lookupList.get(3).getLanguage());

        lookupList = LocaleUtils.localeLookupList(Locale.JAPAN, Locale.ENGLISH);
        assertEquals(Locale.JAPAN, lookupList.get(0));
        assertEquals(Locale.JAPAN, lookupList.get(1));
        assertNull(lookupList.get(2));
        assertSame(Locale.JAPAN, lookupList.get(0).getLanguage());
        assertSame(Locale.JAPAN, lookupList.get(1).getLanguage());
    }

    @Test
    public void testLocaleUtilities() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();

        assertEquals(118, availableLocales.size());
        assertTrue(availableLocaleSet.contains(Locale.ENGLISH));
        assertTrue(availableLocaleSet.contains(Locale.US));
        assertTrue(availableLocaleSet.contains(Locale.FRENCH));
        assertTrue(availableLocaleSet.contains(Locale.GERMAN));

        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("US");
        assertEquals(8, languagesByCountry.size());
        assertTrue(languagesByCountry.contains(Locale.ENGLISH));
        assertTrue(languagesByCountry.contains(Locale.SPANISH));
        assertTrue(languagesByCountry.contains(Locale.FRENCH));
        assertTrue(languagesByCountry.contains(Locale.ITALIAN));
        assertTrue(languagesByCountry.contains(Locale.POLISH));
        assertTrue(languagesByCountry.contains(Locale.PORTUGUESE));
        assertTrue(languagesByCountry.contains(Locale.DANISH));

        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        assertEquals(62, countriesByLanguage.size());
        assertTrue(countriesByLanguage.contains(Locale.ENGLISH));
        assertTrue(countriesByLanguage.contains(Locale.AUSTRALIA));
        assertTrue(countriesByLanguage.contains(Locale.CANADA));
        assertTrue(countriesByLanguage.contains(Locale.INDIA));
        assertTrue(countriesByLanguage.contains(Locale.NEW_ZEALAND));
        assertTrue(countriesByLanguage.contains(Locale.SOUTH_AFRICA));
        assertTrue(countriesByLanguage.contains(Locale.UK));
        assertTrue(countriesByLanguage.contains(Locale.USA));

        // Test to make sure the lists are not modified
        List<Locale> availableLocales2 = new ArrayList<>(availableLocales);
        availableLocales2.add(Locale.JAPAN);
        assertFalse(availableLocales.contains(Locale.JAPAN));

        List<Locale> languagesByCountry2 = new ArrayList<>(languagesByCountry);
        languagesByCountry2.remove(Locale.ITALIAN);
        assertFalse(languagesByCountry.contains(Locale.ITALIAN));
    }
}