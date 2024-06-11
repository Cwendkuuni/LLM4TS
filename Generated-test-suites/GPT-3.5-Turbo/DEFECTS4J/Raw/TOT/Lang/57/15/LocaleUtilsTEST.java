
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidLocaleString() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals(Locale.UK, locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleString_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("invalid_locale");
    }

    @Test
    public void testToLocale_NullLocaleString() {
        Locale locale = LocaleUtils.toLocale(null);
        assertNull(locale);
    }

    @Test
    public void testLocaleLookupList_WithLocale_ReturnsExpectedList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_WithNullLocale_ReturnsDefaultLocale() {
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Collections.singletonList(defaultLocale);
        List<Locale> result = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList_ReturnsAvailableLocaleList() {
        List<Locale> availableLocales = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(availableLocales, result);
    }

    @Test
    public void testAvailableLocaleSet_ReturnsAvailableLocaleSet() {
        Set<Locale> availableLocales = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertEquals(availableLocales, result);
    }

    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ReturnsTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testIsAvailableLocale_WithNonAvailableLocale_ReturnsFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz")));
    }

    @Test
    public void testLanguagesByCountry_WithValidCountryCode_ReturnsLanguagesList() {
        List<Locale> expected = Arrays.asList(Locale.CANADA_FRENCH);
        List<Locale> result = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, result);
    }

    @Test
    public void testLanguagesByCountry_WithInvalidCountryCode_ReturnsEmptyList() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> result = LocaleUtils.languagesByCountry("ZZ");
        assertEquals(expected, result);
    }

    @Test
    public void testLanguagesByCountry_WithNullCountryCode_ReturnsEmptyList() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage_WithValidLanguageCode_ReturnsCountriesList() {
        List<Locale> expected = Arrays.asList(Locale.CANADA_FRENCH);
        List<Locale> result = LocaleUtils.countriesByLanguage("fr");
        assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage_WithInvalidLanguageCode_ReturnsEmptyList() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> result = LocaleUtils.countriesByLanguage("zz");
        assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage_WithNullLanguageCode_ReturnsEmptyList() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertEquals(expected, result);
    }

}
