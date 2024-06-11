package org.apache.commons.lang;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        try {
            LocaleUtils.toLocale("e");
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        try {
            LocaleUtils.toLocale("EN_GB");
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(new Locale("fr","CA","xxx"));
        expectedList.add(new Locale("fr","CA"));
        expectedList.add(new Locale("fr"));
        assertEquals(expectedList, LocaleUtils.localeLookupList(new Locale("fr","CA","xxx")));

        expectedList.clear();
        expectedList.add(new Locale("fr", "CA", "xxx"));
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        expectedList.add(new Locale("en"));
        assertEquals(expectedList, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(Locale.CANADA);
        expectedList.add(Locale.CANADA_FRENCH);
        expectedList.add(Locale.ENGLISH);
        expectedList.add(Locale.FRANCE);
        expectedList.add(Locale.FRENCH);
        expectedList.add(Locale.ROOT);
        assertEquals(expectedList, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = new HashSet<>();
        expectedSet.add(Locale.CANADA);
        expectedSet.add(Locale.CANADA_FRENCH);
        expectedSet.add(Locale.ENGLISH);
        expectedSet.add(Locale.FRANCE);
        expectedSet.add(Locale.FRENCH);
        expectedSet.add(Locale.ROOT);
        assertEquals(expectedSet, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("de", "DE")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(Locale.CANADA);
        expectedList.add(Locale.CANADA_FRENCH);
        assertEquals(expectedList, LocaleUtils.languagesByCountry("CA"));

        expectedList.clear();
        expectedList.add(Locale.FRANCE);
        expectedList.add(Locale.FRENCH);
        assertEquals(expectedList, LocaleUtils.languagesByCountry("FR"));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(Locale.CANADA);
        expectedList.add(Locale.CANADA_FRENCH);
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("fr"));

        expectedList.clear();
        expectedList.add(Locale.FRANCE);
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("fr"));
    }
}