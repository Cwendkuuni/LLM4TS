import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnNull() {
        Locale result = LocaleUtils.toLocale(null);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLength_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidCharset_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("EN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidSeparator_ThrowIllegalArgumentException() {
        LocaleUtils.toLocale("en$US");
    }

    @Test
    public void testToLocale_LanguageOnly_ReturnLocaleWithLanguage() {
        Locale result = LocaleUtils.toLocale("en");
        assertNotNull(result);
        assertEquals("en", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test
    public void testToLocale_LanguageAndCountry_ReturnLocaleWithLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("en_US");
        assertNotNull(result);
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
    }

    @Test
    public void testToLocale_LanguageCountryAndVariant_ReturnLocaleWithLanguageCountryAndVariant() {
        Locale result = LocaleUtils.toLocale("en_US_xxx");
        assertNotNull(result);
        assertEquals("en", result.getLanguage());
        assertEquals("US", result.getCountry());
        assertEquals("xxx", result.getVariant());
    }

    @Test
    public void testLocaleLookupList_LocaleNotNull_ReturnLookupList() {
        Locale locale = new Locale("fr", "CA");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
    }

    @Test
    public void testLocaleLookupList_LocaleAndDefaultLocaleNotNull_ReturnLookupList() {
        Locale locale = new Locale("fr", "CA");
        Locale defaultLocale = new Locale("en");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(defaultLocale, result.get(3));
    }

    @Test
    public void testAvailableLocaleList_ReturnAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertTrue(result.contains(Locale.getDefault()));
    }

    @Test
    public void testAvailableLocaleSet_ReturnAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertTrue(result.contains(Locale.getDefault()));
    }

    @Test
    public void testIsAvailableLocale_LocaleInAvailableLocales_ReturnTrue() {
        Locale locale = Locale.getDefault();
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_LocaleNotInAvailableLocales_ReturnFalse() {
        Locale locale = new Locale("zz");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry_CountryCodeIsNull_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_CountryCodeIsValid_ReturnLanguagesList() {
        List<Locale> result = LocaleUtils.languagesByCountry("CA");
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains(new Locale("fr", "CA")));
    }

    @Test
    public void testLanguagesByCountry_InvalidCountryCode_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry("XX");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_LanguageCodeIsNull_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_LanguageCodeIsValid_ReturnCountriesList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains(new Locale("fr", "CA")));
    }

    @Test
    public void testCountriesByLanguage_InvalidLanguageCode_ReturnEmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("XX");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}