import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        // Test empty constructor
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertEquals("", format.toPattern());
        assertNull(format.getArgument(0));

        // Test constructor with pattern and default locale
        format = new ExtendedMessageFormat("{0} is {1, choice, 0#Number One|1#Number Two}");
        assertEquals("{0} is {1, choice, 0#Number One|1#Number Two}", format.toPattern());
        Object[] args = new Object[]{"One", "1"};
        assertEquals("One is Number One", format.format(args));
        args = new Object[]{"Two", "0"};
        assertEquals("Two is Number Two", format.format(args));

        // Test constructor with pattern, default locale, and registry
        Map<String, FormatFactory> registry = new java.util.HashMap<>();
        registry.put("NumberFormatter", new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                if (args == null || args.isEmpty()) {
                    return new NumberFormat();
                }
                if (args.equals("NumberTwoFormatter")) {
                    return new DecimalFormat("#,##0.##");
                }
                return null;
            }
        });
        format = new ExtendedMessageFormat("{0, number, NumberFormatter}", registry);
        assertEquals("{0, number, NumberFormatter}", format.toPattern());
        args = new Object[]{"123.456"};
        assertEquals("123.456", format.format(args));
        args = new Object[]{"123"};
        assertEquals("123", format.format(args));

        // Test constructor with pattern, custom locale, and registry
        format = new ExtendedMessageFormat("{0, number, NumberFormatter}", Locale.US, registry);
        assertEquals("{0, number, NumberFormatter}", format.toPattern());
        args = new Object[]{"123.456"};
        assertEquals("123.456", format.format(args));
        args = new Object[]{"123"};
        assertEquals("123", format.format(args));

        // Test constructor with pattern, custom locale, and custom format
        FormatFactory numberFormatter = new FormatFactory() {
            @Override
            public Format getFormat(String name, String args, Locale locale) {
                if (args == null || args.isEmpty()) {
                    return new NumberFormat();
                }
                if (args.equals("NumberTwoFormatter")) {
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
                    decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
                    return decimalFormat;
                }
                return null;
            }
        };
        registry.put("NumberFormatter", numberFormatter);
        format = new ExtendedMessageFormat("{0, number, NumberFormatter}", Locale.US, registry);
        assertEquals("{0, number, NumberFormatter}", format.toPattern());
        args = new Object[]{"123.456"};
        assertEquals("123,456.456", format.format(args));
        args = new Object[]{"123"};
        assertEquals("123", format.format(args));

        // Test getFormat(int, Format) method
        try {
            format.setFormat(0, new DateFormat());
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // Test getFormatByArgumentIndex(int, Format) method
        try {
            format.setFormatByArgumentIndex(0, new DateFormat());
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // Test getFormats(Format[]) method
        try {
            format.setFormats(new Format[0]);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // Test getFormatsByArgumentIndex(Format[]) method
        try {
            Format[] formats = new Format[1];
            formats[0] = new DateFormat();
            format.setFormatsByArgumentIndex(new int[]{0}, formats);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}

interface FormatFactory {
    Format getFormat(String name, String args, Locale locale);
}