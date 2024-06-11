import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList_1() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr")
        );
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
    }

    @Test
    public void testLocaleLookupList_2() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("en")
        );
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale_True() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_False() {
        Locale locale = new Locale("zz", "ZZ");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales())
            .stream()
            .filter(locale -> "US".equals(locale.getCountry()) && locale.getVariant().isEmpty())
            .collect(Collectors.toList());
        assertEquals(expected, LocaleUtils.languagesByCountry("US"));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales())
            .stream()
            .filter(locale -> "en".equals(locale.getLanguage()) && !locale.getCountry().isEmpty() && locale.getVariant().isEmpty())
            .collect(Collectors.toList());
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
    }

}