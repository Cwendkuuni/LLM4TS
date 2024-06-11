import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;
import java.util.List;
import org.apache.commons.lang3.LocaleUtils;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en_US");
        assertEquals("en", locale1.getLanguage());
        assertEquals("US", locale1.getCountry());

        Locale locale2 = LocaleUtils.toLocale("fr");
        assertEquals("fr", locale2.getLanguage());
        assertEquals("", locale2.getCountry());
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = LocaleUtils.toLocale("en_US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(4, result.size());

        List<Locale> result2 = LocaleUtils.localeLookupList(null, Locale.ENGLISH);
        assertEquals(1, result2.size());
        assertEquals(Locale.ENGLISH, result2.get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
    }

    @Test
    public void testAvailableLocaleSet() {
        assertNotNull(LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "ES")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertNotNull(result);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertNotNull(result);
    }
}