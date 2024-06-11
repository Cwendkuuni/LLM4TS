import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_EmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidLanguageCode() {
        assertNull(LocaleUtils.toLocale("XX"));
    }

    @Test
    public void testToLocale_InvalidCountryCode() {
        assertNull(LocaleUtils.toLocale("en_xX"));
    }

    @Test
    public void testToLocale_ValidLocale() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_CountryCodeMissing() {
        Locale expected = new Locale("en");
        Locale actual = LocaleUtils.toLocale("en");
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_EmptyList() {
        List<Locale> actualLocales = LocaleUtils.localeLookupList(null);
        assertEmpty(actualLocales);
    }

    @Test
    public void testLocaleLookupList_OneLocale() {
        Locale locale = new Locale("fr", "CA");
        List<Locale> actualLocales = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedLocales = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        Locale locale = new Locale("fr", "CA");
        Locale defaultLocale = new Locale("en");
        List<Locale> actualLocales = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expectedLocales = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testAvailableLocaleList_EmptyList() {
        List<Locale> actualLocales = LocaleUtils.availableLocaleList();
        assertTrue(actualLocales.isEmpty());
    }

    @Test
    public void testAvailableLocaleList_AvailableLocales() {
        List<Locale> expectedLocales = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN);
        List<Locale> actualLocales = LocaleUtils.availableLocaleList();
        assertEquals(expectedLocales, actualLocales);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("en_GB_xx");
    }

    @Test
    public void testLanguagesByCountry_EmptyList() {
        String countryCode = "";
        List<Locale> actualLocales = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(actualLocales.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_AvailableLocales() {
        String countryCode = "CA";
        List<Locale> expectedLocales = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        List<Locale> actualLocales = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testCountryByLanguage_EmptyList() {
        String languageCode = "";
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(actualLocales.isEmpty());
    }

    @Test
    public void testCountryByLanguage_AvailableLocales() {
        String languageCode = "fr";
        List<Locale> expectedLocales = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"));
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale locale = new Locale("fr", "CA", "xx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

}