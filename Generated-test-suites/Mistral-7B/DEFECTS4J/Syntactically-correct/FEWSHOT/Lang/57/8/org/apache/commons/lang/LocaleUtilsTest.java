package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_xxx"));

        // Test invalid locale strings
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(""));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("xx"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_xx"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_xx"));

        // Test empty country code
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_"));

        // Test empty language code
        assertEquals(Locale.US, LocaleUtils.toLocale("_GB"));
        assertEquals(Locale.US, LocaleUtils.toLocale("_US_"));

        // Test missing separator
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("enx"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GBx"));

        // Test too short locale string
        assertNull(LocaleUtils.toLocale("en"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_GB"));

        // Test toLocale with null input
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), Locale.ENGLISH);

        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertArrayEquals(expectedList.toArray(new Locale[0]), list.toArray(new Locale[0]));

        List<Locale> listDefault = LocaleUtils.localeLookupList(locale, Locale.ENGLISH);
        assertArrayEquals(expectedList.toArray(new Locale[0]), listDefault.toArray(new Locale[0]));

        Locale defaultLocale = Locale.ENGLISH;
        List<Locale> listEmpty = LocaleUtils.localeLookupList(defaultLocale);
        assertEquals(0, listEmpty.size());

        Locale localeEmpty = null;
        List<Locale> listNull = LocaleUtils.localeLookupList(localeEmpty);
        assertEquals(0, listNull.size());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        Map<String, Integer> countryCountMap = new HashMap<>();
        Map<String, Integer> languageCountMap = new HashMap<>();

        for (Locale locale : list) {
            String country = locale.getCountry();
            if (country != null && country.length() > 0) {
                int count = countryCountMap.getOrDefault(country, 0);
                countryCountMap.put(country, count + 1);
            }
            String language = locale.getLanguage();
            if (language != null && language.length() > 0) {
                int count = languageCountMap.getOrDefault(language, 0);
                languageCountMap.put(language, count + 1);
            }
        }

        // Verify each country appears at least once
        for (Map.Entry<String, Integer> entry : countryCountMap.entrySet()) {
            String country = entry.getKey();
            int count = entry.getValue();
            assertTrue(LocaleUtils.isAvailableLocale(new Locale(country, "")));
            assertTrue(LocaleUtils.isAvailableLocale(new Locale("", country)));
            assertTrue(LocaleUtils.isAvailableLocale(new Locale("", country, "")));
            assertTrue(count > 0);
        }

        // Verify each language appears at least once
        for (Map.Entry<String, Integer> entry : languageCountMap.entrySet()) {
            String language = entry.getKey();
            int count = entry.getValue();
            assertTrue(LocaleUtils.isAvailableLocale(new Locale(language)));
            assertTrue(count > 0);
        }

        // Test empty list
        List<Locale> emptyList = LocaleUtils.availableLocaleList();
        assertEquals(0, emptyList.size());
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("CA");
        assertEquals(3, langs.size());

        assertTrue(langs.contains(Locale.FRENCH));
        assertTrue(langs.contains(Locale.ENGLISH));
        assertTrue(langs.contains(Locale.SPANISH));

        List<Locale> langsEmpty = LocaleUtils.languagesByCountry(null);
        assertEquals(0, langsEmpty.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(2, countries.size());

        assertTrue(countries.contains(Locale.ENGLISH));
        assertTrue(countries.contains(Locale.US));

        List<Locale> countriesEmpty = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, countriesEmpty.size());
    }
}