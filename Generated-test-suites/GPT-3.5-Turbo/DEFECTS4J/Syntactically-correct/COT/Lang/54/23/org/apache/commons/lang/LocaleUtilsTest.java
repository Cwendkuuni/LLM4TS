import org.junit.Test;
import org.apache.commons.lang.LocaleUtils;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // basic test cases
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals("en", locale1.getLanguage());
        assertEquals("", locale1.getCountry());

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale2.getLanguage());
        assertEquals("GB", locale2.getCountry());

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale3.getLanguage());
        assertEquals("GB", locale3.getCountry());
        assertEquals("xxx", locale3.getVariant());

        // test invalid format
        try {
            Locale locale4 = LocaleUtils.toLocale("eng");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // test locale with variant
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> result1 = LocaleUtils.localeLookupList(locale1);
        assertEquals(3, result1.size());
        assertEquals(locale1, result1.get(0));
        assertEquals(new Locale("fr", "CA"), result1.get(1));
        assertEquals(new Locale("fr"), result1.get(2));

        // test locale without variant
        Locale locale2 = new Locale("en");
        Locale defaultLocale = new Locale("es");
        List<Locale> result2 = LocaleUtils.localeLookupList(locale2, defaultLocale);
        assertEquals(2, result2.size());
        assertEquals(locale2, result2.get(0));
        assertEquals(defaultLocale, result2.get(1));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertTrue(result instanceof Set);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));

        Locale locale2 = new Locale("ja");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("fr");
        assertNotNull(result);
        assertTrue(result instanceof List);
        assertFalse(result.isEmpty());
    }
}