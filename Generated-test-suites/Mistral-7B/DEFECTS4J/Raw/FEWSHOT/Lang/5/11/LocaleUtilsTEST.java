package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRANCE, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.GERMANY, LocaleUtils.toLocale("de_DE"));
        assertEquals(Locale.CHINA, LocaleUtils.toLocale("zh_CN"));

        // Test invalid inputs
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("en_UK")); // missing country code
        assertNull(LocaleUtils.toLocale("en_GB_")); // missing language code
        assertNull(LocaleUtils.toLocale("en_GB__")); // extra underscore
        assertNull(LocaleUtils.toLocale("en_GB_XX")); // invalid variant
        assertNull(LocaleUtils.toLocale("en_GB_XXX")); // invalid variant length
        assertNull(LocaleUtils.toLocale("en_GB_XXX_XXX")); // invalid variant length
        assertNull(LocaleUtils.toLocale("en_GB_XXX_XXX_XXX")); // invalid variant length

        // Test empty variant
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_"));

        // Test uppercase language code
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("EN"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("EN_US"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("FR"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("DE"));
        assertEquals(Locale.CHINESE, LocaleUtils.toLocale("ZH"));

        // Test country code with single letter
        assertEquals(Locale.ITALY, LocaleUtils.toLocale("it"));
        assertEquals(Locale.JAPAN, LocaleUtils.toLocale("ja"));

        // Test country code with multiple letters
        assertEquals(Locale.UK, LocaleUtils.toLocale("GB"));
        assertEquals(Locale.CANADA, LocaleUtils.toLocale("CA"));

        // Test separator
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_VARIANT"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US_VARIANT_VARIANT"));

        // Test empty country code
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_"));

        // Test country code with empty string
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = Locale.FRANCE;

        // Test empty list
        List<Locale> list1 = LocaleUtils.localeLookupList(null);
        assertEquals(0, list1.size());

        // Test single locale
        List<Locale> list2 = LocaleUtils.localeLookupList(locale);
        assertEquals(1, list2.size());
        assertSame(locale, list2.get(0));

        // Test multiple locales
        List<Locale> list3 = LocaleUtils.localeLookupList(Locale.ENGLISH, locale);
        assertEquals(2, list3.size());
        assertEquals(Locale.ENGLISH, list3.get(0));
        assertSame(locale, list3.get(1));

        // Test default locale
        List<Locale> list4 = LocaleUtils.localeLookupList(locale, Locale.US);
        assertEquals(3, list4.size());
        assertEquals(locale, list4.get(0));
        assertEquals(locale, list4.get(1));
        assertEquals(Locale.US, list4.get(2));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        int size = list.size();

        // Test empty list
        List<Locale> emptyList = Collections.emptyList();
        assertEquals(emptyList, list);

        // Test non-empty list
        assertFalse(list.isEmpty());
        assertTrue(size > 0);

        // Test order
        Locale locale1 = new Locale("en", "US");
        Locale locale2 = new Locale("fr", "FR");
        List<Locale> expectedList = Arrays.asList(locale1, locale2);
        for (int i = 0; i < size; i++) {
            Locale locale = list.get(i);
            assertTrue(expectedList.contains(locale));
            assertEquals(locale, list.get(i));
        }
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";

        // Test empty list
        List<Locale> list1 = LocaleUtils.languagesByCountry(null);
        assertEquals(0, list1.size());

        // Test valid country code
        List<Locale> list2 = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(1, list2.size());
        assertEquals(new Locale("en", countryCode), list2.get(0));

        // Test invalid country code
        assertNull(LocaleUtils.languagesByCountry(""));
        assertNull(LocaleUtils.languagesByCountry("unknown"));

        // Test multiple valid country codes
        List<Locale> list3 = LocaleUtils.languagesByCountry(Arrays.asList("US", countryCode).toArray(new String[0]));
        assertEquals(2, list3.size());
        assertEquals(new Locale("en", "US"), list3.get(0));
        assertEquals(new Locale("en", countryCode), list3.get(1));
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";

        // Test empty list
        List<Locale> list1 = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, list1.size());

        // Test valid language code
        List<Locale> list2 = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(1, list2.size());
        assertEquals(new Locale(languageCode, "US"), list2.get(0));

        // Test invalid language code
        assertNull(LocaleUtils.countriesByLanguage(""));
        assertNull(LocaleUtils.countriesByLanguage("unknown"));

        // Test multiple valid language codes
        List<Locale> list3 = LocaleUtils.countriesByLanguage(Arrays.asList(languageCode, "fr").toArray(new String[0]));
        assertEquals(2, list3.size());
        assertEquals(new Locale(languageCode, "US"), list3.get(0));
        assertEquals(new Locale("fr", "CA"), list3.get(1));
    }

}