
import org.junit.Test;
import org.apache.commons.lang.LocaleUtils;
import java.util.List;
import java.util.Locale;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());

        // Test with variant
        locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("xxx", locale.getVariant());

        // Test with lowercase language and uppercase country
        locale = LocaleUtils.toLocale("fr_CA");
        assertEquals("fr", locale.getLanguage());
        assertEquals("CA", locale.getCountry());

        // Test invalid formats
        try {
            LocaleUtils.toLocale("abc");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
        try {
            LocaleUtils.toLocale("en_GB_xxxx");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");

        // Test with locale only
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(3, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));

        // Test with default locale
        Locale defaultLocale = new Locale("en");
        list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, list.size());
        assertEquals(defaultLocale, list.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test that the available locale list is not empty
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertFalse(list.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test that the available locale set is not empty
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertFalse(set.isEmpty());
    }

    @Test
    public void testisAvailableLocale() {
        // Test available locale
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));

        // Test unavailable locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test existing country
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertFalse(list.isEmpty());

        // Test non-existing country
        assertTrue(LocaleUtils.languagesByCountry("XX").isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test existing language
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertFalse(list.isEmpty());

        // Test non-existing language
        assertTrue(LocaleUtils.countriesByLanguage("xx").isEmpty());
    }
}

