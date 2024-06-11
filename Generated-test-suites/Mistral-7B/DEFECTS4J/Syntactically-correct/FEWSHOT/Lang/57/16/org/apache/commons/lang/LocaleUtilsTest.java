package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals("en", LocaleUtils.toLocale("en").getLanguage());
        assertEquals("GB", LocaleUtils.toLocale("en_GB").getCountry());
        assertEquals("en_GB", LocaleUtils.toLocale("en_GB").toLanguageTag());
        assertEquals("en_GB_xxx", LocaleUtils.toLocale("en_GB_xxx").toLanguageTag());
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertSame(Locale.US, LocaleUtils.toLocale(""));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("ab_CD_XXX"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("ab_CD"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("ab_CD_XXX_XX"));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> locales = LocaleUtils.localeLookupList(Locale.US, Locale.FRENCH);
        assertEquals(5, locales.size());
        assertEquals(Locale.US, locales.get(0));
        assertEquals(Locale.US, locales.get(1));
        assertEquals(Locale.ENGLISH, locales.get(2));
        assertEquals(Locale.FRENCH, locales.get(3));
        assertEquals(Locale.FRENCH, locales.get(4));

        locales = LocaleUtils.localeLookupList(Locale.FRENCH);
        assertEquals(3, locales.size());
        assertEquals(Locale.FRENCH, locales.get(0));
        assertEquals(Locale.FRENCH, locales.get(1));
        assertEquals(Locale.ENGLISH, locales.get(2));

        locales = LocaleUtils.localeLookupList(Locale.ITALIAN);
        assertEquals(4, locales.size());
        assertEquals(Locale.ITALIAN, locales.get(0));
        assertEquals(Locale.ITALIAN, locales.get(1));
        assertEquals(Locale.ITALIAN, locales.get(2));
        assertEquals(Locale.ENGLISH, locales.get(3));

        locales = LocaleUtils.localeLookupList(Locale.CHINESE);
        assertEquals(5, locales.size());
        assertEquals(Locale.CHINESE, locales.get(0));
        assertEquals(Locale.CHINESE, locales.get(1));
        assertEquals(Locale.CHINESE, locales.get(2));
        assertEquals(Locale.ENGLISH, locales.get(3));
        assertEquals(Locale.ENGLISH, locales.get(4));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(223, availableLocales.size());
        assertTrue(availableLocales.contains(Locale.US));
        assertTrue(availableLocales.contains(Locale.FRENCH));
        assertTrue(availableLocales.contains(Locale.ITALIAN));
        assertTrue(availableLocales.contains(Locale.CHINESE));

        List<Locale> englishLocales = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(13, englishLocales.size());
        assertTrue(englishLocales.contains(Locale.ENGLISH));
        assertTrue(englishLocales.contains(Locale.ENGLISH_US));
        assertTrue(englishLocales.contains(Locale.ENGLISH_GB));
        assertTrue(englishLocales.contains(Locale.ENGLISH_INDIA));
        assertTrue(englishLocales.contains(Locale.ENGLISH_AUSTRALIA));
        assertTrue(englishLocales.contains(Locale.ENGLISH_CANADA));
        assertTrue(englishLocales.contains(Locale.ENGLISH_NEW_ZEALAND));
        assertTrue(englishLocales.contains(Locale.ENGLISH_SOUTH_AFRICA));
        assertTrue(englishLocales.contains(Locale.ENGLISH_IRELAND));
        assertTrue(englishLocales.contains(Locale.ENGLISH_Jamaica));
        assertTrue(englishLocales.contains(Locale.ENGLISH_Trinidad_and_Tobago));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> frenchLocales = LocaleUtils.languagesByCountry("CA");
        assertEquals(3, frenchLocales.size());
        assertTrue(frenchLocales.contains(Locale.FRENCH_CA));
        assertTrue(frenchLocales.contains(Locale.FRENCH));
        assertTrue(frenchLocales.contains(Locale.ENGLISH));

        List<Locale> frenchLocales2 = LocaleUtils.languagesByCountry("FR");
        assertEquals(2, frenchLocales2.size());
        assertTrue(frenchLocales2.contains(Locale.FRENCH));
        assertTrue(frenchLocales2.contains(Locale.FRENCH_FR));

        List<Locale> italianLocales = LocaleUtils.languagesByCountry("IT");
        assertEquals(2, italianLocales.size());
        assertTrue(italianLocales.contains(Locale.ITALIAN));
        assertTrue(italianLocales.contains(Locale.ITALIAN_IT));

        List<Locale> chineseLocales = LocaleUtils.languagesByCountry("CN");
        assertEquals(2, chineseLocales.size());
        assertTrue(chineseLocales.contains(Locale.SIMPLIFIED_CHINESE));
        assertTrue(chineseLocales.contains(Locale.SIMPLIFIED_CHINESE_CN));

        List<Locale> chineseLocales2 = LocaleUtils.languagesByCountry("TW");
        assertEquals(2, chineseLocales2.size());
        assertTrue(chineseLocales2.contains(Locale.TRADITIONAL_CHINESE));
        assertTrue(chineseLocales2.contains(Locale.TRADITIONAL_CHINESE_TW));

        List<Locale> emptyLocaleList = LocaleUtils.languagesByCountry(null);
        assertEquals(0, emptyLocaleList.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> englishLocales = LocaleUtils.countriesByLanguage("EN");
        assertEquals(11, englishLocales.size());
        assertTrue(englishLocales.contains(Locale.ENGLISH_US));
        assertTrue(englishLocales.contains(Locale.ENGLISH_GB));
        assertTrue(englishLocales.contains(Locale.ENGLISH_INDIA));
        assertTrue(englishLocales.contains(Locale.ENGLISH_AUSTRALIA));
        assertTrue(englishLocales.contains(Locale.ENGLISH_CANADA));
        assertTrue(englishLocales.contains(Locale.ENGLISH_NEW_ZEALAND));
        assertTrue(englishLocales.contains(Locale.ENGLISH_SOUTH_AFRICA));
        assertTrue(englishLocales.contains(Locale.ENGLISH_IRELAND));
        assertTrue(englishLocales.contains(Locale.ENGLISH_Jamaica));
        assertTrue(englishLocales.contains(Locale.ENGLISH_Trinidad_and_Tobago));
        assertTrue(englishLocales.contains(Locale.ENGLISH_ZA));

        List<Locale> italianLocales = LocaleUtils.countriesByLanguage("IT");
        assertEquals(1, italianLocales.size());
        assertTrue(italianLocales.contains(Locale.ITALIAN_IT));

        List<Locale> frenchLocales = LocaleUtils.countriesByLanguage("FR");
        assertEquals(3, frenchLocales.size());
        assertTrue(frenchLocales.contains(Locale.FRENCH_FR));
        assertTrue(frenchLocales.contains(Locale.FRENCH_CA));
        assertTrue(frenchLocales.contains(Locale.FRENCH_BE));

        List<Locale> chineseLocales = LocaleUtils.countriesByLanguage("ZH");
        assertEquals(2, chineseLocales.size());
        assertTrue(chineseLocales.contains(Locale.SIMPLIFIED_CHINESE_CN));
        assertTrue(chineseLocales.contains(Locale.TRADITIONAL_CHINESE_TW));

        List<Locale> emptyLocaleList = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, emptyLocaleList.size());
    }
}