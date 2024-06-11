
import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;

import java.util.*;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString_ReturnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyString_ReturnDefaultLocale() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThrowException() {
        LocaleUtils.toLocale("#invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_LengthLessThan2_ThrowException() {
        LocaleUtils.toLocale("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_SingleCharacter_ThrowException() {
        LocaleUtils.toLocale("a");
    }

    @Test
    public void testToLocale_ValidFormat_ReturnLocale() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", "US", "variant"), LocaleUtils.toLocale("en_US_variant"));
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnEmptyList() {
        assertEquals(0, LocaleUtils.localeLookupList(null).size());
    }

    @Test
    public void testLocaleLookupList_ValidLocale_ReturnList() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        assertEquals(4, lookupList.size());
        assertTrue(lookupList.contains(locale));
        assertTrue(lookupList.contains(new Locale("en", "US")));
        assertTrue(lookupList.contains(new Locale("en", "")));
        assertTrue(lookupList.contains(Locale.getDefault()));
    }

    @Test
    public void testAvailableLocaleList_ReturnList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
        assertTrue(availableLocales.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet_ReturnSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocales);
        assertTrue(availableLocales.size() > 0);
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale_UnavailableLocale_ReturnFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz")));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
        assertEquals(0, LocaleUtils.languagesByCountry(null).size());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnList() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertTrue(languages.size() > 0);
        for(Locale language : languages){
            assertEquals("US", language.getCountry());
            assertTrue(language.getVariant().isEmpty());
        }
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
        assertEquals(0, LocaleUtils.countriesByLanguage(null).size());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnList() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertTrue(countries.size() > 0);
        for(Locale country : countries){
            assertEquals("en", country.getLanguage());
            assertFalse(country.getCountry().isEmpty());
            assertTrue(country.getVariant().isEmpty());
        }
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}
