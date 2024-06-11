
import org.junit.Assert;
import org.junit.Test;
import java.text.Format;
import java.text.Format.Field;
import java.util.Formatter;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor_withPatternAndLocale() {
        String pattern = "Hello, {0}!";
        Locale locale = Locale.getDefault();
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale);
        Assert.assertEquals(pattern, format.toPattern());
    }

    @Test
    public void testConstructor_withPatternAndRegistry() {
        String pattern = "Today is {0,date,yyyy-MM-dd}.";
        Map<String, FormatFactory> registry = null;
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Assert.assertEquals(pattern, format.toPattern());
    }

    @Test
    public void testConstructor_withPatternLocaleAndRegistry() {
        String pattern = "You have {0,number} messages.";
        Locale locale = Locale.getDefault();
        Map<String, FormatFactory> registry = null;
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale, registry);
        Assert.assertEquals(pattern, format.toPattern());
    }

    @Test
    public void testEquals() {
        String pattern = "{0,number,percent}";
        Locale locale = Locale.US;
        ExtendedMessageFormat format1 = new ExtendedMessageFormat(pattern, locale);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat(pattern, locale);
        Assert.assertEquals(format1, format2);
    }

    @Test
    public void testHashCode() {
        String pattern = "The answer is {0,number,integer}.";
        Locale locale = Locale.FRANCE;
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale);
        int expectedHash = pattern.hashCode() * 31 + locale.hashCode();
        Assert.assertEquals(expectedHash, format.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.setFormats(new Format[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.setFormatsByArgumentIndex(new Format[0]);
    }

    // Additional tests for potential edge cases and exception handling scenarios
}
