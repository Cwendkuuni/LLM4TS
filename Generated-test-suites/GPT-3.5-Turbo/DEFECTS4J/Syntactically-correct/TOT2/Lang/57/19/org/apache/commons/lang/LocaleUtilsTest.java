import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_valid() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalid() {
        LocaleUtils.toLocale("xyz");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertEquals(4, LocaleUtils.localeLookupList(locale).size());
    }

    @Test
    public void testAvailableLocaleList() {
        assertNotNull(LocaleUtils.availableLocaleList());
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(2, LocaleUtils.languagesByCountry("US").size());
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(2, LocaleUtils.countriesByLanguage("en").size());
    }
    
    //Edge cases and error scenarios could be added to the test cases as needed

}