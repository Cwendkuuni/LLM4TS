package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        expected = new Locale("fr", "FR");
        actual = LocaleUtils.toLocale("fr_FR");
        assertEquals(expected, actual);

        expected = new Locale("es", "ES");
        actual = LocaleUtils.toLocale("es_ES");
        assertEquals(expected, actual);

        expected = new Locale("de", "");
        actual = LocaleUtils.toLocale("de");
        assertEquals(expected, actual);

        expected = new Locale("en", "");
        actual = LocaleUtils.toLocale("en");
        assertEquals(expected, actual);

        String invalidFormat1 = "en_abc";
        try {
            actual = LocaleUtils.toLocale(invalidFormat1);
            fail("Expected IllegalArgumentException for invalid format: " + invalidFormat1);
        } catch (IllegalArgumentException e) {
            // expected
        }

        String invalidFormat2 = "en_US_abc";
        try {
            actual = LocaleUtils.toLocale(invalidFormat2);
            fail("Expected IllegalArgumentException for invalid format: " + invalidFormat2);
        } catch (IllegalArgumentException e) {
            // expected
        }

        String invalidFormat3 = "en_US_123";
        try {
            actual = LocaleUtils.toLocale(invalidFormat3);
            fail("Expected IllegalArgumentException for invalid format: " + invalidFormat3);
        } catch (IllegalArgumentException e) {
            // expected
        }

        String invalidFormat4 = "en_US_abc_123";
        try {
            actual = LocaleUtils.toLocale(invalidFormat4);
            fail("Expected IllegalArgumentException for invalid format: " + invalidFormat4);
        } catch (IllegalArgumentException e) {
            // expected
        }

        String invalidFormat5 = "en_US_abc_123_xyz";
        try {
            actual = LocaleUtils.toLocale(invalidFormat5);
            fail("Expected IllegalArgumentException for invalid format: " + invalidFormat5);
        } catch (IllegalArgumentException e) {
            // expected
        }

        String invalidFormat6 = "en_US_abc_123_xyz_123";
        try {
            actual = LocaleUtils.toLocale(invalidFormat6);
            fail("Expected IllegalArgumentException for invalid format: " + invalidFormat6);
        } catch (IllegalArgumentException e) {
            // expected
        }

        String invalidFormat7 = "_123";
        try {
            actual = LocaleUtils.toLocale(invalidFormat7);
            fail("Expected IllegalArgumentException for invalid format: " + invalidFormat7);
        } catch (IllegalArgumentException e) {
            // expected
        }

        String invalidFormat8 = "";
        try {
            actual = LocaleUtils.toLocale(invalidFormat8);
            fail("Expected IllegalArgumentException for invalid format: " + invalidFormat8);
        } catch (IllegalArgumentException e) {
            // expected
        }

        String invalidFormat9 = null;
        assertNull(LocaleUtils.toLocale(invalidFormat9));
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> actual = LocaleUtils.localeLookupList(baseLocale, new Locale("en"));
        assertEquals(expected, actual);

        expected = Arrays.asList(new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        actual = LocaleUtils.localeLookupList(baseLocale);
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.RUSSIAN, Locale.SPANISH, Locale.TRADITIONAL_TAIWAN, Locale.SIMPLIFIED_TWI, Locale.TRADITIONAL_TWI, Locale.ARABIC, Locale.CZECH, Locale.DANISH, Locale.DUTCH, Locale.FINNISH, Locale.GREEK, Locale.HEBREW, Locale.HUNGARIAN, Locale.ICELANDIC, Locale.INDONESIAN, Locale.IRISH, Locale.ITALIAN, Locale.JAVANESE, Locale.KANNADA, Locale.KOREAN, Locale.LATIN, Locale.LITHUANIAN, Locale.NORWEGIAN, Locale.POLISH, Locale.PORTUGUESE, Locale.ROMANIAN, Locale.SERBIAN, Locale.SLOVAK, Locale.SLovenian, Locale.SWEDISH, Locale.THAI, Locale.TURKISH, Locale.UKRAINIAN, Locale.VIETNAMESE, Locale.WELSH);
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<Locale>(Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.RUSSIAN, Locale.SPANISH, Locale.TRADITIONAL_TAIWAN, Locale.SIMPLIFIED_TWI, Locale.TRADITIONAL_TWI, Locale.ARABIC, Locale.CZECH, Locale.DANISH, Locale.DUTCH, Locale.FINNISH, Locale.GREEK, Locale.HEBREW, Locale.HUNGARIAN, Locale.ICELANDIC, Locale.INDONESIAN, Locale.IRISH, Locale.ITALIAN, Locale.JAVANESE, Locale.KANNADA, Locale.KOREAN, Locale.LATIN, Locale.LITHUANIAN, Locale.NORWEGIAN, Locale.POLISH, Locale.PORTUGUESE, Locale.ROMANIAN, Locale.SERBIAN, Locale.SLOVAK, Locale.SLovenian, Locale.SWEDISH, Locale.THAI, Locale.TURKISH, Locale.UKRAINIAN, Locale.VIETNAMESE, Locale.WELSH));
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SIMPLIFIED_CHINESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.TRADITIONAL_CHINESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.JAPANESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.KOREAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.RUSSIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SPANISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.TRADITIONAL_TAIWAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SIMPLIFIED_TWI));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.TRADITIONAL_TWI));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ARABIC));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CZECH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.DANISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.DUTCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FINNISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GREEK));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.HEBREW));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.HUNGARIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ICELANDIC));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.INDONESIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.IRISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.JAVANESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.KANNADA));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.KOREAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.LATIN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.LITHUANIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.NORWEGIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.POLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.PORTUGUESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ROMANIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SERBIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SLOVAK));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SLovenian));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SWEDISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.THAI));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.TURKISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.UKRAINIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.VIETNAMESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.WELSH));

        Locale unavailableLocale = new Locale("xx", "yy", "zz");
        assertFalse(LocaleUtils.isAvailableLocale(unavailableLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("fr"), new Locale("en"), new Locale("es"), new Locale("de"));
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("CA"), new Locale("US"), new Locale("MX"), new Locale("ES"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, actual);
    }
}