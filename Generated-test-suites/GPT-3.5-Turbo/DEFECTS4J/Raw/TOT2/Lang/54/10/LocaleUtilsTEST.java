import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale1);

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale2);

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale3);
    }

    @Test
    public void testToLocaleInvalidInput() {
        try {
            LocaleUtils.toLocale(null);
            fail("Exception not thrown for null input");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            LocaleUtils.toLocale("abc");
            fail("Exception not thrown for invalid input");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testLocaleLookupList() {
        List localeList1 = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr")), localeList1);

        List localeList2 = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en")), localeList2);
    }

    // Additional test cases for other methods in LocaleUtils class can be added here

}