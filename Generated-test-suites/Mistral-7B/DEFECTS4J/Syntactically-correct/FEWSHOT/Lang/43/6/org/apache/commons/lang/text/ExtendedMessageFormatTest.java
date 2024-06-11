package org.apache.commons.lang.text;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.FormatFactory;
import org.apache.commons.cli.SimpleFormat;
import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.StrMatcher;

public class ExtendedMessageFormatTest {

    @Test
    public void testNewInstance() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}{1}");
        assertEquals("", fmt.toPattern());
        assertNull(fmt.getFormat(0));
        assertNull(fmt.getFormat(1));
    }

    @Test
    public void testNewInstanceWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomFormat", (messageKey, format, locale) -> new SimpleFormat(format));

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{CustomFormat}{0}{1}", registry);
        assertEquals("{CustomFormat}{0}{1}", fmt.toPattern());
        assertNotNull(fmt.getFormat("CustomFormat"));
        assertNull(fmt.getFormat(0));
        assertNull(fmt.getFormat(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        new ExtendedMessageFormat("{x}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatDescription() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomFormat", (messageKey, format, locale) -> new SimpleFormat(format));

        new ExtendedMessageFormat("{CustomFormat}{}");
    }

    @Test
    public void testFormatElement() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomFormat", (messageKey, format, locale) -> new SimpleFormat("{0}"));

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{CustomFormat}{0}{1}", registry);
        assertEquals("{CustomFormat}{0}{1}", fmt.toPattern());
        assertNotNull(fmt.getFormat("CustomFormat"));
        assertNotNull(fmt.getFormat(0));
        assertNotNull(fmt.getFormat(1));
    }

    @Test
    public void testFormatElementWithArgumentIndex() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomFormat", (messageKey, format, locale) -> new SimpleFormat("{1}"));

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{CustomFormat}{0}{1}", registry);
        assertEquals("{CustomFormat}{0}{1}", fmt.toPattern());
        assertNotNull(fmt.getFormat("CustomFormat"));
        assertNotNull(fmt.getFormat(1));
        assertNull(fmt.getFormat(0));
    }

    @Test
    public void testFormatElementWithFormatNameAndStyle() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomFormat", (messageKey, format, locale) -> new SimpleFormat("{1}, {0}"));

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{CustomFormat}{0}{1}", registry);
        assertEquals("{CustomFormat}{0}{1}", fmt.toPattern());
        Format format = fmt.getFormat("CustomFormat");
        assertNotNull(format);
        SimpleFormat simpleFormat = (SimpleFormat) format;
        assertEquals("{1}, {0}", simpleFormat.getPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}{1}", null);
        fmt.setFormat(0, new SimpleFormat("{2}"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}{1}", null);
        fmt.setFormatByArgumentIndex(0, new SimpleFormat("{2}"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}{1}", null);
        Format[] formats = {new SimpleFormat("{2}"), new SimpleFormat("{3}")};
        fmt.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}{1}", null);
        Format[] formats = {new SimpleFormat("{2}"), new SimpleFormat("{3}")};
        fmt.setFormatsByArgumentIndex(formats);
    }

    @Test
    public void testApplyPattern() throws ParseException {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomFormat", (messageKey, format, locale) -> new SimpleFormat("{0}"));

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{CustomFormat}{0}{1}", registry);
        fmt.applyPattern("{CustomFormat}{0}{1}");
        assertArrayEquals(new Object[]{"CustomFormat", "0", "1"}, fmt.getArguments());
        assertNotNull(fmt.getFormat("CustomFormat"));
        assertEquals("{0}", fmt.getFormat("CustomFormat").getPattern());
    }

    @Test
    public void testApplyPatternWithEscapedQuotes() throws ParseException {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomFormat", (messageKey, format, locale) -> new SimpleFormat("{0}"));

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{{CustomFormat}{0}{'''''''1'''''''}}", registry);
        fmt.applyPattern("{{CustomFormat}{0}{'''''''1'''''''}}");
        assertArrayEquals(new Object[]{"CustomFormat", "0", ""}, fmt.getArguments());
        assertNotNull(fmt.getFormat("CustomFormat"));
        assertEquals("{0}", fmt.getFormat("CustomFormat").getPattern());

        String pattern = fmt.toPattern();
        ParsePosition pos = new ParsePosition(0);
        SimpleFormat format = (SimpleFormat) fmt.getFormat("CustomFormat");
        Object[] args = fmt.getArguments();
        assertEquals(3, pattern.length());
        assertEquals(START_FE, pattern.charAt(0));
        assertEquals(readArgumentIndex(pattern, pos), Integer.parseInt(pattern.substring(1, 3)));
        assertEquals(START_FMT, pattern.charAt(4));
        assertEquals("CustomFormat", pattern.substring(5, 13));
        assertEquals(END_FE, pattern.charAt(13));
        assertEquals(START_FE, pattern.charAt(15));
        assertEquals(readArgumentIndex(pattern, pos), Integer.parseInt(pattern.substring(16, 18)));
        assertEquals(END_FE, pattern.charAt(18));
        assertEquals(format.getPattern(), pattern.substring(19, pattern.length()));
        assertEquals(args[0], fmt.getArgument(0));
        assertEquals(args[1], fmt.getArgument(1));
    }

    @Test
    public void testToPattern() throws ParseException {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomFormat", (messageKey, format, locale) -> new SimpleFormat("{0}"));

        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{CustomFormat}{0}{1}", registry);
        fmt.applyPattern("{CustomFormat}{0}{1}");
        assertEquals("{CustomFormat}{0}{1}", fmt.toPattern());

        fmt = new ExtendedMessageFormat("{{CustomFormat}{0}{'''''''1'''''''}}", registry);
        fmt.applyPattern("{{CustomFormat}{0}{'''''''1'''''''}}");
        assertEquals("{{CustomFormat}{0}{'''''''1'''''''}}", fmt.toPattern());
        assertFalse(fmt.containsKey("CustomFormat"));
    }

    private int readArgumentIndex(String pattern, ParsePosition pos) {
        ExtendedMessageFormat.ExtendedMessageFormatTest.parseArgumentIndex(pattern, pos);
        return pos.getIndex();
    }
}

