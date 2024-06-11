import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;
import java.util.List;
import java.util.Locale;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_withValidString() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("en", "GB", "XXX"), LocaleUtils.toLocale("en_GB_XXX"));
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale(Locale.getDefault().toString()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_withInvalidString() {
        LocaleUtils.toLocale("invalid_locale");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_withNullString() {
        LocaleUtils.toLocale(null);
    }

    @Test
    public void testLocaleLookupList_withLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = List.of(locale, new Locale("fr", "CA"), new Locale("fr"), Locale.getDefault());
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_withLocaleAndDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.ENGLISH;
        List<Locale> expected = List.of(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertTrue(availableLocales.contains(Locale.ENGLISH));
        assertTrue(availableLocales.contains(Locale.FRENCH));
        assertTrue(availableLocales.contains(Locale.GERMAN));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertTrue(availableLocales.contains(Locale.ENGLISH));
        assertTrue(availableLocales.contains(Locale.FRENCH));
        assertTrue(availableLocales.contains(Locale.GERMAN));
    }

    @Test
    public void testIsAvailableLocale_withAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale_withUnavailableLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid", "invalid")));
    }

    @Test
    public void testLanguagesByCountry_withValidCountryCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assertTrue(languages.contains(Locale.ENGLISH));
        assertTrue(languages.contains(Locale.FRENCH));
    }

    @Test
    public void testLanguagesByCountry_withInvalidCountryCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry("invalid");
        assertTrue(languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_withValidLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertTrue(countries.contains(Locale.US));
        assertTrue(countries.contains(Locale.UK));
    }

    @Test
    public void testCountriesByLanguage_withInvalidLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("invalid");
        assertTrue(countries.isEmpty());
    }
}