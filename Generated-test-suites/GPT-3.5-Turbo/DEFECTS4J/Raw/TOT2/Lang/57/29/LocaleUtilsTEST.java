import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(Locale.ENGLISH, locale1);
        
        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(Locale.UK, locale2);
        
        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale3.getLanguage());
        assertEquals("GB", locale3.getCountry());
        assertEquals("xxx", locale3.getVariant());
        
        // Edge case: Invalid locale format
        try {
            LocaleUtils.toLocale("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        
        // Edge case: Null input returns null
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        
        assertEquals(4, result.size());
        assertEquals(new Locale("fr", "CA", "xxx"), result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr"), result.get(2));
        assertEquals(Locale.getDefault(), result.get(3));
        
        // Edge case: Null input returns empty list
        assertTrue(LocaleUtils.localeLookupList(null).isEmpty());
    }

    // Additional test cases for other methods can be added here

}