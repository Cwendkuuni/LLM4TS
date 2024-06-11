
import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString_ReturnsNull() {
        Locale locale = LocaleUtils.toLocale(null);
        assertNull(locale);
    }

    @Test
    public void testToLocale_EmptyString_ReturnsEmptyLocale() {
        Locale locale = LocaleUtils.toLocale("");
        assertEquals("", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ExceptionThrown() {
        LocaleUtils.toLocale("##");
    }
}
