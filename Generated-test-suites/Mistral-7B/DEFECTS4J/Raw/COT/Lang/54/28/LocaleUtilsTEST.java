import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));

        // Edge cases and exception handling tests
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException for invalid input length");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_aA");
            fail("Expected IllegalArgumentException for invalid char in language code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_AA");
            fail("Expected IllegalArgumentException for invalid char in country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_a");
            fail("Expected IllegalArgumentException for invalid separator");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_ZZ");
            fail("Expected IllegalArgumentException for invalid country code length");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_ZZ_aa");
            fail("Expected IllegalArgumentException for invalid variant length");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_ZZ_aaa");
            fail("Expected IllegalArgumentException for invalid variant length");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en"));
        List<Locale> resultList = LocaleUtils.localeLookupList(new Locale("en", "US", ""));
        assertEquals(expectedList, resultList);

        // Edge cases and exception handling tests
        try {
            LocaleUtils.localeLookupList(null);
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("", ""));
            fail("Expected IllegalArgumentException for empty input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("", "GB", ""));
            fail("Expected IllegalArgumentException for empty language code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("en", "", ""));
            fail("Expected IllegalArgumentException for empty country code");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("en", "GB", "zz"));
            fail("Expected IllegalArgumentException for invalid separator");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("en", "GB", "aaa"));
            fail("Expected IllegalArgumentException for invalid variant length");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add more test cases for other methods in the class as needed

}