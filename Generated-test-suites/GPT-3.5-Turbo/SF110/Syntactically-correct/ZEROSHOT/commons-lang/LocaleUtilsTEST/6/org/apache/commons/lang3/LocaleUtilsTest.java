import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Before
    public void setUp() {
        // Setup code
    }

    @Test
    public void testToLocale_nullString_returnNull() {
        String str = null;
        Locale result = LocaleUtils.toLocale(str);
        assertNull(result);
    }

    @Test
    public void testToLocale_emptyString_returnLocaleWithEmptyLanguageAndCountry() {
        String str = "";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat_throwIllegalArgumentException() {
        String str = "invalid#format";
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat_throwIllegalArgumentException2() {
        String str = "a";
        LocaleUtils.toLocale(str);
    }

    @Test
    public void testToLocale_validFormatLowerCase_returnLocaleWithLanguageOnly() {
        String str = "en";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals("en", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    // Add more test cases for LocaleUtils.toLocale()

    @Test
    public void testLocaleLookupList_singleLocale_returnListWithOneLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(1, result.size());
        assertEquals(locale, result.get(0));
    }

    // Add more test cases for LocaleUtils.localeLookupList()

    @Test
    public void testAvailableLocaleList_returnListOfAvailableLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        // Check if the list contains expected locales
    }

    // Add more test cases for LocaleUtils.availableLocaleList()

    @Test
    public void testAvailableLocaleSet_returnSetOfAvailableLocales() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        // Check if the set contains expected locales
    }

    // Add more test cases for LocaleUtils.availableLocaleSet()

    @Test
    public void testIsAvailableLocale_availableLocale_returnTrue() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_unavailableLocale_returnFalse() {
        Locale locale = new Locale("xyz", "XYZ");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertFalse(result);
    }

    // Add more test cases for LocaleUtils.isAvailableLocale()

    @Test
    public void testLanguagesByCountry_nullCountryCode_returnEmptyList() {
        String countryCode = null;
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(result.isEmpty());
    }

    // Add more test cases for LocaleUtils.languagesByCountry()

    @Test
    public void testCountriesByLanguage_nullLanguageCode_returnEmptyList() {
        String languageCode = null;
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(result.isEmpty());
    }

    // Add more test cases for LocaleUtils.countriesByLanguage()

}