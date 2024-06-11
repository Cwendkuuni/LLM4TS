import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Locale;

public class LocaleUtilsTest {

    @Before
    public void setUp() {
        // Set up any necessary configurations or dependencies before each test case
    }

    @After
    public void tearDown() {
        // Clean up any resources after each test case
    }

    // Test Case 1
    @Test
    public void testToLocale_validLocaleString_returnLocaleObject() {
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");

        Locale resultLocale = LocaleUtils.toLocale(localeString);

        assertEquals(expectedLocale, resultLocale);
    }

    // Test Case 2
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLocaleString_throwIllegalArgumentException() {
        String invalidLocaleString = "en_USA";

        LocaleUtils.toLocale(invalidLocaleString);
    }

    // Test Case 3
    @Test
    public void testLocaleLookupList_existingLocale_returnLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedLocaleLookupList = new ArrayList<>();
        expectedLocaleLookupList.add(new Locale("fr", "CA", "xxx"));
        expectedLocaleLookupList.add(new Locale("fr", "CA"));
        expectedLocaleLookupList.add(new Locale("fr"));
        expectedLocaleLookupList.add(new Locale("en"));

        List<Locale> resultLocaleLookupList = LocaleUtils.localeLookupList(locale, defaultLocale);

        assertEquals(expectedLocaleLookupList, resultLocaleLookupList);
    }

    // Test Case 4
    @Test
    public void testLocaleLookupList_nullLocale_returnEmptyLocaleLookupList() {
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedLocaleLookupList = new ArrayList<>();

        List<Locale> resultLocaleLookupList = LocaleUtils.localeLookupList(null, defaultLocale);

        assertEquals(expectedLocaleLookupList, resultLocaleLookupList);
    }

    // Test Case 5
    @Test
    public void testAvailableLocaleList_getAvailableLocales_returnAvailableLocaleList() {
        List<Locale> expectedAvailableLocaleList = new ArrayList<>(Arrays.asList(Locale.getAvailableLocales()));

        List<Locale> resultAvailableLocaleList = LocaleUtils.availableLocaleList();

        assertEquals(expectedAvailableLocaleList, resultAvailableLocaleList);
    }

    // Test Case 6
    @Test
    public void testAvailableLocaleSet_getAvailableLocales_returnAvailableLocaleSet() {
        Set<Locale> expectedAvailableLocaleSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));

        Set<Locale> resultAvailableLocaleSet = LocaleUtils.availableLocaleSet();

        assertEquals(expectedAvailableLocaleSet, resultAvailableLocaleSet);
    }

    // Test Case 7
    @Test
    public void testIsAvailableLocale_existingLocale_returnTrue() {
        Locale existingLocale = new Locale("en", "US");

        boolean result = LocaleUtils.isAvailableLocale(existingLocale);

        assertTrue(result);
    }

    // Test Case 8
    @Test
    public void testIsAvailableLocale_nonExistingLocale_returnFalse() {
        Locale nonExistingLocale = new Locale("xx", "XX");

        boolean result = LocaleUtils.isAvailableLocale(nonExistingLocale);

        assertFalse(result);
    }

    // Test Case 9
    @Test
    public void testLanguagesByCountry_existingCountryCode_returnListOfLanguages() {
        String countryCode = "US";
        List<Locale> expectedLanguages = new ArrayList<>();
        expectedLanguages.add(new Locale("en", "US"));

        List<Locale> resultLanguages = LocaleUtils.languagesByCountry(countryCode);

        assertEquals(expectedLanguages, resultLanguages);
    }

    // Test Case 10
    @Test
    public void testLanguagesByCountry_nonExistingCountryCode_returnEmptyList() {
        String nonExistingCountryCode = "XX";
        List<Locale> expectedLanguages = new ArrayList<>();

        List<Locale> resultLanguages = LocaleUtils.languagesByCountry(nonExistingCountryCode);

        assertEquals(expectedLanguages, resultLanguages);
    }

    // Test Case 11
    @Test
    public void testCountriesByLanguage_existingLanguageCode_returnListOfCountries() {
        String languageCode = "en";
        List<Locale> expectedCountries = new ArrayList<>();
        expectedCountries.add(new Locale("en", "US"));

        List<Locale> resultCountries = LocaleUtils.countriesByLanguage(languageCode);

        assertEquals(expectedCountries, resultCountries);
    }

    // Test Case 12
    @Test
    public void testCountriesByLanguage_nonExistingLanguageCode_returnEmptyList() {
        String nonExistingLanguageCode = "xx";
        List<Locale> expectedCountries = new ArrayList<>();

        List<Locale> resultCountries = LocaleUtils.countriesByLanguage(nonExistingLanguageCode);

        assertEquals(expectedCountries, resultCountries);
    }

}