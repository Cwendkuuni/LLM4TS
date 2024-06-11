import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        assertEquals(expectedLocale, actualLocale);

        // Edge cases and exception handling
        // Null input
        String nullInput = null;
        Locale nullInputLocale = LocaleUtils.toLocale(nullInput);
        assertNull(nullInputLocale);

        // Empty string
        String emptyString = "";
        Locale emptyStringLocale = LocaleUtils.toLocale(emptyString);
        assertEquals(Locale.getDefault(), emptyStringLocale);

        // Invalid format
        String invalidFormat = "123";
        try {
            Locale invalidFormatLocale = LocaleUtils.toLocale(invalidFormat);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidLanguage = "xx_XX";
        try {
            Locale invalidLanguageLocale = LocaleUtils.toLocale(invalidLanguage);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidCountry = "en_aA";
        try {
            Locale invalidCountryLocale = LocaleUtils.toLocale(invalidCountry);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidSeparator = "en_GBX";
        try {
            Locale invalidSeparatorLocale = LocaleUtils.toLocale(invalidSeparator);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Length
        String tooShort = "en";
        try {
            Locale tooShortLocale = LocaleUtils.toLocale(tooShort);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String tooLong = "en_GB_XX_XX";
        try {
            Locale tooLongLocale = LocaleUtils.toLocale(tooLong);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality
        String baseLocale = "fr_CA_xxx";
        Locale expectedLocale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(expectedLocale);

        Locale expectedBaseLocale = expectedLocale;
        Locale expectedCountryLocale = new Locale("fr", "CA");
        Locale expectedLanguageLocale = new Locale("fr", "");
        Locale expectedDefaultLocale = Locale.getDefault();

        assertEquals(expectedBaseLocale, localeList.get(0));
        assertEquals(expectedCountryLocale, localeList.get(1));
        assertEquals(expectedLanguageLocale, localeList.get(2));
        assertEquals(expectedDefaultLocale, localeList.get(3));

        // Empty input
        Locale emptyInput = null;
        List<Locale> emptyInputLocaleList = LocaleUtils.localeLookupList(emptyInput);
        assertTrue(emptyInputLocaleList.isEmpty());

        // Default locale only
        Locale defaultLocale = Locale.getDefault();
        List<Locale> defaultLocaleList = LocaleUtils.localeLookupList(defaultLocale);
        assertEquals(defaultLocale, defaultLocaleList.get(0));
        assertTrue(defaultLocaleList.size() == 1);

        // Null default locale
        Locale nullDefaultLocale = null;
        List<Locale> nullDefaultLocaleList = LocaleUtils.localeLookupList(nullDefaultLocale);
        assertTrue(nullDefaultLocaleList.isEmpty());
    }
}