import org.junit.*;
import java.util.concurrent.*;
import java.util.*;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_withNullString_returnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_withEmptyString_returnsEmptyLocale() {
        Locale locale = LocaleUtils.toLocale("");
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_withInvalidFormat_throwsIllegalArgumentException() {
        LocaleUtils.toLocale("#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_withInvalidFormat2_throwsIllegalArgumentException() {
        LocaleUtils.toLocale("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_withInvalidFormat3_throwsIllegalArgumentException() {
        LocaleUtils.toLocale("_");
    }

    // Add more tests for toLocale() method

    @Test
    public void testLocaleLookupList_withSingleLocale_returnsListWithSingleLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> expectedList = Arrays.asList(locale);
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, list);
    }

    // Add more tests for localeLookupList() method

    @Test
    public void testAvailableLocaleList_returnsListOfAvailableLocales() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertTrue(list.size() > 0);
    }

    // Add more tests for availableLocaleList() method

    @Test
    public void testAvailableLocaleSet_returnsSetOfAvailableLocales() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        assertTrue(set.size() > 0);
    }

    // Add more tests for availableLocaleSet() method

    @Test
    public void testIsAvailableLocale_withAvailableLocale_returnsTrue() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    // Add more tests for isAvailableLocale() method

    @Test
    public void testLanguagesByCountry_withNullCountryCode_returnsEmptyList() {
        List<Locale> list = LocaleUtils.languagesByCountry(null);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    // Add more tests for languagesByCountry() method

    @Test
    public void testCountriesByLanguage_withNullLanguageCode_returnsEmptyList() {
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    // Add more tests for countriesByLanguage() method

}