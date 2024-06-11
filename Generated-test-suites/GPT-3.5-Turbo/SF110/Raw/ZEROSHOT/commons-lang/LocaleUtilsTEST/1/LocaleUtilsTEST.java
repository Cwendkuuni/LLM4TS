import org.junit.*;
import java.util.concurrent.*;
import java.util.*;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnNull() {
        String str = null;
        Locale result = LocaleUtils.toLocale(str);
        assertNull(result);
    }

    @Test
    public void testToLocale_EmptyInput_ReturnLocaleWithoutLanguageAndCountry() {
        String str = "";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_throwIllegalArgumentException() {
        String str = "#invalid";
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_throwIllegalArgumentException2() {
        String str = "T";
        LocaleUtils.toLocale(str);
    }

    @Test
    public void testToLocale_ValidFormat_ReturnLocaleWithLanguage() {
        String str = "en";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals("en", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_throwIllegalArgumentException3() {
        String str = "en_";
        LocaleUtils.toLocale(str);
    }

    @Test
    public void testToLocale_ValidFormat_ReturnLocaleWithLanguageAndCountry() {
        String str = "en_US";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_throwIllegalArgumentException4() {
        String str = "en_US_";
        LocaleUtils.toLocale(str);
    }

    @Test
    public void testLocaleLookupList_SingleLocale_ReturnListWithSingleLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(1, result.size());
        assertEquals(locale, result.get(0));
    }

    @Test
    public void testLocaleLookupList_LocaleWithVariant_ReturnListWithTwoLocales() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(2, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale("en", "US")));
    }

    @Test
    public void testLocaleLookupList_LocaleWithCountry_ReturnListWithTwoLocales() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale, new Locale("en", "GB"));
        assertEquals(3, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale("en", "")));
        assertTrue(result.contains(new Locale("en", "GB")));
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertEquals(0, result.size());
    }

    @Test
    public void testAvailableLocaleList_ReturnNonEmptyList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertFalse(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_ReturnNonEmptySet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertFalse(result.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnTrue() {
        Locale locale = Locale.ENGLISH;
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_UnavailableLocale_ReturnFalse() {
        Locale locale = new Locale("xx");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
        String countryCode = null;
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_AvailableCountryCode_ReturnLanguagesList() {
        String countryCode = "US";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_UnavailableCountryCode_ReturnEmptyList() {
        String countryCode = "xx";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
        String languageCode = null;
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_AvailableLanguageCode_ReturnCountriesList() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_UnavailableLanguageCode_ReturnEmptyList() {
        String languageCode = "xx";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(result.isEmpty());
    }
}