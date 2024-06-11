import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString_ReturnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyString_ReturnLocaleWithEmptyLanguageAndCountry() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ExceptionThrown() {
        LocaleUtils.toLocale("en#US");
    }

    // Add more test cases for edge cases and exceptions if needed

    // Tests for localeLookupList methods

    @Test
    public void testLocaleLookupList_SameLocale_ReturnListWithSingleLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(1, result.size());
        assertEquals(locale, result.get(0));
    }

    // Add more test cases for other methods

}