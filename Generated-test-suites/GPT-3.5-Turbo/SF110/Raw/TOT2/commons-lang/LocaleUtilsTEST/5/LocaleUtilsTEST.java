import static org.junit.Assert.*;

import java.util.Locale;
import java.util.List;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertNull(LocaleUtils.toLocale(null));
        
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        
        assertThrows(IllegalArgumentException.class, () -> {
            LocaleUtils.toLocale("#en");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            LocaleUtils.toLocale("e");
        });
        
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", "US", "POSIX"), LocaleUtils.toLocale("en_US_POSIX"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = Locale.getDefault();
        
        List<Locale> expectedList = List.of(locale, new Locale("en", ""), new Locale("", "US"), defaultLocale);
        
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale, defaultLocale));
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
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = List.of(new Locale("en_US"), new Locale("en_GB"));
        
        assertEquals(expectedList, LocaleUtils.languagesByCountry("US"));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = List.of(new Locale("en_US"), new Locale("en_GB"));
        
        assertEquals(expectedList, LocaleUtils.countriesByLanguage("en"));
    }
}