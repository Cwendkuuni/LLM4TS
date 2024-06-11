package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test valid locales with no variant
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr", "FR"), LocaleUtils.toLocale("fr_FR"));
        assertEquals(new Locale("es", "ES"), LocaleUtils.toLocale("es_ES"));

        // Test valid locales with variant
        assertEquals(new Locale("en", "US", "en_US"), LocaleUtils.toLocale("en_US_en_US"));
        assertEquals(new Locale("fr", "FR", "fr_FR_fr_FR"), LocaleUtils.toLocale("fr_FR_fr_FR"));
        assertEquals(new Locale("es", "ES", "es_ES_es_ES"), LocaleUtils.toLocale("es_ES_es_ES"));

        // Test invalid locale formats
        try {
            LocaleUtils.toLocale("en_US_en");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US--en");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US--en_US--en");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_en_US--");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_en_US____");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_en_US__en");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US__en");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US___");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US___en");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US___en___");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        assertNotNull(LocaleUtils.localeLookupList(null));
        assertEquals(Collections.EMPTY_LIST, LocaleUtils.localeLookupList(null));

        // Test single locale
        List localeList = LocaleUtils.localeLookupList(new Locale("en", "US"));
        assertArrayEquals(new Locale[] {new Locale("en", "US"), new Locale("en", ""), new Locale(""), null},
                localeList.toArray());

        // Test multiple locales
        List localeList2 = LocaleUtils.localeLookupList(new Locale("fr", "CA", "fr_CA"));
        assertArrayEquals(new Locale[] {
                new Locale("fr", "CA", "fr_CA"),
                new Locale("fr", "CA", ""),
                new Locale("fr", "", ""),
                new Locale("", "", ""),
                new Locale("", "", null)
        }, localeList2.toArray());

        // Test default locale
        List localeList3 = LocaleUtils.localeLookupList(new Locale("fr", "CA"), new Locale("en"));
        assertArrayEquals(new Locale[] {
                new Locale("fr", "CA", "fr_CA"),
                new Locale("fr", "CA", ""),
                new Locale("fr", "", ""),
                new Locale("en", "", ""),
                new Locale("", "", null)
        }, localeList3.toArray());
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocaleList = LocaleUtils.availableLocaleList();
        assertTrue(availableLocaleList.size() > 0);

        // Test empty list
        List emptyLocaleList = Collections.emptyList();
        assertFalse(availableLocaleList.equals(emptyLocaleList));

        // Test same list
        List sameLocaleList = availableLocaleList;
        assertTrue(availableLocaleList.equals(sameLocaleList));

        // Test different list
        List differentLocaleList = Arrays.asList(new Locale("es", "ES"), new Locale("fr", "FR"));
        assertFalse(availableLocaleList.equals(differentLocaleList));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set availableLocaleSet = LocaleUtils.availableLocaleSet();
        assertTrue(availableLocaleSet.size() > 0);

        // Test empty set
        Set emptyLocaleSet = Collections.emptySet();
        assertFalse(availableLocaleSet.equals(emptyLocaleSet));

        // Test same set
        Set sameLocaleSet = availableLocaleSet;
        assertTrue(availableLocaleSet.equals(sameLocaleSet));

        // Test different set
        Set differentLocaleSet = new HashSet();
        differentLocaleSet.add(new Locale("es", "ES"));
        differentLocaleSet.add(new Locale("fr", "FR"));
        assertFalse(availableLocaleSet.equals(differentLocaleSet));
    }

    @Test
    public void testIsAvailableLocale() {
        Set availableLocaleSet = LocaleUtils.availableLocaleSet();
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "FR")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("es", "ES")));

        // Test invalid locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "US_INVALID")));

        // Test empty locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));

        // Test null locale
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        List languagesByCountry = LocaleUtils.languagesByCountry("US");
        assertFalse(languagesByCountry.isEmpty());

        // Test empty country
        List emptyCountryList = Collections.emptyList();
        assertFalse(languagesByCountry.equals(emptyCountryList));

        // Test same country
        List sameCountryList = languagesByCountry;
        assertTrue(languagesByCountry.equals(sameCountryList));

        // Test different country
        List differentCountryList = Arrays.asList(new Locale("es", "ES"), new Locale("fr", "FR"));
        assertFalse(languagesByCountry.equals(differentCountryList));

        // Test null country
        List nullCountryList = Collections.emptyList();
        assertEquals(nullCountryList, LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        assertFalse(countriesByLanguage.isEmpty());

        // Test empty language
        List emptyLanguageList = Collections.emptyList();
        assertFalse(countriesByLanguage.equals(emptyLanguageList));

        // Test same language
        List sameLanguageList = countriesByLanguage;
        assertTrue(countriesByLanguage.equals(sameLanguageList));

        // Test different language
        List differentLanguageList = Arrays.asList(new Locale("es", "ES"), new Locale("fr", "FR"));
        assertFalse(countriesByLanguage.equals(differentLanguageList));

        // Test null language
        List nullLanguageList = Collections.emptyList();
        assertEquals(nullLanguageList, LocaleUtils.countriesByLanguage(null));
    }
}