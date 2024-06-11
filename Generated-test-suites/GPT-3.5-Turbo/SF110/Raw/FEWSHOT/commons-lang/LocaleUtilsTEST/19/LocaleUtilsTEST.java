import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        assertEquals(null, LocaleUtils.toLocale(null));
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en_"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("", "US"), LocaleUtils.toLocale("_US"));
        assertEquals(new Locale("en", "US", "POSIX"), LocaleUtils.toLocale("en_US_POSIX"));
        
        try {
            LocaleUtils.toLocale("#invalid");
            assertTrue(false);
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }

        try {
            LocaleUtils.toLocale("e");
            assertTrue(false);
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }

        try {
            LocaleUtils.toLocale("_u");
            assertTrue(false);
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }

        try {
            LocaleUtils.toLocale("_U");
            assertTrue(false);
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }

        try {
            LocaleUtils.toLocale("en_US_i");
            assertTrue(false);
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }

        try {
            LocaleUtils.toLocale("_US_PS");
            assertTrue(false);
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US", "POSIX");
        Locale defaultLocale = new Locale("en", "GB");

        List<Locale> expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", ""));
        expected.add(defaultLocale);

        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, result);

        result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, result);
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = new ArrayList<>(LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST);
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = new HashSet<>(LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET);
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("en", "UM"));
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertEquals(expected, result);

        result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = new ArrayList<>();
        expected.add(new Locale("en", "US"));
        expected.add(new Locale("es", "US"));
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, result);

        result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }
}