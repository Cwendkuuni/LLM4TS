import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleNullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocaleValidInput() {
        Locale expected = new Locale("en", "GB", "xxx");
        Locale actual = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        LocaleUtils.toLocale("invalid_locale_format");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(3, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
        assertTrue(availableLocales.contains(Locale.US));
    }

    @Test
    public void testAvailableLocaleSet() {
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.JAPAN));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.UK));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesForCountry = LocaleUtils.languagesByCountry("US");
        assertNotNull(languagesForCountry);
        assertTrue(languagesForCountry.contains(Locale.ENGLISH));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesForLanguage = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countriesForLanguage);
        assertTrue(countriesForLanguage.contains(Locale.US));
    }

}