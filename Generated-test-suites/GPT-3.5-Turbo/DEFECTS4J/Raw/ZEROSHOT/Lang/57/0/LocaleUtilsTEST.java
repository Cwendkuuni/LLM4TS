import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;


public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Null input should return null
        assertNull(LocaleUtils.toLocale(null));

        // Valid locale strings
        assertEquals(Locale.US, LocaleUtils.toLocale("en"));
        assertEquals(Locale.UK, LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("ja", "", "JP"), LocaleUtils.toLocale("ja__JP"));
        assertEquals(new Locale("es", "", ""), LocaleUtils.toLocale("es__"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        // Invalid format should throw IllegalArgumentException
        LocaleUtils.toLocale("invalid(locale)");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");

        // Expected list should be [fr_CA_xxx, fr_CA, fr, en]
        List expected = new ArrayList();
        expected.add(locale);
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        expected.add(defaultLocale);

        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testLocaleLookupListNullLocale() {
        Locale locale = null;
        Locale defaultLocale = new Locale("en");

        // Expected list should be [en]
        List expected = new ArrayList();
        expected.add(defaultLocale);

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
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xyz")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<>();
        expected.add(Locale.US);
        expected.add(Locale.UK);

        assertEquals(expected, LocaleUtils.languagesByCountry("US"));
    }

    @Test
    public void testLanguagesByCountryNullCountry() {
        List<Locale> expected = Collections.emptyList();

        assertEquals(expected, LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<>();
        expected.add(Locale.US);
        expected.add(Locale.CANADA);

        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
    }

    @Test
    public void testCountriesByLanguageNullLanguage() {
        List<Locale> expected = Collections.emptyList();

        assertEquals(expected, LocaleUtils.countriesByLanguage(null));
    }
}