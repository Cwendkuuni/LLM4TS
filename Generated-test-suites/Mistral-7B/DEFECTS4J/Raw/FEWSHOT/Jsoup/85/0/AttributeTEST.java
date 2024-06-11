package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());

        Attribute attrParent = new Attributes();
        attr = new Attribute("key", "value", attrParent);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertSame(attrParent, attr.getParent());

        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
        assertEquals("value", attr.getValue());
        assertSame(attrParent, attr.getParent());

        attr.setValue("newValue");
        assertEquals("newKey", attr.getKey());
        assertEquals("newValue", attr.getValue());
        assertSame(attrParent, attr.getParent());
    }

    @Test
    public void testCreateAttributeFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "value=\"foo\"");
        assertEquals("key", attr.getKey());
        assertEquals("foo", attr.getValue());
        assertNull(attr.getParent());
    }

    @Test
    public void testIsBooleanAttribute() {
        for (String attrName : Attribute.booleanAttributes) {
            assertTrue(Attribute.isBooleanAttribute(attrName));
        }

        String attrName = "notABoolean";
        assertFalse(Attribute.isBooleanAttribute(attrName));

        Attribute attr = new Attribute(attrName, null);
        assertFalse(attr.isBooleanAttribute());

        attr = new Attribute(attrName, "");
        assertFalse(attr.isBooleanAttribute());

        attr = new Attribute(attrName, "true");
        assertTrue(attr.isBooleanAttribute());

        attr = new Attribute(attrName, "false");
        assertTrue(attr.isBooleanAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        assertEquals(true, Attribute.shouldCollapseAttribute("input", null, out));
        assertEquals(true, Attribute.shouldCollapseAttribute("input", "", out));
        assertEquals(true, Attribute.shouldCollapseAttribute("input", "input", out));
        assertEquals(true, Attribute.shouldCollapseAttribute("input", "true", out));
        assertEquals(true, Attribute.shouldCollapseAttribute("input", "false", out));
        assertEquals(true, Attribute.shouldCollapseAttribute("input", "input type=\"text\"", out));
        assertEquals(false, Attribute.shouldCollapseAttribute("class", "foo bar", out));
        assertEquals(false, Attribute.shouldCollapseAttribute("class", "foo", out));
        assertEquals(false, Attribute.shouldCollapseAttribute("class", "foo bar baz", out));
    }

    @Test
    public void testEqualsAndHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");

        assertEquals(attr1, attr1);
        assertEquals(attr1, attr2);
        assertEquals(attr1.hashCode(), attr2.hashCode());

        assertNotEquals(attr1, attr3);
        assertNotEquals(attr1.hashCode(), attr3.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute cloned = attr.clone();
        assertEquals(attr, cloned);
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneException() {
        Attribute attr = new Attribute("key", "value");
        attr.clone();
    }
}