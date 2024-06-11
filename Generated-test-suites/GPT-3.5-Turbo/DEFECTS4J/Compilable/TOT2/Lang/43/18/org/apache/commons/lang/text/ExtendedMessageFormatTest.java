package org.apache.commons.lang.text;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", Locale.getDefault());
        assertNotNull(emf);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", Locale.getDefault());
        emf.applyPattern("Hi {0}");
        assertEquals("Hi {0}", emf.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("testFormat", new TestFormatFactory());

        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", Locale.getDefault(), registry);
        emf.applyPattern("Hi {1,testFormat}");
        assertEquals("Hi {1,testFormat}", emf.toPattern());
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", Locale.getDefault());
        assertEquals("Hello {0}", emf.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", Locale.getDefault());
        emf.setFormat(0, new TestFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", Locale.getDefault());
        emf.setFormatByArgumentIndex(0, new TestFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", Locale.getDefault());
        emf.setFormats(new Format[]{new TestFormat()});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", Locale.getDefault());
        emf.setFormatsByArgumentIndex(new Format[]{new TestFormat()});
    }

    private static class TestFormat extends Format {
        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            return toAppendTo.append(obj);
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return source;
        }
    }

    private static class TestFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String args, Locale locale) {
            return new TestFormat();
        }
    }
}