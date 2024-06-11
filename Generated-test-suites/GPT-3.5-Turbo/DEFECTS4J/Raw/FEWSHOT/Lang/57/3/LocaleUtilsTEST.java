import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("en_GB_xx");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale1 = new Locale("fr", "CA", "xxx");
        Locale defaultLocale1 = new Locale("en");
        List<Locale> expected1 = Arrays.asList(
            new Locale("fr", "CA", "xxx"),
            new Locale("fr", "CA"),
            new Locale("fr"),
            new Locale("en")
        );
        assertEquals(expected1, LocaleUtils.localeLookupList(locale1, defaultLocale1));
        
        Locale locale2 = new Locale("es", "MX");
        Locale defaultLocale2 = new Locale("fr");
        List<Locale> expected2 = Arrays.asList(
            new Locale("es", "MX"),
            new Locale("es"),
            new Locale("fr")
        );
        assertEquals(expected2, LocaleUtils.localeLookupList(locale2, defaultLocale2));
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
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));

        Locale locale2 = new Locale("xx", "YY");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", "GB"));
        expected.add(new Locale("en", "CA"));
        assertEquals(expected, LocaleUtils.languagesByCountry("US"));

        List<Locale> expected2 = new ArrayList<>();
        assertEquals(expected2, LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("FR", "CA"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("fr"));

        List<Locale> expected2 = new ArrayList<>();
        assertEquals(expected2, LocaleUtils.countriesByLanguage(null));
    }
}