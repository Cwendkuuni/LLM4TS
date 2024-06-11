import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.text.Format;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test", Locale.ENGLISH);
        assertNotNull(extendedMessageFormat);
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("testFormat", new TestFormatFactory());
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test", registry);
        assertNotNull(extendedMessageFormat);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test {0}");
        extendedMessageFormat.applyPattern("New {1}");
        assertEquals(extendedMessageFormat.toPattern(), "New {0}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test {0}");
        extendedMessageFormat.applyPattern("New {1");
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test {0}");
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("testFormat", new TestFormatFactory());
        extendedMessageFormat = new ExtendedMessageFormat("Test {0}", registry);
        Format format = extendedMessageFormat.getFormat("testFormat{style}");
        assertNotNull(format);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test");
        assertEquals(extendedMessageFormat.toPattern(), "Test");
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test");
        try {
            extendedMessageFormat.setFormat(0, new TestFormat());
        } catch (UnsupportedOperationException e) {
            // This is expected
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test");
        try {
            extendedMessageFormat.setFormatByArgumentIndex(0, new TestFormat());
        } catch (UnsupportedOperationException e) {
            // This is expected
        }
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test");
        try {
            extendedMessageFormat.setFormats(new Format[]{new TestFormat()});
        } catch (UnsupportedOperationException e) {
            // This is expected
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Test");
        try {
            extendedMessageFormat.setFormatsByArgumentIndex(new Format[]{new TestFormat()});
        } catch (UnsupportedOperationException e) {
            // This is expected
        }
    }

    private static class TestFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String args, Locale locale) {
            return new TestFormat();
        }
    }

    private static class TestFormat extends Format {
        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return toAppendTo.append(obj.toString());
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
    }
}