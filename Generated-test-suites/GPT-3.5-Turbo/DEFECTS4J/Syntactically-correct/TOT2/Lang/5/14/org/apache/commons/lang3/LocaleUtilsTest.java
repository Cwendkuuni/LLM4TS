import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Typical use cases
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Edge cases
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testLocaleLookupList() {
        // Typical use cases
        assertEquals(4, LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx")).size());

        // Edge cases
        assertEquals(1, LocaleUtils.localeLookupList(null).size());
    }

    @Test
    public void testAvailableLocaleList() {
        // Typical use cases
        assertEquals(true, LocaleUtils.availableLocaleList().contains(new Locale("en")));

        // Edge cases
        assertEquals(false, LocaleUtils.availableLocaleList().contains(new Locale("xx")));
    }

    @Test
    public void testAvailableLocaleSet() {
        // Typical use cases
        assertTrue(LocaleUtils.availableLocaleSet().contains(new Locale("fr")));
        assertFalse(LocaleUtils.availableLocaleSet().contains(new Locale("xx")));
    }

    @Test
    public void testIsAvailableLocale() {
        // Typical use cases
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en_US")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));

        // Edge cases
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

}