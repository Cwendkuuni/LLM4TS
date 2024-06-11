package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("#"));
    }

    // Add more test cases for the remaining scenarios in the toLocale method

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> expectedList = List.of(locale, new Locale("en", ""));
        assertEquals(expectedList, LocaleUtils.localeLookupList(locale));
    }

    // Add more test cases for the remaining methods in the LocaleUtils class

}