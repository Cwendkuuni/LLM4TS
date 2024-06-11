import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(Locale.ENGLISH, locale1);

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(Locale.UK, locale2);

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale3);
    }

    @Test
    public void testToLocaleInvalidFormat() {
        try {
            LocaleUtils.toLocale("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(3, result.size());
        assertEquals(new Locale("fr", "CA", "xxx"), result.get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertNotNull(locales);
        assertTrue(locales.size() > 0);
    }

    // Additional test cases for other methods can be added here
    
}