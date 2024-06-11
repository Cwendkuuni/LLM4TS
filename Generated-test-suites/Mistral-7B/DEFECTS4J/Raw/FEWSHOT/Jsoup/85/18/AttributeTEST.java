package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testCreateAndSetAttributes() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());

        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test
    public void testBooleanAttributes() {
        Attribute attr = new Attribute("checked", null);
        assertTrue(attr.isBooleanAttribute());

        attr = new Attribute("notABoolean", null);
        assertFalse(attr.isBooleanAttribute());

        attr = new Attribute("checked", "true");
        assertTrue(attr.isBooleanAttribute());
        assertTrue(attr.isDataAttribute());

        attr = new Attribute("checked", "false");
        assertTrue(attr.isBooleanAttribute());
        assertTrue(attr.isDataAttribute());

        attr = new Attribute("checked", "");
        assertTrue(attr.isBooleanAttribute());
        assertTrue(attr.isDataAttribute());

        attr = new Attribute("checked", null);
        assertFalse(attr.shouldCollapseAttribute(null));
        assertTrue(attr.shouldCollapseAttribute(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html)));

        attr = new Attribute("notABoolean", "value");
        assertFalse(attr.shouldCollapseAttribute(null));
        assertFalse(attr.shouldCollapseAttribute(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testEqualsAndHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        Attribute attr3 = new Attribute("key1", "value1");

        assertEquals(attr1, attr3);
        assertEquals(attr1.hashCode(), attr3.hashCode());

        assertNotEquals(attr1, attr2);
        assertNotEquals(attr1.hashCode(), attr2.hashCode());

        Attribute attr4 = attr1.clone();
        assertEquals(attr1, attr4);
        assertEquals(attr1.hashCode(), attr4.hashCode());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetKeyWithNullParent() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithEmptyKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithNullKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
    }

    @Test
    public void testGetAndSetParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value");
        attr.setParent(parent);
        assertEquals(parent, attr.getParent());

        parent = null;
        attr.setParent(parent);
        assertNull(attr.getParent());
    }
}