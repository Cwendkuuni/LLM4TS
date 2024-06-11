package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalid() {
        LocaleUtils.toLocale("en_G");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertEquals(Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr")), LocaleUtils.localeLookupList(locale));
        assertEquals(Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en")), LocaleUtils.localeLookupList(locale, new Locale("en")));
    }

    @Test
    public void testLocaleLookupListNull() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null, new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertEquals(Locale.getAvailableLocales().length, locales.size());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertEquals(Locale.getAvailableLocales().length, locales.size());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("abc")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assertTrue(languages.contains(new Locale("en", "CA")));
        assertTrue(languages.contains(new Locale("fr", "CA")));
        assertFalse(languages.contains(new Locale("en", "US")));
    }

    @Test
    public void testLanguagesByCountryNull() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertTrue(countries.contains(new Locale("en", "CA")));
        assertTrue(countries.contains(new Locale("en", "US")));
        assertFalse(countries.contains(new Locale("fr", "CA")));
    }

    @Test
    public void testCountriesByLanguageNull() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}