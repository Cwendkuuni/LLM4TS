import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test
    public void testToLocale_validLocaleWithCountry() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
    }

    @Test
    public void testToLocale_validLocaleWithVariant() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("xxx", locale.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLength() {
        LocaleUtils.toLocale("en_GB_x");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidLanguageCode() {
        LocaleUtils.toLocale("1n_GB_xxx");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidCountryCode() {
        LocaleUtils.toLocale("en_gb_xxx");
    }

    @Test
    public void testLocaleLookupList_localeNonNull() {
        Locale locale = LocaleUtils.toLocale("fr_CA_xxx");
        assertNotNull(locale);
        assertEquals(4, LocaleUtils.localeLookupList(locale).size());
        assertEquals(locale, LocaleUtils.localeLookupList(locale).get(0));
        assertEquals(LocaleUtils.toLocale("fr_CA"), LocaleUtils.localeLookupList(locale).get(1));
        assertEquals(LocaleUtils.toLocale("fr"), LocaleUtils.localeLookupList(locale).get(2));
        assertEquals(Locale.getDefault(), LocaleUtils.localeLookupList(locale).get(3));
    }

    @Test
    public void testLocaleLookupList_localeNull() {
        assertNull(LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_defaultLocale() {
        Locale locale = LocaleUtils.toLocale("de_AT");
        Locale defaultLocale = LocaleUtils.toLocale("en_US");
        assertEquals(4, LocaleUtils.localeLookupList(locale, defaultLocale).size());
        assertEquals(locale, LocaleUtils.localeLookupList(locale, defaultLocale).get(0));
        assertEquals(LocaleUtils.toLocale("de"), LocaleUtils.localeLookupList(locale, defaultLocale).get(1));
        assertEquals(LocaleUtils.toLocale("de"), LocaleUtils.localeLookupList(locale, defaultLocale).get(2));
        assertEquals(defaultLocale, LocaleUtils.localeLookupList(locale, defaultLocale).get(3));

    }

    @Test
    public void testAvailableLocaleList() {
        assertNotNull(LocaleUtils.availableLocaleList());
        assertTrue(LocaleUtils.availableLocaleList().size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertTrue(LocaleUtils.availableLocaleSet().size() > 0);
    }

    @Test
    public void testIsAvailableLocale_availableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testIsAvailableLocale_unavailableLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz", "ZZ")));
    }

    @Test
    public void testLanguagesByCountry_validCountryCode() {
        assertEquals(11, LocaleUtils.languagesByCountry("GB").size());
    }

    @Test
    public void testLanguagesByCountry_invalidCountryCode() {
        assertTrue(LocaleUtils.languagesByCountry("XX").isEmpty());
    }

    @Test
    public void testCountriesByLanguage_validLanguageCode() {
        assertEquals(28, LocaleUtils.countriesByLanguage("en").size());
    }

    @Test
    public void testCountriesByLanguage_invalidLanguageCode() {
        assertTrue(LocaleUtils.countriesByLanguage("XX").isEmpty());
    }
}