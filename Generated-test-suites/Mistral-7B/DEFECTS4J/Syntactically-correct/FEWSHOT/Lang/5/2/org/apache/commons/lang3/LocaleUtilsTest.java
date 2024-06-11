package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Empty string
        assertNull(LocaleUtils.toLocale(""));

        // One letter language
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.FRENCH, Locale.FRENCH);

        // Two letters language
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, Locale.FRENCH);

        // Language with a country
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, Locale.FRENCH);

        // Language with a country and a variant
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_foo"));
        assertEquals(Locale.FRENCH, Locale.FRENCH);

        // Incorrect format
        assertNull(LocaleUtils.toLocale("en_GB_foo_bar"));
        assertNull(LocaleUtils.toLocale("en_US_foo"));
        assertNull(LocaleUtils.toLocale("en_foo"));
        assertNull(LocaleUtils.toLocale("en_US"));
        assertNull(LocaleUtils.toLocale("en"));
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testLocaleLookupList() {
        Locale enUS = new Locale("en", "US");
        Locale enGB = new Locale("en", "GB");
        Locale en = Locale.ENGLISH;

        List<Locale> list = LocaleUtils.localeLookupList(enUS);
        assertEquals(4, list.size());
        assertTrue(list.contains(enUS));
        assertTrue(list.contains(en));
        assertTrue(list.contains(Locale.US));
        assertTrue(list.contains(Locale.ENGLISH));

        list = LocaleUtils.localeLookupList(enGB);
        assertEquals(3, list.size());
        assertTrue(list.contains(enGB));
        assertTrue(list.contains(en));
        assertTrue(list.contains(Locale.ENGLISH));

        list = LocaleUtils.localeLookupList(en);
        assertEquals(2, list.size());
        assertTrue(list.contains(en));
        assertTrue(list.contains(Locale.ENGLISH));

        list = LocaleUtils.localeLookupList(null);
        assertEquals(0, list.size());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        int size = list.size();
        assertTrue(size > 0);

        List<Locale> sublist = list.subList(0, 10);
        for (Locale locale : sublist) {
            assertNotNull(locale);
        }

        Locale locale1 = list.get(0);
        Locale locale2 = list.get(size - 1);

        assertSame(locale1, locale1);
        assertSame(locale2, locale2);

        assertFalse(list.contains(Locale.ENGLISH));
        assertFalse(list.contains(Locale.US));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("CA");
        int size = list.size();
        assertTrue(size > 0);

        List<Locale> sublist = list.subList(0, 10);
        for (Locale locale : sublist) {
            assertNotNull(locale);
        }

        Locale locale1 = list.get(0);
        Locale locale2 = list.get(size - 1);

        assertSame(locale1, locale1);
        assertSame(locale2, locale2);

        assertEquals("en_CA", locale1.toLanguageTag());
        assertEquals("es_ES", locale2.toLanguageTag());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        int size = list.size();
        assertTrue(size > 0);

        List<Locale> sublist = list.subList(0, 10);
        for (Locale locale : sublist) {
            assertNotNull(locale);
        }

        Locale locale1 = list.get(0);
        Locale locale2 = list.get(size - 1);

        assertSame(locale1, locale1);
        assertSame(locale2, locale2);

        assertEquals("CA", locale1.getCountry());
        assertEquals("US", locale2.getCountry());
    }
}