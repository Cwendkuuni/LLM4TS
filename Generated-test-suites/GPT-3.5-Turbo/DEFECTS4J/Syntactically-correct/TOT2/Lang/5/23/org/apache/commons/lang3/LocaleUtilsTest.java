import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertEquals(4, LocaleUtils.localeLookupList(locale).size());
        assertTrue(LocaleUtils.localeLookupList(locale).contains(locale));
        assertTrue(LocaleUtils.localeLookupList(locale).contains(new Locale("fr", "CA")));
        assertTrue(LocaleUtils.localeLookupList(locale).contains(new Locale("fr")));
        assertTrue(LocaleUtils.localeLookupList(locale).contains(Locale.getDefault()));
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
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("aaaa")));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(2, LocaleUtils.languagesByCountry("US").size());
        assertTrue(LocaleUtils.languagesByCountry("US").contains(Locale.ENGLISH));
        assertTrue(LocaleUtils.languagesByCountry("CA").contains(Locale.CANADA));
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(1, LocaleUtils.countriesByLanguage("en").size());
        assertTrue(LocaleUtils.countriesByLanguage("en").contains(Locale.US));
    }
}