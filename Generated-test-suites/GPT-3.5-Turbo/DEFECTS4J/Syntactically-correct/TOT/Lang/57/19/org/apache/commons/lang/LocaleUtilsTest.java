import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals("en", locale1.getLanguage());
        assertEquals("", locale1.getCountry());

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale2.getLanguage());
        assertEquals("GB", locale2.getCountry());

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale3.getLanguage());
        assertEquals("GB", locale3.getCountry());
        assertEquals("xxx", locale3.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat1() {
        LocaleUtils.toLocale("invalidformat");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat2() {
        LocaleUtils.toLocale("en_GB_");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> locales1 = LocaleUtils.localeLookupList(locale1);
        assertEquals(3, locales1.size());
        assertEquals(locale1, locales1.get(0));
        assertEquals(new Locale("fr", "CA"), locales1.get(1));
        assertEquals(new Locale("fr"), locales1.get(2));

        Locale locale2 = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> locales2 = LocaleUtils.localeLookupList(locale2, defaultLocale);
        assertEquals(4, locales2.size());
        assertEquals(locale2, locales2.get(0));
        assertEquals(new Locale("fr", "CA"), locales2.get(1));
        assertEquals(new Locale("fr"), locales2.get(2));
        assertEquals(defaultLocale, locales2.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
        assertFalse(locales.isEmpty());
        assertTrue(locales.contains(Locale.US));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertNotNull(locales);
        assertFalse(locales.isEmpty());
        assertTrue(locales.contains(Locale.US));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertNotNull(languages);
        assertFalse(languages.isEmpty());
        assertTrue(languages.contains(Locale.ENGLISH));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
        assertTrue(countries.contains(Locale.US));
    }

}