import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Assert;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatBasicUsage() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());

        String pattern = "Hello {0,choice,0#{}1#{}} in {2,choice,0#English{1}1#French{0}}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);

        Object[] arguments = new Object[]{1, "French"};
        String formattedMessage = format.format(arguments);
        Assert.assertEquals("Hello 1 in French.", formattedMessage);
    }

    @Test
    public void testExtendedMessageFormatWithNullInput() {
        Map<String, FormatFactory> registry = new HashMap<>();

        String pattern = "Hello {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);

        Object[] arguments = new Object[]{null};
        try {
            format.format(arguments);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Argument index 0 must not be null", e.getMessage());
        }
    }

    @Test
    public void testExtendedMessageFormatWithMissingFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();

        String pattern = "Hello {0,formatName}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);

        Object[] arguments = new Object[]{"{1}"};
        try {
            format.applyPattern(pattern);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Format registry does not contain formatName", e.getMessage());
        }
    }

    @Test
    public void testExtendedMessageFormatWithInvalidFormatNameArgument() {
        Map<String, FormatFactory> registry = new HashMap<>();

        String pattern = "Hello {0,formatName,invalidFormatName}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);

        Object[] arguments = new Object[]{"Argument"};
        try {
            format.applyPattern(pattern);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid format name: invalidFormatName", e.getMessage());
        }
    }

    @Test
    public void testExtendedMessageFormatWithMissingFormatStyleArgument() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());

        String pattern = "Hello {0,formatName,style}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);

        Object[] arguments = new Object[]{"", "style"};
        try {
            format.applyPattern(pattern);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Format style is missing", e.getMessage());
        }
    }

    @Test
    public void testExtendedMessageFormatWithInvalidFormatStyleArgument() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customFormat", new CustomFormatFactory());

        String pattern = "Hello {0,formatName,invalidStyle}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);

        Object[] arguments = new Object[]{"", "invalidStyle"};
        try {
            format.applyPattern(pattern);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid format style: invalidStyle", e.getMessage());
        }
    }

    @Test
    public void testExtendedMessageFormatWithEmptyFormatRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}.", null);

        Object[] arguments = new Object[]{"World"};
        try {
            format.applyPattern("Hello {0}.");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("No format factories are available in the registry", e.getMessage());
        }
    }

    @Test
    public void testExtendedMessageFormatWithInvalidPattern() {
        Map<String, FormatFactory> registry = new HashMap<>();

        String pattern = "Hello {0}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);

        Object[] arguments = new Object[]{null};
        try {
            format.applyPattern("InvalidPattern{0}.");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid pattern: InvalidPattern{0}.", e.getMessage());
        }
    }

    private static class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String formatName, String formatStyle, Locale locale) {
            if ("customFormat".equals(formatName)) {
                if ("style1".equals(formatStyle)) {
                    return new CustomFormat("CustomFormat", locale);
                }
            }
            throw new UnsupportedOperationException();
        }

        private static class CustomFormat extends MessageFormat {
            public CustomFormat(String pattern, Locale locale) {
                super(pattern, locale);
            }

            @Override
            public String format(Object[] arguments, ParsePosition pos) {
                return "CustomFormat applied.";
            }
        }
    }
}