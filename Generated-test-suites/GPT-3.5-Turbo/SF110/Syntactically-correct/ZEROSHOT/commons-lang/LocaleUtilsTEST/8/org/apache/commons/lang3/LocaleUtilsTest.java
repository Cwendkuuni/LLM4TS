import org.junit.Test;
import java.util.Locale;
import org.apache.commons.lang3.LocaleUtils;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_nullString_returnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_emptyString_returnLocaleWithEmptyLanguageAndCountry() {
        Locale expectedLocale = new Locale("", "");
        assertEquals(expectedLocale, LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat_throwIllegalArgumentException() {
        LocaleUtils.toLocale("#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat_throwIllegalArgumentException2() {
        LocaleUtils.toLocale("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat_throwIllegalArgumentException3() {
        LocaleUtils.toLocale("__");
    }

    // Add more test cases for toLocale() method

    @Test
    public void testLocaleLookupList_nullLocale_returnEmptyList() {
        assertTrue(LocaleUtils.localeLookupList(null).isEmpty());
    }

    // Add more test cases for localeLookupList() method

    @Test
    public void testAvailableLocaleList_returnUnmodifiableList() {
        assertNotNull(LocaleUtils.availableLocaleList());
        assertTrue(LocaleUtils.availableLocaleList() instanceof java.util.Collections.UnmodifiableList);
    }

    // Add more test cases for availableLocaleList() method

    @Test
    public void testAvailableLocaleSet_returnUnmodifiableSet() {
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertTrue(LocaleUtils.availableLocaleSet() instanceof java.util.Collections.UnmodifiableSet);
    }

    // Add more test cases for availableLocaleSet() method

    @Test
    public void testIsAvailableLocale_availableLocale_returnTrue() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    // Add more test cases for isAvailableLocale() method

    @Test
    public void testLanguagesByCountry_nullCountryCode_returnEmptyList() {
        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
    }

    // Add more test cases for languagesByCountry() method

    @Test
    public void testCountriesByLanguage_nullLanguageCode_returnEmptyList() {
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }

    // Add more test cases for countriesByLanguage() method

}