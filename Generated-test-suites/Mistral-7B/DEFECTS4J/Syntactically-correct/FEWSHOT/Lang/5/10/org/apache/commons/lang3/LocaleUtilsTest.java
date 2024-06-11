package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // valid test cases
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_foo"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_foo"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR_foo"));

        // invalid test cases
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("XX"));
        assertNull(LocaleUtils.toLocale("XX_YY"));
        assertNull(LocaleUtils.toLocale("XX_YY_ZZ"));
        assertNull(LocaleUtils.toLocale("XX_YY_ZZ_"));
        assertNull(LocaleUtils.toLocale("XX_YY_ZZ_XX"));
        assertNull(LocaleUtils.toLocale("XX_YY_ZZ_XX_"));
        assertNull(LocaleUtils.toLocale("XX_YY_ZZ_XX_XX"));
        assertNull(LocaleUtils.toLocale("XX_YY_ZZ_XX_XX_"));

        // test uppercase language and country codes
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("Fr_CA"));

        // test empty separator
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("Fr_"));

        // test empty country
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_XX"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_XX_"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_XX_XX"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_XX_XX_"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_XX_XX_XX"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_XX_XX_XX_"));

        // test empty language
        assertNull(LocaleUtils.toLocale("_XX"));
        assertNull(LocaleUtils.toLocale("_XX_"));
        assertNull(LocaleUtils.toLocale("_XX_XX"));
        assertNull(LocaleUtils.toLocale("_XX_XX_"));
        assertNull(LocaleUtils.toLocale("_XX_XX_XX"));
        assertNull(LocaleUtils.toLocale("_XX_XX_XX_"));

        // test invalid separator
        assertNull(LocaleUtils.toLocale("en_XX_"));
        assertNull(LocaleUtils.toLocale("en_XX_XX"));
        assertNull(LocaleUtils.toLocale("en_XX_XX_"));
        assertNull(LocaleUtils.toLocale("en_XX_XX_XX"));
        assertNull(LocaleUtils.toLocale("en_XX_XX_XX_"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        List<Locale> list = LocaleUtils.localeLookupList(locale);

        assertEquals(5, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(Locale.ENGLISH, list.get(1));
        assertEquals(Locale.ENGLISH, list.get(2));
        assertEquals(Locale.US, list.get(3));
        assertEquals(Locale.ENGLISH, list.get(4));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en", "GB");

        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);

        assertEquals(6, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(Locale.ENGLISH, list.get(1));
        assertEquals(Locale.ENGLISH, list.get(2));
        assertEquals(Locale.US, list.get(3));
        assertEquals(Locale.ENGLISH, list.get(4));
        assertEquals(defaultLocale, list.get(5));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();

        assertTrue(availableLocaleList.size() > 0);

        List<String> languages = Arrays.asList("en", "fr", "de", "es", "it");
        for (String language : languages) {
            List<String> countryList = Arrays.asList("", "US", "CA", "DE", "ES", "IT");
            for (String country : countryList) {
                List<Locale> locales = LocaleUtils.languagesByCountry(country);
                for (Locale locale : locales) {
                    if (locale.getLanguage().equals(language)) {
                        assertTrue(availableLocaleList.contains(locale));
                    }
                }
            }
        }
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> englishLocales = LocaleUtils.languagesByCountry("US");
        assertEquals(5, englishLocales.size());
        List<Locale> frenchLocales = LocaleUtils.languagesByCountry("CA");
        assertEquals(3, frenchLocales.size());

        List<Locale> bothLocales = LocaleUtils.localeLookupList(new Locale("fr", "CA"));
        assertEquals(Locale.FRENCH, bothLocales.get(0));
        assertEquals(Locale.CANADA, bothLocales.get(1));
        assertEquals(Locale.FRENCH, bothLocales.get(2));

        List<Locale> englishCanada = LocaleUtils.languagesByCountry("CA");
        assertEquals(2, englishCanada.size());
        assertEquals(Locale.ENGLISH, englishCanada.get(0));
        assertEquals(Locale.CANADA, englishCanada.get(1));

        List<Locale> englishUSA = LocaleUtils.languagesByCountry("US");
        assertEquals(2, englishUSA.size());
        assertEquals(Locale.ENGLISH, englishUSA.get(0));
        assertEquals(Locale.US, englishUSA.get(1));

        List<Locale> englishWorld = LocaleUtils.languagesByCountry("");
        assertEquals(1, englishWorld.size());
        assertEquals(Locale.ENGLISH, englishWorld.get(0));

        List<Locale> emptyList = LocaleUtils.languagesByCountry(null);
        assertEquals(0, emptyList.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> englishLocales = LocaleUtils.countriesByLanguage("en");
        assertEquals(5, englishLocales.size());
        List<Locale> frenchLocales = LocaleUtils.countriesByLanguage("fr");
        assertEquals(3, frenchLocales.size());

        List<Locale> englishUS = LocaleUtils.countriesByLanguage("en_US");
        assertEquals(1, englishUS.size());
        assertEquals(Locale.US, englishUS.get(0));

        List<Locale> englishCA = LocaleUtils.countriesByLanguage("en_CA");
        assertEquals(1, englishCA.size());
        assertEquals(Locale.CANADA, englishCA.get(0));

        List<Locale> emptyList = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, emptyList.size());
    }
}