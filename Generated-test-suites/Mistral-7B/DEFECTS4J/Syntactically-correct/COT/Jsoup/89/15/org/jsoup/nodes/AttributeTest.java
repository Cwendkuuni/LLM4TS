package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Test;

/**
 * Test class for Attribute.
 */
public class AttributeTest {

    @Test
    public void testAttributeConstructorWithKeyAndValue() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
    }

    @Test
    public void testAttributeConstructorWithKeyValueAndParent() {
        String key = "testKey";
        String value = "testValue";
        Attributes parent = new Attributes();

        Attribute attribute = new Attribute(key, value, parent);

        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
        assertSame(parent, attribute.parent);
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeNullKey() {
        String key = null;
        String value = "testValue";

        new Attribute(key, value);
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeNullValue() {
        String key = "testKey";
        String value = null;

        new Attribute(key, value);
    }

    @Test
    public void testAttributeSetKey() {
        String initialKey = "initialKey";
        String value = "testValue";
        Attribute attribute = new Attribute(initialKey, value);
        String newKey = "newKey";

        attribute.setKey(newKey);

        assertEquals(newKey, attribute.getKey());
    }

    @Test
    public void testHtmlMethod() throws IOException {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());

        assertEquals("testKey=\"testValue\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testToStringMethod() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        assertEquals("testKey=\"testValue\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "unencodedTestKey";
        String encodedValue = "encodedTestValue";

        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);

        assertEquals(unencodedKey, attribute.getKey());
        assertEquals("encodedTestValue", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        String key1 = "testKey1";
        String key2 = "data-testKey2";

        Attribute attribute1 = new Attribute(key1, null);
        Attribute attribute2 = new Attribute(key2, null);

        assertTrue(attribute1.isDataAttribute());
        assertTrue(attribute2.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Document.OutputSettings out = new Document.OutputSettings();

        Attribute attribute1 = new Attribute("testKey1", "testValue1");
        Attribute attribute2 = new Attribute("testKey2", "");
        Attribute attribute3 = new Attribute("testKey3");
        Attribute attribute4 = new Attribute("testKey4", "testValue4");

        assertTrue(attribute1.shouldCollapseAttribute(out));
        assertFalse(attribute2.shouldCollapseAttribute(out));
        assertFalse(attribute3.shouldCollapseAttribute(out));
        assertTrue(attribute4.shouldCollapseAttribute(out));
    }

    @Test
    public void testCloneMethod() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        Attribute clonedAttribute = attribute.clone();

        assertEquals(key, clonedAttribute.getKey());
        assertEquals(value, clonedAttribute.getValue());
        assertSame(attribute.parent, clonedAttribute.parent);
    }

    @Test
    public void testEqualsMethod() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        String key2 = "testKey2";
        String value2 = "testValue2";
        Attribute attribute1 = new Attribute(key1, value1);
        Attribute attribute2 = new Attribute(key2, value2);
        Attribute attribute3 = new Attribute(key1, value1);
        Attribute attribute4 = new Attribute(key1, value2);

        assertTrue(attribute1.equals(attribute3));
        assertFalse(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(null));
    }

    @Test
    public void testHashCodeMethod() {
        String key1 = "testKey1";
        String key2 = "testKey2";
        String value1 = "testValue1";
        String value2 = "testValue2";
        Attribute attribute1 = new Attribute(key1, value1);
        Attribute attribute2 = new Attribute(key2, value2);

        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }
}