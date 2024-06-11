import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale1);

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale2);

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale3);
    }

    @Test
    public void testToLocaleInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            LocaleUtils.toLocale("123");
        });
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertTrue(result.contains(new Locale("fr", "CA", "xxx")));
        assertTrue(result.contains(new Locale("fr", "CA")));
        assertTrue(result.contains(new Locale("fr")));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertTrue(result.contains(new Locale("fr", "CA", "xxx")));
        assertTrue(result.contains(new Locale("fr", "CA")));
        assertTrue(result.contains(new Locale("fr")));
        assertTrue(result.contains(new Locale("en")));
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertTrue(result.contains(new Locale("en", "US")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertTrue(result.contains(new Locale("en", "US")));
    }
}