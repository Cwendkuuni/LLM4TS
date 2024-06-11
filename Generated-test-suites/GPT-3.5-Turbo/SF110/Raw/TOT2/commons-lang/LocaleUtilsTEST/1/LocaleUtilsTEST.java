import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString_ReturnNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyString_ReturnEmptyLocale() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ThrowException() {
        LocaleUtils.toLocale("#invalid");
    }

    // Additional test cases for other branches of the toLocale method can be added here

    @Test
    public void testLocaleLookupList_WithDefaultLocale_ReturnListWithLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("fr", "FR");
        assertEquals(Collections.singletonList(locale), LocaleUtils.localeLookupList(locale, defaultLocale));
    }

    // Additional test cases for other methods can be added here

    // Test cases for edge cases and exception handling scenarios can be added as well

}