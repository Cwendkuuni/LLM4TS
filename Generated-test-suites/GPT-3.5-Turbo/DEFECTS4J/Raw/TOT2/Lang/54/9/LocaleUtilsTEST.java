import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(Locale.ENGLISH, locale1);

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(Locale.UK, locale2);

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale3.getLanguage());
        assertEquals("GB", locale3.getCountry());
        assertEquals("xxx", locale3.getVariant());
    }

    @Test
    public void testToLocaleException() {
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("eng");
            fail("Expected IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
    }

    @Test
    public void testLocaleLookupListWithDefault() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
        assertEquals(defaultLocale, list.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
        assertTrue(locales.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.US));
        assertTrue(LocaleUtils.availableLocaleSet().contains(Locale.FRANCE));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRANCE));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zzz")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("FR");
        assertNotNull(languages);
        assertTrue(languages.contains(Locale.FRENCH));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(countries);
        assertTrue(countries.contains(Locale.FRANCE));
    }
}