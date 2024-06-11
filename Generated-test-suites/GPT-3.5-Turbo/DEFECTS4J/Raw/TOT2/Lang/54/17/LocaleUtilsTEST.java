import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        
        // Edge cases
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_US"));
        
        // Error scenarios
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("invalid"));
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case
        Locale locale = new Locale("fr", "CA", "xxx");
        assertNotNull(LocaleUtils.localeLookupList(locale));
        
        // Edge cases
        assertEquals(4, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")).size());
        
        // Error scenarios
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.localeLookupList(null, new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        // Typical use case
        assertNotNull(LocaleUtils.availableLocaleList());
        
        // Edge cases
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
        
        // Error scenarios
        assertNotNull(LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Typical use case
        assertNotNull(LocaleUtils.availableLocaleSet());
        
        // Edge cases
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
        
        // Error scenarios
        assertNotNull(LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        // Typical use case
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        
        // Edge cases
        assertFalse(LocaleUtils.isAvailableLocale(Locale.JAPANESE));
        
        // Error scenarios
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // Typical use case
        assertEquals(2, LocaleUtils.languagesByCountry("US").size());
        
        // Edge cases
        assertEquals(0, LocaleUtils.languagesByCountry("XX").size());
        
        // Error scenarios
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        // Typical use case
        assertEquals(6, LocaleUtils.countriesByLanguage("en").size());
        
        // Edge cases
        assertEquals(0, LocaleUtils.countriesByLanguage("xx").size());
        
        // Error scenarios
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.countriesByLanguage(null));
    }
}