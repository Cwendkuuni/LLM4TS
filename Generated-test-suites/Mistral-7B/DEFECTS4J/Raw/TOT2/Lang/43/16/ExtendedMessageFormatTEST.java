import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang.text.SimpleFormatFactory;
import org.junit.Assert;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testSimpleMessageFormat() {
        String pattern = "Hello {0}, your total is {1}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currency", new SimpleFormatFactory() {
            @Override
            public Format getFormat(String name, String format, Locale locale) {
                return new MessageFormat("#,##0.00");
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        Object[] args = new Object[]{"John", 123.45};
        String result = format.format(args);
        String expected = "Hello John, your total is #,##0.00";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCustomFormat() {
        String pattern = "Hello {0}, your total is {currency format=myCurrency {1}}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("myCurrency", new SimpleFormatFactory() {
            @Override
            public Format getFormat(String name, String format, Locale locale) {
                return new MessageFormat("#,##0.00%");
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        Object[] args = new Object[]{"John", 123.45};
        String result = format.format(args);
        String expected = "Hello John, your total is #,##0.00%";
        Assert.assertEquals(expected, result);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        String pattern = "{invalidFormat {0}}";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testMissingFormat() {
        String pattern = "Hello {0}, your total is {missingFormat {1}}";
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
    }
}