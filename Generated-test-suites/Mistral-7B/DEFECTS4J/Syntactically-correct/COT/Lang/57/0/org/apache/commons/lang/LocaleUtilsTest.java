import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_FR", LocaleUtils.toLocale("fr_FR").toString());

        // Edge cases and exception handling tests
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_"); // missing country code
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xX"); // invalid country code
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx"); // country code too long
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx"); // variant too long
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_XX"); // country code has wrong case
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx"); // variant has wrong case
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx_xx"); // variant too long
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx_xx_xx"); // variant too long
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US__"); // missing language code
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("__US"); // missing language code
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx_xx_xx_xx"); // variant too long
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_xx_xx_xx_xx_xx_xx"); // variant too long
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality tests
        Locale locale = new Locale("fr", "CA", "xxx");
        List list = LocaleUtils.localeLookupList(locale);
        assertEquals(3, list.size());
        assertEquals(locale, list.get(0));
        assertEquals("fr_CA", list.get(1).toString());
        assertEquals("fr", list.get(2).toString());

        locale = new Locale("fr");
        list = LocaleUtils.localeLookupList(locale, Locale.getDefault());
        assertEquals(3, list.size());
        assertEquals(locale, list.get(0));
        assertEquals("fr_FR", list.get(1).toString());
        assertEquals(Locale.getDefault().toString(), list.get(2).toString());

        // Edge cases and exception handling tests
        try {
            LocaleUtils.localeLookupList(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList("");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA", "xxx"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(Locale.getDefault(), null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(Locale.getDefault(), new Locale("fr", "CA", "xxx"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List list = LocaleUtils.availableLocaleList();
        assertTrue("Size should be greater than zero", list.size() > 0);

        // Edge cases and exception handling tests
        try {
            LocaleUtils.availableLocaleList(); // simple call
        } catch (Exception e) {
            fail("Expected no exception");
        }

        try {
            LocaleUtils.availableLocaleList(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Basic functionality test
        Set set = LocaleUtils.availableLocaleSet();
        assertTrue("Size should be greater than zero", set.size() > 0);

        // Edge cases and exception handling tests
        try {
            LocaleUtils.availableLocaleSet(); // simple call
        } catch (Exception e) {
            fail("Expected no exception");
        }

        try {
            LocaleUtils.availableLocaleSet(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality tests
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr", "CA", "xxx")));

        // Edge cases and exception handling tests
        try {
            LocaleUtils.isAvailableLocale(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            LocaleUtils.isAvailableLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.isAvailableLocale(new Locale("fr", "CA", "xxx").toString());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality tests
        List list = LocaleUtils.languagesByCountry("CA");
        assertTrue("Size should be greater than zero", list.size() > 0);
        assertEquals("en", list.get(0).getLanguage());
        assertEquals("fr", list.get(1).getLanguage());

        // Edge cases and exception handling tests
        try {
            LocaleUtils.languagesByCountry(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            LocaleUtils.languagesByCountry("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.languagesByCountry("xx");
            fail("Expected empty list");
        } catch (Exception e) {
            // Expected
        }

        try {
            LocaleUtils.languagesByCountry("CA_XX"); // invalid country code
            fail("Expected empty list");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality tests
        List list = LocaleUtils.countriesByLanguage("en");
        assertTrue("Size should be greater than zero", list.size() > 0);
        assertEquals("CA", list.get(0).getCountry());
        assertEquals("GB", list.get(1).getCountry());

        // Edge cases and exception handling tests
        try {
            LocaleUtils.countriesByLanguage(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            LocaleUtils.countriesByLanguage("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.countriesByLanguage("xx");
            fail("Expected empty list");
        } catch (Exception e) {
            // Expected
        }

        try {
            LocaleUtils.countriesByLanguage("en_XX"); // invalid language code
            fail("Expected empty list");
        } catch (Exception e) {
            // Expected
        }
    }
}