import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    private ExtendedMessageFormat messageFormat;

    @Before
    public void setUp() {
        messageFormat = new ExtendedMessageFormat("");
    }

    @Test
    public void testConstructorPatternLocale() {
        Locale locale = Locale.US;
        String pattern = "Test";
        messageFormat = new ExtendedMessageFormat(pattern, locale);
        Assert.assertEquals(pattern, messageFormat.toPattern());
        Assert.assertEquals(locale, messageFormat.getLocale());
    }

    @Test
    public void testConstructorPatternLocaleRegistry() {
        Locale locale = Locale.US;
        String pattern = "Test";
        Map<String, FormatFactory> registry = new HashMap<>();
        messageFormat = new ExtendedMessageFormat(pattern, locale, registry);
        Assert.assertEquals(pattern, messageFormat.toPattern());
        Assert.assertEquals(locale, messageFormat.getLocale());
        Assert.assertEquals(registry, messageFormat.getRegistry());
    }

    @Test
    public void testSetFormat() {
        try {
            messageFormat.setFormat(0, null);
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        try {
            messageFormat.setFormatByArgumentIndex(0, null);
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }
    }

    @Test
    public void testSetFormats() {
        try {
            messageFormat.setFormats(new Format[]{});
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        try {
            messageFormat.setFormatsByArgumentIndex(new Format[]{});
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat other = new ExtendedMessageFormat("");
        Assert.assertTrue(messageFormat.equals(messageFormat));
        Assert.assertFalse(messageFormat.equals(null));
        Assert.assertFalse(messageFormat.equals(new Object()));
        Assert.assertTrue(messageFormat.equals(other));
    }

    @Test
    public void testHashCode() {
        int hashCode = messageFormat.hashCode();
        Assert.assertEquals(hashCode, messageFormat.hashCode());
    }

    @Test
    public void testGetFormatWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("test", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat(arguments, locale);
            }
        });
        messageFormat = new ExtendedMessageFormat("{0,test,yyyy-MM-dd}");
        messageFormat.setRegistry(registry);
        Object[] args = {new Date()};
        Assert.assertEquals("2000-01-01", messageFormat.format(args));
    }

    @Test
    public void testApplyPatternWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("test", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new SimpleDateFormat(arguments, locale);
            }
        });
        messageFormat.applyPattern("{0,test,yyyy-MM-dd}");
        messageFormat.setRegistry(registry);
        Object[] args = {new Date()};
        Assert.assertEquals("2000-01-01", messageFormat.format(args));
    }
    
}