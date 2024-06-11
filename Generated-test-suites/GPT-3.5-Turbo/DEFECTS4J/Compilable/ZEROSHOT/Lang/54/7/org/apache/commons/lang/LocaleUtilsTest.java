package org.apache.commons.lang;
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_ValidInput_2() {
        Locale locale = LocaleUtils.toLocale("en");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
        assertEquals("", locale.getVariant());
    }

    @Test
    public void testToLocale_ValidInput_5() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("", locale.getVariant());
    }

    @Test
    public void testToLocale_ValidInput_MoreThan5() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("xxx", locale.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_1() {
        LocaleUtils.toLocale("e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_3() {
        LocaleUtils.toLocale("enga");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_4() {
        LocaleUtils.toLocale("en_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_6() {
        LocaleUtils.toLocale("en_GB_");
    }

    @Test
    public void testLocaleLookupList_StartFromLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocales = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            Locale.getDefault()
        );
        List<Locale> actualLocales = LocaleUtils.localeLookupList(locale);
        assertNotNull(actualLocales);
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testLocaleLookupList_StartFromLocale_DefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedLocales = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("en")
        );
        List<Locale> actualLocales = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertNotNull(actualLocales);
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedLocales = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actualLocales = LocaleUtils.availableLocaleList();
        assertNotNull(actualLocales);
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedLocales = new HashSet<>(LocaleUtils.availableLocaleList());
        Set<Locale> actualLocales = LocaleUtils.availableLocaleSet();
        assertNotNull(actualLocales);
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testIsAvailableLocale_ExistingLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_NonExistingLocale() {
        Locale locale = new Locale("xy", "Z1");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_ExistingCountryCode() {
        String countryCode = "CA";
        List<Locale> expectedLanguages = Arrays.asList(
            new Locale("fr"),
            new Locale("en")
        );
        List<Locale> actualLanguages = LocaleUtils.languagesByCountry(countryCode);
        assertNotNull(actualLanguages);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testLanguagesByCountry_NonExistingCountryCode() {
        String countryCode = "ZZ";
        List<Locale> expectedLanguages = Collections.emptyList();
        List<Locale> actualLanguages = LocaleUtils.languagesByCountry(countryCode);
        assertNotNull(actualLanguages);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testCountriesByLanguage_ExistingLanguageCode() {
        String languageCode = "fr";
        List<Locale> expectedCountries = Arrays.asList(
            new Locale("CA")
        );
        List<Locale> actualCountries = LocaleUtils.countriesByLanguage(languageCode);
        assertNotNull(actualCountries);
        assertEquals(expectedCountries, actualCountries);
    }

    @Test
    public void testCountriesByLanguage_NonExistingLanguageCode() {
        String languageCode = "xy";
        List<Locale> expectedCountries = Collections.emptyList();
        List<Locale> actualCountries = LocaleUtils.countriesByLanguage(languageCode);
        assertNotNull(actualCountries);
        assertEquals(expectedCountries, actualCountries);
    }

}