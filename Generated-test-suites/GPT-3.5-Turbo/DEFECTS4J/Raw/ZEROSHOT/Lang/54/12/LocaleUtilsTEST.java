import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_xxxx"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = List.of(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        assertNotNull(LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertNotNull(LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = List.of(new Locale("en", "CA"), new Locale("fr", "CA"));
        assertEquals(expected, LocaleUtils.languagesByCountry("CA"));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = List.of(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "AU"), new Locale("en", "CA"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
    }

}