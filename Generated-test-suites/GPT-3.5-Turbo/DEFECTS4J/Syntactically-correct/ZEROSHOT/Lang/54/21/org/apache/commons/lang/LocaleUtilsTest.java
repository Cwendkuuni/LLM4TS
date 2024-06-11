import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String str = "en";
        Locale result = LocaleUtils.toLocale(str);
        assertEquals(new Locale("en", ""), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        String str = "123";
        LocaleUtils.toLocale(str);
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assertTrue(result);
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertNotNull(result);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertNotNull(result);
    }
}