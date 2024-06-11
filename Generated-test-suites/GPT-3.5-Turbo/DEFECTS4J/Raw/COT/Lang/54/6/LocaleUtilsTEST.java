import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testToLocaleNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List expected = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            Locale.getDefault()
        );
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
    }

    @Test
    public void testLocaleLookupListWithDefault() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List expected = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("en")
        );
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testLocaleLookupListNull() {
        assertNull(LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        List expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set expected = new HashSet(Locale.getAvailableLocales());
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "yy")));
    }

    @Test
    public void testLanguagesByCountry() {
        List expected = Arrays.asList(
            new Locale("en", "US"),
            new Locale("en", "UM")
        );
        assertEquals(expected, LocaleUtils.languagesByCountry("US"));
    }

    @Test
    public void testLanguagesByCountryNull() {
        assertEquals(Collections.EMPTY_LIST, LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List expected = Arrays.asList(
            new Locale("en", "US"),
            new Locale("es", "US")
        );
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
    }

    @Test
    public void testCountriesByLanguageNull() {
        assertEquals(Collections.EMPTY_LIST, LocaleUtils.countriesByLanguage(null));
    }

}