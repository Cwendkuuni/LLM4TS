import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidInput() {
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale("en_US");
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidInput_LanguageCodeCase() {
        String invalidLocaleString = "En_GB";
        assertFalse("Expected IllegalArgumentException for invalid locale format: " + invalidLocaleString,
                LocaleUtils.toLocale(invalidLocaleString) instanceof Locale);
    }

    @Test
    public void testToLocale_InvalidInput_Separator() {
        String invalidLocaleString = "en_GB_xxx";
        assertFalse("Expected IllegalArgumentException for invalid locale format: " + invalidLocaleString,
                LocaleUtils.toLocale(invalidLocaleString) instanceof Locale);
    }

    @Test
    public void testLocaleLookupList_BaseCase() {
        Locale baseLocale = new Locale("fr", "CA", "abc");
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA", "abc"),
                new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale, new Locale("en"));
        assertEquals(expectedList, actualLocaleList);
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(null);
        assertEquals(expectedList, actualLocaleList);
    }

    @Test
    public void testLocaleLookupList_NullDefaultLocale() {
        Locale baseLocale = new Locale("fr", "CA", "abc");
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA", "abc"),
                new Locale("fr", "CA"), new Locale("fr"), null);
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale);
        List<Locale> actualLocaleListWithDefaultLocale = LocaleUtils.localeLookupList(baseLocale, null);
        assertEquals(expectedList, actualLocaleList);
        assertEquals(expectedList, actualLocaleListWithDefaultLocale);
    }

    @Test
    public void testIsAvailableLocale_BaseCase() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidInput() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry_BaseCase() {
        String countryCode = "CA";
        List<Locale> expectedLanguages = Arrays.asList(new Locale("fr", "CA"),
                new Locale("en", "CA"));
        List<Locale> actualLanguages = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testLanguagesByCountry_EmptyInput() {
        String emptyCountryCode = null;
        List<Locale> emptyList = Collections.emptyList();
        List<Locale> actualLanguages = LocaleUtils.languagesByCountry(emptyCountryCode);
        assertEquals(emptyList, actualLanguages);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountry_InvalidInput() {
        String invalidCountryCode = "";
        List<Locale> actualLanguages = LocaleUtils.languagesByCountry(invalidCountryCode);
        fail("Expected IllegalArgumentException: " +
                "Invalid country code: " + invalidCountryCode);
    }

    @Test
    public void testCountriesByLanguage_BaseCase() {
        String languageCode = "fr";
        List<Locale> expectedCountries = Arrays.asList(new Locale("fr", "CA"),
                new Locale("fr", "FR"));
        List<Locale> actualCountries = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedCountries, actualCountries);
    }

    @Test
    public void testCountriesByLanguage_EmptyInput() {
        String emptyLanguageCode = null;
        List<Locale> emptyList = Collections.emptyList();
        List<Locale> actualCountries = LocaleUtils.countriesByLanguage(emptyLanguageCode);
        assertEquals(emptyList, actualCountries);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountriesByLanguage_InvalidInput() {
        String invalidLanguageCode = "";
        List<Locale> actualCountries = LocaleUtils.countriesByLanguage(invalidLanguageCode);
        fail("Expected IllegalArgumentException: " +
                "Invalid language code: " + invalidLanguageCode);
    }
}