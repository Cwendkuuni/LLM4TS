package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AttributeTest {

    private Attribute attr;

    @Before
    public void setUp() {
        attr = new Attribute("key", "value");
    }

    @Test
    public void testGetKey() {
        assertEquals("key", attr.getKey());
    }

    @Test
    public void testSetKey() {
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testGetStringValue() {
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testSetStringValue() {
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test
    public void testHtml() throws IOException {
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(attr.isDataAttribute());
        attr = new Attribute("id", "value");
        assertFalse(attr.isDataAttribute());
    }

    @Test
    public void testshouldCollapseAttribute() throws IOException {
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(attr.shouldCollapseAttribute(out));
        attr = new Attribute("id", "value");
        assertFalse(attr.shouldCollapseAttribute(out));

        out.syntax(Document.OutputSettings.Syntax.xml);
        attr = new Attribute("checked", "");
        assertTrue(attr.shouldCollapseAttribute(out));
        attr = new Attribute("checked", "true");
        assertFalse(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(attr.isBooleanAttribute());
        attr = new Attribute("id", "value");
        assertFalse(attr.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttribute2() {
        assertTrue(attr.isBooleanAttribute());
        attr = new Attribute("checked", null);
        assertTrue(attr.isBooleanAttribute());

        attr = new Attribute("allowfullscreen", null);
        assertFalse(attr.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute other = new Attribute("key", "value");
        Attribute other2 = new Attribute("key2", "value2");
        Attribute other3 = new Attribute("key", "value");

        assertEquals(attr, attr);
        assertEquals(attr, other);
        assertEquals(other, attr);

        assertNotEquals(attr, other2);
        assertNotEquals(other2, attr);
        assertNotEquals(other2, other);

        assertNotEquals(attr, null);
        assertNotEquals(null, attr);
        assertNotEquals(attr, new Object());
    }

    @Test
    public void testHashCode() {
        Attribute other = new Attribute("key", "value");
        Attribute other2 = new Attribute("key2", "value2");

        int hashcode1 = attr.hashCode();
        int hashcode2 = other.hashCode();
        int hashcode3 = other2.hashCode();

        assertEquals(hashcode1, hashcode1);
        assertEquals(hashcode1, hashcode2);
        assertEquals(hashcode2, hashcode2);

        assertNotEquals(hashcode1, hashcode3);
        assertNotEquals(hashcode2, hashcode1);
        assertNotEquals(hashcode2, hashcode3);

        Attribute clone = attr.clone();
        assertEquals(hashcode1, clone.hashCode());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test(expected = SerializationException.class)
    public void testCreateFromEncodedWithException() throws IOException {
        Attribute.createFromEncoded(null, "key=\"value\"");
    }

    @Test
    public void testClone() {
        Attribute clone = attr.clone();
        assertEquals(attr, clone);
    }

    @Test
    public void testMap() {
        Map<String, Attribute> attributes = new HashMap<>();
        attributes.put("key1", new Attribute("key1", "value1"));
        attributes.put("key2", new Attribute("key2", "value2"));

        Attribute attr = attributes.get("key1");
        assertEquals("value1", attr.getValue());
    }
}