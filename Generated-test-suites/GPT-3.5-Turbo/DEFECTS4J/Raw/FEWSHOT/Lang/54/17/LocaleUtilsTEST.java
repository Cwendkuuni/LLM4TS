import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocaleUtilsTest {
    
    // Test toLocale method
    @Test
    public void testToLocale() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("fr", "CA", "xxx"), LocaleUtils.toLocale("fr_CA_xxx"));
    }

    // Test localeLookupList method
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("fr", "CA", "xxx"));
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        expected.add(Locale.getDefault());
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, result);
    }

    // Test availableLocaleList method
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en"));
        expected.add(new Locale("en", "GB"));
        expected.add(new Locale("fr", "CA"));
        expected.add(Locale.getDefault());
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(expected, result);
    }

    // Test availableLocaleSet method
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>();
        expected.add(new Locale("en"));
        expected.add(new Locale("en", "GB"));
        expected.add(new Locale("fr", "CA"));
        expected.add(Locale.getDefault());
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertEquals(expected, result);
    }

    // Test isAvailableLocale method
    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("de")));
    }

    // Test languagesByCountry method
    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("fr", "CA"));
        List<Locale> result = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, result);
    }

    // Test countriesByLanguage method
    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "GB"));
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, result);
    }
    
}