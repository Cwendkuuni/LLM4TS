package org.apache.commons.lang.text;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.cli.util.ExtendedMessageFormat;
import org.apache.commons.cli.util.FormatFactory;
import org.apache.commons.cli.util.FormatFactoryImpl;
import org.apache.commons.lang.StringUtils;

public class ExtendedMessageFormatTest {

    private static final String TEST_PATTERN = "{0,date,short}{1}{2,choice,0}{2,choice,1}{2,choice,2}";
    private static final String TEST_PATTERN_WITH_CUSTOM_FORMAT = "{0,date,short}{1}{2,format,myCustomFormat}";

    @Test
    public void testExtendedMessageFormatDefaultLocale() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(TEST_PATTERN);
        assertNotNull(messageFormat);
        String result = messageFormat.format(new Object[]{"2023-03-13", "foo", "zero"});
        assertEquals("03/13/2023foozero", result);
    }

    @Test
    public void testExtendedMessageFormatCustomLocale() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(TEST_PATTERN, Locale.US);
        assertNotNull(messageFormat);
        String result = messageFormat.format(new Object[]{"2023-03-13", "foo", "zero"});
        assertEquals("03-13-2023 foo zero", result);
    }

    @Test
    public void testExtendedMessageFormatCustomRegistry() {
        FormatFactory registry = new HashMap<>();
        registry.put("myCustomFormat", new FormatFactoryImpl() {
            @Override
            public Format getFormat(String pattern, Object[] arguments, Locale locale) {
                return new MyCustomFormat();
            }
        });

        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(TEST_PATTERN_WITH_CUSTOM_FORMAT, null, registry);
        assertNotNull(messageFormat);
        String result = messageFormat.format(new Object[]{"2023-03-13", "foo", "zero"});
        assertEquals("03/13/2023foo{MyCustomFormat}zero", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtendedMessageFormatBadPattern() {
        new ExtendedMessageFormat("{");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtendedMessageFormatMissingFormatName() {
        new ExtendedMessageFormat("{0,date,short}{1}{2,choice}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtendedMessageFormatMissingFormatStyle() {
        new ExtendedMessageFormat("{0,date,short,format,myFormat}{1}{2,choice}");
    }

    @Test
    public void testExtendedMessageFormatParsePosition() throws ParseException {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(TEST_PATTERN);
        assertNotNull(messageFormat);

        ParsePosition pos = new ParsePosition(0);
        messageFormat.parse(TEST_PATTERN, pos);
        assertEquals(TEST_PATTERN.length(), pos.getIndex());

        messageFormat.applyPattern(TEST_PATTERN);
        String pattern = messageFormat.toPattern();
        assertEquals(TEST_PATTERN, pattern);
    }

    @Test
    public void testExtendedMessageFormatFormat() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(TEST_PATTERN);
        assertNotNull(messageFormat);

        String result = messageFormat.format(new Object[]{"2023-03-13", "foo", "zero"});
        assertEquals("03/13/2023foozero", result);
    }

    @Test
    public void testExtendedMessageFormatSetFormat() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testExtendedMessageFormatSetFormatByArgumentIndex() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testExtendedMessageFormatSetFormats() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testExtendedMessageFormatSetFormatsByArgumentIndex() {
        throw new UnsupportedOperationException();
    }

    static class MyCustomFormat extends MessageFormat {
        public MyCustomFormat() {
            super(DUMMY_PATTERN, Locale.getDefault());
        }

        @Override
        public String format(Object obj, String format, Object... args) {
            return "MyCustomFormat";
        }

        @Override
        public ParsePosition parse(String source, ParsePosition pos) {
            return new ParsePosition(pos.getIndex());
        }
    }
}

class FormatFactoryImpl implements FormatFactory {
    @Override
    public Format getFormat(String pattern, Object[] arguments, Locale locale) {
        throw new UnsupportedOperationException();
    }
}