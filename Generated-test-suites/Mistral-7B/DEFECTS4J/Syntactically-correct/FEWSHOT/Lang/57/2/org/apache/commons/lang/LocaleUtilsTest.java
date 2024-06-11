package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR_VARIANT"));
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it"));
        assertEquals(Locale.ITALY, LocaleUtils.toLocale("it_IT"));
        assertEquals(Locale.ITALY, LocaleUtils.toLocale("it_IT_VARIANT"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de"));
        assertEquals(Locale.GERMANY, LocaleUtils.toLocale("de_DE"));
        assertEquals(Locale.GERMANY, LocaleUtils.toLocale("de_DE_VARIANT"));

        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("abcd"));
        assertNull(LocaleUtils.toLocale("en_US_abcd"));
        assertNull(LocaleUtils.toLocale("en_US_-"));

        // Test invalid format
        try {
            LocaleUtils.toLocale("en_us");
            assert False();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_en");
            assert False();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US___");
            assert False();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US__en");
            assert False();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_en__");
            assert False();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US__en___");
            assert False();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();

        List<Locale> list = LocaleUtils.localeLookupList(Locale.FRENCH);
        assertArrayEquals(new Locale[] { Locale.FRENCH, Locale.FRENCH, Locale.FRENCH }, list.toArray());

        list = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        assertArrayEquals(new Locale[] { Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.ENGLISH }, list.toArray());

        list = LocaleUtils.localeLookupList(Locale.ITALIAN);
        assertArrayEquals(new Locale[] { Locale.ITALIAN, Locale.ITALIAN, Locale.ITALY }, list.toArray());

        list = LocaleUtils.localeLookupList(Locale.ITALIAN, Locale.GERMAN);
        assertArrayEquals(new Locale[] { Locale.ITALIAN, Locale.ITALIAN, Locale.ITALIAN, Locale.ITALY, Locale.GERMAN }, list.toArray());

        list = LocaleUtils.localeLookupList(null);
        assertArrayEquals(new Locale[0], list.toArray());

        list = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertArrayEquals(new Locale[] { Locale.ENGLISH }, list.toArray());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Map<String, List<Locale>> expected = new HashMap<>();

        List<Locale> enLocales = new ArrayList<>();
        enLocales.add(Locale.ENGLISH);
        expected.put("en", enLocales);

        List<Locale> frLocales = new ArrayList<>();
        frLocales.add(Locale.FRENCH);
        expected.put("fr", frLocales);

        List<Locale> deLocales = new ArrayList<>();
        deLocales.add(Locale.GERMAN);
        expected.put("de", deLocales);

        for (Locale locale : availableLocales) {
            List<Locale> list = Arrays.asList(Locale.ROOT, locale);
            String key = locale.getLanguage();
            List<Locale> list2 = expected.get(key);
            assertTrue(list2.contains(locale));
            list2.add(locale);
        }

        for (Map.Entry<String, List<Locale>> entry : expected.entrySet()) {
            String key = entry.getKey();
            List<Locale> list = entry.getValue();
            assertEquals(list.size(), 3 +LocaleUtils.getAvailableVariants(key).size());
        }
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> englishLocales = LocaleUtils.languagesByCountry("US");
        assertArrayEquals(new Locale[] { Locale.ENGLISH, Locale.ENGLISH, Locale.ENGLISH }, englishLocales.toArray());

        List<Locale> frenchLocales = LocaleUtils.languagesByCountry("FR");
        assertArrayEquals(new Locale[] { Locale.FRENCH, Locale.FRENCH, Locale.FRENCH }, frenchLocales.toArray());

        List<Locale> germanLocales = LocaleUtils.languagesByCountry("DE");
        assertArrayEquals(new Locale[] { Locale.GERMAN, Locale.GERMAN, Locale.GERMANY }, germanLocales.toArray());

        List<Locale> empty = LocaleUtils.languagesByCountry(null);
        assertArrayEquals(new Locale[0], empty.toArray());

        List<Locale> empty2 = LocaleUtils.languagesByCountry("");
        assertArrayEquals(new Locale[0], empty2.toArray());

        List<Locale> itLocales = LocaleUtils.languagesByCountry("IT");
        assertArrayEquals(new Locale[] { Locale.ITALIAN, Locale.ITALIAN, Locale.ITALY }, itLocales.toArray());

        List<Locale> itLocales2 = LocaleUtils.languagesByCountry("IT_IT");
        assertArrayEquals(new Locale[] { Locale.ITALIAN, Locale.ITALIAN, Locale.ITALIAN, Locale.ITALY }, itLocales2.toArray());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> englishLocales = LocaleUtils.countriesByLanguage("en");
        assertArrayEquals(new Locale[] { Locale.ENGLISH, Locale.ENGLISH, Locale.ENGLISH }, englishLocales.toArray());

        List<Locale> frenchLocales = LocaleUtils.countriesByLanguage("fr");
        assertArrayEquals(new Locale[] { Locale.FRENCH, Locale.FRENCH, Locale.FRENCH }, frenchLocales.toArray());

        List<Locale> germanLocales = LocaleUtils.countriesByLanguage("de");
        assertArrayEquals(new Locale[] { Locale.GERMAN, Locale.GERMANY, Locale.GERMANY }, germanLocales.toArray());

        List<Locale> empty = LocaleUtils.countriesByLanguage(null);
        assertArrayEquals(new Locale[0], empty.toArray());

        List<Locale> empty2 = LocaleUtils.countriesByLanguage("");
        assertArrayEquals(new Locale[0], empty2.toArray());

        List<Locale> frLocales = LocaleUtils.countriesByLanguage("fr_FR");
        assertArrayEquals(new Locale[] { Locale.FRENCH, Locale.FRENCH }, frLocales.toArray());

        List<Locale> deLocales = LocaleUtils.countriesByLanguage("de_DE");
        assertArrayEquals(new Locale[] { Locale.GERMANY, Locale.GERMANY }, deLocales.toArray());

        List<Locale> itLocales = LocaleUtils.countriesByLanguage("it_IT");
        assertArrayEquals(new Locale[] { Locale.ITALY, Locale.ITALY }, itLocales.toArray());
    }

    @Test
    public void testGetAvailableVariants() {
        assertEquals(Collections.emptyList(), LocaleUtils.getAvailableVariants(null));
        assertEquals(Collections.emptyList(), LocaleUtils.getAvailableVariants(""));
        assertEquals(Collections.singletonList(Locale.ENGLISH), LocaleUtils.getAvailableVariants("en"));
        assertEquals(Arrays.asList(Locale.ENGLISH, Locale.SCOTTS_SCOTLAND), LocaleUtils.getAvailableVariants("en_GB"));
        assertEquals(Arrays.asList(Locale.ENGLISH, Locale.ENGLISH_US, Locale.ENGLISH_CA), LocaleUtils.getAvailableVariants("en_US"));
        assertEquals(Collections.singletonList(Locale.ITALIAN), LocaleUtils.getAvailableVariants("it"));
        assertEquals(Arrays.asList(Locale.ITALIAN, Locale.ITALIAN_IT, Locale.ITALIAN_CH), LocaleUtils.getAvailableVariants("it_IT"));
        assertEquals(Arrays.asList(Locale.ITALIAN, Locale.ITALIAN_IT, Locale.ITALIAN_CH, Locale.ITALIAN_SW), LocaleUtils.getAvailableVariants("it_CH"));
        assertEquals(Collections.singletonList(Locale.GERMAN), LocaleUtils.getAvailableVariants("de"));
        assertEquals(Arrays.asList(Locale.GERMAN, Locale.GERMAN_AT, Locale.GERMAN_CH), LocaleUtils.getAvailableVariants("de_AT"));
        assertEquals(Arrays.asList(Locale.GERMANY, Locale.GERMANY_DE, Locale.GERMANY_AT), LocaleUtils.getAvailableVariants("de_DE"));
    }
}