import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatWithDefaultRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} is {1} years old.");
        Object[] args = new Object[]{"John Doe", 30};
        String formattedMessage = format.format(args);
        assertEquals("John Doe is 30 years old.", formattedMessage);
    }

    @Test
    public void testExtendedMessageFormatWithCustomRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customDateFormat", new CustomDateFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} was born on {1}", registry);
        Object[] args = new Object[]{"John Doe", "2001-01-01"};
        String formattedMessage = format.format(args);
        assertEquals("John Doe was born on 2001-01-01", formattedMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatElement() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} is {invalidFormat} years old.");
        Object[] args = new Object[]{"John Doe", 30};
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatElement() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} is {1} years old.");
        Object[] args = new Object[]{"John Doe"};
    }

    @Test
    public void testQuotedStringInFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{'Hello'} world.");
        Object[] args = new Object[]{"Hello"};
        String formattedMessage = format.format(args);
        assertEquals("Hello world.", formattedMessage);
    }

    @Test
    public void testNestedQuotedStringInFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{'Hello' {'World'}}");
        Object[] args = new Object[]{"Hello", "World"};
        String formattedMessage = format.format(args);
        assertEquals("Hello World.", formattedMessage);
    }

    @Test
    public void testFormatFactory() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customCurrencyFormat", new CustomCurrencyFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat("Currency: {0, formatName=customCurrencyFormat}", registry);
        Object[] args = new Object[]{123.45};
        String formattedMessage = format.format(args);
        assertEquals("Currency: $123.45", formattedMessage);
    }

    @Test
    public void testFormatFactoryWithArguments() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("customCurrencyFormat", new CustomCurrencyFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat("Currency: {0, number, formatName=customCurrencyFormat}", registry);
        Object[] args = new Object[]{123.45d};
        String formattedMessage = format.format(args);
        assertEquals("Currency: $123.45", formattedMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatFactory() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("invalidFormatFactory", new Object());

        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, formatName=invalidFormatFactory}", registry);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.setFormat(0, new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.setFormatByArgumentIndex(0, new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        Format[] formats = new Format[1];
        format.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        Format[] formats = new Format[1];
        format.setFormatsByArgumentIndex(formats);
    }

}

class CustomDateFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String format, Locale locale, String style) {
        if ("customDateFormat".equals(format)) {
            return new SimpleDateFormat("dd/MM/yyyy");
        }
        return null;
    }
}

class CustomCurrencyFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String format, Locale locale, String style) {
        if ("customCurrencyFormat".equals(format)) {
            return new DecimalFormat("$#,##0.##");
        }
        return null;
    }
}