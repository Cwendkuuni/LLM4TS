package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test null input
        assertNull(LocaleUtils.toLocale(null));

        // Test simple locale
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals("en", locale1.getLanguage());
        assertEquals("", locale1.getCountry());
        assertNull(locale1.getVariant());

        // Test locale with country
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale2.getLanguage());
        assertEquals("GB", locale2.getCountry());
        assertNull(locale2.getVariant());

        // Test locale with country and variant
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale3.getLanguage());
        assertEquals("GB", locale3.getCountry());
        assertEquals("xxx", locale3.getVariant());
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty input
        List<Locale> list1 = LocaleUtils.localeLookupList(null);
        assertTrue(list1.isEmpty());

        // Test null country input
        List<Locale> list2 = LocaleUtils.localeLookupList(null, Locale.getDefault());
        assertTrue(list2.isEmpty());

        // Test simple locale
        Locale locale1 = Locale.getDefault();
        List<Locale> list3 = LocaleUtils.localeLookupList(locale1);
        List<Locale> expectedList1 = Arrays.asList(locale1);
        assertEquals(expectedList1, list3);

        // Test locale with country
        Locale locale2 = new Locale("fr", "CA");
        List<Locale> list4 = LocaleUtils.localeLookupList(locale2);
        List<Locale> expectedList2 = Arrays.asList(locale2, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expectedList2, list4);

        // Test locale with country and variant
        Locale locale3 = new Locale("fr", "CA", "xx");
        List<Locale> list5 = LocaleUtils.localeLookupList(locale3, Locale.getDefault());
        List<Locale> expectedList3 = Arrays.asList(locale3, new Locale("fr", "CA"), new Locale("fr"), Locale.getDefault());
        assertEquals(expectedList3, list5);

        // Test locale with default locale
        Locale locale4 = Locale.getDefault();
        List<Locale> list6 = LocaleUtils.localeLookupList(locale4);
        List<Locale> expectedList4 = Arrays.asList(locale4);
        assertEquals(expectedList4, list6);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> list1 = LocaleUtils.availableLocaleList();
        assertTrue(list1.isEmpty());

        // Test non-empty list
        LocaleUtils localeUtils = new LocaleUtils();
        List<Locale> list2 = LocaleUtils.availableLocaleList();
        List<Locale> expectedList1 = new ArrayList<Locale>(5);
        expectedList1.add(new Locale("da", "DK"));
        expectedList1.add(new Locale("de", "AT"));
        expectedList1.add(new Locale("en", "GB"));
        expectedList1.add(new Locale("es", "ES"));
        expectedList1.add(new Locale("fr", "FR"));
        assertEquals(expectedList1, list2);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test empty set
        Set<Locale> set1 = LocaleUtils.availableLocaleSet();
        assertTrue(set1.isEmpty());

        // Test non-empty set
        LocaleUtils localeUtils = new LocaleUtils();
        Set<Locale> set2 = LocaleUtils.availableLocaleSet();
        List<Locale> expectedList = new ArrayList<Locale>(5);
        expectedList.add(new Locale("da", "DK"));
        expectedList.add(new Locale("de", "AT"));
        expectedList.add(new Locale("en", "GB"));
        expectedList.add(new Locale("es", "ES"));
        expectedList.add(new Locale("fr", "FR"));
        assertTrue(set2.containsAll(expectedList));
        assertEquals(expectedList.size(), set2.size());
    }

    @Test
    public void testIsAvailableLocale() {
        // Test empty list
        LocaleUtils localeUtils = new LocaleUtils();
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test invalid locale
        Locale locale1 = new Locale("en", "");
        assertFalse(LocaleUtils.isAvailableLocale(locale1));

        // Test invalid locale with country
        Locale locale2 = new Locale("en", "GB", "");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));

        // Test valid locale
        List<Locale> locales = LocaleUtils.availableLocaleList();
        Locale locale3 = locales.get(0);
        assertTrue(LocaleUtils.isAvailableLocale(locale3));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty country
        String country1 = "";
        List<Locale> list1 = LocaleUtils.languagesByCountry(country1);
        assertTrue(list1.isEmpty());

        // Test invalid country
        String country2 = "XX";
        List<Locale> list2 = LocaleUtils.languagesByCountry(country2);
        assertTrue(list2.isEmpty());

        // Test valid country
        String country3 = "CA";
        List<Locale> list3 = LocaleUtils.languagesByCountry(country3);
        List<Locale> expectedList = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        assertEquals(expectedList, list3);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty language
        String language1 = "";
        List<Locale> list1 = LocaleUtils.countriesByLanguage(language1);
        assertTrue(list1.isEmpty());

        // Test invalid language
        String language2 = "XX";
        List<Locale> list2 = LocaleUtils.countriesByLanguage(language2);
        assertTrue(list2.isEmpty());

        // Test valid language
        String language3 = "fr";
        List<Locale> list3 = LocaleUtils.countriesByLanguage(language3);
        List<Locale> expectedList = Arrays.asList(new Locale("CA"), new Locale("BE"), new Locale("CH"));
        assertEquals(expectedList, list3);
    }
}