class SimpleFormat implements Format {
    private final String pattern;

    public SimpleFormat(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String format(Object obj, Locale locale) {
        return StrMatcher.format(pattern, obj);
    }

    @Override
    public ParsePosition parse(String source, ParsePosition pos) {
        ParsePosition pos1 = new ParsePosition(pos.getIndex());
        StrMatcher.parsePattern(pattern, source, pos1);
        if (pos1.getIndex() > pos.getIndex()) {
            pos.setIndex(pos1.getIndex());
        }
        return pos;
    }

    @Override
    public String getPattern() {
        return pattern;
    }
}

class StrMatcher {
    private static final int FORMAT_START = 0;
    private static final int FORMAT_END = 1;
    private static final int ARGUMENT_INDEX_START = 2;
    private static final int ARGUMENT_INDEX_END = 3;
    private static final int ESCAPE_SEQUENCE_START = 4;
    private static final int ESCAPE_SEQUENCE_END = 5;
    private static final int ARGUMENT_START = 6;
    private static final int ARGUMENT_END = 7;
    private static final int QUOTE = 8;
    private static final int OTHER = 9;

    public static void parsePattern(String pattern, String source, ParsePosition pos) {
        int state = FORMAT_START;
        int index = pos.getIndex();
        int length = source.length();

        while (index < length) {
            switch (state) {
            case FORMAT_START: {
                if (source.charAt(index) == START_FE) {
                    state = ARGUMENT_INDEX_START;
                    next(pos);
                } else {
                    appendQuotedString(source, pos, null, false);
                    next(pos);
                }
                break;
            }
            case ARGUMENT_INDEX_START: {
                int argumentIndexStart = index;
                int argumentIndexEnd = index;
                next(pos);
                while (Character.isDigit(source.charAt(pos.getIndex()))) {
                    argumentIndexEnd = pos.getIndex();
                    next(pos);
                }
                pos.setIndex(argumentIndexEnd);
                setIndex(index, argumentIndexStart, argumentIndexEnd);
                state = ESCAPE_SEQUENCE_START;
                break;
            }
            case ESCAPE_SEQUENCE_START: {
                if (source.charAt(pos.getIndex()) == ESCAPED_QUOTE) {
                    state = QUOTE;
                    next(pos);
                } else {
                    setIndex(index, argumentIndexEnd, pos.getIndex());
                    state = FORMAT_END;
                }
                break;
            }
            case QUOTE: {
                setIndex(index, argumentIndexEnd, pos.getIndex());
                state = FORMAT_END;
                break;
            }
            case FORMAT_END: {
                if (source.charAt(pos.getIndex()) == END_FE) {
                    next(pos);
                    int argumentIndex = Integer.parseInt(source.substring(argumentIndexStart, argumentIndexEnd));
                    setIndex(index, argumentIndexEnd, pos.getIndex());
                    state = ARGUMENT_START;
                    break;
                }
                appendQuotedString(source, pos, null, false);
                break;
            }
            case ARGUMENT_START: {
                if (source.charAt(pos.getIndex()) == START_FMT) {
                    state = FORMAT_START;
                    next(pos);
                    break;
                }
                appendQuotedString(source, pos, null, false);
                break;
            }
            default:
                appendChar(source.charAt(index), pos);
                next(pos);
            }
        }
    }

