import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, formatName, style=short, format=Date}{1}");
        assertNotNull(format);
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, formatName, style=short, format=Date}{1}", Locale.US);
        assertNotNull(format);
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String formatStyle, Locale locale) {
                if ("date".equals(formatName) && "short".equals(formatStyle)) {
                    return new DateFormat();
                }
                return null;
            }
        };
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, formatName, style=short, format=Date}{1}", null, Map.of("date", formatFactory));
        assertNotNull(format);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, formatName, style=short, format=Date}{1}", Locale.US);
        String pattern = format.toPattern();
        assertEquals("{0,formatName,style=short,format=Date}{1}", pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(null);
        format.applyPattern("{0}");
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatElement() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}{1}", Locale.US);
        format.applyPattern("{0}{1}{2}");
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatFactoryName() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String formatStyle, Locale locale) {
                if ("invalidFormatName".equals(formatName)) {
                    return new StringFormat();
                }
                return null;
            }
        };
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, formatName=invalidFormatName}", Locale.US, Map.of("invalidFormatName", formatFactory));
        format.applyPattern("{0}");
        fail();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, formatName=date, format=Date}{1}", Locale.US);
        format.setFormat(0, new StringFormat());
        fail();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, formatName=date, format=Date}{1}", Locale.US);
        format.setFormatByArgumentIndex(0, new StringFormat());
        fail();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, formatName=date, format=Date}{1}", Locale.US);
        Format[] formats = {new StringFormat(), new StringFormat()};
        format.setFormats(formats);
        fail();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, formatName=date, format=Date}{1}", Locale.US);
        Format[] formats = {new StringFormat(), new StringFormat()};
        format.setFormatsByArgumentIndex(formats);
        fail();
    }
}

class StringFormat implements Format {
    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        return null;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        return null;
    }
}