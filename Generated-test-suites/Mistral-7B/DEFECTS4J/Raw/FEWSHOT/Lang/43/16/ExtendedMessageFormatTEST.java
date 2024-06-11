package org.apache.commons.lang.text;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.Util;
import org.apache.commons.lang.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

    @Test
    public void testEmpty() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{}");
        assertEquals("", format.format(null, new Object[0]));
    }

    @Test
    public void testSimple() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new Locale("en", "US"),
                new HashMap<String, FormatFactory>() {{
                    put("date", () -> new java.text.SimpleDateFormat("dd/MM/yy"));
                }});
        assertEquals("12/01/19 test", format.format(new Object[] {"test", new java.util.Date()}, new ParsePosition(0)));
    }

    @Test
    public void testOptional() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,currency}[{1}]", new Locale("en", "US"),
                new HashMap<String, FormatFactory>() {{
                    put("currency", () -> new DecimalFormat("#,##0.00"));
                }});
        assertEquals("1234.56 test $1234.56", format.format(new Object[] {"test", 1234.56}, new ParsePosition(0)));
        assertEquals("1234.56 test", format.format(new Object[] {"test"}, new ParsePosition(0)));
    }

    @Test
    public void testNested() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} [{1,choice,0}{2}|{3}]", new Locale("en", "US"),
                new HashMap<String, FormatFactory>() {{
                    put("date", () -> new SimpleDateFormat("dd/MM/yy"));
                    put("time", () -> new SimpleTimeFormat("HH:mm"));
                }});
        assertEquals("12/01/19 12:01 test 12:01 AM", format.format(new Object[] {"test", "AM"}, new ParsePosition(0)));
        assertEquals("12/01/19 12:01 test 12:01 PM", format.format(new Object[] {"test", "PM"}, new ParsePosition(0)));
        assertEquals("12/01/19 12:01 test", format.format(new Object[] {"test"}, new ParsePosition(0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}{1}", new Locale("en", "US"),
                new HashMap<String, FormatFactory>() {{
                    put("date", () -> new SimpleDateFormat("dd/MM/yy"));
                }});
        format.applyPattern("{0}{1}{2}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingArgument() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}{1}", new Locale("en", "US"),
                new HashMap<String, FormatFactory>() {{
                    put("date", () -> new SimpleDateFormat("dd/MM/yy"));
                }});
        format.applyPattern("{2}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}{1}", new Locale("en", "US"),
                new HashMap<String, FormatFactory>() {{
                    put("date", () -> new SimpleDateFormat("dd/MM/yy"));
                }});
        format.applyPattern("{invalid}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgument() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}{1}", new Locale("en", "US"),
                new HashMap<String, FormatFactory>() {{
                    put("date", () -> new SimpleDateFormat("dd/MM/yy"));
                }});
        format.applyPattern("{0foo}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}{1}", new Locale("en", "US"),
                new HashMap<String, FormatFactory>() {{
                    put("date", () -> new SimpleDateFormat("dd/MM/yy"));
                }});
        format.applyPattern("{2}");
    }

    static interface FormatFactory {
        Format getFormat(String name, String style, Locale locale);
    }
}