import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testEmptyFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        String pattern = format.toPattern();
        assertEquals("", pattern);
        String result = format.format(new Object[0], new Object[0][0]);
        assertEquals("", result);
    }

    @Test
    public void testSimpleFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.ENGLISH);
        String pattern = format.toPattern();
        assertEquals("{0}, {1}", pattern);
        Object[] args = {"Hello", "World"};
        String result = format.format(args);
        assertEquals("Hello, World", result);
    }

    @Test
    public void testCustomFormat() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                return new SimpleDateFormat(args);
            }
        };
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, d 'Day {0}'}", Locale.ENGLISH, Collections.singletonMap("date", formatFactory));
        String pattern = format.toPattern();
        assertEquals("{0, date, d 'Day {0}'}", pattern);
        Date date = new Date();
        String result = format.format(new Object[]{date});
        String expected = String.format("Day %td", date);
        assertEquals(expected, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.ENGLISH);
        format.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.ENGLISH);
        format.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.ENGLISH);
        Format[] newFormats = new Format[1];
        format.setFormats(newFormats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.ENGLISH);
        Format[] newFormats = new Format[1];
        format.setFormatsByArgumentIndex(newFormats);
    }
}

class SimpleDateFormat implements Format {
    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        Date date = (Date) obj;
        toAppendTo.append(String.format("%td", date));
        return toAppendTo;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        throw new UnsupportedOperationException();
    }
}

class FormatFactory {
    @Override
    public Format getFormat(String name, String args, Locale locale) {
        if ("date".equals(name)) {
            return new SimpleDateFormat(args);
        }
        return null;
    }
}