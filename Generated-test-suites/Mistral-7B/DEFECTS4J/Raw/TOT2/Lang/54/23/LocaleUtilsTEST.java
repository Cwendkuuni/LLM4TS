package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Tests for the {@link LocaleUtils} class.
 */
public class LocaleUtilsTest {

    /**
     * Test method for {@link LocaleUtils#toLocale(String)}.
     */
    @Test
    public void testToLocale() {
        List<String> testCases = Arrays.asList(
                "en",
                "en_GB",
                "en_GB_xxx",
                "fr",
                "fr_CA",
                "fr_CA_xxx",
                "es",
                "es_ES",
                "es_ES_xxx",
                "invalid"
        );

        for (String testCase : testCases) {
            Locale expected = testCase.isEmpty() ? null : new Locale(testCase.substring(0, 2), testCase.substring(3));
            Locale actual = LocaleUtils.toLocale(testCase);
            assertEquals(expected.getDisplayName(), actual.getDisplayName());
            assertEquals(expected.getLanguage(), actual.getLanguage());
            assertEquals(expected.getCountry(), actual.getCountry());
            assertEquals(expected.getVariant(), actual.getVariant());
        }
    }

    /**
     * Test method for {@link LocaleUtils#localeLookupList(Locale)}.
     */
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getDisplayName(), actual.get(i).getDisplayName());
            assertEquals(expected.get(i).getLanguage(), actual.get(i).getLanguage());
            assertEquals(expected.get(i).getCountry(), actual.get(i).getCountry());
            assertEquals(expected.get(i).getVariant(), actual.get(i).getVariant());
        }

        locale = new Locale("fr");
        expected = Arrays.asList(locale, new Locale("fr"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getDisplayName(), actual.get(i).getDisplayName());
            assertEquals(expected.get(i).getLanguage(), actual.get(i).getLanguage());
            assertEquals(expected.get(i).getCountry(), actual.get(i).getCountry());
            assertEquals(expected.get(i).getVariant(), actual.get(i).getVariant());
        }

        locale = null;
        expected = Collections.emptyList();
        actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected.size(), actual.size());
    }

    /**
     * Test method for {@link LocaleUtils#availableLocaleList()}.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.GERMAN, Locale.FRENCH, Locale.ENGLISH);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getDisplayName(), actual.get(i).getDisplayName());
            assertEquals(expected.get(i).getLanguage(), actual.get(i).getLanguage());
            assertEquals(expected.get(i).getCountry(), actual.get(i).getCountry());
            assertEquals(expected.get(i).getVariant(), actual.get(i).getVariant());
        }
    }

    /**
     * Test method for {@link LocaleUtils#isAvailableLocale(Locale)}.
     */
    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("es", "MX");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("invalid");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test method for {@link LocaleUtils#languagesByCountry(String)}.
     */
    @Test
    public void testLanguagesByCountry() {
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.FRENCH);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getDisplayName(), actual.get(i).getDisplayName());
            assertEquals(expected.get(i).getLanguage(), actual.get(i).getLanguage());
            assertEquals(expected.get(i).getCountry(), actual.get(i).getCountry());
            assertEquals(expected.get(i).getVariant(), actual.get(i).getVariant());
        }

        actual = LocaleUtils.languagesByCountry(null);
        expected = Collections.emptyList();
        assertEquals(expected.size(), actual.size());
    }

    /**
     * Test method for {@link LocaleUtils#countriesByLanguage(String)}.
     */
    @Test
    public void testCountriesByLanguage() {
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(Locale.US, Locale.GB, Locale.CA);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getDisplayName(), actual.get(i).getDisplayName());
            assertEquals(expected.get(i).getLanguage(), actual.get(i).getLanguage());
            assertEquals(expected.get(i).getCountry(), actual.get(i).getCountry());
            assertEquals(expected.get(i).getVariant(), actual.get(i).getVariant());
        }

        actual = LocaleUtils.countriesByLanguage(null);
        expected = Collections.emptyList();
        assertEquals(expected.size(), actual.size());
    }
}