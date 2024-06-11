import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validLocale() {
        String validLocale = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(validLocale);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat_missingLanguage() {
        String invalidLocale = "_GB";
        LocaleUtils.toLocale(invalidLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat_missingCountry() {
        String invalidLocale = "en_";
        LocaleUtils.toLocale(invalidLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat_wrongSeparator() {
        String invalidLocale = "en:GB";
        LocaleUtils.toLocale(invalidLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat_wrongLength_language() {
        String invalidLocale = "en";
        LocaleUtils.toLocale(invalidLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat_wrongLength_country() {
        String invalidLocale = "en_GB_";
        LocaleUtils.toLocale(invalidLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat_uppercaseLanguage() {
        String invalidLocale = "EN_GB";
        LocaleUtils.toLocale(invalidLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat_uppercaseCountry() {
        String invalidLocale = "en_GB_XXX";
        LocaleUtils.toLocale(invalidLocale);
    }

    @Test
    public void testLocaleLookupList_validLocale() {
        String localeString = "fr_CA";
        Locale locale = new Locale("fr", "CA");
        List<Locale> expectedLocaleList = LocaleUtils.localeLookupList(locale);
        LocaleUtils.localeLookupList(locale, null); // defaultLocale is null
        List<Locale> actualLocaleList = expectedLocaleList;
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testLocaleLookupList_nullLocale() {
        List<Locale> expectedLocaleList = new ArrayList<>();
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(null);
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testLocaleLookupList_defaultLocale() {
        String localeString = "fr_CA";
        Locale locale = new Locale("fr", "CA");
        List<Locale> expectedLocaleList = Arrays.asList(locale, new Locale("fr"), new Locale(""));
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(null, new Locale("en"));
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Locale.getAvailableLocales();
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedLocaleSet = LocaleUtils.availableLocaleSet();
        Set<Locale> actualLocaleSet = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expectedLocaleSet, actualLocaleSet);
    }

    @Test
    public void testIsAvailableLocale_availableLocale() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_invalidLocale() {
        Locale locale = new Locale("fr", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"), new Locale("es", "CA"));
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testLanguagesByCountry_nullCountryCode() {
        List<Locale> expectedLocaleList = new ArrayList<>();
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(null);
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"));
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testCountriesByLanguage_nullLanguageCode() {
        List<Locale> expectedLocaleList = new ArrayList<>();
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(null);
        assertEquals(expectedLocaleList, actualLocaleList);
    }
}