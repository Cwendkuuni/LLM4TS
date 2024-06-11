import static org.junit.Assert.*;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_basic() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en"), locale);
    }

    @Test
    public void testToLocale_country() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale);
    }

    @Test
    public void testToLocale_variant() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_invalidFormat() {
        Locale locale = LocaleUtils.toLocale("INVALID");
    }

    @Test
    public void testLocaleLookupList_startFromLocale() {
        List<Locale> list = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertEquals(3, list.size());
        assertEquals(new Locale("fr", "CA", "xxx"), list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
    }

    @Test
    public void testLocaleLookupList_startFromLocale_defaultLocale() {
        List<Locale> list = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertEquals(4, list.size());
        assertEquals(new Locale("fr", "CA", "xxx"), list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
        assertEquals(new Locale("en"), list.get(3));
    }

    // Additional test cases for other methods in LocaleUtils class can be added here

}