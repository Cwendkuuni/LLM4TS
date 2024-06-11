import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        String input1 = "en";
        Locale expected1 = new Locale("en", "");
        Locale result1 = LocaleUtils.toLocale(input1);
        assertEquals(expected1, result1);

        String input2 = "en_GB";
        Locale expected2 = new Locale("en", "GB");
        Locale result2 = LocaleUtils.toLocale(input2);
        assertEquals(expected2, result2);

        String input3 = "en_GB_XXX";
        Locale expected3 = new Locale("en", "GB", "XXX");
        Locale result3 = LocaleUtils.toLocale(input3);
        assertEquals(expected3, result3);

        // Test invalid inputs
        String input4 = "";
        Locale result4 = LocaleUtils.toLocale(input4);
        assertNull(result4);

        String input5 = "invalidFormat";
        try {
            Locale result5 = LocaleUtils.toLocale(input5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        String input6 = "en_aA"; // invalid language code
        try {
            Locale result6 = LocaleUtils.toLocale(input6);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        String input7 = "en_ZZ"; // invalid country code
        try {
            Locale result7 = LocaleUtils.toLocale(input7);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        String input8 = "en_aA_ZZ"; // invalid variant code
        try {
            Locale result8 = LocaleUtils.toLocale(input8);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid inputs
        Locale locale = new Locale("fr", "CA", "xxx");
        List list = LocaleUtils.localeLookupList(locale);
        List expected = Arrays.asList(
                new Locale("fr","CA","xxx"),
                new Locale("fr","CA"),
                new Locale("fr"),
                new Locale("en")
        );
        assertEquals(expected, list);

        // Test empty list
        Locale localeEmpty = null;
        List listEmpty = LocaleUtils.localeLookupList(localeEmpty);
        List expectedEmpty = Collections.emptyList();
        assertEquals(expectedEmpty, listEmpty);

        // Test default locale
        Locale localeDefault = new Locale("en");
        Locale defaultLocale = new Locale("fr");
        List listDefault = LocaleUtils.localeLookupList(localeDefault, defaultLocale);
        List expectedDefault = Arrays.asList(
                new Locale("en"),
                new Locale("fr")
        );
        assertEquals(expectedDefault, listDefault);
    }

    // Add test cases for other methods: availableLocaleList(), isAvailableLocale(), languagesByCountry(), countriesByLanguage()
}