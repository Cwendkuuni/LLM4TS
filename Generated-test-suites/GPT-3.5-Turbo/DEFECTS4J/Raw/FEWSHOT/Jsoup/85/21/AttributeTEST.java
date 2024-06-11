package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.SerializationException;
import org.jsoup.helper.Entities;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
    @Test
    public void testAttributeConstructor() {
        String key = "key";
        String val = "value";
        Attributes parent = new Attributes();
        Attribute attr = new Attribute(key, val, parent);

        assertNotNull(attr);
        assertEquals(key, attr.getKey());
        assertEquals(val, attr.getValue());
        assertEquals(parent, attr.parent);
    }

    @Test
    public void testGetKey() {
        String key = "key";
        String val = "value";
        Attribute attr = new Attribute(key, val);

        assertEquals(key, attr.getKey());
    }

    @Test
    public void testSetKey() {
        String key = "key";
        String val = "value";
        Attribute attr = new Attribute(key, val);

        String newKey = "newKey";
        attr.setKey(newKey);

        assertEquals(newKey, attr.getKey());
    }

    @Test
    public void testGetValue() {
        String key = "key";
        String val = "value";
        Attribute attr = new Attribute(key, val);

        assertEquals(val, attr.getValue());
    }

    @Test
    public void testSetValue() {
        String key = "key";
        String val = "value";
        Attribute attr = new Attribute(key, val);

        String newVal = "newValue";
        String oldVal = attr.setValue(newVal);

        assertEquals(newVal, attr.getValue());
        assertEquals(val, oldVal);
    }

    @Test
    public void testHtml() {
        String key = "key";
        String val = "value";
        Attribute attr = new Attribute(key, val);

        String expected = key + "=\"" + val + "\"";
        String actual = attr.html();

        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String key = "key";
        String val = "value";
        Attribute attr = new Attribute(key, val);

        String expected = key + "=\"" + val + "\"";
        String actual = attr.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "key";
        String encodedValue = "&lt;value&gt;";
        Attribute attr = Attribute.createFromEncoded(unencodedKey, encodedValue);

        String expectedValue = "<value>";
        assertEquals(unencodedKey, attr.getKey());
        assertEquals(expectedValue, attr.getValue());
        assertNull(attr.parent);
    }

    @Test
    public void testIsDataAttribute() {
        String key = "data-key";
        String val = "value";
        Attribute attr = new Attribute(key, val);

        assertTrue(attr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        OutputSettings out = new OutputSettings();
        String key = "key";
        String val = "value";

        // Test when val is null
        Attribute attr1 = new Attribute(key, null);
        assertTrue(attr1.shouldCollapseAttribute(out));

        // Test when val is empty and equal to key
        Attribute attr2 = new Attribute(key, "");
        assertTrue(attr2.shouldCollapseAttribute(out));

        // Test when val is empty and not equal to key
        Attribute attr3 = new Attribute(key, " ");
        assertFalse(attr3.shouldCollapseAttribute(out));

        // Test when val is not empty and equal to key
        Attribute attr4 = new Attribute(key, key);
        assertFalse(attr4.shouldCollapseAttribute(out));

        // Test when val is not empty and not equal to key
        Attribute attr5 = new Attribute(key, val);
        assertFalse(attr5.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        String key = "key";
        String val = "value";
        Attribute attr = new Attribute(key, val);

        assertFalse(attr.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        String key = "key";
        String val = "value";
        Attribute attr1 = new Attribute(key, val);
        Attribute attr2 = new Attribute(key, val);
        Attribute attr3 = new Attribute(val, key);

        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
    }

    @Test
    public void testHashCode() {
        String key = "key";
        String val = "value";
        Attribute attr1 = new Attribute(key, val);
        Attribute attr2 = new Attribute(key, val);

        assertEquals(attr1.hashCode(), attr2.hashCode());
    }

    @Test
    public void testClone() {
        String key = "key";
        String val = "value";
        Attribute attr = new Attribute(key, val);

        Attribute clonedAttr = attr.clone();

        assertEquals(attr, clonedAttr);
        assertNotSame(attr, clonedAttr);
    }
}