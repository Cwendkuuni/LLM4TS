import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;

public class LocaleUtilsTest {
  
    @Test
    public void testToLocale() {
        // Test valid input
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        
        // Test invalid input
        try {
            LocaleUtils.toLocale("EN");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("eng_GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
  
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.ENGLISH;
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertEquals(expected, availableLocaleList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        assertEquals(expected, availableLocaleSet);
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("zz", "ZZ")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales()).stream()
                .filter(locale -> locale.getCountry().equals("US"))
                .collect(Collectors.toList());
        assertEquals(expected, LocaleUtils.languagesByCountry("US"));
        assertEquals(Collections.EMPTY_LIST, LocaleUtils.languagesByCountry("ZZ"));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales()).stream()
                .filter(locale -> locale.getLanguage().equals("en") && !locale.getCountry().isEmpty() && locale.getVariant().isEmpty())
                .collect(Collectors.toList());
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
        assertEquals(Collections.EMPTY_LIST, LocaleUtils.countriesByLanguage("zz"));
    }
  
}