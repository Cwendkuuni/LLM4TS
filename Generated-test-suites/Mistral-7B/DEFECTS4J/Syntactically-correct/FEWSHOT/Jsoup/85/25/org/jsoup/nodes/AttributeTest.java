package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AttributeTest {
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());
    }

    @Test
    public void testCreateAttributeWithParent() {
        Attribute attr = new Attribute("key", "value", attributes);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertSame(attributes, attr.getParent());
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        String newValue = "newValue";
        String oldValue = attr.setValue(newValue);
        assertEquals("newValue", attr.getValue());
        assertEquals("value", oldValue);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String expected = "key=\"value\"";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute());

        attr = new Attribute("data-key", "value");
        assertTrue(attr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Attribute attr = new Attribute("key", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        assertFalse(attr.shouldCollapseAttribute(out));

        attr.setKey("key");
        attr.setValue("");
        assertTrue(attr.shouldCollapseAttribute(out));

        attr.setValue("value");
        assertFalse(attr.shouldCollapseAttribute(out));

        attr.setKey("data-key");
        assertTrue(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isBooleanAttribute());

        attr.setKey("checked");
        assertTrue(attr.isBooleanAttribute());

        attr.setKey("notBoolean");
        assertFalse(attr.isBooleanAttribute());
    }

    @Test
    public void testToString() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        assertFalse(attr1.equals(attr2));
        assertTrue(attr1.equals(attr3));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        int hashcode1 = attr1.hashCode();
        int hashcode2 = attr2.hashCode();
        int hashcode3 = attr3.hashCode();

        assertEquals(hashcode1, hashcode3);
        assertNotEquals(hashcode1, hashcode2);
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals("key", clonedAttr.getKey());
        assertEquals("value", clonedAttr.getValue());
        assertNull(clonedAttr.getParent());
    }

    @Test
    public void testMapMethods() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Map<String, Attribute> map = new HashMap<>();
        map.put("key1", attr1);
        map.put("key2", attr2);

        assertEquals(attr1, map.get("key1"));
        assertEquals(attr2, map.get("key2"));

        attr1.setKey("newKey");
        assertEquals("newKey", map.get("key1").getKey());

        attr1.setValue("newValue");
        assertEquals("newValue", map.get("key1").getValue());
    }

    @Test
    public void testIsDataAttributeWithPrefix() {
        Attribute attr = new Attribute("data:prefixkey", "value");
        assertTrue(attr.isDataAttribute());

        attr.setKey("prefix:key");
        assertTrue(attr.isDataAttribute());

        attr.setKey("prefixkey");
        assertFalse(attr.isDataAttribute());
    }
}