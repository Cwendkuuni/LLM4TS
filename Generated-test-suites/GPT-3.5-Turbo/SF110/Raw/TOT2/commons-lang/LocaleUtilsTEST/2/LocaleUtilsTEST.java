import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;

import java.util.Locale;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_invalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            LocaleUtils.toLocale("#");
        });
    }

    @Test
    public void testLocaleLookupList_bothNull() {
        Locale locale = null;
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(0, result.size());
    }

    // Add more test cases...

    @Test
    public void testToLocale_emptyInput() {
        Locale result = LocaleUtils.toLocale("");
        assertEquals("", result.getLanguage());
        assertEquals("", result.getCountry());
    }

    @Test
    public void testLocaleLookupList_localeNull() {
        List<Locale> result = LocaleUtils.localeLookupList(null, new Locale("en", "US"));
        assertEquals(1, result.size());
    }

    // Add more test cases...

}