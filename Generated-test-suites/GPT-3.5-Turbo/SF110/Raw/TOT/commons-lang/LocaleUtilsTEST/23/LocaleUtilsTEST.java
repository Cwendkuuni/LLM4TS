import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_nullString_returnNullLocale() {
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_emptyString_returnEmptyLocale() {
        Locale expectedLocale = new Locale("", "");
        Assert.assertEquals(expectedLocale, LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat_throwIllegalArgumentException() {
        LocaleUtils.toLocale("#invalid_format");
    }

    // Add more test cases for toLocale() method

    @Test
    public void testLocaleLookupList_localeAndDefaultLocaleProvided_returnLookupList() {
        Locale locale = new Locale("en");
        Locale defaultLocale = new Locale("fr");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("en", ""), defaultLocale);
        Assert.assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    // Add more test cases for localeLookupList() method

    @Test
    public void testAvailableLocaleList_returnListOfAvailableLocales() {
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expectedList, LocaleUtils.availableLocaleList());
    }

    // Add more test cases for availableLocaleList() method

    @Test
    public void testAvailableLocaleSet_returnSetOfAvailableLocales() {
        Set<Locale> expectedSet = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Assert.assertEquals(expectedSet, LocaleUtils.availableLocaleSet());
    }

    // Add more test cases for availableLocaleSet() method

    @Test
    public void testIsAvailableLocale_availableLocale_returnTrue() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testIsAvailableLocale_unavailableLocale_returnFalse() {
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("xy")));
    }

    // Add more test cases for isAvailableLocale() method

    @Test
    public void testLanguagesByCountry_existingCountryCode_returnListOfLanguages() {
        List<Locale> expectedList = Arrays.asList(Locale.ENGLISH, Locale.FRENCH);
        Assert.assertEquals(expectedList, LocaleUtils.languagesByCountry("US"));
    }

    @Test
    public void testLanguagesByCountry_nonExistingCountryCode_returnEmptyList() {
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("XY"));
    }

    // Add more test cases for languagesByCountry() method

    @Test
    public void testCountriesByLanguage_existingLanguageCode_returnListOfCountries() {
        List<Locale> expectedList = Arrays.asList(Locale.US, Locale.CANADA);
        Assert.assertEquals(expectedList, LocaleUtils.countriesByLanguage("en"));
    }

    @Test
    public void testCountriesByLanguage_nonExistingLanguageCode_returnEmptyList() {
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("xy"));
    }

    // Add more test cases for countriesByLanguage() method

}