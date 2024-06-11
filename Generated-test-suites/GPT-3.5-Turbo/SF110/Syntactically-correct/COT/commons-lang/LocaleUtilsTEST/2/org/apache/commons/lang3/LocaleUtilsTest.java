
import org.junit.Test;
import java.util.Locale;
import java.util.List;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput_ReturnNull() {
        String str = null;
        Locale result = LocaleUtils.toLocale(str);
        assertNull(result);
    }

    @Test
    public void testToLocale_EmptyStringInput_ReturnLocaleWithEmptyLanguageAndCountry() {
        String str = "";
        Locale result = LocaleUtils.toLocale(str);
        assertNotNull(result);
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test
    public void testToLocale_InvalidFormatWithHashSymbol_ThrowIllegalArgumentException() {
        String str = "en#US";
        assertThrows(IllegalArgumentException.class, () -> {
            LocaleUtils.toLocale(str);
        });
    }

    @Test
    public void testToLocale_InvalidFormatWithLessThan2Characters_ThrowIllegalArgumentException() {
        String str = "e";
        assertThrows(IllegalArgumentException.class, () -> {
            LocaleUtils.toLocale(str);
        });
    }

    // Add more test cases for other public methods in LocaleUtils class
    
}