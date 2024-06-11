import org.junit.*;
import static org.junit.Assert.*;
import java.util.concurrent.*;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnsNull() {
        Locale result = LocaleUtils.toLocale(null);
        assertNull(result);
    }

    @Test
    public void testToLocale_EmptyString_ReturnsEmptyLocale() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatWithHash_ThrowsException() {
        LocaleUtils.toLocale("en#US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatLengthLessThan2_ThrowsException() {
        LocaleUtils.toLocale("e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatFirstCharNotUnderscore_ThrowsException() {
        LocaleUtils.toLocale("en-US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatOccurrences_ThrowsException() {
        LocaleUtils.toLocale("en_US_MORE");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatUpperCaseNotPresent_ThrowsException() {
        LocaleUtils.toLocale("_us");
    }

    @Test
    public void testToLocale_ValidFormatWithOneOccurrence_ReturnsLocale() {
        Locale result = LocaleUtils.toLocale("en_US");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatEmptyCountry_ThrowsException() {
        LocaleUtils.toLocale("en__MORE");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatEmptyLanguageAndNonEmptyVariant_ThrowsException() {
        LocaleUtils.toLocale("__MORE");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatLengthLessThan3WithUpperCase_ThrowsException() {
        LocaleUtils.toLocale("_Us");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormatLengthLessThan5WithUnderscore_ThrowsException() {
        LocaleUtils.toLocale("_us_");
    }

    @Test
    public void testToLocale_ValidFormatWithUpperCase_ReturnsLocale() {
        Locale result = LocaleUtils.toLocale("_US");
        assertEquals("", result.getLanguage());
        assertEquals("US", result.getCountry());
    }

    @Test
    public void testToLocale_ValidFormatWithCountryAndVariant_ReturnsLocale() {
        Locale result = LocaleUtils.toLocale("_US_MORE");
        assertEquals("", result.getLanguage());
        assertEquals("US", result.getCountry());
        assertEquals("MORE", result.getVariant());
    }

    @Test
    public void testLocaleLookupList_LocaleNotNull_ReturnsList() {
        Locale input = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(input);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.contains(input));
        assertTrue(result.contains(new Locale(input.getLanguage(), input.getCountry())));
        assertTrue(result.contains(new Locale(input.getLanguage(), "")));
        assertTrue(result.contains(Locale.getDefault()));
    }

    @Test
    public void testLocaleLookupList_LocaleNull_ReturnsListWithDefaultLocale() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertNotNull(result);
        assertEquals(Locale.getDefault(), result.get(0));
    }

    @Test
    public void testAvailableLocaleList_ReturnsList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet_ReturnsSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale_NotAvailableLocale_ReturnsFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(Locale.JAPAN));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsList() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

}