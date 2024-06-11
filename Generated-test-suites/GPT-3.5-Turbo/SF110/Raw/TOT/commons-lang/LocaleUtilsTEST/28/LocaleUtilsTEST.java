import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyString_ReturnsLocaleWithEmptyLanguageAndCountry() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowsIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("en#US");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en#US", e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormatWithLengthLessThan2_ThrowsIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("e");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: e", e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidFormatWithOneOccurrence_ReturnsLocaleWithLowerCaseLanguageAndUpperCaseCountry() {
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
    }

    @Test
    public void testToLocale_ValidFormatWithTwoOccurrences_ReturnsLocaleWithLowerCaseLanguageAndUpperCaseCountryAndVariant() {
        assertEquals(new Locale("en", "US", "variant"), LocaleUtils.toLocale("en_US_variant"));
    }

    @Test
    public void testToLocale_ValidFormatWithNoOccurrencesAndTwoLengthLowerCase_ReturnsLocaleWithLowerCaseLanguage() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocale_ValidFormatWithNoOccurrencesAndTwoLengthUpperCase_ReturnsLocaleWithUpperCaseLanguage() {
        assertEquals(new Locale("EN"), LocaleUtils.toLocale("EN"));
    }

    @Test
    public void testToLocale_ValidFormatWithNoOccurrencesAndThreeLengthLowerCase_ReturnsLocaleWithLowerCaseLanguage() {
        assertEquals(new Locale("eng"), LocaleUtils.toLocale("eng"));
    }

    @Test
    public void testToLocale_ValidFormatWithNoOccurrencesAndThreeLengthUpperCase_ReturnsLocaleWithUpperCaseLanguage() {
        assertEquals(new Locale("ENG"), LocaleUtils.toLocale("ENG"));
    }

    @Test
    public void testToLocale_ValidFormatWithTwoOccurrencesAndSecondEmpty_ReturnsLocaleWithLowerCaseLanguageAndEmptyCountry() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en__"));
    }

    @Test
    public void testToLocale_InvalidFormatWithUnderscoreAsFirstCharacterAndLengthLessThan3_ThrowsIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("_e");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: _e", e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormatWithUnderscoreAsFirstCharacterAndSecondAndThirdCharactersAreNotUpperCase_ThrowsIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("_en");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: _en", e.getMessage());
        }
    }

    @Test
    public void testLocaleLookupList_WithNullLocale_ReturnsEmptyList() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_WithLocaleAndDefaultLocale_ReturnsListWithLocaleAndDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en", "UK");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("en", ""), defaultLocale);
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList_ReturnsListOfAvailableLocales() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
        assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_ReturnsSetOfAvailableLocales() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocales);
        assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ReturnsTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testIsAvailableLocale_WithUnavailableLocale_ReturnsFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "MX")));
    }

    @Test
    public void testLanguagesByCountry_WithNullCountryCode_ReturnsEmptyList() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    // Need additional test cases for languagesByCountry method

    @Test
    public void testCountriesByLanguage_WithNullLanguageCode_ReturnsEmptyList() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }

    // Need additional test cases for countriesByLanguage method

}