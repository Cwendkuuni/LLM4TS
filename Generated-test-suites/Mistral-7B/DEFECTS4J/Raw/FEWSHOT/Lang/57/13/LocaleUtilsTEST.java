package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR_xxx"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR_xxx_yyy"));

        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_xx_zzz"));
        assertNull(LocaleUtils.toLocale("xx_xx_zzz_ww"));

        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB")); // JDK1.3 behaviour
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_xx")); // JDK1.3 behaviour

        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB")); // JDK1.4 behaviour
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_xx")); // JDK1.4 behaviour
    }

    @Test
    public void testLocaleLookupList() {
        List availableLocales = availableLocaleList();

        Locale defaultLocale = Locale.ENGLISH;
        Locale testLocale = new Locale("fr", "CA");

        List testList = localeLookupList(testLocale);
        List defaultList = localeLookupList(defaultLocale);

        assertEquals(4, testList.size());
        assertEquals(testLocale, testList.get(0));
        assertEquals(Locale.FRENCH, testList.get(1));
        assertEquals(Locale.FRENCH, testList.get(2));
        assertEquals(defaultLocale, testList.get(3));

        assertEquals(1, defaultList.size());
        assertEquals(defaultLocale, defaultList.get(0));
    }

    @Test
    public void testLocaleLookupListWithDefault() {
        Locale defaultLocale = Locale.ENGLISH;
        Locale testLocale = new Locale("fr", "CA");

        List testList = localeLookupList(testLocale, defaultLocale);
        List emptyList = localeLookupList(null);

        assertEquals(5, testList.size());
        assertEquals(testLocale, testList.get(0));
        assertEquals(Locale.FRENCH, testList.get(1));
        assertEquals(Locale.FRENCH, testList.get(2));
        assertEquals(Locale.FRENCH, testList.get(3));
        assertEquals(defaultLocale, testList.get(4));

        assertEquals(0, emptyList.size());
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocales = availableLocaleList();

        assertEquals(216, availableLocales.size());
        assertFalse(availableLocales.contains(Locale.ROOT));

        for (Locale locale : availableLocales) {
            assertTrue(isAvailableLocale(locale));
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        Set availableLocales = availableLocaleSet();

        assertEquals(216, availableLocales.size());
        assertFalse(availableLocales.contains(Locale.ROOT));

        for (Locale locale : availableLocaleList()) {
            assertTrue(availableLocales.contains(locale));
        }
    }

    @Test
    public void testIsAvailableLocale() {
        List availableLocales = availableLocaleList();

        for (Locale locale : availableLocales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        assertFalse(LocaleUtils.isAvailableLocale(Locale.ROOT));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "US";

        List languages = languagesByCountry(countryCode);

        assertEquals(10, languages.size());
        assertTrue(languages.contains(Locale.ENGLISH));
        assertTrue(languages.contains(Locale.ITALIAN));
        assertTrue(languages.contains(Locale.SPANISH));
        // ...
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";

        List countries = countriesByLanguage(languageCode);

        assertEquals(11, countries.size());
        assertTrue(countries.contains(Locale.FRENCH));
        assertTrue(countries.contains(Locale.CANADIAN_FRENCH));
        assertTrue(countries.contains(Locale.FRENCH_SWISS));
        // ...
    }
}