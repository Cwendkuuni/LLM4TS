import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale localeEn = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), localeEn);

        Locale localeEnGB = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), localeEnGB);

        Locale localeEnGBXxx = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), localeEnGBXxx);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("abc");
    }

    @Test
    public void testLocaleLookupList() {
        Locale localeFr = new Locale("fr");
        List<Locale> result = LocaleUtils.localeLookupList(localeFr);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(new Locale("fr", ""), result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));

        Locale localeFrCA = new Locale("fr", "CA");
        result = LocaleUtils.localeLookupList(localeFrCA, new Locale("en"));
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(new Locale("fr", "CA"), result.get(0));
        assertEquals(new Locale("fr"), result.get(1));
        assertEquals(new Locale("en"), result.get(2));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xyz")));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("GB");
        assertNotNull(result);
        assertTrue(result.contains(new Locale("en", "GB")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertNotNull(result);
        assertTrue(result.contains(new Locale("en", "GB")));
    }
}