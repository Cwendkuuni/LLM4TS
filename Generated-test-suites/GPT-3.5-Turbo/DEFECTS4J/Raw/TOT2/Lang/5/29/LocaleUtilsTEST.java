import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_basic() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test
    public void testToLocale_edgeCases() {
        // Testing lowercase language code
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        
        // Testing uppercase country code
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        
        // Testing variant constructor behavior
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test
    public void testLocaleLookupList_basic() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
    }

    @Test
    public void testLocaleLookupList_edgeCases() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(4, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(new Locale("en"), result.get(3));
    }

    @Test
    public void testAvailableLocaleList_basic() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet_basic() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testIsAvailableLocale_basic() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CANADA));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("abc", "def")));
    }

    @Test
    public void testLanguagesByCountry_basic() {
        List<Locale> result = LocaleUtils.languagesByCountry("GB");
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testCountriesByLanguage_basic() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }
}