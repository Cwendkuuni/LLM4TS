import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class LocaleUtilsTest {

    private Locale locale;

    public LocaleUtilsTest(Locale locale) {
        this.locale = locale;
    }

    @Parameterized.Parameters
    public static List<Locale> locales() {
        return Arrays.asList(
                Locale.GERMANY,
                Locale.US,
                Locale.UK,
                Locale.FRANCE,
                Locale.ITALY,
                Locale.JAPAN,
                Locale.KOREA
        );
    }

    @Test
    public void testToLocale_ValidLanguage_ValidResult() {
        // Test a valid language code
        String language = locale.getLanguage();
        String countryCode = locale.getCountry();
        String variant = locale.getVariant();

        String str = language + "_" + countryCode + "_" + variant;
        Locale result = LocaleUtils.toLocale(str);

        assertEquals(locale, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_ThrowsIllegalArgumentException() {
        // Test an invalid locale format
        String invalidLocale = "invalidLocale";
        LocaleUtils.toLocale(invalidLocale);
    }

    @Test
    public void testLocaleLookupList_ValidLocale_ValidResult() {
        // Test locale lookup list with valid locale
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        assertEquals(4, lookupList.size());
        assertEquals(locale, lookupList.get(0));
        assertEquals(new Locale(locale.getLanguage(), locale.getCountry()), lookupList.get(1));
        assertEquals(new Locale(locale.getLanguage(), ""), lookupList.get(2));
        assertEquals(Locale.getDefault(), lookupList.get(3));
    }

    @Test
    public void testLocaleLookupList_NullLocale_ValidResult() {
        // Test locale lookup list with null locale
        List<Locale> lookupList = LocaleUtils.localeLookupList(null);
        assertEquals(0, lookupList.size());
    }

    @Test
    public void testLocaleLookupList_ValidLocale_DefaultLocale_ValidResult() {
        // Test locale lookup list with valid locale and default locale
        Locale defaultLocale = Locale.UK;
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(5, lookupList.size());
        assertEquals(locale, lookupList.get(0));
        assertEquals(new Locale(locale.getLanguage(), locale.getCountry()), lookupList.get(1));
        assertEquals(new Locale(locale.getLanguage(), ""), lookupList.get(2));
        assertEquals(Locale.getDefault(), lookupList.get(3));
        assertEquals(defaultLocale, lookupList.get(4));
    }

    @Test
    public void testLocaleLookupList_NullLocale_DefaultLocale_ValidResult() {
        // Test locale lookup list with null locale and default locale
        Locale defaultLocale = Locale.UK;
        List<Locale> lookupList = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(1, lookupList.size());
        assertEquals(defaultLocale, lookupList.get(0));
    }

    @Test
    public void testAvailableLocaleList_ValidResult() {
        // Test available locale list
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
        assertFalse(availableLocales.isEmpty());
        assertTrue(availableLocales.contains(Locale.US));
        assertTrue(availableLocales.contains(Locale.UK));
        assertTrue(availableLocales.contains(Locale.FRANCE));
    }

    @Test
    public void testAvailableLocaleSet_ValidResult() {
        // Test available locale set
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocales);
        assertFalse(availableLocales.isEmpty());
        assertTrue(availableLocales.contains(Locale.US));
        assertTrue(availableLocales.contains(Locale.UK));
        assertTrue(availableLocales.contains(Locale.FRANCE));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnsTrue() {
        // Test isAvailableLocale with an available locale
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testIsAvailableLocale_UnavailableLocale_ReturnsFalse() {
        // Test isAvailableLocale with an unavailable locale
        assertFalse(LocaleUtils.isAvailableLocale(Locale.CANADA));
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ValidResult() {
        // Test languages by country with valid country code
        List<Locale> languages = LocaleUtils.languagesByCountry(locale.getCountry());
        assertNotNull(languages);
        assertFalse(languages.isEmpty());
        assertTrue(languages.contains(locale));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_EmptyResult() {
        // Test languages by country with null country code
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        assertNotNull(languages);
        assertTrue(languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ValidResult() {
        // Test countries by language with valid language code
        List<Locale> countries = LocaleUtils.countriesByLanguage(locale.getLanguage());
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
        assertTrue(countries.contains(locale));
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_EmptyResult() {
        // Test countries by language with null language code
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }
}