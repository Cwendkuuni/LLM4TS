
package org.apache.commons.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertNull(LocaleUtils.toLocale(null));
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("pt", "BR"), LocaleUtils.toLocale("pt_BR"));
        assertEquals(new Locale("zh", "CN"), LocaleUtils.toLocale("zh_CN"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("invalidFormat");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguageCode() {
        LocaleUtils.toLocale("EN_GB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidCountryCode() {
        LocaleUtils.toLocale("en_gb");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidSeparator() {
        LocaleUtils.toLocale("en-GB");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = Collections.singletonList(defaultLocale);
        List<Locale> actualList = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertFalse(availableLocales.isEmpty());
        assertTrue(availableLocales.contains(new Locale("en")));
        assertTrue(availableLocales.contains(new Locale("fr")));
        assertTrue(availableLocales.contains(new Locale("es")));
    }

    @Test
    public void testAvailableLocaleSet() {
        assertEquals(LocaleUtils.availableLocaleList().size(), LocaleUtils.availableLocaleSet().size());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("es")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("pt")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("de")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("BR");
        assertFalse(languages.isEmpty());
        assertTrue(languages.contains(new Locale("pt", "BR")));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        assertTrue(languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertFalse(countries.isEmpty());
        assertTrue(countries.contains(new Locale("en", "US")));
        assertTrue(countries.contains(new Locale("en", "GB")));
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertTrue(countries.isEmpty());
    }

}
