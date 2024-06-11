import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String[] testCases = new String[]{"en", "en_GB", "en_GB_XXX"};
        for (String s : testCases) {
            Locale locale = LocaleUtils.toLocale(s);
            String expectedLanguage = s.substring(0, 2);
            String expectedCountry = s.length() > 2 ? s.substring(2, 5) : "";
            assertEquals(expectedLanguage, locale.getLanguage());
            assertEquals(expectedCountry, locale.getCountry());
        }
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_XXX_invalid");
    }

    @Test
    public void testLocaleLookupList() {
        Locale defaultLocale = new Locale("en", "US");
        Locale locale = new Locale("fr", "CA", "XXX");
        List<Locale> expectedResult = Arrays.asList(
                new Locale("fr", "CA", "XXX"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                defaultLocale
        );
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedResult, result);
        result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expectedResult, result);
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertTrue(availableLocales.containsAll(LocaleUtils.cAvailableLocaleList));
        assertEquals(LocaleUtils.cAvailableLocaleList.size(), availableLocales.size());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertTrue(availableLocales.containsAll(LocaleUtils.cAvailableLocaleList));
        assertEquals(LocaleUtils.cAvailableLocaleList.size(), availableLocales.size());
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = Arrays.asList(Locale.CHINESE, Locale.FRENCH, Locale.GERMAN);
        for (Locale locale : availableLocales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> frenchLocales = LocaleUtils.languagesByCountry("CA");
        assertEquals(2, frenchLocales.size());
        assertEquals(Locale.FRENCH, frenchLocales.get(0));
        assertEquals(Locale.FRENCH_CANADA, frenchLocales.get(1));
        List<Locale> englishLocales = LocaleUtils.languagesByCountry("US");
        assertEquals(1, englishLocales.size());
        assertEquals(Locale.ENGLISH_US, englishLocales.get(0));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> englishCanadaLocales = LocaleUtils.countriesByLanguage("en");
        assertEquals(1, englishCanadaLocales.size());
        assertEquals(Locale.CANADA, englishCanadaLocales.get(0));
        List<Locale> frenchFranceLocales = LocaleUtils.countriesByLanguage("fr");
        assertEquals(1, frenchFranceLocales.size());
        assertEquals(Locale.FRANCE, frenchFranceLocales.get(0));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}