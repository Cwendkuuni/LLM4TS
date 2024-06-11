package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_emptyString_returnsDefaultLocale() {
        Locale defaultLocale = new Locale("", "");
        assertEquals(defaultLocale, LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_invalidLocaleFormat_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            LocaleUtils.toLocale("invalid_locale");
        });
    }

    @Test
    public void testLocaleLookupList_localeNotNull_returnsListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en", "");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("en", ""));
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList_returnsList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
    }

    @Test
    public void testAvailableLocaleSet_returnsSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocales);
    }

    @Test
    public void testIsAvailableLocale_validLocale_returnsTrue() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_nullCountry_returnsEmptyList() {
        List<Locale> langs = LocaleUtils.languagesByCountry(null);
        assertEquals(0, langs.size());
    }

    @Test
    public void testCountriesByLanguage_nullLanguage_returnsEmptyList() {
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, countries.size());
    }

}