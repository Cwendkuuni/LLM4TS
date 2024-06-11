package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testNewAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());
    }

    @Test
    public void testNewAttributeWithParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value", parent);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertSame(parent, attr.getParent());
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testSetKeyNull() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueNull() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String expected = "key=\"value\"";
        assertEquals(expected, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);

        assertTrue(Attribute.shouldCollapseAttribute("key", null, out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "", out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "true", out));
        assertTrue(Attribute.shouldCollapseAttribute("key", "false", out));

        out.syntax(Document.OutputSettings.Syntax.xml);
        assertFalse(Attribute.shouldCollapseAttribute("key", null, out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "", out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "key", out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "true", out));
        assertFalse(Attribute.shouldCollapseAttribute("key", "false", out));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertTrue(Attribute.isBooleanAttribute("disabled"));
        assertFalse(Attribute.isBooleanAttribute("class"));
        assertFalse(Attribute.isBooleanAttribute("id"));
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(Attribute.isDataAttribute("data-foo"));
        assertTrue(Attribute.isDataAttribute("data-foo='bar'"));
        assertFalse(Attribute.isDataAttribute("key"));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attr = new Attribute("key", "value");
        Attribute cloned = attr.clone();
        assertEquals(attr.getKey(), cloned.getKey());
        assertEquals(attr.getValue(), cloned.getValue());
        assertNull(cloned.getParent());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");
        Attribute attr4 = new Attribute("key1", null);

        assertTrue(attr1.equals(attr3));
        assertFalse(attr1.equals(attr2));
        assertFalse(attr1.equals(null));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");
        Attribute attr4 = new Attribute("key1", null);

        assertEquals(attr1.hashCode(), attr3.hashCode());
        assertNotEquals(attr1.hashCode(), attr2.hashCode());
        assertNotEquals(attr1.hashCode(), attr4.hashCode());
    }

    @Test
    public void testCreateAttributesMap() {
        String[] keys = {"key1", "key2", "key3"};
        String[] values = {"value1", "value2", "value3"};
        Attribute[] attrs = new Attribute[keys.length];
        Map<String, String> map = new LinkedHashMap<>();

        for (int i = 0; i < keys.length; i++) {
            attrs[i] = new Attribute(keys[i], values[i]);
            map.put(keys[i], values[i]);
        }

        Attributes atts = new Attributes();
        for (Attribute attr : attrs) {
            atts.put(attr);
        }

        assertEquals(map.size(), atts.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Attribute attr = atts.get(entry.getKey());
            assertEquals(entry.getKey(), attr.getKey());
            assertEquals(entry.getValue(), attr.getValue());
        }
    }
}