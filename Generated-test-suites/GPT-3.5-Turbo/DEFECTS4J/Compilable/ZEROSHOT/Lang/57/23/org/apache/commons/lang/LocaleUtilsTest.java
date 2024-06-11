package org.apache.commons.lang;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class LocaleUtilsTest {

    private LocaleUtils localeUtils;

    @Before
    public void setUp() {
        localeUtils = new LocaleUtils();
    }

    @Test
    public void testToLocale_validLocaleString() {
        String localeString = "en_GB";
        Locale expectedLocale = new Locale("en", "GB");
        Locale actualLocale = localeUtils.toLocale(localeString);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocale_invalidLocaleString() {
        String localeString = "invalid";
        try {
            Locale actualLocale = localeUtils.toLocale(localeString);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + localeString, e.getMessage());
        }
    }

    @Test
    public void testLocaleLookupList_withLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"));

        List<Locale> actualList = localeUtils.localeLookupList(locale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupList_withLocaleAndDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("en"));

        List<Locale> actualList = localeUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = new ArrayList<>(
            Arrays.asList(Locale.getAvailableLocales()));

        List<Locale> actualList = localeUtils.availableLocaleList();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = new HashSet<>(
            Arrays.asList(Locale.getAvailableLocales()));

        Set<Locale> actualSet = localeUtils.availableLocaleSet();
        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testIsAvailableLocale_withAvailableLocale() {
        Locale locale = Locale.ENGLISH;
        boolean isAvailable = localeUtils.isAvailableLocale(locale);
        assertTrue(isAvailable);
    }

    @Test
    public void testIsAvailableLocale_withUnavailableLocale() {
        Locale locale = new Locale("xyz");
        boolean isAvailable = localeUtils.isAvailableLocale(locale);
        assertFalse(isAvailable);
    }

    @Test
    public void testLanguagesByCountry_withValidCountryCode() {
        String countryCode = "CA";
        List<Locale> expectedList = new ArrayList<>(Arrays.asList(
            new Locale("fr", "CA"),
            new Locale("en", "CA")));

        List<Locale> actualList = localeUtils.languagesByCountry(countryCode);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLanguagesByCountry_withInvalidCountryCode() {
        String countryCode = "INVALID";
        List<Locale> expectedList = Collections.emptyList();

        List<Locale> actualList = localeUtils.languagesByCountry(countryCode);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage_withValidLanguageCode() {
        String languageCode = "fr";
        List<Locale> expectedList = new ArrayList<>(Arrays.asList(
            new Locale("fr", "CA"),
            new Locale("fr", "CH"),
            new Locale("fr", "FR")));

        List<Locale> actualList = localeUtils.countriesByLanguage(languageCode);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage_withInvalidLanguageCode() {
        String languageCode = "INVALID";
        List<Locale> expectedList = Collections.emptyList();

        List<Locale> actualList = localeUtils.countriesByLanguage(languageCode);
        assertEquals(expectedList, actualList);
    }
}