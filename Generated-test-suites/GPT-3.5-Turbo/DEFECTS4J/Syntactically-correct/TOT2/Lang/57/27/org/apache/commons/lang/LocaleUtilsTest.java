import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        
        assertEquals(new Locale("en", ""), locale1);
        assertEquals(new Locale("en", "GB"), locale2);
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> locales1 = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        List<Locale> locales2 = LocaleUtils.localeLookupList(new Locale("en"));
        
        assertEquals(3, locales1.size());
        assertTrue(locales1.contains(new Locale("fr", "CA", "xxx")));
        assertTrue(locales1.contains(new Locale("fr", "CA")));
        assertTrue(locales1.contains(new Locale("fr")));
        assertTrue(locales1.contains(Locale.getDefault()));
        
        assertEquals(4, locales2.size());
        assertTrue(locales2.contains(new Locale("en")));
        assertTrue(locales2.contains(Locale.getDefault()));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        
        assertNotNull(availableLocales);
        assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        
        assertNotNull(availableLocales);
        assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        
        assertNotNull(languages);
        assertFalse(languages.isEmpty());
        assertTrue(languages.contains(new Locale("en", "US")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
        assertTrue(countries.contains(new Locale("en", "US")));
    }
}