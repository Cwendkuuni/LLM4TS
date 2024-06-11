import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat messageFormat;
    private FormatFactory formatFactory;

    @Before
    public void setUp() {
        FormatFactoryRegistry registry = new FormatFactoryRegistry();
        formatFactory = new MyFormatFactory();
        registry.put("customFormat", formatFactory);
        messageFormat = new ExtendedMessageFormat("{0,format(\"Hello {1, customFormat} {2}\")}", Locale.ENGLISH, registry);
    }

    @Test
    public void testBasicUsage() {
        String[] args = new String[]{"Alice", "John", "Doe"};
        Object[] objects = new Object[]{args};
        String result = messageFormat.format(objects);
        assertEquals("Hello John Doe Alice", result);
    }

    @Test
    public void testCustomFormat() {
        String[] args = new String[]{"Alice", "John", "Doe"};
        Object[] objects = new Object[]{args};
        messageFormat.applyPattern("{0,format(\"Hello {1, customFormat} {2}\")}");
        String result = messageFormat.format(objects);
        assertEquals("Hello [Alice] John Doe Alice", result);
        assertNotNull(messageFormat.getFormats());
        Format customFormat = messageFormat.getFormats()[0];
        assertEquals(MyFormat.class, customFormat.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        messageFormat = new ExtendedMessageFormat("{0,format(\"Hello {1} {2}\")}");
        String[] args = new String[]{"Alice", "John", "Doe"};
        Object[] objects = new Object[]{args};
        messageFormat.applyPattern("{0,format(\"Hello {1} {2}\")}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormat() {
        messageFormat = new ExtendedMessageFormat("{0,format(\"Hello {1, missingFormat} {2}\")}");
        String[] args = new String[]{"Alice", "John", "Doe"};
        Object[] objects = new Object[]{args};
        messageFormat.applyPattern("{0,format(\"Hello {1, missingFormat} {2}\")}");
    }

    @Test(expected = ParseException.class)
    public void testInvalidFormat() {
        messageFormat = new ExtendedMessageFormat("{0,format(\"Hello {1, format=\"invalidFormat\"} {2}\")}");
        String[] args = new String[]{"Alice", "John", "Doe"};
        Object[] objects = new Object[]{args};
        messageFormat.applyPattern("{0,format(\"Hello {1, format=\"invalidFormat\"} {2}\")}");
    }

    @Test
    public void testFormatFactory() {
        String[] args = new String[]{"Alice", "John", "Doe"};
        Object[] objects = new Object[]{args};
        messageFormat = new ExtendedMessageFormat("{0,format(\"Hello {1, format='customFormat'} {2}\")}", Locale.ENGLISH, null);
        messageFormat.setFormatFactory(formatFactory);
        messageFormat.applyPattern("{0,format(\"Hello {1, format='customFormat'} {2}\")}");
        String result = messageFormat.format(objects);
        assertEquals("Hello [Alice] John Doe Alice", result);
    }

    static class MyFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String format, Locale locale) {
            if (name.equals("customFormat")) {
                return new MyFormat();
            }
            return null;
        }

        static class MyFormat extends Format {
            @Override
            public String format(Object obj, Locale locale, String format, ParsePosition parsePosition) {
                return obj.toString().replaceAll("\\[", "").replaceAll("\\]", "");
            }
        }
    }
}