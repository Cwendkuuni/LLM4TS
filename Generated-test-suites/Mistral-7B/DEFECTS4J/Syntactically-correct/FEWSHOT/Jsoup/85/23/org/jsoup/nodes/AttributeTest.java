package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testConstruction() {
        Attribute attr = new Attribute("key", "value");
        Attribute attrNullParent = new Attribute("key", "value", null);
        Attribute attrParent = new Attributes(new HashMap<>()).put("key", attr);

        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.parent);
        assertEquals("key", attrParent.get(0).getKey());
        assertEquals("value", attrParent.get(0).getValue());
        assertEquals(attr, attrParent.get(0));
    }

    @Test
    public void testSettingKey() {
        Attribute attr = new Attribute("key1", "value1");
        attr.setKey("key2");
        assertEquals("key2", attr.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingInvalidKey() {
        Attribute attr = new Attribute("key1", "value1");
        attr.setKey(null);
    }

    @Test
    public void testSettingValue() {
        Attribute attr = new Attribute("key", "value1");
        String value2 = "value2";
        assertEquals("value1", attr.getValue());
        attr.setValue(value2);
        assertEquals("value2", attr.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingInvalidValue() {
        Attribute attr = new Attribute("key", "value1");
        attr.setValue(null);
    }

    @Test
    public void testSettingParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value");
        attr.setParent(parent);
        assertEquals(parent, attr.parent);
        assertEquals(1, parent.size());
        assertEquals(attr, parent.get(0));
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder accum = StringUtil.borrowBuilder();
        attr.html(accum, new Document.OutputSettings());
        String expected = "key=\"value\"";
        assertEquals(expected, StringUtil.releaseBuilder(accum));
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute());

        Attribute dataAttr = new Attribute("data-key", "value");
        assertTrue(dataAttr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attr1 = new Attribute("key1", null);
        Attribute attr2 = new Attribute("key2", "");
        Attribute attr3 = new Attribute("key3", "true");
        Attribute attr4 = new Attribute("key4", "false");
        Attribute attr5 = new Attribute("key5", "key5");
        Attribute attr6 = new Attribute("boolean-attribute", null);

        assertTrue(attr1.shouldCollapseAttribute(out));
        assertFalse(attr2.shouldCollapseAttribute(out));
        assertTrue(attr3.shouldCollapseAttribute(out));
        assertFalse(attr4.shouldCollapseAttribute(out));
        assertFalse(attr5.shouldCollapseAttribute(out));
        assertTrue(attr6.shouldCollapseAttribute(out));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        assertTrue(attr1.equals(attr3));
        assertFalse(attr1.equals(attr2));
    }

    @Test
    public void testClone() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute clonedAttr = attr1.clone();

        assertEquals(attr1, clonedAttr);
        assertNotEquals(attr1.parent, clonedAttr.parent);
    }
}