import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocaleUtilsTest {
    @Test
    public void testToLocale_NullInput_ReturnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyString_ReturnLocaleWithEmptyLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("");
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("en#US");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en#US", e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormatWithLengthLessThan2_ThrowIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("e");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: e", e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormatWithLowerCaseLanguageAndLength2_ReturnLocaleWithValidLanguage() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
    }

    @Test
    public void testToLocale_InvalidFormatWithLowerCaseLanguageAndLength3_ReturnLocaleWithValidLanguage() {
        Locale locale = LocaleUtils.toLocale("eng");
        assertEquals("eng", locale.getLanguage());
    }

    @Test
    public void testToLocale_InvalidFormatWithLowerCaseLanguageAndUpperCaseCountry_ReturnLocaleWithValidLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testToLocale_InvalidFormatWithLowerCaseLanguageAndUpperCaseCountryAndNonEmptyVariant_ReturnLocaleWithValidLanguageAndCountry() {
        Locale locale = LocaleUtils.toLocale("en_US_variant");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("variant", locale.getVariant());
    }

    @Test
    public void testToLocale_InvalidFormatWithLowerCaseLanguageAndEmptyCountryAndNonEmptyVariant_ReturnLocaleWithValidLanguageAndEmptyCountry() {
        Locale locale = LocaleUtils.toLocale("en__variant");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
        assertEquals("variant", locale.getVariant());
    }

    @Test
    public void testToLocale_InvalidFormatWithUnderscoreAndLengthLessThan3_ThrowIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("_e");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: _e", e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormatWithUnderscoreAndUpperCaseCharacters_ReturnLocaleWithValidCountry() {
        Locale locale = LocaleUtils.toLocale("_US");
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnEmptyList() {
        List<Locale> locales = LocaleUtils.localeLookupList(null);
        assertTrue(locales.isEmpty());
    }

    @Test
    public void testLocaleLookupList_NonNullLocale_ReturnListWithLocaleAndEmptyCountryLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> locales = LocaleUtils.localeLookupList(locale);
        assertEquals(2, locales.size());
        assertTrue(locales.contains(locale));
        assertTrue(locales.contains(new Locale(locale.getLanguage(), locale.getCountry())));
    }

    @Test
    public void testLocaleLookupList_NonNullLocaleWithNonEmptyVariant_ReturnListWithLocaleAndEmptyCountryLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US", "variant");
        Locale defaultLocale = new Locale("fr", "CA");
        List<Locale> locales = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(3, locales.size());
        assertTrue(locales.contains(locale));
        assertTrue(locales.contains(new Locale(locale.getLanguage(), locale.getCountry())));
        assertTrue(locales.contains(defaultLocale));
    }

    @Test
    public void testAvailableLocaleList_ReturnNonEmptyList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertFalse(locales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_ReturnNonEmptySet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertFalse(locales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
    }

    @Test
    public void testIsAvailableLocale_NotAvailableLocale_ReturnFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "YY")));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
        List<Locale> locales = LocaleUtils.languagesByCountry(null);
        assertTrue(locales.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnListWithMatchingLocales() {
        List<Locale> locales = LocaleUtils.languagesByCountry("US");
        assertEquals(1, locales.size());
        assertTrue(locales.contains(new Locale("en", "US")));
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
        List<Locale> locales = LocaleUtils.countriesByLanguage(null);
        assertTrue(locales.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnListWithMatchingLocales() {
        List<Locale> locales = LocaleUtils.countriesByLanguage("en");
        assertEquals(1, locales.size());
        assertTrue(locales.contains(new Locale("en", "US")));
    }

    // Additional Test Cases for Exception Handling

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithUnderscoreAndUpperCaseCharactersAndInvalidLength_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_USD");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithUnderscoreAndLowerCaseCharacters_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_us");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithUnderscoreAndEmptyCountryLength_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithUnderscoreAndEmptyLanguageLength_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("__GB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithEmptyLanguageAndNonEmptyCountry_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("_GB");
    }
    
    //
}