import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String[] testCases = new String[]{"en", "en_GB", "en_GB_US", "fr", "fr_FR", "fr_CA", "fr_CA_XX"};
        for (String testCase : testCases) {
            Locale expected = new Locale(testCase.substring(0, 2), testCase.substring(3, testCase.length()));
            Locale actual = LocaleUtils.toLocale(testCase);
            assertEquals(expected, actual);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_XXX"); // invalid length or invalid separator
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "XX");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr", ""), new Locale(""), new Locale(""));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupListNull() {
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = Arrays.asList(Locale.FRENCH, Locale.GERMAN, Locale.ENGLISH);
        for (Locale locale : availableLocales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        Locale notAvailableLocale = new Locale("unknown", "unknown");
        assertFalse(LocaleUtils.isAvailableLocale(notAvailableLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        List<Locale> expected = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountryNull() {
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> actual = LocaleUtils.countriesByLanguage("fr");
        List<Locale> expected = Arrays.asList(new Locale("FR", "CA"), new Locale("FR", "BE"));
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguageNull() {
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        assertTrue(actual.isEmpty());
    }
}