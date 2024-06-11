package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("fr", "CA", "xxx"), LocaleUtils.toLocale("fr_CA_xxx"));
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalid1() {
        LocaleUtils.toLocale("en1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalid2() {
        LocaleUtils.toLocale("en_GB_xxxx");
    }

    @Test
    public void testLocaleLookupList1() {
        List<Locale> locales = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertEquals(3, locales.size());
        assertEquals(new Locale("fr", "CA", "xxx"), locales.get(0));
        assertEquals(new Locale("fr", "CA"), locales.get(1));
        assertEquals(new Locale("fr"), locales.get(2));
    }

    @Test
    public void testLocaleLookupList2() {
        List<Locale> locales = LocaleUtils.localeLookupList(new Locale("en"));
        assertEquals(2, locales.size());
        assertEquals(new Locale("en"), locales.get(0));
        assertEquals(Locale.getDefault(), locales.get(1));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertTrue(locales.contains(Locale.ENGLISH));
        assertTrue(locales.contains(Locale.FRENCH));
        assertTrue(locales.contains(Locale.GERMAN));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertTrue(locales.contains(Locale.ENGLISH));
        assertTrue(locales.contains(Locale.FRENCH));
        assertTrue(locales.contains(Locale.GERMAN));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("CA");
        assertTrue(langs.contains(new Locale("en", "CA")));
        assertTrue(langs.contains(new Locale("fr", "CA")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertTrue(countries.contains(new Locale("fr", "CA")));
        assertTrue(countries.contains(new Locale("fr", "FR")));
    }
}