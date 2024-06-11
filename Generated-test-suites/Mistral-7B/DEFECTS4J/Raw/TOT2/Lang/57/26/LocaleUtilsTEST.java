import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.lang.LocaleUtils;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        expected = new Locale("es", "ES");
        actual = LocaleUtils.toLocale("es_ES");
        assertEquals(expected, actual);

        expected = new Locale("fr", "");
        actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);

        // Test invalid locale strings
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            LocaleUtils.toLocale("abcd");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}

        try {
            LocaleUtils.toLocale("en_US_abcd");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US", "variant");
        List<Locale> expected = LocaleUtils.localeLookupList(locale);
        List<Locale> actual = new ArrayList<>();
        actual.add(locale);
        actual.add(new Locale("en", "US"));
        actual.add(new Locale("en", ""));
        assertEquals(expected, actual);

        locale = new Locale("fr", "CA");
        expected = LocaleUtils.localeLookupList(locale, new Locale("en", "US"));
        actual = new ArrayList<>();
        actual.add(locale);
        actual.add(new Locale("fr", "CA"));
        actual.add(new Locale("fr", ""));
        actual.add(new Locale("en", "US"));
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("ab", "cd")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> actual = LocaleUtils.languagesByCountry("US");
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("es", "US"));
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(new Locale("US", "en"), new Locale("CA", "en"), new Locale("GB", "en"));
        assertEquals(expected, actual);
    }
}