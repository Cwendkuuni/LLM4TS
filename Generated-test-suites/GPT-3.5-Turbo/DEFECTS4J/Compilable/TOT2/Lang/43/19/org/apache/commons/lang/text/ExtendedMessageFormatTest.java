package org.apache.commons.lang.text;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatConstructor() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", new HashMap<String, Format>());
        assertNotNull(emf);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.applyPattern("New Pattern {0}");
        assertEquals("New Pattern {0}", emf.toPattern());
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        try {
            emf.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
        } catch (UnsupportedOperationException e) {
            assertEquals("Setting format is not supported", e.getMessage());
        }
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        try {
            emf.setFormatByArgumentIndex(0, new SimpleDateFormat("yyyy-MM-dd"));
        } catch (UnsupportedOperationException e) {
            assertEquals("Setting format by argument index is not supported", e.getMessage());
        }
    }

    @Test
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        try {
            emf.setFormats(new Format[]{new SimpleDateFormat("yyyy-MM-dd")});
        } catch (UnsupportedOperationException e) {
            assertEquals("Setting formats is not supported", e.getMessage());
        }
    }

    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        try {
            emf.setFormatsByArgumentIndex(new Format[]{new SimpleDateFormat("yyyy-MM-dd")});
        } catch (UnsupportedOperationException e) {
            assertEquals("Setting formats by argument index is not supported", e.getMessage());
        }
    }

    @Test
    public void testFormatWithValidArguments() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        String result = emf.format(new Object[]{"123"});
        assertEquals("Test 123", result);
    }

    @Test
    public void testFormatWithNullArgument() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        String result = emf.format(new Object[]{null});
        assertEquals("Test null", result);
    }

    @Test
    public void testFormatWithMissingArgument() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        String result = emf.format(new Object[]{});
        assertEquals("Test {0}", result);
    }

    @Test
    public void testFormatWithMultipleArguments() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0} {1}");
        String result = emf.format(new Object[]{"123", "456"});
        assertEquals("Test 123 456", result);
    }

    @Test
    public void testFormatWithCustomFormat() {
        Map<String, Format> registry = new HashMap<>();
        registry.put("custom", new SimpleDateFormat("yyyy-MM-dd"));
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0,custom}");
        emf.applyPattern("Test {0,custom}");
        String result = emf.format(new Object[]{"2022-12-31"});
        assertEquals("Test 2022-12-31", result);
    }

}