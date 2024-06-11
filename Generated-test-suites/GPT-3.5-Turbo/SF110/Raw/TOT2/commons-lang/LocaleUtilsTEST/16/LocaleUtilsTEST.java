import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.LocaleUtils;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use case
        Locale normalLocale = LocaleUtils.toLocale("en_US");
        assertEquals("en", normalLocale.getLanguage());
        assertEquals("US", normalLocale.getCountry());

        // Edge case of empty string
        Locale emptyLocale = LocaleUtils.toLocale("");
        assertEquals("", emptyLocale.getLanguage());
        assertEquals("", emptyLocale.getCountry());

        // Error scenario of invalid locale format
        try {
            Locale invalidLocale = LocaleUtils.toLocale("test_invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use case
        Locale inputLocale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(inputLocale);
        assertEquals(4, result.size());
        assertTrue(result.contains(inputLocale));
        assertEquals("en", result.get(1).getLanguage());
        assertEquals("", result.get(1).getCountry());

        // Edge case of default locale
        Locale defaultLocale = new Locale("es", "ES");
        List<Locale> resultDefault = LocaleUtils.localeLookupList(inputLocale, defaultLocale);
        assertTrue(resultDefault.contains(defaultLocale));
    }

    // Other test cases for other methods in LocaleUtils class

}