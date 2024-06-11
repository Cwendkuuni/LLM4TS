import org.junit.Test;
import java.util.Locale;
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

        try {
            locale = LocaleUtils.toLocale("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");

        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertNotNull(localeList);
        assertEquals(4, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
        assertEquals(defaultLocale, localeList.get(3));

        localeList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertNotNull(localeList);
        assertEquals(4, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
        assertEquals(defaultLocale, localeList.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertNotNull(localeList);
        assertTrue(localeList.contains(new Locale("en")));
        assertTrue(localeList.contains(new Locale("fr", "FR")));
        assertTrue(localeList.contains(new Locale("es", "ES")));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertNotNull(localeSet);
        assertTrue(localeSet.contains(new Locale("en")));
        assertTrue(localeSet.contains(new Locale("fr", "FR")));
        assertTrue(localeSet.contains(new Locale("es", "ES")));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "FR")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("es", "ES")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("de", "DE")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("FR");
        assertNotNull(languages);
        assertTrue(languages.contains(new Locale("fr", "FR")));
        assertFalse(languages.contains(new Locale("es", "ES")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(countries);
        assertTrue(countries.contains(new Locale("fr", "FR")));
        assertFalse(countries.contains(new Locale("fr", "CA")));
    }
}