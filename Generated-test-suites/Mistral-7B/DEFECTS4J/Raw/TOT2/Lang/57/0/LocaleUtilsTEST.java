import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidInput() {
        String input1 = "en";
        Locale expected1 = new Locale("en", "");
        Locale actual1 = LocaleUtils.toLocale(input1);
        assertEquals(expected1, actual1);

        String input2 = "en_GB";
        Locale expected2 = new Locale("en", "GB");
        Locale actual2 = LocaleUtils.toLocale(input2);
        assertEquals(expected2, actual2);

        String input3 = "en_GB_XXX";
        Locale expected3 = new Locale("en", "GB", "XXX");
        Locale actual3 = LocaleUtils.toLocale(input3);
        assertEquals(expected3, actual3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_WrongLength() {
        String input = "en_GB_XXX_YYY";
        LocaleUtils.toLocale(input); // throws IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_WrongCharacters() {
        String input = "en_GB_XXX_YYY_%$^&";
        LocaleUtils.toLocale(input); // throws IllegalArgumentException
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        String baseLocale = "fr_CA";
        Locale defaultLocale = new Locale("en");

        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA", "XXX"),
                new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));

        List<Locale> actualList = LocaleUtils.localeLookupList(new Locale(baseLocale), defaultLocale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupList_ValidInput_EmptyInput() {
        Locale defaultLocale = new Locale("en");
        List<Locale> actualList = LocaleUtils.localeLookupList(null, defaultLocale);
        assertTrue(actualList.isEmpty());
    }

    @Test
    public void testLocaleLookupList_ValidInput_NullDefaultLocale() {
        String baseLocale = "fr_CA";
        List<Locale> actualList = LocaleUtils.localeLookupList(new Locale(baseLocale), null);
        assertTrue(actualList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList_ValidInput() {
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> actualList = Collections.list(LocaleUtils.availableLocaleSet().toArray());
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testIsAvailableLocale_ValidInput() {
        Locale locale = new Locale("fr_CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        Locale locale2 = new Locale("fr_CA_XXX");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testLanguagesByCountry_ValidInput() {
        String countryCode = "CA";

        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLanguagesByCountry_InvalidInput() {
        String countryCode = "ZZ";
        List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(actualList.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountry_NullInput() {
        String countryCode = null;
        LocaleUtils.languagesByCountry(countryCode); // throws IllegalArgumentException
    }

    @Test
    public void testCountriesByLanguage_ValidInput() {
        String languageCode = "fr";

        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"));
        List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage_InvalidInput() {
        String languageCode = "ZZ";
        List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(actualList.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountriesByLanguage_NullInput() {
        String languageCode = null;
        LocaleUtils.countriesByLanguage(languageCode); // throws IllegalArgumentException
    }
}