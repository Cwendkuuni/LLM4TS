package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertNull(LocaleUtils.toLocale(null));

        // Valid test cases
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE"));

        // Invalid test cases
        assertNull(LocaleUtils.toLocale("en_GB_xxx")); // wrong number of elements
        assertNull(LocaleUtils.toLocale("en_GB_xx")); // missing separator
        assertNull(LocaleUtils.toLocale("en_GBx")); // invalid separator
        assertNull(LocaleUtils.toLocale("en_GB_xx_xx")); // too many elements
        assertNull(LocaleUtils.toLocale("en_GB_xx_xx_xx")); // too many elements
        assertNull(LocaleUtils.toLocale("en_GB__")); // missing country
        assertNull(LocaleUtils.toLocale("en__")); // missing country
        assertNull(LocaleUtils.toLocale("en_GB")); // missing language
        assertNull(LocaleUtils.toLocale("en")); // missing country
        assertNull(LocaleUtils.toLocale("en_GB_en")); // duplicate language
        assertNull(LocaleUtils.toLocale("en_GB_fr")); // different language
        assertNull(LocaleUtils.toLocale("en_GB_-")); // missing language

        // Test for empty strings
        assertEquals(Locale.ROOT, LocaleUtils.toLocale(""));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));

        // Test for null elements
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en", null));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale(null, "US"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale(null, null));

        // Test for invalid country code
        assertNull(LocaleUtils.toLocale("en_XX", null));
        assertNull(LocaleUtils.toLocale(null, "XX"));

        // Test for invalid language code
        assertNull(LocaleUtils.toLocale("xx_FR", null));
        assertNull(LocaleUtils.toLocale(null, "xx"));

        // Test for language code with digits
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR_x"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        assertEquals(3, lookupList.size());
        assertEquals(locale, lookupList.get(0));
        assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        assertEquals(new Locale("fr"), lookupList.get(2));

        lookupList = LocaleUtils.localeLookupList(locale, Locale.ENGLISH);
        assertEquals(4, lookupList.size());
        assertEquals(locale, lookupList.get(0));
        assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        assertEquals(new Locale("fr"), lookupList.get(2));
        assertEquals(Locale.ENGLISH, lookupList.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertEquals(198, localeList.size());
        assertTrue(localeList.contains(Locale.ENGLISH));
        assertTrue(localeList.contains(new Locale("fr", "FR")));
        assertTrue(localeList.contains(new Locale("es", "ES")));
        assertTrue(localeList.contains(new Locale("de", "DE")));

        List<Locale> emptyList = Collections.emptyList();
        assertEquals(emptyList, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("CA");
        assertEquals(3, langs.size());
        assertTrue(langs.contains(new Locale("en", "CA")));
        assertTrue(langs.contains(new Locale("fr", "CA")));
        assertTrue(langs.contains(new Locale("es", "CA")));

        langs = LocaleUtils.languagesByCountry("US");
        assertEquals(1, langs.size());
        assertTrue(langs.contains(Locale.ENGLISH));

        langs = LocaleUtils.languagesByCountry("XX");
        assertSame(Collections.emptyList(), langs);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(6, countries.size());
        assertTrue(countries.contains(new Locale("US")));
        assertTrue(countries.contains(new Locale("GB")));
        assertTrue(countries.contains(new Locale("CA")));
        assertTrue(countries.contains(new Locale("AU")));
        assertTrue(countries.contains(new Locale("IE")));
        assertTrue(countries.contains(new Locale("ZA")));

        countries = LocaleUtils.countriesByLanguage("es");
        assertEquals(14, countries.size());
        assertTrue(countries.contains(new Locale("ES")));
        assertTrue(countries.contains(new Locale("MX")));
        assertTrue(countries.contains(new Locale("AR")));
        assertTrue(countries.contains(new Locale("CO")));
        assertTrue(countries.contains(new Locale("CR")));
        assertTrue(countries.contains(new Locale("DO")));
        assertTrue(countries.contains(new Locale("EC")));
        assertTrue(countries.contains(new Locale("GT")));
        assertTrue(countries.contains(new Locale("HN")));
        assertTrue(countries.contains(new Locale("NI")));
        assertTrue(countries.contains(new Locale("PY")));
        assertTrue(countries.contains(new Locale("SV")));

        countries = LocaleUtils.countriesByLanguage("xx");
        assertSame(Collections.emptyList(), countries);
    }
}