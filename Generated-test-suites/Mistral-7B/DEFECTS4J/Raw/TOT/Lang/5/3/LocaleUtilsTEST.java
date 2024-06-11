package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Tests for {@link LocaleUtils} class.
 */
public class LocaleUtilsTest {

    /**
     * Tests {@link LocaleUtils#toLocale(String)} method.
     */
    @Test
    public void testToLocale() {
        List<String> testCases = Arrays.asList(
                "en",
                "en_GB",
                "en_GB_US",
                "fr_FR",
                "fr_FR_CA",
                "es_ES_ES",
                "es_ES",
                "es",
                "es_XX",
                "es_XX_ES",
                "es_XX_LA",
                "",
                "  ",
                "en_GB_123",
                "en_GB_123_123",
                "en_GB_123_123_123",
                null
        );

        for (String testCase : testCases) {
            Locale expected = (testCase.isEmpty() || testCase.equals("")) ? null : new Locale(testCase);
            Locale actual = LocaleUtils.toLocale(testCase);
            Assert.assertEquals("Invalid locale conversion for test case: " + testCase, expected, actual);
        }
    }

    /**
     * Tests {@link LocaleUtils#localeLookupList(Locale)} method.
     */
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        Assert.assertEquals("Invalid locale lookup list for test case: " + locale, expected, actual);

        locale = new Locale("en");
        expected = Arrays.asList(new Locale("en"));
        actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals("Invalid locale lookup list for test case: " + locale, expected, actual);

        locale = null;
        expected = Collections.emptyList();
        actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        Assert.assertEquals("Invalid locale lookup list for test case: " + locale, expected, actual);
    }

    /**
     * Tests {@link LocaleUtils#availableLocaleList()} and {@link LocaleUtils#availableLocaleSet()} methods.
     */
    @Test
    public void testAvailableLocales() {
        List<Locale> availableLocalesList = LocaleUtils.availableLocaleList();
        Set<Locale> availableLocalesSet = LocaleUtils.availableLocaleSet();

        List<Locale> testCases = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("en"),
                new Locale("en_US"),
                new Locale("es"),
                new Locale("es_ES"),
                new Locale("es_MX"),
                new Locale("de"),
                new Locale("de_DE"),
                new Locale("zh_CN"),
                new Locale("zh_TW"),
                new Locale("it"),
                new Locale("it_IT"),
                new Locale("ko"),
                new Locale("ko_KR"),
                null
        );

        for (Locale testCase : testCases) {
            if (testCase != null) {
                boolean isAvailableLocaleList = availableLocalesList.contains(testCase);
                boolean isAvailableLocaleSet = availableLocalesSet.contains(testCase);
                Assert.assertTrue("Invalid available locale for test case: " + testCase, isAvailableLocaleList || isAvailableLocaleSet);
            }
        }

        Assert.assertEquals("Invalid number of available locales", 32, availableLocalesList.size());
        Assert.assertEquals("Invalid number of available locales", 32, availableLocalesSet.size());
    }

    /**
     * Tests {@link LocaleUtils#languagesByCountry(String)} method.
     */
    @Test
    public void testLanguagesByCountry() {
        List<String> testCases = Arrays.asList("FR", "GB", "US");

        for (String testCase : testCases) {
            List<Locale> expected = Arrays.asList(
                    new Locale("fr", testCase),
                    new Locale("fr", ""),
                    new Locale("fr")
            );
            List<Locale> actual = LocaleUtils.languagesByCountry(testCase);
            Assert.assertEquals("Invalid languages by country for test case: " + testCase, expected, actual);
        }

            List<String> emptyTestCases = Arrays.asList("");
            for (String testCase : emptyTestCases) {
                List<Locale> expected = Collections.emptyList();
                List<Locale> actual = LocaleUtils.languagesByCountry(testCase);
                Assert.assertEquals("Invalid languages by country for test case: " + testCase, expected, actual);
            }

            List<String> nullTestCases = Arrays.asList(null);
            for (String testCase : nullTestCases) {
                List<Locale> expected = Collections.emptyList();
                List<Locale> actual = LocaleUtils.languagesByCountry(testCase);
                Assert.assertEquals("Invalid languages by country for test case: " + testCase, expected, actual);
            }
        }

    /**
     * Tests {@link LocaleUtils#countriesByLanguage(String)} method.
     */
    @Test
    public void testCountriesByLanguage() {
        List<String> testCases = Arrays.asList("en", "fr");

        for (String testCase : testCases) {
            List<Locale> expected = Arrays.asList(
                    new Locale(testCase, "CA"),
                    new Locale(testCase, "GB"),
                    new Locale(testCase, "US"),
                    new Locale(testCase, ""),
                    new Locale("", testCase)
            );
            List<Locale> actual = LocaleUtils.countriesByLanguage(testCase);
            Assert.assertEquals("Invalid countries by language for test case: " + testCase, expected, actual);
        }

        List<String> emptyTestCases = Arrays.asList("");
        for (String testCase : emptyTestCases) {
            List<Locale> expected = Collections.emptyList();
            List<Locale> actual = LocaleUtils.countriesByLanguage(testCase);
            Assert.assertEquals("Invalid countries by language for test case: " + testCase, expected, actual);
        }

        List<String> nullTestCases = Arrays.asList(null);
        for (String testCase : nullTestCases) {
            List<Locale> expected = Collections.emptyList();
            List<Locale> actual = LocaleUtils.countriesByLanguage(testCase);
            Assert.assertEquals("Invalid countries by language for test case: " + testCase, expected, actual);
        }
    }
}