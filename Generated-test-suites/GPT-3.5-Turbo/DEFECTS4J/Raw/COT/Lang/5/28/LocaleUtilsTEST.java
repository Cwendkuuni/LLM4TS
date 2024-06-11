import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_WithValidLocaleString_ReturnsLocaleObject() {
        String localeString = "en_GB";
        Locale result = LocaleUtils.toLocale(localeString);
        assertEquals(Locale.UK, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_WithInvalidLocaleString_ThrowsIllegalArgumentException() {
        String localeString = "invalid_locale";
        LocaleUtils.toLocale(localeString);
    }

    @Test
    public void testLocaleLookupList_WithLocale_ReturnsLocaleLookupList() {
        Locale locale = Locale.UK;
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(3, result.size());
        assertEquals(Locale.UK, result.get(0));
        assertEquals(Locale.ENGLISH, result.get(1));
        assertEquals(Locale.ROOT, result.get(2));
    }

    @Test
    public void testLocaleLookupList_WithLocaleAndDefaultLocale_ReturnsLocaleLookupList() {
        Locale locale = Locale.US;
        Locale defaultLocale = Locale.UK;
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, result.size());
        assertEquals(Locale.US, result.get(0));
        assertEquals(Locale.ENGLISH, result.get(1));
        assertEquals(Locale.ROOT, result.get(2));
        assertEquals(Locale.UK, result.get(3));
    }

    @Test
    public void testAvailableLocaleList_ReturnsAvailableLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertTrue(result.contains(Locale.US));
        assertTrue(result.contains(Locale.UK));
        assertTrue(result.contains(Locale.ENGLISH));
        assertTrue(result.contains(Locale.ROOT));
    }

    @Test
    public void testAvailableLocaleSet_ReturnsAvailableLocalesAsSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertTrue(result.contains(Locale.US));
        assertTrue(result.contains(Locale.UK));
        assertTrue(result.contains(Locale.ENGLISH));
        assertTrue(result.contains(Locale.ROOT));
    }

    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ReturnsTrue() {
        Locale locale = Locale.US;
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_WithUnavailableLocale_ReturnsFalse() {
        Locale locale = new Locale("xyz");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_WithValidCountryCode_ReturnsLanguagesByCountry() {
        String countryCode = "GB";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(result.contains(Locale.ENGLISH));
    }

    @Test
    public void testLanguagesByCountry_WithInvalidCountryCode_ReturnsEmptyList() {
        String countryCode = "XYZ";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_WithValidLanguageCode_ReturnsCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(result.contains(Locale.UK));
    }

    @Test
    public void testCountriesByLanguage_WithInvalidLanguageCode_ReturnsEmptyList() {
        String languageCode = "xyz";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(result.isEmpty());
    }

    // Additional test cases for potential edge cases and exception handling scenarios can be added here

}

