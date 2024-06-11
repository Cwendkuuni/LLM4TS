import org.junit.Test;
import java.util.Locale;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(null, LocaleUtils.toLocale(null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en-GB");
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        assertEquals(expected, LocaleUtils.localeLookupList(locale, defaultLocale));
    }
    
    @Test
    public void testLocaleLookupListWithNull() {
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(defaultLocale);
        assertEquals(expected, LocaleUtils.localeLookupList(null, defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, LocaleUtils.availableLocaleList());
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expected, LocaleUtils.availableLocaleSet());
    }
    
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"));
        assertEquals(expected, LocaleUtils.languagesByCountry("US"));
    }
    
    @Test
    public void testLanguagesByCountryWithNull() {
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.languagesByCountry(null));
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("fr", "CA"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));
    }
    
    @Test
    public void testCountriesByLanguageWithNull() {
        List<Locale> expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.countriesByLanguage(null));
    }
}