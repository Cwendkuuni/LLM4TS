import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ExtendedMessageFormatTest {

    @Test
    public void testEmptyPattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("");
        assertEquals("", fmt.toPattern());
        assertNull(fmt.getArguments());
    }

    @Test
    public void testSimplePattern() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} Hello {1}!");
        assertEquals("{0} Hello {1}!", fmt.toPattern());
        assertNull(fmt.getArguments());
    }

    @Test
    public void testCustomFormat() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                if (name.equals("CustomFormat")) {
                    return new CustomFormat();
                }
                return null;
            }
        };
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{CustomFormat, number, #, ##0} {0} {CustomFormat, date, MMM dd, yyyy}", Locale.US, formatFactory);
        assertEquals("{CustomFormat, number, #, ##0} {0} {CustomFormat, date, MMM dd, yyyy}", fmt.toPattern());
        Object[] args = {"42", "2023-03-15"};
        Object[] result = fmt.format(args);
        assertEquals("[42, Wed Mar 15, 2023]", StringUtils.join(Arrays.asList(result), ", "));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadFormatElement() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1} {2} {3} {4} {5} {6}", Locale.US);
        fmt.applyPattern("{0} {1} {2} {3} {4} {5} {7}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadFormatDescription() {
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{CustomFormat, invalid}", Locale.US);
        fmt.applyPattern("{CustomFormat, invalid} {0}");
    }

    @Test
    public void testCustomFormatWithArguments() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String style, Locale locale) {
                if (name.equals("CustomFormat")) {
                    return new CustomFormat();
                }
                return null;
            }
        };
        ExtendedMessageFormat fmt = new ExtendedMessageFormat("{CustomFormat, number, #, ##0} {0} {CustomFormat, date, MMM dd, yyyy}", Locale.US, formatFactory);
        Object[] args = {"42", "2023-03-15"};
        Object[] result = fmt.format(args);
        assertEquals("[42, Mar 15, 2023]", StringUtils.join(Arrays.asList(result), ", "));
    }

    static class CustomFormat implements Format {
        @Override
        public String format(Object obj) {
            if (obj instanceof Number) {
                Number number = (Number) obj;
                return number.toString();
            } else if (obj instanceof String) {
                String str = (String) obj;
                return str.substring(0, 3);
            }
            throw new IllegalArgumentException("Unsupported object type.");
        }

        @Override
        public ParsePosition parseObject(String source, ParsePosition pos) {
            return null;
        }

        @Override
        public String toString() {
            return "CustomFormat";
        }
    }
}