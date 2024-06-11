import static org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class LocaleUtilsTest {

    private LocaleUtils localeUtils;

    @Before
    public void setup() {
        localeUtils = new LocaleUtils();
    }

    @Test
    public void testToLocale_Null_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_ValidLocaleString_ReturnsLocaleObject() {
        Locale expectedLocale = new Locale("en", "GB");
        Locale actualLocale = LocaleUtils.toLocale("en_GB");
        assertEquals(expectedLocale, actualLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleString_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("invalid_locale");
    }

    @Test
    public void testLocaleLookupList_LocaleNotNull_DefaultLocaleNotNull_ReturnsListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedLocaleList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testLocaleLookupList_LocaleNotNull_DefaultLocaleNull_ReturnsListWithLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocaleList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(locale, null);
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testAvailableLocaleList_ReturnsListOfAvailableLocales() {
        List<Locale> expectedLocaleList = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actualLocaleList = LocaleUtils.availableLocaleList();
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testAvailableLocaleSet_ReturnsSetOfAvailableLocales() {
        Set<Locale> expectedLocaleSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> actualLocaleSet = LocaleUtils.availableLocaleSet();
        assertEquals(expectedLocaleSet, actualLocaleSet);
    }

    @Test
    public void testIsAvailableLocale_LocaleAvailable_ReturnsTrue() {
        Locale locale = new Locale("en", "GB");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_LocaleNotAvailable_ReturnsFalse() {
        Locale locale = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_CountryCodeNotNull_ReturnsListOfLanguages() {
        List<Locale> expectedLanguageList = Arrays.asList(new Locale("en"), new Locale("es"), new Locale("fr"));
        List<Locale> actualLanguageList = LocaleUtils.languagesByCountry("US");
        assertEquals(expectedLanguageList, actualLanguageList);
    }

    @Test
    public void testLanguagesByCountry_CountryCodeNull_ReturnsEmptyList() {
        List<Locale> expectedLanguageList = Collections.emptyList();
        List<Locale> actualLanguageList = LocaleUtils.languagesByCountry(null);
        assertEquals(expectedLanguageList, actualLanguageList);
    }

    @Test
    public void testCountriesByLanguage_LanguageCodeNotNull_ReturnsListOfCountries() {
        List<Locale> expectedCountriesList = Arrays.asList(new Locale("US"), new Locale("CA"));
        List<Locale> actualCountriesList = LocaleUtils.countriesByLanguage("en");
        assertEquals(expectedCountriesList, actualCountriesList);
    }

    @Test
    public void testCountriesByLanguage_LanguageCodeNull_ReturnsEmptyList() {
        List<Locale> expectedCountriesList = Collections.emptyList();
        List<Locale> actualCountriesList = LocaleUtils.countriesByLanguage(null);
        assertEquals(expectedCountriesList, actualCountriesList);
    }

}