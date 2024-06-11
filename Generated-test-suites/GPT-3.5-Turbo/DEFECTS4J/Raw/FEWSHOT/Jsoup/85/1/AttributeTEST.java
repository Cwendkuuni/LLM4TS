package org.jsoup.nodes;

import org.jsoup.SerializationException;
import org.jsoup.internal.StringUtil;
import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 A single key + value attribute. (Only used for presentation.)
 */
public class AttributeTest {
    @Test
    public void testAttributeConstructor() {
        Attribute attr1 = new Attribute("key1", "value1");
        assertEquals("key1", attr1.getKey());
        assertEquals("value1", attr1.getValue());
        assertNull(attr1.parent);

        Attributes parent = new Attributes();
        Attribute attr2 = new Attribute("key2", "value2", parent);
        assertEquals("key2", attr2.getKey());
        assertEquals("value2", attr2.getValue());
        assertEquals(parent, attr2.parent);
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
        assertNull(attr.parent);

        Attributes parent = new Attributes();
        Attribute attr2 = new Attribute("key", "value", parent);
        attr2.setKey("newKey");
        assertEquals("newKey", attr2.getKey());
        assertSame(parent, attr2.parent);
        assertEquals(-1, Arrays.asList(parent.keys).indexOf("key"));
        assertNotEquals(-1, Arrays.asList(parent.keys).indexOf("newKey"));
    }

    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
        assertNull(attr.parent);

        Attributes parent = new Attributes();
        Attribute attr2 = new Attribute("key", "value", parent);
        attr2.setValue("newValue");
        assertEquals("newValue", attr2.getValue());
        assertSame(parent, attr2.parent);
        assertEquals(-1, Arrays.asList(parent.vals).indexOf("value"));
        assertNotEquals(-1, Arrays.asList(parent.vals).indexOf("newValue"));
    }

    @Test
    public void testHtml() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.html());

        Attributes parent = new Attributes();
        Attribute attr2 = new Attribute("href", "index.html", parent);
        Document.OutputSettings out = new Document.OutputSettings();
        assertEquals("href=\"index.html\"", attr2.html(out));
    }

    @Test
    public void testToString() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("href", "index.html");
        assertEquals("hreffromEncoded.html", attr.getKey());
        assertEquals("index.htmlfromEncoded.html", attr.getValue());
        assertNull(attr.parent);
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(Attribute.isDataAttribute("key"));
        assertFalse(Attribute.isDataAttribute("data-key"));
        assertTrue(Attribute.isDataAttribute("data-key-value"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attr1 = new Attribute("key", "");
        Attribute attr2 = new Attribute("key", "key");

        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(attr1.shouldCollapseAttribute(out));
        assertFalse(attr2.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("allowfullscreen"));
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertTrue(Attribute.isBooleanAttribute("async"));
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        Attribute attr3 = new Attribute("key", "newValue");
        Attribute attr4 = new Attribute("newKey", "value");
        Attribute attr5 = new Attribute("newKey", "newValue");

        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
        assertFalse(attr1.equals(attr4));
        assertFalse(attr1.equals(attr5));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        Attribute attr3 = new Attribute("key", "newValue");
        Attribute attr4 = new Attribute("newKey", "value");

        assertEquals(attr1.hashCode(), attr2.hashCode());
        assertNotEquals(attr1.hashCode(), attr3.hashCode());
        assertNotEquals(attr1.hashCode(), attr4.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = attr1.clone();

        assertEquals(attr1.getKey(), attr2.getKey());
        assertEquals(attr1.getValue(), attr2.getValue());
        assertNull(attr1.parent);
        assertNull(attr2.parent);
        assertNotSame(attr1, attr2);
    }
}