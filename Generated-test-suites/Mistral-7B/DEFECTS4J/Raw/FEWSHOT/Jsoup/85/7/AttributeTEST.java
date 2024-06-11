package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributeTest {
    @Test
    public void testCreateAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals("value", attr.toString());
    }

    @Test
    public void testCreateAttributeWithParent() {
        Attributes attributes = new Attributes();
        Attribute attr = new Attribute("key", "value", attributes);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals("value", attr.toString());
        assertEquals(attr, attributes.get("key"));
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key1", "value");
        attr.setKey("key2");
        assertEquals("key2", attr.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyNull() {
        Attribute attr = new Attribute("key1", "value");
        attr.setKey(null);
    }

    @Test
    public void testSetKeyEmpty() {
        Attribute attr = new Attribute("key1", "value");
        attr.setKey("");
        assertEquals("", attr.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value1");
        attr.setValue("value2");
        assertEquals("value2", attr.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueNull() {
        Attribute attr = new Attribute("key", "value1");
        attr.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
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
    public void testShouldCollapseAttribute() {
        Attribute attr = new Attribute("key", "");
        assertTrue(attr.shouldCollapseAttribute(null));

        attr = new Attribute("key", "value");
        assertFalse(attr.shouldCollapseAttribute(null));

        attr = new Attribute("key", "key");
        assertTrue(attr.shouldCollapseAttribute(null));

        attr = new Attribute("boolean", "");
        assertTrue(attr.shouldCollapseAttribute(null));

        attr = new Attribute("boolean", "true");
        assertTrue(attr.shouldCollapseAttribute(null));

        attr = new Attribute("boolean", "false");
        assertFalse(attr.shouldCollapseAttribute(null));

        attr = new Attribute("key", "value", new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html));
        assertTrue(attr.shouldCollapseAttribute(null));

        attr = new Attribute("key", "value", new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml));
        assertFalse(attr.shouldCollapseAttribute(null));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value1");

        assertEquals(attr1, attr1);
        assertEquals(attr1, attr2);
        assertNotEquals(attr1, attr3);
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value1");

        assertEquals(attr1.hashCode(), attr1.hashCode());
        assertEquals(attr1.hashCode(), attr2.hashCode());
        assertNotEquals(attr1.hashCode(), attr3.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();

        assertEquals(attr, clonedAttr);
        assertNotEquals(attr, clonedAttr);
    }

    @Test
    public void testIsBooleanAttributeDeprecated() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute("key"));
    }

    @Test
    public void testBooleanAttributes() {
        List<String> booleanAttributes = Arrays.asList(Attribute.booleanAttributes);

        assertTrue(booleanAttributes.contains("allowfullscreen"));
        assertTrue(booleanAttributes.contains("async"));
        // ... and so on for all boolean attributes
    }
}