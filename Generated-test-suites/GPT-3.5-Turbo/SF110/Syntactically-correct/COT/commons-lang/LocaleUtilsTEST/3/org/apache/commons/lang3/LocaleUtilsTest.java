import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString_ReturnNull() {
        String str = null;
        Locale result = LocaleUtils.toLocale(str);
        assertEquals(null, result);
    }

    @Test
    public void testToLocale_EmptyString_ReturnEmptyLocale() {
        String str = "";
        Locale result = LocaleUtils.toLocale(str);
        Locale expected = new Locale("", "");
        assertEquals(expected, result);
    }

    @Test
    public void testToLocale_ValidLocaleFormat_ReturnLocale() {
        String str = "en_US";
        Locale result = LocaleUtils.toLocale(str);
        Locale expected = new Locale("en", "US");
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_ThrowIllegalArgumentException() {
        String str = "InvalidLocale";
        LocaleUtils.toLocale(str);
    }

    @Test
    public void testLocaleLookupList_OneLocale_ReturnListWithOneLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_LocaleWithVariant_ReturnListWithTwoLocales() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US", "variant"));
        expected.add(new Locale("en", "US"));
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_LocaleWithCountry_ReturnListWithTwoLocales() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("en", "GB");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", "GB"));
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList_ReturnListOfAvailableLocales() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet_ReturnSetOfAvailableLocales() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale_ReturnTrue() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_UnavailableLocale_ReturnFalse() {
        Locale locale = new Locale("fr", "FR");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnListofLanguages() {
        String countryCode = "US";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnEmptyList() {
        String countryCode = null;
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnListofCountries() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnEmptyList() {
        String languageCode = null;
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

}