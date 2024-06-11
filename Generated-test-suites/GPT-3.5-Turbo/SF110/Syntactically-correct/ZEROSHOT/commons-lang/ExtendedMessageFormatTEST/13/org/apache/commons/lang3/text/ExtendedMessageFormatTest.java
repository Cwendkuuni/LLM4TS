package org.apache.commons.lang3.text;

import java.text.Format;
import java.text.FormatFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExtendedMessageFormatTest {
    private ExtendedMessageFormat extendedMessageFormat;

    @Before
    public void setup() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("dummy", new DummyFormatFactory());

        extendedMessageFormat = new ExtendedMessageFormat("", Locale.getDefault(), registry);
    }

    @Test
    public void testToPattern() {
        String pattern = "Hello {0}";
        extendedMessageFormat.applyPattern(pattern);
        Assert.assertEquals(pattern, extendedMessageFormat.toPattern());
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0}";
        extendedMessageFormat.applyPattern(pattern);
        Assert.assertEquals(pattern, extendedMessageFormat.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        extendedMessageFormat.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        extendedMessageFormat.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        extendedMessageFormat.setFormats(new Format[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        extendedMessageFormat.setFormatsByArgumentIndex(new Format[0]);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}");

        Assert.assertEquals(format1, format2);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}");

        Assert.assertEquals(format1.hashCode(), format2.hashCode());
    }

    // DummyFormatFactory
    private static class DummyFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String name, String args, Locale locale) {
            return null;
        }
    }
}