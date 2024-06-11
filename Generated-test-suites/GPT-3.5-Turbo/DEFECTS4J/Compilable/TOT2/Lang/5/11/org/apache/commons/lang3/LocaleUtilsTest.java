package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_nullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_validInput() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("INVALID_FORMAT");
    }

    @Test
    public void testLocaleLookupList() {
        Locale startLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = List.of(startLocale, new Locale("fr", "CA"), new Locale("fr"), Locale.getDefault());
        assertEquals(expectedList, LocaleUtils.localeLookupList(startLocale));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale startLocale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = List.of(startLocale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        assertEquals(expectedList, LocaleUtils.localeLookupList(startLocale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        assertTrue(LocaleUtils.availableLocaleList().contains(Locale.US));
    }

    @Test
    public void testAvailableLocaleSet() {
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("FR");
        assertTrue(languages.contains(Locale.FRENCH));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertTrue(countries.contains(Locale.FRANCE));
    }
}