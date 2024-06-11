package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;

public class LocaleUtilsTest {

    private static List<Locale> availableLocales;

    @BeforeClass
    public static void initAvailableLocales() {
        availableLocales = LocaleUtils.availableLocaleList();
    }

    @Test
    public void testToLocale_Empty() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_LanguageOnly() {
        Locale expected = new Locale("en");
        Locale actual = LocaleUtils.toLocale("en");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_LanguageCountry() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_LanguageCountryVariant() {
        Locale expected = new Locale("en", "US", "ms-MY");
        Locale actual = LocaleUtils.toLocale("en_US_ms-MY");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LanguageOnlyUppercase() {
        LocaleUtils.toLocale("EN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LanguageCountryLowercase() {
        LocaleUtils.toLocale("en_gb");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LanguageCountrySeparator() {
        LocaleUtils.toLocale("en_GB_en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LanguageCountryEmptyCountry() {
        LocaleUtils.toLocale("en_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LanguageCountryEmptyLanguage() {
        LocaleUtils.toLocale("_GB");
    }

    @Test
    public void testLocaleLookupList_SameLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(Collections.singletonList(locale), list);
    }

    @Test
    public void testLocaleLookupList_SameCountry() {
        Locale locale1 = new Locale("en", "US");
        Locale locale2 = new Locale("en", "US", "ms-MY");
        List<Locale> list = LocaleUtils.localeLookupList(locale1, locale2);
        List<Locale> expected = new ArrayList<>();
        expected.add(locale1);
        expected.add(locale2);
        assertEquals(expected, list);
    }

    @Test
    public void testLocaleLookupList_DifferentCountry() {
        Locale locale1 = new Locale("en", "US");
        Locale locale2 = new Locale("es", "ES");
        List<Locale> list = LocaleUtils.localeLookupList(locale1, locale2);
        List<Locale> expected = new ArrayList<>();
        expected.add(locale1);
        expected.add(locale2);
        assertEquals(expected, list);
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        Locale locale = null;
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(Collections.singletonList(defaultLocale), list);
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale locale = new Locale("en", "US", "ms-MY");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_EmptyCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.emptyList(), list);
    }

    @Test
    public void testLanguagesByCountry_AvailableLanguages() {
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        List<Locale> expected = availableLocales;
        assertEquals(expected, list);
    }

    @Test
    public void testCountriesByLanguage_EmptyLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.emptyList(), list);
    }

    @Test
    public void testCountriesByLanguage_AvailableCountries() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = new ArrayList<>();
        for (Locale locale : availableLocales) {
            if ("en".equals(locale.getLanguage())) {
                expected.add(locale);
            }
        }
        assertEquals(expected, list);
    }

}