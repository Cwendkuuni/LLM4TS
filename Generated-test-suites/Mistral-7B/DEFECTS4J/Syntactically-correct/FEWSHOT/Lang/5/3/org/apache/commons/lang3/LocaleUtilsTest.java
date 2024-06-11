package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertLinesMatch;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

public class LocaleUtilsTest {

    private static final Map<String, List<Locale>> cLanguagesByCountry;
    private static final Map<String, List<Locale>> cCountriesByLanguage;
    private static final List<Locale> availableLocaleList;
    private static final Set<Locale> availableLocaleSet;

    @BeforeClass
    public static void beforeClass() {
        cLanguagesByCountry = new HashMap<>();
        cCountriesByLanguage = new HashMap<>();
        availableLocaleList = LocaleUtils.availableLocaleList();
        availableLocaleSet = LocaleUtils.availableLocaleSet();
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToLocale() {
        // Test empty string
        assertNull(LocaleUtils.toLocale(""));
        // Test null
        assertNull(LocaleUtils.toLocale(null));
        // Test lowercase language
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        // Test uppercase language
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("EN"));
        // Test invalid language
        assertNull(LocaleUtils.toLocale("xx"));
        // Test empty country
        assertEquals(Locale.ROOT, LocaleUtils.toLocale("en"));
        // Test empty country + variant
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_"));
        // Test two-letter country
        assertEquals(Locale.CANADA, LocaleUtils.toLocale("ca"));
        // Test two-letter country + empty variant
        assertEquals(Locale.CANADA, LocaleUtils.toLocale("ca"));
        // Test two-letter country + variant
        assertEquals(Locale.CANADA_FRENCH, LocaleUtils.toLocale("ca_fr"));
        // Test language with multiple country codes
        assertEquals(Locale.GERMANY, LocaleUtils.toLocale("de_AT"));
        assertEquals(Locale.AUSTRIA, LocaleUtils.toLocale("de_AT"));
        // Test language with multiple country codes and variant
        assertEquals(Locale.GERMANY_EST, LocaleUtils.toLocale("de_DE_EST"));
        assertEquals(Locale.GERMANY_EST, LocaleUtils.toLocale("de_DE_EST"));
        // Test locale with language, country, and variant
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR_FR"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR_FR"));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = Locale.US;
        Locale variantLocale = new Locale("en", "US_VARIANT");
        Locale emptyCountryLocale = new Locale("en");
        Locale emptyLocale = Locale.ROOT;

        List<Locale> list = LocaleUtils.localeLookupList(baseLocale);
        assertEquals(4, list.size());
        assertSame(baseLocale, list.get(0));
        assertSame(emptyCountryLocale, list.get(1));
        assertSame(emptyLocale, list.get(2));
        assertNull(list.get(3));

        list = LocaleUtils.localeLookupList(variantLocale);
        assertEquals(5, list.size());
        assertSame(variantLocale, list.get(0));
        assertSame(emptyCountryLocale, list.get(1));
        assertSame(emptyLocale, list.get(2));
        assertSame(baseLocale, list.get(3));
        assertNull(list.get(4));

        list = LocaleUtils.localeLookupList(emptyCountryLocale);
        assertEquals(3, list.size());
        assertSame(emptyCountryLocale, list.get(0));
        assertSame(emptyLocale, list.get(1));
        assertSame(baseLocale, list.get(2));

        list = LocaleUtils.localeLookupList(emptyLocale);
        assertEquals(1, list.size());
        assertSame(emptyLocale, list.get(0));

        list = LocaleUtils.localeLookupList(null);
        assertEquals(0, list.size());

        List<Locale> list2 = LocaleUtils.localeLookupList(baseLocale, emptyLocale);
        assertEquals(5, list2.size());
        assertSame(baseLocale, list2.get(0));
        assertSame(emptyCountryLocale, list2.get(1));
        assertSame(emptyLocale, list2.get(2));
        assertSame(emptyLocale, list2.get(3));
        assertSame(emptyLocale, list2.get(4));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertEquals(277, list.size());

        Random random = new Random();
        int index = random.nextInt(list.size());
        Locale locale = list.get(index);
        assertNotNull(locale);
        assertEquals(locale.getClass(), Locale.class);

        List<String> countries = Arrays.asList("US", "CA", "DE", "ZA", "JP", "CN");
        for (String country : countries) {
            List<Locale> langs = LocaleUtils.languagesByCountry(country);
            assertFalse(langs.isEmpty());
            for (Locale lang : langs) {
                assertNotNull(lang);
                assertEquals(lang.getClass(), Locale.class);
                assertEquals(lang.getCountry(), country);
            }
        }
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertEquals(277, set.size());

        Random random = new Random();
        Locale locale;
        for (int i = 0; i < 10; i++) {
            locale = set.iterator().next();
            assertNotNull(locale);
            assertEquals(locale.getClass(), Locale.class);
        }
    }

    //-----------------------------------------------------------------------
    @Test
    public void testLanguagesByCountry() {
        Locale US_EN = new Locale("en", "US");
        Locale CA_EN = new Locale("en", "CA");
        Locale DE_DE = new Locale("de", "DE");
        Locale ZA_EN = new Locale("en", "ZA");
        Locale JP_JP = new Locale("ja", "JP");
        Locale CN_ZH = new Locale("zh", "CN");

        List<Locale> languagesByCountryUS = LocaleUtils.languagesByCountry("US");
        assertArrayEquals(new Locale[] {US_EN}, languagesByCountryUS.toArray());

        List<Locale> languagesByCountryCA = LocaleUtils.languagesByCountry("CA");
        assertArrayEquals(new Locale[] {US_EN, CA_EN}, languagesByCountryCA.toArray());

        List<Locale> languagesByCountryDE = LocaleUtils.languagesByCountry("DE");
        assertArrayEquals(new Locale[] {DE_DE}, languagesByCountryDE.toArray());

        List<Locale> languagesByCountryZA = LocaleUtils.languagesByCountry("ZA");
        assertArrayEquals(new Locale[] {US_EN, ZA_EN}, languagesByCountryZA.toArray());

        List<Locale> languagesByCountryJP = LocaleUtils.languagesByCountry("JP");
        assertArrayEquals(new Locale[] {JP_JP}, languagesByCountryJP.toArray());

        List<Locale> languagesByCountryCN = LocaleUtils.languagesByCountry("CN");
        assertArrayEquals(new Locale[] {CN_ZH}, languagesByCountryCN.toArray());

        List<Locale> languagesByCountryUnknown = LocaleUtils.languagesByCountry("XX");
        assertFalse(languagesByCountryUnknown.isEmpty());
    }

    //-----------------------------------------------------------------------
    @Test
    public void testCountriesByLanguage() {
        Locale EN_US = new Locale("en", "US");
        Locale EN_CA = new Locale("en", "CA");
        Locale DE_DE = new Locale("de", "DE");
        Locale DE_AT = new Locale("de", "AT");
        Locale ZH_CN = new Locale("zh", "CN");
        Locale ZH_TW = new Locale("zh", "TW");

        List<Locale> countriesByLanguageEN = LocaleUtils.countriesByLanguage("en");
        assertArrayEquals(new Locale[] {EN_US, EN_CA}, countriesByLanguageEN.toArray());

        List<Locale> countriesByLanguageDE = LocaleUtils.countriesByLanguage("de");
        assertArrayEquals(new Locale[] {DE_DE, DE_AT}, countriesByLanguageDE.toArray());

        List<Locale> countriesByLanguageZH = LocaleUtils.countriesByLanguage("zh");
        assertArrayEquals(new Locale[] {ZH_CN, ZH_TW}, countriesByLanguageZH.toArray());

        List<Locale> countriesByLanguageUnknown = LocaleUtils.countriesByLanguage("XX");
        assertFalse(countriesByLanguageUnknown.isEmpty());

        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_ZZ").getLanguage());
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToLocale_InvalidLocaleString() {
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xxxx"));
        assertNull(LocaleUtils.toLocale("xxxxx"));
        assertNull(LocaleUtils.toLocale("xxxxxx"));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToLocale_InvalidLocaleString_InvalidLanguage() {
        assertNull(LocaleUtils.toLocale("xx_XX"));
        assertNull(LocaleUtils.toLocale("xx_XXX"));
        assertNull(LocaleUtils.toLocale("xx_XXX_XXX"));
        assertNull(LocaleUtils.toLocale("xx_XXX_XXX_XXX"));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToLocale_InvalidLocaleString_InvalidCountry() {
        assertNull(LocaleUtils.toLocale("xx_XX_xx"));
        assertNull(LocaleUtils.toLocale("xx_XX_xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_XX_xx_xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_XX_xx_xx_xx_xx"));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToLocale_InvalidLocaleString_InvalidSeparator() {
        assertNull(LocaleUtils.toLocale("xx_XX-xx"));
        assertNull(LocaleUtils.toLocale("xx_XX-xx-xx"));
        assertNull(LocaleUtils.toLocale("xx_XX-xx-xx-xx"));
        assertNull(LocaleUtils.toLocale("xx_XX-xx-xx-xx-xx"));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToLocale_InvalidLocaleString_EmptyLanguage() {
        assertNull(LocaleUtils.toLocale("_XX"));
        assertNull(LocaleUtils.toLocale("_XX_XX"));
        assertNull(LocaleUtils.toLocale("_XX_XX_XX"));
        assertNull(LocaleUtils.toLocale("_XX_XX_XX_XX"));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToLocale_InvalidLocaleString_EmptyCountry() {
        assertNull(LocaleUtils.toLocale("xx_"));
        assertNull(LocaleUtils.toLocale("xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_xx_xx_xx"));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToLocale_InvalidLocaleString_EmptySeparator() {
        assertNull(LocaleUtils.toLocale("xx_XX"));
        assertNull(LocaleUtils.toLocale("xx_XX_XX"));
        assertNull(LocaleUtils.toLocale("xx_XX_XX_XX"));
        assertNull(LocaleUtils.toLocale("xx_XX_XX_XX_XX"));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToLocale_InvalidLocaleString_InvalidLanguageLength() {
        assertNull(LocaleUtils.toLocale("xxxxxxxxxxxxxxxxxxx_XX"));
        assertNull(LocaleUtils.toLocale("xxxxxxxxxxxxxxxxxxx_XX_XX"));
        assertNull(LocaleUtils.toLocale("xxxxxxxxxxxxxxxxxxx_XX_XX_XX"));
        assertNull(LocaleUtils.toLocale("xxxxxxxxxxxxxxxxxxx_XX_XX_XX_XX"));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToLocale_InvalidLocaleString_InvalidCountryLength() {
        assertNull(LocaleUtils.toLocale("xx_XXxxxxxxxxxxxxxx_XX"));
        assertNull(LocaleUtils.toLocale("xx_XXxxxxxxxxxxxxxx_XX_XX"));
        assertNull(LocaleUtils.toLocale("xx_XXxxxxxxxxxxxxxx_XX_XX_XX"));
        assertNull(LocaleUtils.toLocale("xx_XXxxxxxxxxxxxxxx_XX_XX_XX_XX"));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToLocale_InvalidLocaleString_MissingLanguage() {
        assertNull(LocaleUtils.toLocale("_XX_XX"));
        assertNull(LocaleUtils.toLocale("_XX_XX_XX"));
        assertNull(LocaleUtils.toLocale("_XX_XX_XX_XX"));
        assertNull(LocaleUtils.toLocale("_XX_XX_XX_XX_XX"));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToLocale_InvalidLocaleString_MissingCountry() {
        assertNull(LocaleUtils.toLocale("xx_"));
        assertNull(LocaleUtils.toLocale("xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_xx_xx_xx"));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToLocale_InvalidLocaleString_MissingSeparator() {
        assertNull(LocaleUtils.toLocale("xx_XX"));
        assertNull(LocaleUtils.toLocale("xx_XX_XX"));
        assertNull(LocaleUtils.toLocale("xx_XX_XX_XX"));
        assertNull(LocaleUtils.toLocale("xx_XX_XX_XX_XX"));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToLocale_InvalidLocaleString_MissingLanguageAndCountry() {
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("xx_"));
        assertNull(LocaleUtils.toLocale("xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_xx_xx_xx"));
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToLocale_InvalidLocaleString_MissingLanguageAndCountryAndSeparator() {
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("xx_"));
        assertNull(LocaleUtils.toLocale("xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_xx_xx_xx"));
    }
}