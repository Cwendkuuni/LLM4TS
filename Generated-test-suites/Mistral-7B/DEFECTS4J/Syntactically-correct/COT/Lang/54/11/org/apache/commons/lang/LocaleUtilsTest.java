import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("enGB"); // no underscore
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupListEmpty() {
        Locale locale = null;
        List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertFalse(actual.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        assertFalse(actual.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocaleInvalidLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        locale.setVariant("invalid"); // add invalid variant
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(new Locale("fr", countryCode), new Locale("en", countryCode));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountryEmpty() {
        String countryCode = null;
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expected = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "BE"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguageEmpty() {
        String languageCode = null;
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(Collections.emptyList(), actual);
    }
}