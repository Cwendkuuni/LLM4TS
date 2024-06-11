import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class ExtendedMessageFormatTest {
    private ExtendedMessageFormat messageFormat;
    private FormatFactory formatFactory;

    @Before
    public void setup() {
        formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                return new MessageFormat(name);
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("TestFormat", formatFactory);
        String pattern = "Test {0,choice,0#Hello|1#World}";
        messageFormat = new ExtendedMessageFormat(pattern, registry);
    }

    @Test
    public void testDefaultConstructor() {
        Assert.assertNull(messageFormat);
    }

    @Test
    public void testConstructorWithPattern() {
        Assert.assertNotNull(messageFormat.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        Assert.assertNotNull(messageFormat.toPattern());
        Assert.assertEquals(Locale.getDefault(), messageFormat.getLocale());
    }

    @Test
    public void testConstructorWithPatternRegistry() {
        Assert.assertNotNull(messageFormat.toPattern());
    }

    @Test
    public void testConstructorWithPatternLocaleRegistry() {
        Assert.assertNotNull(messageFormat.toPattern());
        Assert.assertEquals(Locale.getDefault(), messageFormat.getLocale());
    }

    @Test
    public void testToPattern() {
        Assert.assertNotNull(messageFormat.toPattern());
    }

    @Test
    public void testApplyPattern() {
        String formattedMessage = messageFormat.format(new Object[]{"World"});
        Assert.assertEquals("Test World", formattedMessage);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        messageFormat.setFormat(0, new MessageFormat("TestFormat"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        messageFormat.setFormatByArgumentIndex(0, new MessageFormat("TestFormat"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        Format[] formats = new Format[1];
        formats[0] = new MessageFormat("TestFormat");
        messageFormat.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        Format[] formats = new Format[1];
        formats[0] = new MessageFormat("TestFormat");
        messageFormat.setFormatsByArgumentIndex(formats);
    }
}

class FormatFactory {
    @Override
    public Format getFormat(String name, String args, Locale locale) {
        return new MessageFormat(name);
    }
}