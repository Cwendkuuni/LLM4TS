package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
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
        assertNull(LocaleUtils.toLocale(null));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US_xxx"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_xxx"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_xx"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_xxxxx"));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expectedList = Arrays.asList(Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.ENGLISH);
        List<Locale> actualList = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        assertArrayEquals(expectedList.toArray(), actualList.toArray());

        expectedList = Arrays.asList(Locale.FRENCH, Locale.FRENCH);
        actualList = LocaleUtils.localeLookupList(Locale.FRENCH);
        assertArrayEquals(expectedList.toArray(), actualList.toArray());

        expectedList = Collections.singletonList(Locale.ENGLISH);
        actualList = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertArrayEquals(expectedList.toArray(), actualList.toArray());

        assertNull(LocaleUtils.localeLookupList(null));
        assertNull(LocaleUtils.localeLookupList(Locale.ENGLISH, null));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actualList = LocaleUtils.availableLocaleList();
        List<Locale> expectedList = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.GERMAN, Locale.FRENCH, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.SPANISH, Locale.PORTUGuese, Locale.RUSSIAN, Locale.POLISH);
        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> actualSet = LocaleUtils.availableLocaleSet();
        Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.GERMAN, Locale.FRENCH, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.SPANISH, Locale.PORTUGuese, Locale.RUSSIAN, Locale.POLISH));
        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ITALIAN_ITALY));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("it", "IT", "IT")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = Arrays.asList(Locale.ENGLISH, Locale.SCOTTISH_GAMES, Locale.WELSH);
        List<Locale> actualList = LocaleUtils.languagesByCountry("GB");
        assertArrayEquals(expectedList.toArray(), actualList.toArray());

        expectedList = Collections.singletonList(Locale.ENGLISH);
        actualList = LocaleUtils.languagesByCountry("US");
        assertArrayEquals(expectedList.toArray(), actualList.toArray());

        List<Locale> emptyList = Collections.emptyList();
        actualList = LocaleUtils.languagesByCountry(null);
        assertArrayEquals(emptyList.toArray(), actualList.toArray());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = Arrays.asList(Locale.ENGLISH, Locale.SCOTTISH_GAMES, Locale.WELSH, Locale.CANADA, Locale.AUSTRALIA);
        List<Locale> actualList = LocaleUtils.countriesByLanguage("en");
        assertArrayEquals(expectedList.toArray(), actualList.toArray());

        expectedList = Collections.singletonList(Locale.GERMAN);
        actualList = LocaleUtils.countriesByLanguage("de");
        assertArrayEquals(expectedList.toArray(), actualList.toArray());

        List<Locale> emptyList = Collections.emptyList();
        actualList = LocaleUtils.countriesByLanguage(null);
        assertArrayEquals(emptyList.toArray(), actualList.toArray());
    }
}