import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expected, LocaleUtils.localeLookupList(locale));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(LocaleUtils.availableLocaleList());
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz", "ZZ")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales()).stream()
            .filter(l -> l.getCountry().equals("US") && l.getVariant().isEmpty())
            .collect(Collectors.toList());
        assertEquals(expected, LocaleUtils.languagesByCountry("US"));
    }

    @Test
    public void testLanguagesByCountryNull() {
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales()).stream()
            .filter(l -> l.getLanguage().equals("en") && !l.getCountry().isEmpty() && l.getVariant().isEmpty())
            .collect(Collectors.toList());
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
    }

    @Test
    public void testCountriesByLanguageNull() {
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.countriesByLanguage(null));
    }

}