import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("#invalid"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("a"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_en"));
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", "US", "POSIX"), LocaleUtils.toLocale("en_US_POSIX"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US", "POSIX");
        List<Locale> expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("en", ""), Locale.getDefault());
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
        assertEquals(expected, LocaleUtils.localeLookupList(locale, Locale.getDefault()));
    }

    @Test
    public void testAvailableLocaleList() {
        assertEquals(SyncAvoid.AVAILABLE_LOCALE_LIST, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertEquals(SyncAvoid.AVAILABLE_LOCALE_SET, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xyz")));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
        assertEquals(Collections.singletonList(Locale.CANADA), LocaleUtils.languagesByCountry("CA").get(0));
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
        assertEquals(Collections.singletonList(Locale.US), LocaleUtils.countriesByLanguage("en").get(0));
    }
}