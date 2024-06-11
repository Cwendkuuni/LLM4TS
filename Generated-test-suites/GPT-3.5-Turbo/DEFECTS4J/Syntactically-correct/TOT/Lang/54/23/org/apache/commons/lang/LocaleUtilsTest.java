import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_validLocale() {
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocale_validLocaleWithCountry() {
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
    }

    @Test
    public void testToLocale_validLocaleWithCountryAndVariant() {
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test
    public void testToLocale_nullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList_singleLocale() {
        Locale locale = new Locale("fr");
        assertEquals(1, LocaleUtils.localeLookupList(locale).size());
        assertEquals(locale, LocaleUtils.localeLookupList(locale).get(0));
    }

    @Test
    public void testLocaleLookupList_localeWithCountry() {
        Locale locale = new Locale("fr", "CA");
        assertEquals(3, LocaleUtils.localeLookupList(locale).size());
        assertEquals(locale, LocaleUtils.localeLookupList(locale).get(0));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.localeLookupList(locale).get(1));
        assertEquals(new Locale("fr"), LocaleUtils.localeLookupList(locale).get(2));
    }

    @Test
    public void testLocaleLookupList_localeWithCountryAndVariant() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertEquals(4, LocaleUtils.localeLookupList(locale).size());
        assertEquals(locale, LocaleUtils.localeLookupList(locale).get(0));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.localeLookupList(locale).get(1));
        assertEquals(new Locale("fr"), LocaleUtils.localeLookupList(locale).get(2));
        assertEquals(Locale.getDefault(), LocaleUtils.localeLookupList(locale).get(3));
    }

    @Test
    public void testLocaleLookupList_emptyInput() {
        assertEquals(0, LocaleUtils.localeLookupList(null).size());
    }

    @Test
    public void testAvailableLocaleList() {
        assertEquals(Locale.getAvailableLocales().length, LocaleUtils.availableLocaleList().size());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertEquals(Locale.getAvailableLocales().length, LocaleUtils.availableLocaleSet().size());
    }

    @Test
    public void testIsAvailableLocale_true() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale_false() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry_validCountryCode() {
        assertEquals(1, LocaleUtils.languagesByCountry("CA").size());
        assertEquals(new Locale("fr", "CA"), LocaleUtils.languagesByCountry("CA").get(0));
    }

    @Test
    public void testLanguagesByCountry_invalidCountryCode() {
        assertEquals(0, LocaleUtils.languagesByCountry("XX").size());
    }

    @Test
    public void testLanguagesByCountry_nullInput() {
        assertEquals(0, LocaleUtils.languagesByCountry(null).size());
    }

    @Test
    public void testCountriesByLanguage_validLanguageCode() {
        assertEquals(1, LocaleUtils.countriesByLanguage("fr").size());
        assertEquals(new Locale("fr", "CA"), LocaleUtils.countriesByLanguage("fr").get(0));
    }

    @Test
    public void testCountriesByLanguage_invalidLanguageCode() {
        assertEquals(0, LocaleUtils.countriesByLanguage("xx").size());
    }

    @Test
    public void testCountriesByLanguage_nullInput() {
        assertEquals(0, LocaleUtils.countriesByLanguage(null).size());
    }
}