import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        String input = "en_GB";
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale(input);
        assertEquals(expected, actual);

        // Edge case: empty input
        input = "";
        actual = LocaleUtils.toLocale(input);
        assertNull(actual);

        // Edge case: invalid input length
        input = "en_GB_";
        try {
            actual = LocaleUtils.toLocale(input);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Edge case: invalid input format
        input = "en_GB_xx";
        try {
            actual = LocaleUtils.toLocale(input);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        // Edge case: null input
        locale = null;
        expected = Collections.emptyList();
        actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        // Edge case: defaultLocale is null
        Locale defaultLocale = null;
        expected = Collections.emptyList();
        actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);

        // Edge case: invalid locale format
        locale = new Locale("fr", "CA", "");
        expected = Collections.singletonList(locale);
        actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Edge case: null input
        locale = null;
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        // Edge case: invalid locale format
        locale = new Locale("fr", "CA", "");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountry() {
        // Edge case: null input
        String input = null;
        LocaleUtils.languagesByCountry(input);
    }

    // Add similar test cases for other methods: countriesByLanguage(), availableLocaleList(), availableLocaleSet(), isAvailableLocale()

}