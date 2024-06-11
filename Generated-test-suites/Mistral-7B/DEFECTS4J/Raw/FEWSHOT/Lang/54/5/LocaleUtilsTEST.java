package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_xxx"));
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("zz"));
        assertNull(LocaleUtils.toLocale("aa_bb"));
        assertNull(LocaleUtils.toLocale("aa_bb_ccc"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_xxx"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_xxx_yyy"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_xxx_yyy_zzz"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_en"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_en_US"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_en_US_variants"));

        // Test variant handling
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_variant"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_variant_variant"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US-variant"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US-variant_variant"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US-variant-variant"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_variant-variant"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_variant-variant-variant"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_variant-variant-variant-variant"));
        assertNull(LocaleUtils.toLocale("en_US_variant-variant-variant-variant-variant"));

        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR_variant"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR_variant_variant"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR-variant"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR-variant_variant"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR-variant-variant"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR-variant-variant-variant"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR-variant-variant-variant-variant"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR-variant-variant-variant-variant-variant"));
        assertNull(LocaleUtils.toLocale("fr_FR-variant-variant-variant-variant-variant-variant"));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(4, localeList.size());
        assertEquals(Locale.ENGLISH, localeList.get(0));
        assertEquals(Locale.ENGLISH, localeList.get(1));
        assertEquals(Locale.ENGLISH, localeList.get(2));
        assertNull(localeList.get(3));

        localeList = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        assertEquals(5, localeList.size());
        assertEquals(Locale.FRENCH, localeList.get(0));
        assertEquals(Locale.FRENCH, localeList.get(1));
        assertEquals(Locale.FRENCH, localeList.get(2));
        assertEquals(Locale.ENGLISH, localeList.get(4));

        localeList = LocaleUtils.localeLookupList(Locale.FRENCH, null);
        assertEquals(3, localeList.size());
        assertEquals(Locale.FRENCH, localeList.get(0));
        assertEquals(Locale.FRENCH, localeList.get(1));
        assertNull(localeList.get(2));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertEquals(226, locales.size());

        List<String> countries = new ArrayList<>();
        for (Locale locale : locales) {
            if (locale.getCountry() != null) {
                countries.add(locale.getCountry());
            }
        }
        List<String> uniqueCountries = new ArrayList<>(new HashSet<>(countries));
        assertEquals(174, uniqueCountries.size());
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertEquals(3, languages.size());
        assertEquals(Locale.ENGLISH, languages.get(0));
        assertEquals(Locale.ENGLISH, languages.get(1));
        assertEquals(Locale.ENGLISH, languages.get(2));

        languages = LocaleUtils.languagesByCountry("CA");
        assertEquals(16, languages.size());
        // Only showing some of the results for brevity
        assertEquals(Locale.FRENCH, languages.get(0));
        assertEquals(Locale.ENGLISH, languages.get(1));
        assertEquals(Locale.SPANISH, languages.get(2));
        assertEquals(Locale.ITALIAN, languages.get(3));
        assertEquals(Locale.GERMAN, languages.get(4));

        languages = LocaleUtils.languagesByCountry(null);
        assertEquals(0, languages.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(25, countries.size());
        assertEquals(Locale.ENGLISH, countries.get(0));
        assertEquals(Locale.ENGLISH, countries.get(1));
        // Only showing some of the results for brevity
        assertEquals(Locale.ENGLISH, countries.get(23));
        assertEquals(Locale.ENGLISH, countries.get(24));

        countries = LocaleUtils.countriesByLanguage("fr");
        assertEquals(11, countries.size());
        assertEquals(Locale.FRENCH, countries.get(0));
        assertEquals(Locale.FRENCH, countries.get(1));
        // Only showing some of the results for brevity
        assertEquals(Locale.FRENCH, countries.get(8));
        assertEquals(Locale.FRENCH, countries.get(9));

        countries = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, countries.size());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SPANISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH_CANADA));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN_AUSTRIA));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALIAN_ITALY));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SPANISH_ESPANA));

        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH_US_ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.FRENCH_FRANCE_FRENCH));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.GERMAN_GERMAN_GERMAN));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ITALIAN_ITALY_ITALIAN));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.SPANISH_ESPANA_SPANISH));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH_US_VARIANT));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.FRENCH_FRANCE_VARIANT));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.GERMAN_GERMAN_VARIANT));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ITALIAN_ITALY_VARIANT));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.SPANISH_ESPANA_VARIANT));
    }
}