package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.jsoup.helper.Validate.*;

import java.util.Arrays;

import org.junit.Test;

public class AttributeTest {

    @Test
    public void testCreateAttributeWithNullKey() {
        try {
            new Attribute(null, "value");
            fail("Should have thrown a NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCreateAttributeWithEmptyKey() {
        try {
            new Attribute("", "value");
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateAttributeWithKey() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.parent);
    }

    @Test
    public void testCreateAttributeWithKeyAndParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value", parent);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals(parent, attr.parent);
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.parent);
    }

    @Test
    public void testSetKeyWithNull() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
        assertNull(attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.parent);
    }

    @Test
    public void testSetKeyWithEmptyString() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("");
        assertEquals("", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.parent);
    }

    @Test
    public void testSetKeyWithSameValueParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key1", "value1", parent);
        attr.setKey("key1");
        assertEquals("key1", attr.getKey());
        assertEquals("value1", attr.getValue());
        assertEquals(parent, attr.parent);
    }

    @Test
    public void testSetKeyWithDifferentValueParent() {
        Attributes parent1 = new Attributes();
        Attribute attr1 = new Attribute("key1", "value1", parent1);
        Attributes parent2 = new Attributes();
        attr1.setKey("key2");
        assertEquals("key2", attr1.getKey());
        assertNotSame(parent1, attr1.parent);
        assertSame(parent2, attr1.parent);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String html = StringUtil.releaseBuilder(sb);
        assertEquals("key=\"value\"", html);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.parent);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isDataAttribute());

        attr = new Attribute("data-key", "value");
        assertTrue(attr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttributeFalse() throws IOException {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeTrue() throws IOException {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        Attribute attr = new Attribute("key", "");
        assertTrue(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeBooleanTrue() throws IOException {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        Attribute attr = new Attribute("checked", "");
        assertTrue(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeBooleanFalse() throws IOException {
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        Attribute attr = new Attribute("not-a-boolean-attribute", "value");
        assertFalse(attr.shouldCollapseAttribute(out));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShouldCollapseAttributeWithNullOut() {
        new Attribute("key", "value").shouldCollapseAttribute(null);
    }
}