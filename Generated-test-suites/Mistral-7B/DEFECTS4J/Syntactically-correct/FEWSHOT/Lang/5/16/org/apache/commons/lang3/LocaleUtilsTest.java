package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertLinesMatch;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // test empty string
        assertNull(LocaleUtils.toLocale(""));

        // test valid locales
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, Locale.FRENCH);
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR_variant"));

        // test invalid locales
        try {
            LocaleUtils.toLocale("en_US_");
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_en");
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US__variant");
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_en_variant");
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US-variant");
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_variant-");
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_variant_-");
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_-variant");
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_-variant-");
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_-variant");
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_-variant-");
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_en_variant--");
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_en_variant--variant");
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_en_variant--variant-");
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_en_variant--variant--");
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // test empty locale
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(null);
        assertTrue(localeLookupList.isEmpty());

        // test valid locale
        Locale locale = Locale.ENGLISH;
        List<Locale> localeLookupList1 = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedLocaleList1 = Arrays.asList(locale, Locale.ENGLISH, Locale.ENGLISH);
        assertArrayEquals(expectedLocaleList1.toArray(), localeLookupList.toArray());

        // test locale with variant
        Locale localeVariant = Locale.ENGLISH;
        localeVariant = new Locale(localeVariant.getLanguage(), localeVariant.getCountry(), "variant");
        List<Locale> localeLookupList2 = LocaleUtils.localeLookupList(localeVariant);
        List<Locale> expectedLocaleList2 = Arrays.asList(locale, locale, locale, localeVariant);
        assertArrayEquals(expectedLocaleList2.toArray(), localeLookupList2.toArray());

        // test locale with empty country
        Locale localeEmptyCountry = new Locale("en", "");
        List<Locale> localeLookupList3 = LocaleUtils.localeLookupList(localeEmptyCountry);
        List<Locale> expectedLocaleList3 = Arrays.asList(locale, locale, locale);
        assertArrayEquals(expectedLocaleList3.toArray(), localeLookupList3.toArray());

        // test locale with empty language
        Locale localeEmptyLanguage = new Locale("", "US");
        List<Locale> localeLookupList4 = LocaleUtils.localeLookupList(localeEmptyLanguage);
        List<Locale> expectedLocaleList4 = Arrays.asList(Locale.US);
        assertArrayEquals(expectedLocaleList4.toArray(), localeLookupList4.toArray());

        // test locale with empty language and country
        Locale localeEmptyBoth = new Locale("");
        List<Locale> localeLookupList5 = LocaleUtils.localeLookupList(localeEmptyBoth);
        List<Locale> expectedLocaleList5 = Collections.emptyList();
        assertArrayEquals(expectedLocaleList5.toArray(), localeLookupList5.toArray());

        // test locale with null defaultLocale
        Locale defaultLocaleNull = null;
        List<Locale> localeLookupList6 = LocaleUtils.localeLookupList(locale, defaultLocaleNull);
        assertTrue(localeLookupList6.isEmpty());

        // test locale with valid defaultLocale
        List<Locale> localeLookupList7 = LocaleUtils.localeLookupList(locale, Locale.ENGLISH);
        List<Locale> expectedLocaleList7 = Arrays.asList(locale, locale, locale, Locale.ENGLISH);
        assertArrayEquals(expectedLocaleList7.toArray(), localeLookupList7.toArray());

        // test locale with different defaultLocale
        Locale defaultLocaleDifferent = new Locale("fr");
        List<Locale> localeLookupList8 = LocaleUtils.localeLookupList(locale, defaultLocaleDifferent);
        List<Locale> expectedLocaleList8 = Arrays.asList(locale, locale, locale, defaultLocaleDifferent);
        assertArrayEquals(expectedLocaleList8.toArray(), localeLookupList8.toArray());
    }

    @Test
    public void testAvailableLocaleList() {
        // test empty list
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertTrue(availableLocaleList.isEmpty());

        // test available locales
        List<Locale> expectedAvailableLocaleList1 = Arrays.asList(Locale.CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH);
        List<Locale> availableLocaleList1 = LocaleUtils.availableLocaleList();
        assertArrayEquals(expectedAvailableLocaleList1.toArray(), availableLocaleList1.toArray());

        // test available locales with variant
        List<Locale> expectedAvailableLocaleList2 = Arrays.asList(Locale.CHINESE, Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH);
        List<Locale> availableLocaleList2 = LocaleUtils.availableLocaleList();
        assertArrayEquals(expectedAvailableLocaleList2.toArray(), availableLocaleList2.toArray());

        // test available locales with multiple variants
        List<Locale> expectedAvailableLocaleList3 = Arrays.asList(Locale.CHINESE, Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.ENGLISH_JP, Locale.ENGLISH_US);
        List<Locale> availableLocaleList3 = LocaleUtils.availableLocaleList();
        assertArrayEquals(expectedAvailableLocaleList3.toArray(), availableLocaleList3.toArray());

        // test available locales with null list
        List<Locale> availableLocaleListNull = null;
        assertNull(LocaleUtils.availableLocaleList(availableLocaleListNull));
    }

    @Test
    public void testAvailableLocaleSet() {
        // test empty set
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        assertTrue(availableLocaleSet.isEmpty());

        // test available locales
        Set<Locale> expectedAvailableLocaleSet1 = new HashSet<>(Arrays.asList(Locale.CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH));
        Set<Locale> availableLocaleSet1 = LocaleUtils.availableLocaleSet();
        assertEquals(expectedAvailableLocaleSet1, availableLocaleSet1);

        // test available locales with variant
        Set<Locale> expectedAvailableLocaleSet2 = new HashSet<>(Arrays.asList(Locale.CHINESE, Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH));
        Set<Locale> availableLocaleSet2 = LocaleUtils.availableLocaleSet();
        assertEquals(expectedAvailableLocaleSet2, availableLocaleSet2);

        // test available locales with multiple variants
        Set<Locale> expectedAvailableLocaleSet3 = new HashSet<>(Arrays.asList(Locale.CHINESE, Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.ENGLISH_JP, Locale.ENGLISH_US));
        Set<Locale> availableLocaleSet3 = LocaleUtils.availableLocaleSet();
        assertEquals(expectedAvailableLocaleSet3, availableLocaleSet3);

        // test available locales with null set
        Set<Locale> availableLocaleSetNull = null;
        assertNull(LocaleUtils.availableLocaleSet(availableLocaleSetNull));
    }

    @Test
    public void testIsAvailableLocale() {
        // test null locale
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // test valid locales
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CHINESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SIMPLIFIED_CHINESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.TRADITIONAL_CHINESE));

        // test invalid locales
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_US_invalid")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_US_invalid-variant")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_US_invalid-variant-")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_US_invalid--")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_US_invalid--variant")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_US_invalid--variant-")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_US_invalid--variant--")));
    }

    @Test
    public void testLanguagesByCountry() {
        // test empty country
        List<Locale> languagesByCountryEmpty = LocaleUtils.languagesByCountry(null);
        assertTrue(languagesByCountryEmpty.isEmpty());

        // test valid countries
        List<Locale> expectedLanguagesByCountry1 = Arrays.asList(new Locale("es"), new Locale("es_ES"), new Locale("es_MX"));
        List<Locale> languagesByCountry1 = LocaleUtils.languagesByCountry("ES");
        assertArrayEquals(expectedLanguagesByCountry1.toArray(), languagesByCountry1.toArray());

        List<Locale> expectedLanguagesByCountry2 = Arrays.asList(new Locale("fr"), new Locale("fr_FR"), new Locale("fr_BE"));
        List<Locale> languagesByCountry2 = LocaleUtils.languagesByCountry("FR");
        assertArrayEquals(expectedLanguagesByCountry2.toArray(), languagesByCountry2.toArray());

        List<Locale> expectedLanguagesByCountry3 = Collections.emptyList();
        List<Locale> languagesByCountry3 = LocaleUtils.languagesByCountry("XY");
        assertArrayEquals(expectedLanguagesByCountry3.toArray(), languagesByCountry3.toArray());

        // test null list
        List<Locale> languagesByCountryNull = null;
        assertNull(LocaleUtils.languagesByCountry(languagesByCountryNull));
    }

    @Test
    public void testCountriesByLanguage() {
        // test empty language
        List<Locale> countriesByLanguageEmpty = LocaleUtils.countriesByLanguage(null);
        assertTrue(countriesByLanguageEmpty.isEmpty());

        // test valid languages
        List<Locale> expectedCountriesByLanguage1 = Arrays.asList(new Locale("US"), new Locale("CA"), new Locale("GB"));
        List<Locale> countriesByLanguage1 = LocaleUtils.countriesByLanguage("EN");
        assertArrayEquals(expectedCountriesByLanguage1.toArray(), countriesByLanguage1.toArray());

        List<Locale> expectedCountriesByLanguage2 = Arrays.asList(new Locale("FR_FR"), new Locale("FR_BE"), new Locale("FR_CH"));
        List<Locale> countriesByLanguage2 = LocaleUtils.countriesByLanguage("FR");
        assertArrayEquals(expectedCountriesByLanguage2.toArray(), countriesByLanguage2.toArray());

        List<Locale> expectedCountriesByLanguage3 = Collections.emptyList();
        List<Locale> countriesByLanguage3 = LocaleUtils.countriesByLanguage("XY");
        assertArrayEquals(expectedCountriesByLanguage3.toArray(), countriesByLanguage3.toArray());

        // test null list
        List<Locale> countriesByLanguageNull = null;
        assertNull(LocaleUtils.countriesByLanguage(countriesByLanguageNull));
    }
}