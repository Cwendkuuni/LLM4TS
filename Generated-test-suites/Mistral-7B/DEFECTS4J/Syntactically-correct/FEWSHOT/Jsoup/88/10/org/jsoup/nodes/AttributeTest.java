package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.parent);

        Attribute attr2 = new Attribute("key", "value", null);
        assertEquals("key", attr2.getKey());
        assertEquals("value", attr2.getValue());
        assertNull(attr2.parent);

        Attribute attr3 = new Attribute("key", "value", new Attributes());
        assertEquals("key", attr3.getKey());
        assertEquals("value", attr3.getValue());
        assertSame(attr3.parent, attr3.parent);
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
        assertEquals("value", attr.getValue());
        assertSame(attr.parent, attr.parent);
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("key", attr.getKey());
        assertEquals("newValue", attr.getValue());
        assertSame(attr.parent, attr.parent);
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
    public void testBooleanAttributes() {
        Attribute attr = new Attribute("checked", null);
        assertTrue(attr.isBooleanAttribute());
        attr.setValue("true");
        assertTrue(attr.isBooleanAttribute());
        assertTrue(attr.shouldCollapseAttribute());
        attr.setValue("false");
        assertFalse(attr.isBooleanAttribute());
        assertFalse(attr.shouldCollapseAttribute());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("data-test", "test");
        assertTrue(attr.isDataAttribute());
        attr.setKey("notData");
        assertFalse(attr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attr = new Attribute("checked", null);
        assertTrue(attr.shouldCollapseAttribute());
        attr.setValue("true");
        assertTrue(attr.shouldCollapseAttribute());
        attr.setValue("false");
        assertFalse(attr.shouldCollapseAttribute());
        attr.setValue("");
        assertTrue(attr.shouldCollapseAttribute());
        attr.setKey("notBoolean");
        assertFalse(attr.shouldCollapseAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");
        Attribute attr4 = new Attribute("key1", null);
        Attribute attr5 = new Attribute("key1", "value1", null);
        Attribute attr6 = new Attribute("key1", "value1", new Attributes());

        assertEquals(attr1, attr3);
        assertEquals(attr1.hashCode(), attr3.hashCode());

        assertNotEquals(attr1, attr2);
        assertNotEquals(attr1.hashCode(), attr2.hashCode());

        assertEquals(attr1, attr4);
        assertEquals(attr1.hashCode(), attr4.hashCode());

        assertEquals(attr1, attr5);
        assertEquals(attr1.hashCode(), attr5.hashCode());

        assertEquals(attr1, attr6);
        assertEquals(attr1.hashCode(), attr6.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clone = attr.clone();
        assertEquals(attr, clone);
        assertEquals(attr.hashCode(), clone.hashCode());
        assertNotSame(attr, clone);
    }
}