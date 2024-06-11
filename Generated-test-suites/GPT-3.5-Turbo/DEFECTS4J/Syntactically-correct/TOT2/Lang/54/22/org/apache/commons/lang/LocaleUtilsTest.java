import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // Edge case: invalid format
        assertThrows(IllegalArgumentException.class, () -> {
            LocaleUtils.toLocale("invalid");
        });
    }

    @Test
    public void testLocaleLookupList() {
        assertEquals(Collections.singletonList(new Locale("fr","CA","xxx")), LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx")));
        
        // Edge case: null locale
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        assertFalse(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertFalse(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("abc")));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(0, LocaleUtils.languagesByCountry("ZZ").size());
        
        // Edge case: null country code
        assertEquals(0, LocaleUtils.languagesByCountry(null).size());
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(0, LocaleUtils.countriesByLanguage("zz").size());
        
        // Edge case: null language code
        assertEquals(0, LocaleUtils.countriesByLanguage(null).size());
    }
}