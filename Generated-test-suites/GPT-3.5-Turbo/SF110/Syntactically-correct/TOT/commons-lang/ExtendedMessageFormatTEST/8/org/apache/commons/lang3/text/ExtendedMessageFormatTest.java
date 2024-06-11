import org.junit.Before;
import org.junit.Test;
import java.text.Format;
import java.text.FormatFactory;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
    private ExtendedMessageFormat extendedMessageFormat;
    private Map<String, FormatFactory> registry;
    
    @Before
    public void setup() {
        registry = new HashMap<>();
        registry.put("number", new DecimalFormatFactory());
        extendedMessageFormat = new ExtendedMessageFormat("Hello {0,number}", registry);
    }
    
    @Test
    public void testToPattern() {
        assertEquals("Hello {0,number}", extendedMessageFormat.toPattern());
    }
    
    @Test
    public void testApplyPatternWithNullRegistry() {
        extendedMessageFormat = new ExtendedMessageFormat("Hello {0,number}", null);
        assertEquals("Hello {0,number}", extendedMessageFormat.toPattern());
    }
    
    @Test
    public void testApplyPatternWithEmptyPattern() {
        extendedMessageFormat.applyPattern("");
        assertEquals("", extendedMessageFormat.toPattern());
    }
    
    @Test
    public void testEqualsWithDifferentFormat() {
        ExtendedMessageFormat otherFormat = new ExtendedMessageFormat("Hello {0,date}", registry);
        assertFalse(extendedMessageFormat.equals(otherFormat));
    }
    
    @Test
    public void testEqualsWithDifferentRegistry() {
        Map<String, FormatFactory> otherRegistry = new HashMap<>();
        otherRegistry.put("number", new DecimalFormatFactory());
        otherRegistry.put("date", new SimpleDateFormatFactory());
        ExtendedMessageFormat otherFormat = new ExtendedMessageFormat("Hello {0,number}", otherRegistry);
        assertFalse(extendedMessageFormat.equals(otherFormat));
    }
    
    @Test
    public void testHashCode() {
        int expectedHashCode = 31 * extendedMessageFormat.hashCode() + registry.hashCode();
        assertEquals(expectedHashCode, extendedMessageFormat.hashCode());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        extendedMessageFormat.setFormat(0, new DecimalFormat());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        extendedMessageFormat.setFormatByArgumentIndex(0, new DecimalFormat());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        extendedMessageFormat.setFormats(new Format[] {new DecimalFormat()});
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        extendedMessageFormat.setFormatsByArgumentIndex(new Format[] {new DecimalFormat()});
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithUnreadableFormatElement() {
        extendedMessageFormat.applyPattern("Hello {0,date");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidArgumentIndex() {
        extendedMessageFormat.applyPattern("Hello {a,number}");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithUnterminatedFormatElement() {
        extendedMessageFormat.applyPattern("Hello {0,number");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidCustomPattern() {
        extendedMessageFormat.applyPattern("Hello {0,number,invalid}");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithUnterminatedQuotedString() {
        extendedMessageFormat.applyPattern("Hello '{0,number}");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithUnterminatedQuotedStringAfterEscape() {
        extendedMessageFormat.applyPattern("Hello ''{0,number}");
    }
    
    private static class DecimalFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return new DecimalFormat(arguments);
        }
    }

    private static class SimpleDateFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return new SimpleDateFormat(arguments, locale);
        }
    }
}