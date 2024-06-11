import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validString() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidString() {
        LocaleUtils.toLocale("invalid_locale");
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("en", "US"));
        assertEquals(3, localeList.size());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertNotNull(availableLocales);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        assertNotNull(availableLocaleSet);
    }

    @Test
    public void testLanguagesByCountry_nullCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        assertNotNull(languages);
        assertEquals(0, languages.size());
    }

    @Test
    public void testCountriesByLanguage_nullCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertNotNull(countries);
        assertEquals(0, countries.size());
    }

}