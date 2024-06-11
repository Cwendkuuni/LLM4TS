package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        String input1 = "en";
        Locale expected1 = new Locale("en", "");
        Locale actual1 = LocaleUtils.toLocale(input1);
        assertEquals(expected1, actual1);

        String input2 = "en_GB";
        Locale expected2 = new Locale("en", "GB");
        Locale actual2 = LocaleUtils.toLocale(input2);
        assertEquals(expected2, actual2);

        String input3 = "en_GB_xxx";
        Locale expected3 = new Locale("en", "GB", "xxx");
        Locale actual3 = LocaleUtils.toLocale(input3);
        assertEquals(expected3, actual3);

        String input4 = "fr_CA_YYY";
        Locale expected4 = new Locale("fr", "CA", "YYY");
        Locale actual4 = LocaleUtils.toLocale(input4);
        assertEquals(expected4, actual4);

        // Test invalid input
        String input5 = null;
        Locale actual5 = LocaleUtils.toLocale(input5);
        assertNull(actual5);

        String input6 = "ab_cd_ef_gh_ij_kl";
        String message6 = "Invalid locale format: " + input6;
        try {
            Locale actual6 = LocaleUtils.toLocale(input6);
            fail(message6);
        } catch (IllegalArgumentException e) {
            assertEquals(message6, e.getMessage());
        }

        String input7 = "ab_cd";
        String message7 = "Invalid locale format: " + input7;
        try {
            Locale actual7 = LocaleUtils.toLocale(input7);
            fail(message7);
        } catch (IllegalArgumentException e) {
            assertEquals(message7, e.getMessage());
        }

        String input8 = "ab_cd_ef";
        String message8 = "Invalid locale format: " + input8;
        try {
            Locale actual8 = LocaleUtils.toLocale(input8);
            fail(message8);
        } catch (IllegalArgumentException e) {
            assertEquals(message8, e.getMessage());
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty input
        Locale locale1 = null;
        List<Locale> expected1 = Collections.emptyList();
        List<Locale> actual1 = LocaleUtils.localeLookupList(locale1);
        assertEquals(expected1, actual1);

        // Test valid input
        String input1 = "en";
        Locale locale2 = new Locale(input1, "");
        List<Locale> expected2 = Arrays.asList(locale2, new Locale(input1, ""));
        List<Locale> actual2 = LocaleUtils.localeLookupList(locale2);
        assertEquals(expected2, actual2);

        String input2 = "en_CA";
        Locale locale3 = new Locale(input2, "");
        List<Locale> expected3 = Arrays.asList(locale3, new Locale(input2, ""), new Locale(input1, ""));
        List<Locale> actual3 = LocaleUtils.localeLookupList(locale3);
        assertEquals(expected3, actual3);

        String input3 = "en_CA_XXX";
        Locale locale4 = new Locale(input3, "");
        List<Locale> expected4 = Arrays.asList(locale4, new Locale(input3, ""), new Locale(input3, ""), new Locale(input1, ""), new Locale(input1, ""));
        List<Locale> actual4 = LocaleUtils.localeLookupList(locale4);
        assertEquals(expected4, actual4);

        String input4 = "fr";
        Locale locale5 = new Locale(input4, "");
        List<Locale> expected5 = new ArrayList<Locale>(3) {
            {
                add(locale5);
                add(new Locale(input4, ""));
                add(new Locale(input1, ""));
            }
        };
        List<Locale> actual5 = LocaleUtils.localeLookupList(locale5);
        assertEquals(expected5, actual5);

        String input5 = "fr_CA";
        Locale locale6 = new Locale(input5, "");
        List<Locale> expected6 = new ArrayList<Locale>(5) {
            {
                add(locale6);
                add(new Locale(input5, ""));
                add(new Locale(input5, "CA"));
                add(new Locale(input4, ""));
                add(new Locale(input1, ""));
            }
        };
        List<Locale> actual6 = LocaleUtils.localeLookupList(locale6);
        assertEquals(expected6, actual6);

        String input6 = "fr_CA_XXX";
        Locale locale7 = new Locale(input6, "");
        List<Locale> expected7 = new ArrayList<Locale>(7) {
            {
                add(locale7);
                add(new Locale(input6, ""));
                add(new Locale(input6, "CA"));
                add(new Locale(input6, "CA", ""));
                add(new Locale(input5, ""));
                add(new Locale(input4, ""));
                add(new Locale(input1, ""));
            }
        };
        List<Locale> actual7 = LocaleUtils.localeLookupList(locale7);
        assertEquals(expected7, actual7);

        // Test invalid input
        String input7 = null;
        Locale locale8 = null;
        List<Locale> expected8 = Collections.emptyList();
        List<Locale> actual8 = LocaleUtils.localeLookupList(locale8, locale8);
        assertEquals(expected8, actual8);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty input
        List<Locale> expected1 = Collections.emptyList();
        List<Locale> actual1 = LocaleUtils.availableLocaleList();
        assertEquals(expected1, actual1);

        // Test valid input
        List<Locale> expected2 = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.CHINESE, Locale.KOREAN, Locale.RUSSIAN);
        List<Locale> actual2 = LocaleUtils.availableLocaleList();
        assertEquals(expected2, actual2);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test empty input
        Set<Locale> expected1 = Collections.emptySet();
        Set<Locale> actual1 = LocaleUtils.availableLocaleSet();
        assertEquals(expected1, actual1);

        // Test valid input
        Set<Locale> expected2 = new HashSet<Locale>(Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.CHINESE, Locale.KOREAN, Locale.RUSSIAN));
        Set<Locale> actual2 = LocaleUtils.availableLocaleSet();
        assertEquals(expected2, actual2);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test empty input
        Locale locale1 = null;
        boolean expected1 = false;
        boolean actual1 = LocaleUtils.isAvailableLocale(locale1);
        assertEquals(expected1, actual1);

        // Test valid input
        Locale locale2 = Locale.ENGLISH;
        boolean expected2 = true;
        boolean actual2 = LocaleUtils.isAvailableLocale(locale2);
        assertEquals(expected2, actual2);

        // Test invalid input
        Locale locale3 = new Locale("ab", "cd");
        boolean expected3 = false;
        boolean actual3 = LocaleUtils.isAvailableLocale(locale3);
        assertEquals(expected3, actual3);
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty input
        String countryCode1 = null;
        List<Locale> expected1 = Collections.emptyList();
        List<Locale> actual1 = LocaleUtils.languagesByCountry(countryCode1);
        assertEquals(expected1, actual1);

        // Test valid input
        String countryCode2 = "US";
        List<Locale> expected2 = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH_US, Locale.ENGLISH_GB, Locale.ENGLISH_CA);
        List<Locale> actual2 = LocaleUtils.languagesByCountry(countryCode2);
        assertEquals(expected2, actual2);

        String countryCode3 = "FR";
        List<Locale> expected3 = Arrays.asList(Locale.FRENCH, Locale.FRENCH_FR, Locale.FRENCH_BE, Locale.FRENCH_CH);
        List<Locale> actual3 = LocaleUtils.languagesByCountry(countryCode3);
        assertEquals(expected3, actual3);

        String countryCode4 = "DE";
        List<Locale> expected4 = Arrays.asList(Locale.GERMAN, Locale.GERMAN_DE, Locale.GERMAN_AT, Locale.GERMAN_CH);
        List<Locale> actual4 = LocaleUtils.languagesByCountry(countryCode4);
        assertEquals(expected4, actual4);

        String countryCode5 = "IT";
        List<Locale> expected5 = Arrays.asList(Locale.ITALIAN, Locale.ITALIAN_IT, Locale.ITALIAN_CH, Locale.ITALIAN_SM);
        List<Locale> actual5 = LocaleUtils.languagesByCountry(countryCode5);
        assertEquals(expected5, actual5);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty input
        String languageCode1 = null;
        List<Locale> expected1 = Collections.emptyList();
        List<Locale> actual1 = LocaleUtils.countriesByLanguage(languageCode1);
        assertEquals(expected1, actual1);

        // Test valid input
        String languageCode2 = "en";
        List<Locale> expected2 = Arrays.asList(Locale.ENGLISH_US, Locale.ENGLISH_GB, Locale.ENGLISH_CA, Locale.ENGLISH_AU, Locale.ENGLISH_ZA, Locale.ENGLISH_IE, Locale.ENGLISH_JM, Locale.ENGLISH_NZ, Locale.ENGLISH_SG, Locale.ENGLISH_TT, Locale.ENGLISH_ZW);
        List<Locale> actual2 = LocaleUtils.countriesByLanguage(languageCode2);
        assertEquals(expected2, actual2);

        String languageCode3 = "fr";
        List<Locale> expected3 = Arrays.asList(Locale.FRENCH_FR, Locale.FRENCH_BE, Locale.FRENCH_CH, Locale.FRENCH_LU, Locale.FRENCH_MC, Locale.FRENCH_CH_JE, Locale.FRENCH_AD, Locale.FRENCH_GF);
        List<Locale> actual3 = LocaleUtils.countriesByLanguage(languageCode3);
        assertEquals(expected3, actual3);

        String languageCode4 = "de";
        List<Locale> expected4 = Arrays.asList(Locale.GERMAN_DE, Locale.GERMAN_AT, Locale.GERMAN_CH, Locale.GERMAN_LI, Locale.GERMAN_CH_LI);
        List<Locale> actual4 = LocaleUtils.countriesByLanguage(languageCode4);
        assertEquals(expected4, actual4);

        String languageCode5 = "it";
        List<Locale> expected5 = Arrays.asList(Locale.ITALIAN_IT, Locale.ITALIAN_SM, Locale.ITALIAN_CH, Locale.ITALIAN_VA, Locale.ITALIAN_IQ);
        List<Locale> actual5 = LocaleUtils.countriesByLanguage(languageCode5);
        assertEquals(expected5, actual5);
    }
}