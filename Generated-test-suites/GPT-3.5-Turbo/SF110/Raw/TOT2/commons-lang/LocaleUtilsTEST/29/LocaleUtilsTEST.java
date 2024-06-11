import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullString() {
        Locale result = LocaleUtils.toLocale(null);
        assertNull(result);
    }
    
    @Test
    public void testToLocale_EmptyString() {
        Locale result = LocaleUtils.toLocale("");
        assertNotNull(result);
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test
    public void testToLocale_InvalidFormat() {
        String str = "en#US";
        try {
            LocaleUtils.toLocale(str);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + str, e.getMessage());
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.contains(new Locale("en", "US")));
        assertTrue(result.contains(new Locale("en", "")));
        assertTrue(result.contains(locale));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    // Add more test cases for other methods as per the requirements

}