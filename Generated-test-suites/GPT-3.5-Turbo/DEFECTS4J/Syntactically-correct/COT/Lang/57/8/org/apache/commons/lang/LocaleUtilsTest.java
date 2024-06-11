
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
        assertEquals("", locale.getVariant());

        locale = LocaleUtils.toLocale("en_GB");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("", locale.getVariant());

        locale = LocaleUtils.toLocale("en_GB_xxx");
        assertNotNull(locale);
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("xxx", locale.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLanguageCode() {
        LocaleUtils.toLocale("EN_GB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountryCode() {
        LocaleUtils.toLocale("en_gb");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr","CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List result = LocaleUtils.localeLookupList(locale);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr","CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));

        result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr","CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(defaultLocale, result.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertTrue(result.contains(Locale.US));
        assertTrue(result.contains(Locale.UK));
        assertTrue(result.contains(Locale.FRANCE));
        assertTrue(result.contains(Locale.GERMANY));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertTrue(result.contains(Locale.US));
        assertTrue(result.contains(Locale.UK));
        assertTrue(result.contains(Locale.FRANCE));
        assertTrue(result.contains(Locale.GERMANY));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.UK));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRANCE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMANY));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("abc")));
    }

    @Test
    public void testLanguagesByCountry() {
        List result = LocaleUtils.languagesByCountry("CA");
        assertNotNull(result);
        assertTrue(result.contains(new Locale("fr", "CA")));
        assertFalse(result.contains(new Locale("en", "CA")));
    }

    @Test
    public void testCountriesByLanguage() {
        List result = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(result);
        assertTrue(result.contains(new Locale("fr", "CA")));
        assertFalse(result.contains(new Locale("fr", "US")));
    }

}
