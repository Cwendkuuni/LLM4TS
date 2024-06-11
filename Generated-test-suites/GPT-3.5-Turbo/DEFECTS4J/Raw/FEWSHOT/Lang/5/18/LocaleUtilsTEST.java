
package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale);

        locale = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale);

        locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale);

        locale = LocaleUtils.toLocale("en_US");
        assertEquals(new Locale("en", "US"), locale);

        locale = LocaleUtils.toLocale("fr_FR");
        assertEquals(new Locale("fr", "FR"), locale);

        locale = LocaleUtils.toLocale("es_ES");
        assertEquals(new Locale("es", "ES"), locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLanguageCode() {
        LocaleUtils.toLocale("E1_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidCountryCode() {
        LocaleUtils.toLocale("es_es");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), Locale.getDefault()), list);

        locale = new Locale("en", "US");
        list = LocaleUtils.localeLookupList(locale);
        assertEquals(Arrays.asList(locale, new Locale("en")), list);

        locale = new Locale("es", "ES", "valencia");
        list = LocaleUtils.localeLookupList(locale);
        assertEquals(Arrays.asList(locale, new Locale("es", "ES")), list);
    }

    @Test
    public void testLocaleLookupList_withDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale), list);

        locale = new Locale("en", "US");
        defaultLocale = new Locale("fr");
        list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(Arrays.asList(locale, new Locale("en"), defaultLocale), list);

        locale = new Locale("es", "ES", "valencia");
        defaultLocale = new Locale("en", "US");
        list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(Arrays.asList(locale, new Locale("es", "ES"), defaultLocale), list);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertTrue(list.contains(Locale.ENGLISH));
        assertTrue(list.contains(Locale.FRENCH));
        assertTrue(list.contains(Locale.GERMAN));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertTrue(set.contains(Locale.ENGLISH));
        assertTrue(set.contains(Locale.FRENCH));
        assertTrue(set.contains(Locale.GERMAN));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertEquals(Arrays.asList(Locale.ENGLISH), list);

        list = LocaleUtils.languagesByCountry("GB");
        assertEquals(Arrays.asList(Locale.ENGLISH), list);

        list = LocaleUtils.languagesByCountry("FR");
        assertEquals(Arrays.asList(Locale.FRENCH), list);
    }

    @Test
    public void testLanguagesByCountry_invalidCountryCode() {
        List<Locale> list = LocaleUtils.languagesByCountry("XYZ");
        assertTrue(list.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertEquals(Arrays.asList(Locale.US, Locale.UK), list);

        list = LocaleUtils.countriesByLanguage("fr");
        assertEquals(Arrays.asList(Locale.FRANCE), list);

        list = LocaleUtils.countriesByLanguage("de");
        assertEquals(Arrays.asList(Locale.GERMANY), list);
    }

    @Test
    public void testCountriesByLanguage_invalidLanguageCode() {
        List<Locale> list = LocaleUtils.countriesByLanguage("xyz");
        assertTrue(list.isEmpty());
    }
}
