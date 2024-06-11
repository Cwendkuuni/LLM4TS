import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang.text.MessageFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat format;

    @Before
    public void setup() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arg, Locale locale) {
                if ("customFormat".equals(name)) {
                    return new MessageFormat("Hello, {0}!") {
                        @Override
                        public String format(Object arg0) {
                            return "Hello, " + arg0;
                        }
                    };
                }
                return null;
            }
        };

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", formatFactory);

        String pattern = "Hello, {customFormat(John)}!";
        this.format = new ExtendedMessageFormat(pattern, registry);
    }

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern");
        Assert.assertEquals("", format.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.ENGLISH);
        Assert.assertEquals("Pattern", format.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", new HashMap<>());
        Assert.assertEquals("Pattern", format.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndLocaleAndRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern", Locale.ENGLISH, new HashMap<>());
        Assert.assertEquals("Pattern", format.toPattern());
    }

    @Test
    public void testApplyPatternWithValidPattern() {
        format.applyPattern(format.toPattern());
        Object[] args = {"John"};
        String result = format.format(args);
        Assert.assertEquals("Hello, John!", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern() {
        format.applyPattern("InvalidPattern");
    }

    @Test
    public void testGetFormat() {
        Format format = this.format.getFormat(0);
        Assert.assertNotNull(format);
        Assert.assertSame(MessageFormat.class, format.getClass());
    }

    @Test
    public void testGetFormatWithInvalidIndex() {
        Assert.assertNull(this.format.getFormat(1));
    }

    @Test
    public void testSetFormatWithValidFormat() {
        Format newFormat = new MessageFormat("Hello, {0}!");
        this.format.setFormat(0, newFormat);
        Object[] args = {"John"};
        String result = this.format.format(args);
        Assert.assertEquals("Hello, John!", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatWithInvalidIndex() {
        this.format.setFormat(0, null);
    }

    @Test
    public void testSetFormats() {
        Format[] newFormats = {new MessageFormat("Hello, {0}!"), new MessageFormat("Hello, {1}!")};
        this.format.setFormats(newFormats);
        Object[] args1 = {"John"};
        Object[] args2 = {"Doe"};
        String result1 = this.format.format(args1);
        String result2 = this.format.format(args2);
        Assert.assertEquals("Hello, John!", result1);
        Assert.assertEquals("Hello, Doe!", result2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsWithInvalidArraysize() {
        Format[] newFormats = {new MessageFormat("Hello, {0}!"), new MessageFormat("Hello, {1}!"), null};
        this.format.setFormats(newFormats);
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        Format newFormat = new MessageFormat("Hello, {1}!");
        this.format.setFormatsByArgumentIndex(1, newFormat);
        Object[] args = {"John", "Doe"};
        String result = this.format.format(args);
        Assert.assertEquals("Hello, Doe!", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexWithInvalidIndex() {
        this.format.setFormatsByArgumentIndex(1, null);
    }

    @Test
    public void testToPattern() {
        Assert.assertEquals("Pattern", format.toPattern());
    }
}