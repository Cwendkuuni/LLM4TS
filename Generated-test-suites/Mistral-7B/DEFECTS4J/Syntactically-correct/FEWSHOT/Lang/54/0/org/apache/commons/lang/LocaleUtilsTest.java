package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertLinesMatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // test empty string
        assertNull(LocaleUtils.toLocale(""));

        // test invalid strings
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_GB_"));
        assertNull(LocaleUtils.toLocale("en_GB_xx"));
        assertNull(LocaleUtils.toLocale("en_GB_xx_"));

        // test valid strings
        assertEquals("en", LocaleUtils.toLocale("en").getLanguage());
        assertEquals("en_GB", LocaleUtils.toLocale("en_GB").toString());
        assertEquals("en_GB_London", LocaleUtils.toLocale("en_GB_London").toString());
        assertEquals("en_GB_London_XX", LocaleUtils.toLocale("en_GB_London_XX").toString());

        // test valid strings with variant
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("en_US_POSIX", LocaleUtils.toLocale("en_US_POSIX").toString());
        assertEquals("en_US_POSIX_x-default", LocaleUtils.toLocale("en_US_POSIX_x-default").toString());

        // test null input
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        Locale localeFrCA = new Locale("fr", "CA");
        Locale localeFr = new Locale("fr");
        Locale localeEn = new Locale("en");
        Locale localeDefault = new Locale("en", "US");

        List<Locale> list = LocaleUtils.localeLookupList(localeFrCA, localeEn);

        // order: localeFrCA, localeFr, localeEn, localeDefault
        assertEquals(4, list.size());
        assertEquals(localeFrCA, list.get(0));
        assertEquals(localeFr, list.get(1));
        assertEquals(localeEn, list.get(2));
        assertEquals(localeDefault, list.get(3));

        // test empty list
        List<Locale> emptyList = LocaleUtils.localeLookupList(null);
        assertEquals(0, emptyList.size());

        // test null input
        assertNull(LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(171, availableLocales.size());

        // test empty list
        List<Locale> emptyList = LocaleUtils.availableLocaleList();
        assertEquals(0, emptyList.size());

        // test null input
        assertNull(LocaleUtils.availableLocaleList(null));
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();

        // true cases
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CHINESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));

        // false cases
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "xx")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "XX")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_GB", "XX")));

        // null input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // test available locales
        for (Locale locale : availableLocales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        // test invalid locales
        for (String invalidLocale : new String[]{"en_GB_XX", "en_GB_xx", "en_GB_xx_"}) {
            assertFalse(LocaleUtils.isAvailableLocale(LocaleUtils.toLocale(invalidLocale)));
        }
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountryFr = LocaleUtils.languagesByCountry("FR");

        // order: fr_FR, fr_CA, fr_BE, fr_CH, fr_LU
        assertEquals(5, languagesByCountryFr.size());

        // test empty list
        List<Locale> emptyList = LocaleUtils.languagesByCountry(null);
        assertEquals(0, emptyList.size());

        // test null input
        assertNull(LocaleUtils.languagesByCountry(null));

        // test available languages by country
        for (Locale locale : languagesByCountryFr) {
            assertTrue(locale.getCountry().equals("FR"));
        }
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguageEn = LocaleUtils.countriesByLanguage("EN");

        // order: AD, AU, BW, CA, GB, IE, IN, JM, NZ, PH, SG, TR, US, ZA, ZM
        assertEquals(14, countriesByLanguageEn.size());

        // test empty list
        List<Locale> emptyList = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, emptyList.size());

        // test null input
        assertNull(LocaleUtils.countriesByLanguage(null));

        // test available countries by language
        for (Locale locale : countriesByLanguageEn) {
            assertTrue(locale.getLanguage().equals("EN"));
        }
    }

}