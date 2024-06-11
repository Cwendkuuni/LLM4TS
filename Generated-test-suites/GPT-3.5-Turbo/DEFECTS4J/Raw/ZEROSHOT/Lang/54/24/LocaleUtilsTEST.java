import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals("en", locale1.getLanguage());
        assertEquals("", locale1.getCountry());
        assertNull(locale1.getVariant());

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale2.getLanguage());
        assertEquals("GB", locale2.getCountry());
        assertNull(locale2.getVariant());

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale3.getLanguage());
        assertEquals("GB", locale3.getCountry());
        assertEquals("xxx", locale3.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidFormat() {
        LocaleUtils.toLocale("en_GB_y");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, result);

        Locale defaultLocale = new Locale("en");
        expected.add(defaultLocale);
        result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = Locale.getAvailableLocales();
        List<Locale> expected = new ArrayList<>(locales);
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = new HashSet<>(Locale.getAvailableLocales());
        Set<Locale> expected = new HashSet<>(locales);
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        Locale invalidLocale = new Locale("xx");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "GB"));
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", "CA"));

        List<Locale> result = LocaleUtils.languagesByCountry("GB");
        assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "GB"));
        expected.add(new Locale("fr", "CA"));

        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, result);
    }
}