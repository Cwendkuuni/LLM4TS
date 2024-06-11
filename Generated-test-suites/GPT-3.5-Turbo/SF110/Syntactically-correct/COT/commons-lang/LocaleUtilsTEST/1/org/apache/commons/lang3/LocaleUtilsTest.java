import org.junit.*;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyString_ReturnEmptyLocale() {
        Locale expected = new Locale("", "");
        assertEquals(expected, LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidFormat_ThrowIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("#");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: #", e.getMessage());
        }
    }

    @Test
    public void testToLocale_LessThanTwoChars_ThrowIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("a");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: a", e.getMessage());
        }
    }

    @Test
    public void testToLocale_LanguageOnly_ValidLowerCase_ReturnLocale() {
        Locale expected = new Locale("en");
        assertEquals(expected, LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocale_LanguageOnly_InvalidLowerCase_ThrowIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("en1");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en1", e.getMessage());
        }
    }

    @Test
    public void testToLocale_LanguageAndCountry_ValidLowerCase_ReturnLocale() {
        Locale expected = new Locale("en", "US");
        assertEquals(expected, LocaleUtils.toLocale("en_US"));
    }

    @Test
    public void testToLocale_LanguageAndCountry_InvalidLowerCase_ThrowIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("en_US1");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_US1", e.getMessage());
        }
    }

    @Test
    public void testToLocale_LanguageCountryAndVariant_ValidLowerCase_ReturnLocale() {
        Locale expected = new Locale("en", "US", "variant");
        assertEquals(expected, LocaleUtils.toLocale("en_US_variant"));
    }

    @Test
    public void testToLocale_LanguageCountryAndVariant_InvalidLowerCase_ThrowIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("en_US_variant1");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_US_variant1", e.getMessage());
        }
    }

    @Test
    public void testToLocale_LanguageCountryAndEmptyVariant_ReturnLocale() {
        Locale expected = new Locale("en", "US");
        assertEquals(expected, LocaleUtils.toLocale("en_US_"));
    }

    @Test
    public void testToLocale_UnderscoreAtSecondIndexOfSizeThree_ThrowIllegalArgumentException() {
        try {
            LocaleUtils.toLocale("_EN");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: _EN", e.getMessage());
        }
    }

    @Test
    public void testLocaleLookupList_SingleLocale_ReturnListWithSingleLocale() {
        Locale locale = new Locale("en");
        List<Locale> expected = new ArrayList<>();
        expected.add(locale);
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
    }

    @Test
    public void testLocaleLookupList_LocaleWithVariant_ReturnListWithOriginalAndLanguageOnlyLocales() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en"));
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
    }

    @Test
    public void testLocaleLookupList_LocaleWithCountry_ReturnListWithOriginalAndLanguageOnlyLocales() {
        Locale locale = new Locale("en", "US");
        List<Locale> expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("en"));
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnEmptyList() {
        List<Locale> expected = new ArrayList<>();
        assertEquals(expected, LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_NullLocaleWithDefault_ReturnListWithDefaultLocale() {
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> expected = new ArrayList<>();
        expected.add(defaultLocale);
        assertEquals(expected, LocaleUtils.localeLookupList(null, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList_ReturnListWithAvailableLocales() {
        // Assuming that the Locale.getAvailableLocales() will return non-empty list
        assertNotNull(LocaleUtils.availableLocaleList());
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_ReturnSetWithAvailableLocales() {
        // Assuming that the Locale.getAvailableLocales() will return non-empty list
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnTrue() {
        Locale locale = new Locale("en");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_NonAvailableLocale_ReturnFalse() {
        Locale locale = new Locale("xxx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnListOfLanguages() {
        String countryCode = "US";
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("es", "US"));
        assertEquals(expected, LocaleUtils.languagesByCountry(countryCode));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnListOfCountries() {
        String languageCode = "en";
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", "GB"));
        assertEquals(expected, LocaleUtils.countriesByLanguage(languageCode));
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }

}