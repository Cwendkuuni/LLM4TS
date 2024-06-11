package org.apache.commons.lang3;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        Locale expectedLocale1 = new Locale("en", "");
        Locale actualLocale1 = LocaleUtils.toLocale("en");
        Assert.assertEquals(expectedLocale1, actualLocale1);

        Locale expectedLocale2 = new Locale("en", "GB");
        Locale actualLocale2 = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(expectedLocale2, actualLocale2);

        Locale expectedLocale3 = new Locale("en", "GB", "xxx");
        Locale actualLocale3 = LocaleUtils.toLocale("en_GB_xxx");
        Assert.assertEquals(expectedLocale3, actualLocale3);

        // Test invalid inputs
        try {
            LocaleUtils.toLocale(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("AbCd");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_Ab");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_Ab_Xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_Ab_Xx_Xx");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedLocaleList1 = Collections.singletonList(defaultLocale);
        List<Locale> actualLocaleList1 = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(expectedLocaleList1, actualLocaleList1);

        // Test input locale with no variant
        Locale inputLocale1 = new Locale("fr", "CA");
        List<Locale> expectedLocaleList2 = Arrays.asList(inputLocale1, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualLocaleList2 = LocaleUtils.localeLookupList(inputLocale1);
        Assert.assertEquals(expectedLocaleList2, actualLocaleList2);

        // Test input locale with country variant
        Locale inputLocale2 = new Locale("fr", "CA", "XXX");
        List<Locale> expectedLocaleList3 = Arrays.asList(inputLocale2, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> actualLocaleList3 = LocaleUtils.localeLookupList(inputLocale2, new Locale("en"));
        Assert.assertEquals(expectedLocaleList3, actualLocaleList3);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> expectedLocaleList1 = Collections.emptyList();
        List<Locale> actualLocaleList1 = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expectedLocaleList1, actualLocaleList1);

        // Test non-empty list
        List<Locale> expectedLocaleList2 = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
        List<Locale> actualLocaleList2 = LocaleUtils.availableLocaleList();
        Assert.assertFalse(expectedLocaleList2.equals(actualLocaleList2)); // Because of different instance
        for (Locale expectedLocale : expectedLocaleList2) {
            boolean found = false;
            for (Locale actualLocale : actualLocaleList2) {
                if (expectedLocale.equals(actualLocale)) {
                    found = true;
                    break;
                }
            }
            Assert.assertTrue(found);
        }
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        String countryCode = null;
        List<Locale> expectedLocaleList1 = Collections.emptyList();
        List<Locale> actualLocaleList1 = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expectedLocaleList1, actualLocaleList1);

        // Test input with available language for the country
        String countryCode1 = "CA";
        List<Locale> expectedLocaleList2 = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        List<Locale> actualLocaleList2 = LocaleUtils.languagesByCountry(countryCode1);
        Assert.assertEquals(expectedLocaleList2, actualLocaleList2);

        // Test input with no available language for the country
        String countryCode2 = "XX";
        List<Locale> expectedLocaleList3 = Collections.emptyList();
        List<Locale> actualLocaleList3 = LocaleUtils.languagesByCountry(countryCode2);
        Assert.assertEquals(expectedLocaleList3, actualLocaleList3);
    }

}