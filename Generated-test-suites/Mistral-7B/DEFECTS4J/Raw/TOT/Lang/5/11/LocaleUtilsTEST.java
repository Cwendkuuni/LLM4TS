package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Valid tests
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("es_ES", LocaleUtils.toLocale("es_ES").toString());
        assertEquals("de_DE", LocaleUtils.toLocale("de_DE").toString());

        // Invalid tests
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US__");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Valid tests
        List<Locale> list = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(Locale.US, list.get(0));
        assertEquals(Locale.ENGLISH, list.get(1));
        assertEquals(Locale.ENGLISH, list.get(2));
        assertEquals(Locale.getDefault(), list.get(3));

        // Invalid tests
        try {
            LocaleUtils.localeLookupList(null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList("");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList("en_US", null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Valid tests
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertTrue(list.size() > 0);
        assertTrue(list.contains(Locale.US));
        assertTrue(list.contains(Locale.ENGLISH));
        assertTrue(list.contains(Locale.getDefault()));

        // Invalid tests
        try {
            LocaleUtils.availableLocaleList().add(Locale.US);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Valid tests
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertTrue(set.size() > 0);
        assertTrue(set.contains(Locale.US));
        assertTrue(set.contains(Locale.ENGLISH));
        assertTrue(set.contains(Locale.getDefault()));

        // Invalid tests
        try {
            set.add(Locale.US);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testLanguagesByCountry() {
        // Valid tests
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertEquals(2, list.size());
        assertTrue(list.contains(Locale.US));
        assertTrue(list.contains(Locale.ENGLISH));

        list = LocaleUtils.languagesByCountry("FR");
        assertEquals(6, list.size());
        assertTrue(list.contains(new Locale("fr", "CA")));
        assertTrue(list.contains(new Locale("fr", "FR")));
        // Add more tests as needed

        // Invalid tests
        try {
            LocaleUtils.languagesByCountry(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCountriesByLanguage() {
        // Valid tests
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertEquals(27, list.size());
        assertTrue(list.contains(Locale.US));
        assertTrue(list.contains(Locale.UK));
        // Add more tests as needed

        // Invalid tests
        try {
            LocaleUtils.countriesByLanguage(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}