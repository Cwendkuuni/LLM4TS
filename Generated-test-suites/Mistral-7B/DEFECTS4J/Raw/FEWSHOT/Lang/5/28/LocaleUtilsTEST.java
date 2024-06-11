package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_EmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLanguageCode() {
        String invalidLocaleString = "XXX";
        try {
            LocaleUtils.toLocale(invalidLocaleString);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidCountryCode() {
        String invalidLocaleString = "en_XX";
        try {
            LocaleUtils.toLocale(invalidLocaleString);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToLocale_InvalidFormat_Separator() {
        String invalidLocaleString = "en_GB:";
        try {
            LocaleUtils.toLocale(invalidLocaleString);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToLocale_InvalidFormat_ShortenedLocaleString() {
        String invalidLocaleString = "en_GB";
        Locale expectedLocale = new Locale("en", "GB");
        Locale locale = LocaleUtils.toLocale(invalidLocaleString);
        assertEquals("Unexpected locale returned for shortened locale string", expectedLocale, locale);
    }

    @Test
    public void testToLocale_ValidLocaleString() {
        String localeString = "en_GB_London";
        Locale expectedLocale = new Locale("en", "GB", "London");
        Locale locale = LocaleUtils.toLocale(localeString);
        assertEquals("Unexpected locale returned for valid locale string", expectedLocale, locale);
    }

    @Test
    public void testLocaleLookupList_EmptyList() {
        Locale locale = null;
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
        assertNotNull("localeLookupList should not be null", localeLookupList);
        assertTrue("localeLookupList should not be empty", localeLookupList.size() > 0);
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        Locale locale = null;
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
        assertNotNull("localeLookupList should not be null", localeLookupList);
        assertTrue("localeLookupList should not be empty", localeLookupList.size() > 0);
    }

    @Test
    public void testLocaleLookupList_SingleLocale() {
        Locale locale = new Locale("en", "GB");
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedLocaleLookupList = Arrays.asList(locale);
        assertEquals("Unexpected locale lookup list for a specific locale", expectedLocaleLookupList, localeLookupList);
    }

    @Test
    public void testLocaleLookupList_MultipleLocales() {
        Locale locale1 = new Locale("fr", "CA", "Vancouver");
        Locale locale2 = new Locale("fr", "CA");
        Locale locale3 = new Locale("fr");
        Locale defaultLocale = new Locale("en");

        List<Locale> localeLookupList = LocaleUtils.localeLookupList(locale1, defaultLocale);
        List<Locale> expectedLocaleLookupList = Arrays.asList(locale1, locale2, locale3, defaultLocale);
        assertEquals("Unexpected locale lookup list for a specific locale with a default locale",
                expectedLocaleLookupList, localeLookupList);
    }

    @Test
    public void testAvailableLocaleList_EmptyList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertTrue("expected an empty list", availableLocaleList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList_AvailableLocales() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> expectedAvailableLocaleList = Arrays.asList(Locale.GERMAN, Locale.FRENCH, Locale.ENGLISH);
        assertTrue("Unexpected available locales list", availableLocaleList.containsAll(expectedAvailableLocaleList));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        Locale locale = Locale.GERMAN;
        assertTrue("Expected true for a known locale", LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_UnknownLocale() {
        Locale locale = new Locale("xx", "yy");
        assertFalse("Expected false for an unknown locale", LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_EmptyCountryCode() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry(null);
        assertTrue("expected an empty list", languagesByCountry.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("US");
        List<Locale> expectedLanguagesByCountry = Arrays.asList(Locale.ENGLISH);
        assertEquals("Unexpected languages by country for a specific country code", expectedLanguagesByCountry, languagesByCountry);
    }

    @Test
    public void testCountriesByLanguage_EmptyLanguageCode() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage(null);
        assertTrue("expected an empty list", countriesByLanguage.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedCountriesByLanguage = Arrays.asList(Locale.US, Locale.GB, Locale.CA, Locale.IE, Locale.AU);
        assertEquals("Unexpected countries by language for a specific language code", expectedCountriesByLanguage, countriesByLanguage);
    }

}