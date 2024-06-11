package org.apache.commons.lang3;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        String[] testCases = {"en", "en_GB", "en_GB_US", "fr_FR", "fr_FR_CA"};
        for (String localeString : testCases) {
            Locale expectedLocale = new Locale(localeString.split("_")[0], localeString.split("_")[1]);
            Locale actualLocale = LocaleUtils.toLocale(localeString);
            Assert.assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            Assert.assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
        }

        // Test invalid input
        String[] invalidInput = {"en_GB_xx", "en_GB_xxx", "en_GB_xx_xx", "en_GB_x"};
        for (String invalidString : invalidInput) {
            try {
                LocaleUtils.toLocale(invalidString);
                Assert.fail("Expected IllegalArgumentException for input: " + invalidString);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }

        // Test null input
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocaleList = LocaleUtils.localeLookupList(baseLocale);
        List<Locale> actualLocaleList = new ArrayList<>();
        actualLocaleList.add(baseLocale);
        actualLocaleList.add(new Locale("fr", "CA"));
        actualLocaleList.add(new Locale("fr"));
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Test default locale
        Locale defaultLocale = new Locale("en");
        expectedLocaleList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        actualLocaleList = new ArrayList<>();
        actualLocaleList.add(baseLocale);
        actualLocaleList.add(new Locale("fr", "CA"));
        actualLocaleList.add(new Locale("fr"));
        actualLocaleList.add(defaultLocale);
        Assert.assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", countryCode), new Locale("en", countryCode));
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Test null input
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "CH"));
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Test null input
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }

    @Test(expected = IllegalStateException.class)
    public void testConcurrentMapAccess() {
        ConcurrentMap<String, List<Locale>> concurrentMap = new ConcurrentHashMap<>();
        List<Locale> list = new ArrayList<>();
        list.add(Locale.FRENCH);
        concurrentMap.putIfAbsent("CA", list);
        concurrentMap.putIfAbsent("CA", Collections.singletonList(Locale.ENGLISH));
        List<Locale> languagesByCountry = concurrentMap.get("CA");
        Assert.assertEquals(2, languagesByCountry.size());
    }
}