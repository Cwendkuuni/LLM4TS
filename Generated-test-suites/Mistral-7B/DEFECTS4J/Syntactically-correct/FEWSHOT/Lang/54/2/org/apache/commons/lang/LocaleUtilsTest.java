package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_XXX")); // variant locale
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xx")); // invalid language
        assertNull(LocaleUtils.toLocale("XX")); // invalid language (uppercase)
        assertNull(LocaleUtils.toLocale("en_XX")); // invalid country
        assertNull(LocaleUtils.toLocale("en_GB_XX")); // invalid variant
        assertNull(LocaleUtils.toLocale("en_GB_XXX_XX")); // invalid variant and country
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertTrue(list.contains(locale));
        assertTrue(list.contains(new Locale("fr", "CA")));
        assertTrue(list.contains(new Locale("fr", "")));
        assertTrue(list.contains(Locale.getDefault()));

        list = LocaleUtils.localeLookupList(Locale.getDefault(), locale);
        assertTrue(list.contains(locale));
        assertTrue(list.contains(new Locale("fr", "CA")));
        assertTrue(list.contains(new Locale("fr", "")));
        assertTrue(list.contains(Locale.getDefault()));

        list = LocaleUtils.localeLookupList(null, null);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        Collection<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(Locale.SIMPLIFIED_CHINESE, availableLocales.stream().filter(l -> l.getLanguage().equals("zh")).findFirst().get());
        assertEquals(Locale.TRADITIONAL_CHINESE, availableLocales.stream().filter(l -> l.getLanguage().equals("zh") && l.getCountry().equals("TW")).findFirst().get());
        assertEquals(Locale.ENGLISH, availableLocales.stream().filter(l -> l.getLanguage().equals("en")).findFirst().get());
        assertEquals(Locale.FRENCH, availableLocales.stream().filter(l -> l.getLanguage().equals("fr")).findFirst().get());
        assertFalse(availableLocales.contains(new Locale("xx", "xx"))); // invalid locale
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assertTrue(languages.contains(Locale.ENGLISH));
        assertTrue(languages.contains(Locale.FRENCH));
        assertFalse(languages.contains(new Locale("xx", ""))); // invalid locale
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertTrue(countries.contains(new Locale("FR", "CA")));
        assertTrue(countries.contains(new Locale("FR", "FR")));
        assertTrue(countries.contains(new Locale("FR", "BE")));
        assertFalse(countries.contains(new Locale("xx", ""))); // invalid locale
    }
}