import org.junit.Assert;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case: converting a valid locale string to a Locale object
        Assert.assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));

        // Edge case: converting a valid locale string with country to a Locale object
        Assert.assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));

        // Error scenario: converting an invalid locale string should throw an exception
        try {
            LocaleUtils.toLocale("invalid");
            Assert.fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException ex) {
            // Exception expected, do nothing
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case: creating a locale lookup list starting from a specific locale
        Locale locale = new Locale("fr", "CA", "xxx");
        List expectedList = new ArrayList();
        expectedList.add(locale);
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr"));
        expectedList.add(Locale.getDefault());

        Assert.assertEquals(expectedList, LocaleUtils.localeLookupList(locale));

        // Edge case: creating a locale lookup list starting from a null locale
        List expectedEmptyList = new ArrayList();

        Assert.assertEquals(expectedEmptyList, LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        // Typical use case: getting the list of available locales
        List availableLocales = LocaleUtils.availableLocaleList();

        // The list should not be null
        Assert.assertNotNull(availableLocales);

        // Edge case: The list should not be empty
        Assert.assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Typical use case: getting the set of available locales
        Set availableLocalesSet = LocaleUtils.availableLocaleSet();

        // The set should not be null
        Assert.assertNotNull(availableLocalesSet);

        // Edge case: The set should not be empty
        Assert.assertFalse(availableLocalesSet.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        // Typical use case: checking if a specific locale is available
        Locale locale = new Locale("en", "US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Edge case: checking if a non-existent locale is available
        Locale invalidLocale = new Locale("xx", "XX");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case: getting the list of languages for a specific country
        List expectedLanguages = new ArrayList();
        expectedLanguages.add(new Locale("en", "US"));
        expectedLanguages.add(new Locale("en", "GB"));

        Assert.assertEquals(expectedLanguages, LocaleUtils.languagesByCountry("US"));

        // Edge case: getting the list of languages for a country with no languages
        List emptyLanguagesList = new ArrayList();

        Assert.assertEquals(emptyLanguagesList, LocaleUtils.languagesByCountry("XX"));
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case: getting the list of countries for a specific language
        List expectedCountries = new ArrayList();
        expectedCountries.add(new Locale("en", "US"));
        expectedCountries.add(new Locale("en", "GB"));

        Assert.assertEquals(expectedCountries, LocaleUtils.countriesByLanguage("en"));

        // Edge case: getting the list of countries for a language with no countries
        List emptyCountriesList = new ArrayList();

        Assert.assertEquals(emptyCountriesList, LocaleUtils.countriesByLanguage("xx"));
    }
}