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
    public void testToLocale_nullInput() {
        LocaleUtils.toLocale(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        LocaleUtils.toLocale("abc");
    }

    @Test
    public void testLocaleLookupList() {
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr")), LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx")));
    }

    @Test
    public void testLocaleLookupList_withDefaultLocale() {
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en")), LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));
    }

    @Test
    public void testLocaleLookupList_nullInput() {
        assertEquals(Arrays.asList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_nullDefaultLocale() {
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr")), LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), null));
    }

    @Test
    public void testAvailableLocaleList() {
        assertTrue(LocaleUtils.availableLocaleList().contains(new Locale("en", "")));
        assertTrue(LocaleUtils.availableLocaleList().contains(new Locale("en", "GB")));
        assertTrue(LocaleUtils.availableLocaleList().contains(new Locale("fr", "CA")));
    }

    @Test
    public void testAvailableLocaleSet() {
        assertTrue(LocaleUtils.availableLocaleSet().contains(new Locale("en", "")));
        assertTrue(LocaleUtils.availableLocaleSet().contains(new Locale("en", "GB")));
        assertTrue(LocaleUtils.availableLocaleSet().contains(new Locale("fr", "CA")));
    }

    @Test
    public void testIsAvailableLocale_true() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "GB")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "CA")));
    }

    @Test
    public void testIsAvailableLocale_false() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("de", "")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("de", "DE")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zh", "CN")));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(Arrays.asList(new Locale("fr", "CA"), new Locale("fr")), LocaleUtils.languagesByCountry("CA"));
        assertEquals(Arrays.asList(new Locale("en", "GB")), LocaleUtils.languagesByCountry("GB"));
    }

    @Test
    public void testLanguagesByCountry_nullInput() {
        assertEquals(Arrays.asList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(Arrays.asList(new Locale("fr", "CA")), LocaleUtils.countriesByLanguage("fr"));
        assertEquals(Arrays.asList(new Locale("en", "GB")), LocaleUtils.countriesByLanguage("en"));
    }

    @Test
    public void testCountriesByLanguage_nullInput() {
        assertEquals(Arrays.asList(), LocaleUtils.countriesByLanguage(null));
    }
}