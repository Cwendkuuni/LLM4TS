
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnNull() {
        Locale result = LocaleUtils.toLocale(null);
        assertNull(result);
    }

    @Test
    public void testToLocale_EmptyInput_ReturnLocaleWithEmptyLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("en#US");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en#US", e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormatWithLengthLessThan2_ThrowIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("e");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: e", e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidLanguageOnly_ReturnLocaleWithLanguage() {
        Locale result = LocaleUtils.toLocale("en");
        assertEquals("en", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test
    public void testToLocale_ValidLanguageAndCountry_ReturnLocaleWithLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("en_US");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }

    @Test
    public void testToLocale_ValidLanguageCountryAndVariant_ReturnLocaleWithLanguageCountryAndVariant() {
        Locale result = LocaleUtils.toLocale("en_US_variant");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
        assertEquals("variant", result.getVariant());
    }

    @Test
    public void testToLocale_InvalidFormatWithUnderscoreInSecondPosition_ThrowIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("_US");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: _US", e.getMessage());
        }
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_ValidLocale_ReturnListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("es", "ES");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(2, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(defaultLocale));
    }

    @Test
    public void testAvailableLocaleList_ReturnListWithAvailableLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertTrue(result.contains(Locale.ENGLISH));
        assertTrue(result.contains(Locale.FRENCH));
    }

    @Test
    public void testAvailableLocaleSet_ReturnSetWithAvailableLocales() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertTrue(result.contains(Locale.ENGLISH));
        assertTrue(result.contains(Locale.FRENCH));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale_NonAvailableLocale_ReturnFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("de")));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnListWithLanguages() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertTrue(result.contains(Locale.ENGLISH));
        assertTrue(result.contains(Locale.FRENCH));
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnListWithCountries() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertTrue(result.contains(Locale.US));
        assertTrue(result.contains(Locale.UK));
    }
}
