import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleBasic() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testToLocaleEmptyString() {
        Locale locale = LocaleUtils.toLocale("");
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("abc#def");
    }

    // More test cases for toLocale method based on edge cases and exceptions should be added here

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        assertEquals(4, LocaleUtils.localeLookupList(locale).size());
    }

    // More test cases for localeLookupList method should be added here

    @Test
    public void testAvailableLocaleList() {
        assertEquals(LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST.size(), LocaleUtils.availableLocaleList().size());
    }

    // More test cases for availableLocaleList method should be added here

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    // More test cases for isAvailableLocale method should be added here

    @Test
    public void testLanguagesByCountry() {
        assertEquals(0, LocaleUtils.languagesByCountry(null).size());
    }

    // More test cases for languagesByCountry method should be added here

    @Test
    public void testCountriesByLanguage() {
        assertEquals(0, LocaleUtils.countriesByLanguage(null).size());
    }

    // More test cases for countriesByLanguage method should be added here
}