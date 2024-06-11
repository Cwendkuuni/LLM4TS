import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

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

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_xx");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);

        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                Locale.getDefault()
        );

        assertEquals(expectedList, lookupList);
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale, defaultLocale);

        List<Locale> expectedList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );

        assertEquals(expectedList, lookupList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertTrue(availableLocales.contains(Locale.ENGLISH));
        assertTrue(availableLocales.contains(Locale.GERMANY));
        assertTrue(availableLocales.contains(Locale.FRANCE));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertTrue(availableLocales.contains(Locale.ENGLISH));
        assertTrue(availableLocales.contains(Locale.GERMANY));
        assertTrue(availableLocales.contains(Locale.FRANCE));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assertTrue(languages.contains(new Locale("en", "CA")));
        assertTrue(languages.contains(new Locale("fr", "CA")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assertTrue(countries.contains(new Locale("fr", "CA")));
        assertTrue(countries.contains(new Locale("fr", "FR")));
    }

}