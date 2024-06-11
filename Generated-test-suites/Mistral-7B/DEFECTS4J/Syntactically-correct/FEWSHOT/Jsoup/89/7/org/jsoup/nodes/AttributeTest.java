package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        attr = new Attribute("key", "value", null);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.parent);

        attr = new Attribute("key", "value", new Attributes());
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertSame(attr.parent, attr.getParent());
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key1", "value");
        attr.setKey("key2");
        assertEquals("key2", attr.getKey());

        attr.setKey(null);
        assertNull(attr.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value1");
        attr.setValue("value2");
        assertEquals("value2", attr.getValue());

        attr.setValue(null);
        assertNull(attr.getValue());
    }

    @Test
    public void testStringRepresentation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.toString());
    }

    @Test
    public void testHtmlRepresentation() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute());

        attr = new Attribute("data-key", "value");
        assertTrue(attr.isDataAttribute());
    }

    @Test
    public void testCollapsibleAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.shouldCollapseAttribute());

        attr = new Attribute("checked", null);
        assertTrue(attr.shouldCollapseAttribute());

        attr = new Attribute("disabled", null);
        assertTrue(attr.shouldCollapseAttribute());

        attr = new Attribute("selected", null);
        assertTrue(attr.shouldCollapseAttribute());

        attr = new Attribute("readonly", null);
        assertTrue(attr.shouldCollapseAttribute());

        attr = new Attribute("required", null);
        assertTrue(attr.shouldCollapseAttribute());

        attr = new Attribute("multiple", null);
        assertTrue(attr.shouldCollapseAttribute());

        attr = new Attribute("hidden", null);
        assertTrue(attr.shouldCollapseAttribute());

        attr = new Attribute("checked", "checked");
        assertTrue(attr.shouldCollapseAttribute());

        attr = new Attribute("selected", "selected");
        assertTrue(attr.shouldCollapseAttribute());

        attr = new Attribute("disabled", "disabled");
        assertTrue(attr.shouldCollapseAttribute());

        attr = new Attribute("readonly", "readonly");
        assertTrue(attr.shouldCollapseAttribute());

        attr = new Attribute("required", "required");
        assertTrue(attr.shouldCollapseAttribute());

        attr = new Attribute("multiple", "multiple");
        assertTrue(attr.shouldCollapseAttribute());

        attr = new Attribute("hidden", "hidden");
        assertTrue(attr.shouldCollapseAttribute());
    }
}