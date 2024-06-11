import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("en", LocaleUtils.toLocale("en").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());

        // Edge cases and exception handling tests
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("abcd");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_AAA");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_zz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_zzz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test localeLookupList
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("en", "US"));
        assertEquals(4, localeList.size());
        assertEquals("en_US", localeList.get(0).toString());
        assertEquals("en_US", localeList.get(1).toString());
        assertEquals("en_US", localeList.get(2).toString());
        assertEquals("en", localeList.get(3).toString());

        List<Locale> localeList2 = LocaleUtils.localeLookupList(new Locale("es"), new Locale("es"));
        assertEquals(3, localeList2.size());
        assertEquals("es_ES", localeList2.get(0).toString());
        assertEquals("es_ES", localeList2.get(1).toString());
        assertEquals("es", localeList2.get(2).toString());

        List<Locale> localeList3 = LocaleUtils.localeLookupList(null, new Locale("en"));
        assertEquals(1, localeList3.size());
        assertEquals("en", localeList3.get(0).toString());

        List<Locale> localeList4 = LocaleUtils.localeLookupList(new Locale("fr", "CA"), null);
        assertEquals(3, localeList4.size());
        assertEquals("fr_CA", localeList4.get(0).toString());
        assertEquals("fr_CA", localeList4.get(1).toString());
        assertEquals("fr", localeList4.get(2).toString());

        List<Locale> localeList5 = LocaleUtils.localeLookupList(new Locale("es"), new Locale("en"));
        assertEquals(1, localeList5.size());
        assertEquals("en", localeList5.get(0).toString());

        // Test isAvailableLocale
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "AR")));

        // Test languagesByCountry
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertEquals(19, languages.size());

        // Test countriesByLanguage
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(53, countries.size());
    }

}