package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // valid test cases
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.ITALY, LocaleUtils.toLocale("it_IT"));
        assertEquals(Locale.GERMANY, LocaleUtils.toLocale("de_DE"));

        // invalid test cases - missing separator
        assertNull(LocaleUtils.toLocale("en_G"));
        assertNull(LocaleUtils.toLocale("en_US_"));

        // invalid test cases - wrong case
        assertNull(LocaleUtils.toLocale("en_GB")); // lower case "gb"
        assertNull(LocaleUtils.toLocale("EN_GB")); // upper case "gb"
        assertNull(LocaleUtils.toLocale("en_FR")); // lower case "fr"
        assertNull(LocaleUtils.toLocale("EN_FR")); // upper case "fr"

        // invalid test cases - missing language or country
        assertNull(LocaleUtils.toLocale("_GB"));
        assertNull(LocaleUtils.toLocale("en_"));

        // invalid test cases - empty strings
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("_"));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = Arrays.asList(Locale.ITALY, Locale.ITALY, Locale.ITALY, Locale.ITALY, Locale.ITALY);
        List<Locale> result = LocaleUtils.localeLookupList(Locale.ITALY, Locale.JAPAN);
        assertEquals(expected, result);

        List<Locale> expected2 = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH, Locale.ENGLISH, Locale.US, Locale.US, Locale.US);
        List<Locale> result2 = LocaleUtils.localeLookupList(Locale.US, Locale.ENGLISH);
        assertEquals(expected2, result2);

        List<Locale> expected3 = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH, Locale.ENGLISH, Locale.US);
        List<Locale> result3 = LocaleUtils.localeLookupList(Locale.US, null);
        assertEquals(expected3, result3);

        List<Locale> expected4 = Collections.emptyList();
        List<Locale> result4 = LocaleUtils.localeLookupList(null, Locale.ENGLISH);
        assertEquals(expected4, result4);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertTrue(result.size() > 0);
        for (Locale locale : result) {
            assertFalse(locale.equals(Locale.ROOT));
        }
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Locale italian = new Locale("it", "IT");
        Locale german = new Locale("de", "DE");
        Locale english = new Locale("en", "US");

        assertTrue(LocaleUtils.isAvailableLocale(english));
        assertTrue(LocaleUtils.isAvailableLocale(italian));
        assertTrue(LocaleUtils.isAvailableLocale(german));

        assertFalse(LocaleUtils.isAvailableLocale(Locale.ROOT));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("GB");
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.SCOTS_Gaelic, Locale.WELSH);
        assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(Locale.US, Locale.GB, Locale.CA, Locale.IE, Locale.AU, Locale.NZ, Locale.ZA);
        assertEquals(expected, result);
    }
}