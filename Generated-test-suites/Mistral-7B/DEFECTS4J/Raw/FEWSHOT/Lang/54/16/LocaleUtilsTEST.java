package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("fr", "CA", "xxx"), LocaleUtils.toLocale("fr_CA_xxx"));
        assertEquals(new Locale("fr", "", ""), LocaleUtils.toLocale("fr"));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
        assertEquals(new Locale("fr", "", "US"), LocaleUtils.toLocale("fr_US"));
        assertNull(LocaleUtils.toLocale(null));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("zx"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_XX"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_XX"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(4, list.size());
        assertTrue(list.contains(locale));
        assertTrue(list.contains(new Locale("fr", "CA")));
        assertTrue(list.contains(new Locale("fr", "")));
        assertTrue(list.contains(Locale.getDefault()));

        list = LocaleUtils.localeLookupList(new Locale("fr"));
        assertEquals(3, list.size());
        assertTrue(list.contains(new Locale("fr", "")));
        assertTrue(list.contains(Locale.getDefault()));
        assertTrue(list.contains(new Locale("fr", "CA")));

        list = LocaleUtils.localeLookupList(Locale.getDefault());
        assertEquals(1, list.size());
        assertTrue(list.contains(Locale.getDefault()));

        list = LocaleUtils.localeLookupList(null);
        assertEquals(0, list.size());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertEquals(3, locales.size());
        assertTrue(locales.contains(new Locale("en", "US")));
        assertTrue(locales.contains(new Locale("fr", "FR")));
        assertTrue(locales.contains(new Locale("es", "ES")));

        List<Locale> locales2 = LocaleUtils.availableLocaleList();
        assertEquals(3, locales2.size());
        assertEquals(locales, locales2);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertEquals(3, set.size());
        assertTrue(set.contains(new Locale("en", "US")));
        assertTrue(set.contains(new Locale("fr", "FR")));
        assertTrue(set.contains(new Locale("es", "ES")));

        Set<Locale> set2 = LocaleUtils.availableLocaleSet();
        assertEquals(3, set2.size());
        assertEquals(set, set2);
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "FR")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("es", "ES")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "GB")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr", "CA")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zx", "xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assertEquals(2, languages.size());
        assertTrue(languages.contains(new Locale("en", "CA")));
        assertTrue(languages.contains(new Locale("fr", "CA")));

        List<Locale> languages2 = LocaleUtils.languagesByCountry("CA");
        assertEquals(2, languages2.size());
        assertEquals(languages, languages2);

        List<Locale> languages3 = LocaleUtils.languagesByCountry("US");
        assertEquals(1, languages3.size());
        assertTrue(languages3.contains(new Locale("en", "US")));

        List<Locale> languages4 = LocaleUtils.languagesByCountry("XX");
        assertEquals(0, languages4.size());

        List<Locale> languages5 = LocaleUtils.languagesByCountry(null);
        assertEquals(0, languages5.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(3, countries.size());
        assertTrue(countries.contains(new Locale("en", "US")));
        assertTrue(countries.contains(new Locale("en", "CA")));
        assertTrue(countries.contains(new Locale("en", "GB")));

        List<Locale> countries2 = LocaleUtils.countriesByLanguage("en");
        assertEquals(3, countries2.size());
        assertEquals(countries, countries2);

        List<Locale> countries3 = LocaleUtils.countriesByLanguage("es");
        assertEquals(1, countries3.size());
        assertTrue(countries3.contains(new Locale("es", "ES")));

        List<Locale> countries4 = LocaleUtils.countriesByLanguage("XX");
        assertEquals(0, countries4.size());

        List<Locale> countries5 = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, countries5.size());
    }
}