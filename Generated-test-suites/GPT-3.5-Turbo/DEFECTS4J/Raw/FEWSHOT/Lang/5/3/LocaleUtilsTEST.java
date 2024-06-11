package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertNull(LocaleUtils.toLocale(null));
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("EN_GB"));
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en__"));
        assertEquals(new Locale("fr", "CA", "xxx"), LocaleUtils.toLocale("fr_CA_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat1() {
        LocaleUtils.toLocale("enGB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat2() {
        LocaleUtils.toLocale("en_GB_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat3() {
        LocaleUtils.toLocale("e");
    }

    @Test
    public void testLocaleLookupList_localeNull() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_defaultLocaleNull() {
        Locale locale = new Locale("en", "US");
        List<Locale> expected = Arrays.asList(locale);
        assertEquals(expected, LocaleUtils.localeLookupList(locale, null));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en", "GB");
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en"), defaultLocale);
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = new ArrayList<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale_true() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testIsAvailableLocale_false() {
        assertFalse(LocaleUtils.isAvailableLocale(Locale.JAPAN));
    }

    @Test
    public void testLanguagesByCountry_countryCodeNull() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testLanguagesByCountry_noLanguages() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("XX"));
    }

    @Test
    public void testLanguagesByCountry() {
        Locale locale1 = new Locale("en", "US");
        Locale locale2 = new Locale("en", "CA");
        List<Locale> expected = Arrays.asList(locale1, locale2);
        assertEquals(expected, LocaleUtils.languagesByCountry("US"));
    }

    @Test
    public void testCountriesByLanguage_languageCodeNull() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }

    @Test
    public void testCountriesByLanguage_noCountries() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("XX"));
    }

    @Test
    public void testCountriesByLanguage() {
        Locale locale1 = new Locale("en", "CA");
        List<Locale> expected = Arrays.asList(locale1);
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
    }
}