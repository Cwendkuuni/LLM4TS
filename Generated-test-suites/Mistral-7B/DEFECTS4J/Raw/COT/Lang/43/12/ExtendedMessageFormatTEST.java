import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang.Validate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat messageFormat;
    private FormatFactory formatFactory;

    @Before
    public void setUp() {
        String pattern = "Hello {0,choice,0#World|1#Hello} {1} {2,number,integer}!";
        Map<String, FormatFactory> registry = new HashMap<>();
        formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if ("number".equals(name)) {
                    return new NumberFormat("0123456789") {
                        @Override
                        public String format(Object obj) {
                            int number = ((Number) obj).intValue();
                            return String.valueOf(number * number);
                        }
                    };
                }
                return null;
            }
        };
        registry.put("number", formatFactory);
        messageFormat = new ExtendedMessageFormat(pattern, registry);
    }

    @Test
    public void testApplyPattern() {
        Object[] args = {"World", 1, 2};
        String formattedMessage = messageFormat.format(args);
        String expectedMessage = "WorldHello 1 4";
        Assert.assertEquals(expectedMessage, formattedMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadPattern() {
        String pattern = "Hello {0,choice,0#World|1#Hello} {1} {2,number,integer}! {3";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
    }

    @Test
    public void testGetFormat() {
        Assert.assertNull(messageFormat.getFormat(0));
        Assert.assertEquals(messageFormat.getFormat("number"), formatFactory);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        messageFormat.setFormat(0, new NumberFormat(""));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        Format[] formats = new Format[1];
        messageFormat.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        messageFormat.setFormatsByArgumentIndex(new Format[1]);
    }

    @Test
    public void testToPattern() {
        String expectedPattern = "Hello {0,choice,0#World|1#Hello} {1} {2,number,integer}!";
        String actualPattern = messageFormat.toPattern();
        Assert.assertEquals(expectedPattern, actualPattern);
    }

    @Test
    public void testParse() throws ParseException {
        String input = "Hello World 1 4!";
        ParsePosition pos = new ParsePosition(0);
        Object[] args = messageFormat.parse(input, pos);
        Assert.assertArrayEquals(new Object[]{"World", 1, 16}, args);
        Assert.assertEquals(input.length(), pos.getIndex());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArgumentIndex() throws ParseException {
        String input = "Hello World {3} 1 4!";
        messageFormat.applyPattern(input);
        ParsePosition pos = new ParsePosition(0);
        messageFormat.parse(input, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidFormatDescription() throws ParseException {
        String input = "Hello World {foo} 1 4!";
        messageFormat.applyPattern(input);
        ParsePosition pos = new ParsePosition(0);
        messageFormat.parse(input, pos);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingFormat() throws ParseException {
        String input = "Hello World {number} 1 4!";
        messageFormat.applyPattern(input);
        ParsePosition pos = new ParsePosition(0);
        messageFormat.setRegistry(null);
        messageFormat.parse(input, pos);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatDescription() {
        String pattern = "Hello {0,choice,0#World|1#Hello} {1} {2,number,integer}! {3}";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
    }

}