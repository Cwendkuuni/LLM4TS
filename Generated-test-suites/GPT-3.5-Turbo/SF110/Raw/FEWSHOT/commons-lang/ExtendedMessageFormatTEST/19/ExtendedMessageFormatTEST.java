
package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.junit.Test;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor1() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("", Locale.getDefault(), null);
        assertNotNull(format);
    }

    @Test
    public void testConstructor2() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("number", new NumberFormatFactory());
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("", Locale.getDefault(), registry);
        assertNotNull(format);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        assertEquals("Hello {0}", format.toPattern());
    }

    @Test
    public void testApplyPattern() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("Today is {0,date}");
        format.applyPattern("Today is {0,date,yyyy-MM-dd}");
        assertEquals("Today is {0,date,yyyy-MM-dd}", format.toPattern());
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello");
        assertEquals(format1, format2);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello");
        assertEquals(format1.hashCode(), format2.hashCode());
    }

    private static class NumberFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            if (name.equals("number")) {
                return NumberFormat.getInstance(locale);
            }
            return null;
        }
    }

    private static class DateFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            if (name.equals("date")) {
                return new SimpleDateFormat(arguments, locale);
            }
            return null;
        }
    }
}
