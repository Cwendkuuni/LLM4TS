import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale_NullString_ReturnsNull() {
        assertNull(LocaleUtils.toLocale(null));
    }
    
    @Test
    public void testToLocale_EmptyString_ReturnsLocaleWithEmptyValues() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_ThrowsException() {
        LocaleUtils.toLocale("#en");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_ThrowsException2() {
        LocaleUtils.toLocale("a");
    }
    
    @Test
    public void testToLocale_ValidLocaleFormatWithNoUnderscore_ReturnsLocaleWithLanguageOnly() {
        Locale result = LocaleUtils.toLocale("en");
        assertEquals("en", result.getLanguage());
        assertEquals("", result.getCountry());
    }
    
    @Test
    public void testToLocale_ValidLocaleFormatWithOneUnderscore_ReturnsLocaleWithLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("en_US");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }
    
    @Test
    public void testToLocale_ValidLocaleFormatWithTwoUnderscores_ReturnsLocaleWithLanguageCountryAndVariant() {
        Locale result = LocaleUtils.toLocale("en_US_POSIX");
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
        assertEquals("POSIX", result.getVariant());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormatWithTwoUnderscores_ThrowsException() {
        LocaleUtils.toLocale("en__POSIX");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormatWithTwoUnderscores_ThrowsException2() {
        LocaleUtils.toLocale("en_US_");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormatWithTwoUnderscores_ThrowsException3() {
        LocaleUtils.toLocale("_US_POSIX");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormatWithTwoUnderscores_ThrowsException4() {
        LocaleUtils.toLocale("__");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormatWithTwoUnderscores_ThrowsException5() {
        LocaleUtils.toLocale("en__");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormatWithTwoUnderscores_ThrowsException6() {
        LocaleUtils.toLocale("_");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormatWithTwoUnderscores_ThrowsException7() {
        LocaleUtils.toLocale("_US");
    }
    
    @Test
    public void testLocaleLookupList_SingleLocale_ReturnsListWithSingleLocale() {
        Locale locale = Locale.US;
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(1, result.size());
        assertEquals(locale, result.get(0));
    }
    
    @Test
    public void testLocaleLookupList_LocaleWithVariant_ReturnsThreeLocalesInOrder() {
        Locale locale = new Locale("en", "US", "POSIX");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("en", "US"), result.get(1));
        assertEquals(new Locale("en"), result.get(2));
    }
    
    @Test
    public void testLocaleLookupList_LocaleWithCountry_ReturnsThreeLocalesInOrder() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("en"), result.get(1));
        assertEquals(new Locale("", "US"), result.get(2));
    }
    
    @Test
    public void testLocaleLookupList_LocaleWithNoCountryOrVariant_ReturnsListWithSameLocaleTwice() {
        Locale locale = new Locale("en");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(2, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(locale, result.get(1));
    }
    
    @Test
    public void testAvailableLocaleList_ReturnsUnmodifiableList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        try {
            result.add(Locale.CANADA);
            fail("Expected UnsupportedOperationException was not thrown.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testAvailableLocaleSet_ReturnsUnmodifiableSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        try {
            result.add(Locale.CANADA);
            fail("Expected UnsupportedOperationException was not thrown.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testIsAvailableLocale_LocaleInAvailableList_ReturnsTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.UK));
    }
    
    @Test
    public void testIsAvailableLocale_LocaleNotInAvailableList_ReturnsFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "ES")));
    }
    
    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_CountryCodeWithoutMatchingLangauge_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry("AU");
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testLanguagesByCountry_CountryCodeWithMatchingLanguage_ReturnsListWithLocales() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertEquals(2, result.size());
        assertEquals(new Locale("", "US"), result.get(0));
        assertEquals(new Locale("en", "US"), result.get(1));
    }
    
    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_LanguageCodeWithoutMatchingCountry_ReturnsEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("fr");
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage_LanguageCodeWithMatchingCountry_ReturnsListWithLocales() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(3, result.size());
        assertEquals(new Locale("en", "US"), result.get(0));
        assertEquals(new Locale("en", "GB"), result.get(1));
        assertEquals(new Locale("en", "CA"), result.get(2));
    }
}