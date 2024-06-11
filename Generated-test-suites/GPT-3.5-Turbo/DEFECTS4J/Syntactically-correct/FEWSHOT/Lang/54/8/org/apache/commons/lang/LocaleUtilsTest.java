import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("fr", "CA", "xxx"), LocaleUtils.toLocale("fr_CA_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat1() {
        LocaleUtils.toLocale("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat2() {
        LocaleUtils.toLocale("us");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat3() {
        LocaleUtils.toLocale("en_gbb");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat4() {
        LocaleUtils.toLocale("en_GB_xxxx");
    }

    @Test
    public void testLocaleLookupList() {
        assertEquals(Arrays.asList(new Locale("en", "GB", "xxx"), new Locale("en", "GB"), new Locale("en"), Locale.getDefault()),
                LocaleUtils.localeLookupList(new Locale("en", "GB", "xxx"), Locale.getDefault()));
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), Locale.getDefault()),
                LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), Locale.getDefault()));
    }

    @Test
    public void testLocaleLookupListNullLocale() {
        assertEquals(Collections.singletonList(Locale.getDefault()), LocaleUtils.localeLookupList(null, Locale.getDefault()));
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
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zzz")));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(Arrays.asList(new Locale("en", "US"), new Locale("es", "US")), LocaleUtils.languagesByCountry("US"));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(Arrays.asList(new Locale("en", "US")), LocaleUtils.countriesByLanguage("en"));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}