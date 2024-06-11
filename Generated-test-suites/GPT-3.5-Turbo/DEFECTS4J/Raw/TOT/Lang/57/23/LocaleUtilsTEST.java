import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_WithValidLanguageCode_ReturnsLocaleObject() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale);
    }

    @Test
    public void testToLocale_WithValidLanguageAndCountryCode_ReturnsLocaleObject() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale);
    }

    @Test
    public void testToLocale_WithValidLanguageCountryAndVariantCode_ReturnsLocaleObject() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_WithInvalidFormat_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("abc");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_WithInvalidLanguageCode_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("123_GB");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_WithInvalidCountryCode_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("ab_123");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_WithInvalidSeparator_ThrowsIllegalArgumentException() {
        LocaleUtils.toLocale("en-GB");
    }

    @Test
    public void testLocaleLookupList_WithLocale_ReturnsUnmodifiableList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));

        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, result);
        assertNotSame(expected, result);
        assertTrue(result instanceof UnmodifiableList);
    }

    @Test
    public void testLocaleLookupList_WithLocaleAndDefaultLocale_ReturnsUnmodifiableList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        expected.add(defaultLocale);

        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, result);
        assertNotSame(expected, result);
        assertTrue(result instanceof UnmodifiableList);
    }

    @Test
    public void testAvailableLocaleList_ReturnsUnmodifiableList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(Locale.getAvailableLocales().length, result.size());
        assertTrue(result instanceof UnmodifiableList);
    }

    @Test
    public void testAvailableLocaleSet_ReturnsUnmodifiableSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertEquals(Locale.getAvailableLocales().length, result.size());
        assertTrue(result instanceof UnmodifiableSet);
    }

    @Test
    public void testIsAvailableLocale_WithAvailableLocale_ReturnsTrue() {
        Locale locale = LocaleUtils.availableLocaleList().get(0);
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_WithUnavailableLocale_ReturnsFalse() {
        Locale locale = new Locale("xyz");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_WithValidCountryCode_ReturnsUnmodifiableList() {
        String countryCode = "CA";
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "CA"));
        expected.add(new Locale("fr", "CA"));

        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, result);
        assertNotSame(expected, result);
        assertTrue(result instanceof UnmodifiableList);
    }

    @Test
    public void testLanguagesByCountry_WithInvalidCountryCode_ReturnsEmptyList() {
        String countryCode = "XYZ";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_WithValidLanguageCode_ReturnsUnmodifiableList() {
        String languageCode = "en";
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "CA"));
        expected.add(new Locale("en", "US"));

        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, result);
        assertNotSame(expected, result);
        assertTrue(result instanceof UnmodifiableList);
    }

    @Test
    public void testCountriesByLanguage_WithInvalidLanguageCode_ReturnsEmptyList() {
        String languageCode = "XYZ";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(result.isEmpty());
    }
}