    public static void parseArgumentIndex(String pattern, ParsePosition pos) {
        int state = ARGUMENT_INDEX_START;
        int index = pos.getIndex();

        while (Character.isDigit(pattern.charAt(index))) {
            next(pos);
        }

        pos.setIndex(pos.getIndex() - 1);
    }

    public static void format(String pattern, Object obj) {
        int length = pattern.length();
        int index = 0;
        StringBuilder sb = new StringBuilder(length);

        while (index < length) {
            if (pattern.charAt(index) == QUOTE) {
                sb.append(QUOTE);
                index++;
                continue;
            }
            if (pattern.charAt(index) == START_FE) {
                int argumentIndex = Integer.parseInt(pattern.substring(index + 1, index + 3));
                sb.append(String.valueOf(obj));
                index += 3;
                continue;
            }
            sb.append(pattern.charAt(index));
            index++;
        }

        System.out.println(sb.toString());
    }

    private static void setIndex(int oldIndex, int newIndexStart, int newIndexEnd) {
        ParsePosition pos = new ParsePosition(oldIndex);
        pos.setIndex(newIndexEnd);
    }

    private static void appendQuotedString(String source, ParsePosition pos, StringBuffer sb, boolean escapingOn) {
        int index = pos.getIndex();
        int length = source.length();

        if (escapingOn && source.charAt(index) == QUOTE) {
            if (sb != null) {
                sb.append(QUOTE);
            }
            next(pos);
            return;
        }

        int lastHold = index;
        for (int i = index; i < length; i++) {
            if (escapingOn && source.substring(i).startsWith(ESCAPED_QUOTE)) {
                if (sb != null) {
                    sb.append(QUOTE);
                }
                pos.setIndex(i + ESCAPED_QUOTE.length());
                lastHold = pos.getIndex();
                continue;
            }

            if (source.charAt(i) == QUOTE) {
                if (sb != null) {
                    sb.append(QUOTE);
                }
                next(pos);
                return;
            }

            next(pos);
        }

        throw new IllegalArgumentException(
                "Unterminated quoted string at position " + lastHold);
    }

    private static void appendChar(char c, ParsePosition pos) {
        sb.append(c);
        next(pos);
    }

    private static void next(ParsePosition pos) {
        pos.setIndex(pos.getIndex() + 1);
    }

    private static StringBuilder sb = new StringBuilder();
}