package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

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
    public void testToLocaleWithEmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocaleWithInvalidFormat() {
        try {
            LocaleUtils.toLocale("aaa");
            assert false;
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testToLocaleWithTwoElements() {
        Locale expectedLocale = new Locale("en", "");
        Locale actualLocale = LocaleUtils.toLocale("en");
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocaleWithThreeElements() {
        Locale expectedLocale = new Locale("en", "GB", "xxx");
        Locale actualLocale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testLocaleLookupListWithNullLocale() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.localeLookupList(null);
        assertSame(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupListWithLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        assertArrayEquals(expectedList.toArray(), actualList.toArray());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> actualList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = LocaleUtils.availableLocaleSet();
        Set<Locale> actualSet = Collections.unmodifiableSet(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testIsAvailableLocaleWithAvailableLocale() {
        Locale locale = Locale.CHINESE;
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocaleWithInvalidLocale() {
        Locale locale = new Locale("aaa", "bbb");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedList = Arrays.asList(new Locale("fr", countryCode), new Locale("en", countryCode));
        List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLanguagesByCountryWithNullCountryCode() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.languagesByCountry(null);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguageWithEnglishLanguage() {
        String languageCode = "en";
        List<Locale> expectedList = Arrays.asList(new Locale(languageCode, "US"), new Locale(languageCode, "CA"), new Locale(languageCode, "GB"));
        List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguageWithNullLanguageCode() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.countriesByLanguage(null);
        assertEquals(expectedList, actualList);
    }
}