package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
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
        assertEquals(attr, attr.setKey("newKey"));
        assertEquals("newKey", attr.getKey());
        assertNull(attr.getParent());
    }

    @Test
    public void testCreateAttributeWithParent() {
        Attribute attr = new Attribute("key", "value", attributes);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals(attributes, attr.getParent());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("value", attr.getValue());
        assertEquals("newValue", attr.setValue("newValue"));
        assertEquals("newValue", attr.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testValueIsNull() {
        Attribute attr = new Attribute("key", null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb).toString());
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
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute());
        attr = new Attribute("data-key", "value");
        assertTrue(attr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertTrue(Attribute.shouldCollapseAttribute(null, null, null));
        assertTrue(Attribute.shouldCollapseAttribute("key", null, null));
        assertTrue(Attribute.shouldCollapseAttribute("key", "", null));
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", null));
        assertTrue(Attribute.shouldCollapseAttribute("key", "value", null));
        assertTrue(Attribute.shouldCollapseAttribute("key", "value", null, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value", null, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.xml)));
        assertFalse(Attribute.shouldCollapseAttribute("key", "value1", null, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isBooleanAttribute());
        attr = new Attribute("key", null);
        assertFalse(attr.isBooleanAttribute());
        attr = new Attribute("checked", null);
        assertTrue(attr.isBooleanAttribute());
    }

    @Test
    public void testIsDataAttributeWithPrefix() {
        assertTrue(Attribute.isDataAttribute("data-key"));
        assertFalse(Attribute.isDataAttribute("key"));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        assertEquals(attr1, attr1);
        assertEquals(attr1, attr2);
        assertNotEquals(attr1, attr3);
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        assertEquals(attr1.hashCode(), attr1.hashCode());
        assertEquals(attr1.hashCode(), attr2.hashCode());
        assertNotEquals(attr1.hashCode(), attr3.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr, clonedAttr);
    }

    @Test
    public void testMapEntryMethods() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals("value", attr.get(attr.getKey()));
        assertSame(attr, attr.forEntry(attr));
    }
}