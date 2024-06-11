package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;

import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value", parent);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals(attr, parent.get("key"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKey() {
        new Attribute("", "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullParent() {
        new Attribute("key", "value", null);
    }

    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testNullNewKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyNewKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("");
    }

    @Test
    public void testSetParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value", null);
        parent.put(attr);
        assertEquals(attr, parent.get("key"));
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
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertTrue(attr.isDataAttribute());
        attr.setKey("data:key");
        assertTrue(attr.isDataAttribute());
        attr.setKey("key");
        assertFalse(attr.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attr = new Attribute("key", "value");
        assertTrue(attr.shouldCollapseAttribute(out));
        attr.setKey("data:key");
        assertFalse(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.isBooleanAttribute());
        attr.setKey("checked");
        assertTrue(attr.isBooleanAttribute());
        attr.setKey("key");
        assertFalse(attr.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeWithBooleanValue() {
        Attribute attr = new Attribute("key", "true");
        assertTrue(attr.isBooleanAttribute());
        attr.setKey("key");
        assertFalse(attr.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeWithEmptyValue() {
        Attribute attr = new Attribute("key", "");
        assertTrue(attr.isBooleanAttribute());
        attr.setKey("key");
        assertFalse(attr.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));
        assertFalse(attr1.equals(null));
    }

    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        int hashCode1 = attr1.hashCode();
        int hashCode2 = attr1.hashCode();
        int hashCode3 = attr2.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals("key", clonedAttr.getKey());
        assertEquals("value", clonedAttr.getValue());
    }

    @Test
    public void testBooleanAttributes() {
        String[] booleanAttributes = Attribute.booleanAttributes;
        assertEquals(booleanAttributes.length, 38);
        assertTrue(Arrays.binarySearch(booleanAttributes, "checked") >= 0);
        assertTrue(Arrays.binarySearch(booleanAttributes, "disabled") >= 0);
        assertTrue(Arrays.binarySearch(booleanAttributes, "multiple") >= 0);
        assertFalse(Arrays.binarySearch(booleanAttributes, "key") >= 0);
    }
}