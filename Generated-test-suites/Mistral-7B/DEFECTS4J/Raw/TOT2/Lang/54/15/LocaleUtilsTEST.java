import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidInput() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "XXX"), new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.FRANCE, Locale.ENGLISH);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        List<Locale> actual = LocaleUtils.localeLookupList(null, Locale.ENGLISH);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testAvailableLocaleList_ValidInput() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADitional_CHINESE, Locale.ENGLISH);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale_ValidInput() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.SIMPLIFIED_CHINESE));
    }

    @Test
    public void testIsAvailableLocale_InvalidInput() {
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH_US));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountry_ValidInput_EmptyCountry() {
        LocaleUtils.languagesByCountry(null);
    }

    @Test
    public void testLanguagesByCountry_ValidInput() {
        List<Locale> expected = Arrays.asList(new Locale("fr_BE"), new Locale("fr_CA"), new Locale("fr_CH"));
        List<Locale> actual = LocaleUtils.languagesByCountry("fr");
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage_ValidInput_EmptyLanguage() {
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidInput() {
        List<Locale> expected = Arrays.asList(new Locale("ZA"), new Locale("GB"), new Locale("US"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, actual);
    }
}