package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.LocaleUtilsTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class LocaleUtilsTest {

    private String input;
    private Locale expected;

    public LocaleUtilsTest(String input, Locale expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{index}: toLocale(\"{0}\") = {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"en", Locale.ENGLISH},
                {"en_GB", Locale.ENGLISH},
                {"en_GB_London", Locale.ENGLISH},
                {"en_GB_London_en", Locale.ENGLISH},
                {"fr", Locale.FRENCH},
                {"fr_FR", Locale.FRENCH},
                {"fr_FR_Paris", Locale.FRENCH},
                {"fr_FR_Paris_fr", Locale.FRENCH},
                {"es", Locale.SPANISH},
                {"es_ES", Locale.SPANISH},
                {"es_ES_Madrid", Locale.SPANISH},
                {"es_ES_Madrid_es", Locale.SPANISH},
                {"it", Locale.ITALIAN},
                {"it_IT", Locale.ITALIAN},
                {"it_IT_Milan", Locale.ITALIAN},
                {"it_IT_Milan_it", Locale.ITALIAN},
                {"de", Locale.GERMAN},
                {"de_DE", Locale.GERMAN},
                {"de_DE_Berlin", Locale.GERMAN},
                {"de_DE_Berlin_de", Locale.GERMAN},
                {"ja", Locale.JAPANESE},
                {"ja_JP", Locale.JAPANESE},
                {"ja_JP_Tokyo", Locale.JAPANESE},
                {"ja_JP_Tokyo_ja", Locale.JAPANESE},
                {"ko", Locale.KOREAN},
                {"ko_KR", Locale.KOREAN},
                {"ko_KR_Seoul", Locale.KOREAN},
                {"ko_KR_Seoul_ko", Locale.KOREAN},
                {"zh_CN", Locale.SIMPLIFIED_CHINESE},
                {"zh_CN_Beijing", Locale.SIMPLIFIED_CHINESE},
                {"zh_CN_Beijing_zh", Locale.SIMPLIFIED_CHINESE},
                {"zh_CN_Shanghai", Locale.SIMPLIFIED_CHINESE},
                {"zh_CN_Shanghai_zh", Locale.SIMPLIFIED_CHINESE},
                {"zh_TW", Locale.TRADITIONAL_CHINESE},
                {"zh_TW_Taipei", Locale.TRADITIONAL_CHINESE},
                {"zh_TW_Taipei_zh", Locale.TRADITIONAL_CHINESE},
                {"zh_TW_Taipei_min_HK", Locale.TRADITIONAL_CHINESE_HONGKONG},
                {"zh_TW_Taipei_min_HK_zh", Locale.TRADITIONAL_CHINESE_HONGKONG}
        });
    }

    @Test
    public void testToLocale() {
        Locale actual = LocaleUtils.toLocale(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList() {
        Locale toTest = Locale.FRENCH;
        List<Locale> localeList = LocaleUtils.localeLookupList(toTest);

        assertEquals(5, localeList.size());
        assertEquals(toTest, localeList.get(0));
        assertEquals(Locale.FRENCH, localeList.get(1));
        assertEquals(Locale.FRENCH, localeList.get(2));
        assertEquals(Locale.ENGLISH, localeList.get(4));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SPANISH));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ITALIAN_ITALY_POMPEII));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "FR";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);

        assertEquals(6, result.size());
        assertTrue(result.contains(Locale.FRENCH));
        assertTrue(result.contains(Locale.FRENCH_CANADA));
        assertTrue(result.contains(Locale.FRENCH_SWITZERLAND));
        assertTrue(result.contains(Locale.FRENCH_BELGIUM));
        assertTrue(result.contains(Locale.FRENCH_LUXEMBOURG));
        assertTrue(result.contains(Locale.FRENCH_MONACO));
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);

        assertEquals(24, result.size());
        assertTrue(result.contains(Locale.ENGLISH));
        assertTrue(result.contains(Locale.ENGLISH_AUSTRALIA));
        assertTrue(result.contains(Locale.ENGLISH_CANADA));
        // ...
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("enGB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleEmptyString() {
        LocaleUtils.toLocale("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleNullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocaleInvalidCountryCode() {
        Locale inputLocale = new Locale("en_XX");
        Locale expectedLocale = null;
        Locale actualLocale = LocaleUtils.toLocale(inputLocale.toString());
        assertNull(actualLocale);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocaleInvalidLanguageCode() {
        Locale inputLocale = new Locale("xx_GB");
        Locale expectedLocale = null;
        Locale actualLocale = LocaleUtils.toLocale(inputLocale.toString());
        assertNull(actualLocale);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocaleEmptyLanguageCode() {
        Locale inputLocale = new Locale("", "GB");
        Locale expectedLocale = null;
        Locale actualLocale = LocaleUtils.toLocale(inputLocale.toString());
        assertNull(actualLocale);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocaleEmptyCountryCode() {
        Locale inputLocale = new Locale("en", "");
        Locale expectedLocale = null;
        Locale actualLocale = LocaleUtils.toLocale(inputLocale.toString());
        assertNull(actualLocale);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testLocaleUtilsInstance() {
        assertNotNull(LocaleUtils.class.newInstance());
    }

    @Test
    public void testIsAvailableLocaleNullInput() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountryNullInput() {
        assertNull(LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguageNullInput() {
        assertNull(LocaleUtils.countriesByLanguage(null));
    }
}