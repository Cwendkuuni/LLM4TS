import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // Test edge cases and exception handling
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("EN_GB"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("eng_GB"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_xxxx"));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("en")
        );
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
        
        // Test for null input
        assertTrue(LocaleUtils.localeLookupList(null, new Locale("en")).isEmpty());
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        List<Locale> expected = Collections.singletonList(new Locale("en"));
        assertEquals(expected, LocaleUtils.localeLookupList(null, new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        assertEquals(LocaleUtils.availableLocaleList().size(), Locale.getAvailableLocales().length);
    }

    @Test
    public void testAvailableLocaleSet() {
        assertEquals(LocaleUtils.availableLocaleSet().size(), Locale.getAvailableLocales().length);
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("abcd")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Collections.singletonList(new Locale("en", "GB"));
        assertEquals(expected, LocaleUtils.languagesByCountry("GB"));
        
        // Test for null input
        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Collections.singletonList(new Locale("en", "GB"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
        
        // Test for null input
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }
}