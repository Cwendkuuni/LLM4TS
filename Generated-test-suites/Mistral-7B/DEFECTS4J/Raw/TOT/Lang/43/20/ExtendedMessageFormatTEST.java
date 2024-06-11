import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.util.DateFormatUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Test class for {@link ExtendedMessageFormat}.
 */
public class ExtendedMessageFormatTest {

    private static final String DEFAULT_PATTERN = "{0, date, short, 'EEEE d MMMM yyyy HH:mm:ss zzz}";
    private static final String CUSTOM_PATTERN = "{1, customDate, 'CustomDateFormat'}";

    @Test
    public void testDefaultMessageFormat() {
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(DEFAULT_PATtern, registry);

        String formatText = messageFormat.format(new Object[]{new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2022")});
        Assert.assertEquals("Monday 01 January 2022 00:00:00 CET", formatText);
    }

    @Test
    public void testCustomMessageFormat() {
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDate", new CustomDateFormatFactory());
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(CUSTOM_PATTERN, registry, Locale.ENGLISH);

        String formatText = messageFormat.format(new Object[]{new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2022"), "CustomDateFormat"});
        Assert.assertEquals("CustomDateFormat", formatText);

        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy");
        messageFormat.applyPattern("{1, customDate, 'CustomDateFormat', 'date', 'long'}");

        formatText = messageFormat.format(new Object[]{customFormat.parse("01-01-2022")});
        Assert.assertEquals("01-Jan-2022", formatText);

        try {
            messageFormat.format(new Object[]{"dummy"});
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid format argument index at position 0", e.getMessage());
        }
    }

    /**
     * Custom date format factory.
     */
    public static class CustomDateFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String formatName, String formatStyle, Locale locale) {
            if (formatName.equals("customDate")) {
                return new SimpleDateFormat("dd-MM-yyyy");
            }
            throw new UnsupportedOperationException("Unsupported format: " + formatName);
        }
    }
}