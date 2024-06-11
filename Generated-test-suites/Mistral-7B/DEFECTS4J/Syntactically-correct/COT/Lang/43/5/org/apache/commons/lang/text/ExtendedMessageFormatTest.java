import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat messageFormat;
    private FormatFactory formatFactory;

    @Before
    public void setUp() {
        formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new MessageFormat(name, locale);
            }
        };
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomFormat", formatFactory);
        messageFormat = new ExtendedMessageFormat("{0}({1})", registry);
    }

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}({1})", Locale.getDefault());
        Assert.assertNotNull(messageFormat);
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}({1})", Locale.getDefault(), registry);
        Assert.assertNotNull(messageFormat);
    }

    @Test
    public void testConstructorWithPatternLocaleAndRegistry() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}({1})", new Locale("en", "US"), registry);
        Assert.assertNotNull(messageFormat);
    }

    @Test
    public void testToPattern() {
        String pattern = messageFormat.toPattern();
        Assert.assertEquals("{0}({1})", pattern);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        messageFormat.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        messageFormat.setFormatByArgumentIndex(1, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        Format[] formats = new Format[1];
        messageFormat.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        Format[] formats = new Format[1];
        messageFormat.setFormatsByArgumentIndex(0, formats);
    }

    @Test
    public void testApplyPattern() {
        messageFormat.applyPattern("{0}({CustomFormat,format:date,time,short})");
        String pattern = messageFormat.toPattern();
        Assert.assertEquals("{0}({CustomFormat,format:date,time,short})", pattern);
    }
}