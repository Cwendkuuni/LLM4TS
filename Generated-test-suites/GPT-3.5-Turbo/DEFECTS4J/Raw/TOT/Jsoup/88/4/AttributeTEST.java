
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setup() {
        attribute = new Attribute("testKey", "testValue");
    }

    @Test
    public void testGetKey() {
        assertEquals("testKey", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetKeyWithNullKey() {
        try {
            attribute.setKey(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testSetKeyWithEmptyKey() {
        try {
            attribute.setKey("");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testSetValueWithExistingParent() {
        Attributes parent = new Attributes();
        parent.put("key1", "value1");
        parent.put("key2", "value2");
        attribute = new Attribute("key1", "value1", parent);
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertEquals("newValue", parent.get("key1"));
    }

    @Test
    public void testSetValueWithNonExistingParent() {
        Attributes parent = new Attributes();
        parent.put("key1", "value1");
        parent.put("key2", "value2");
        attribute = new Attribute("key3", "value3", parent);
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
        assertNull(parent.get("key3"));
    }

    @Test
    public void testHtml() {
        assertEquals("testKey=\"testValue\"", attribute.html());
    }

    @Test
    public void testToString() {
        assertEquals("testKey=\"testValue\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("testKey", "testValue");
        assertEquals("testKey", encodedAttribute.getKey());
        assertEquals("testValue", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testIsDataAttributeWithDataPrefix() {
        Attribute dataAttribute = new Attribute("data-key", "value");
        assertTrue(dataAttribute.isDataAttribute());
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanValue() {
        Attribute booleanAttribute = new Attribute("selected", null);
        assertTrue(booleanAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeWithEmptyValue() {
        Attribute emptyValueAttribute = new Attribute("testKey", "");
        assertTrue(emptyValueAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeWithKeySameAsValue() {
        Attribute keySameAsValueAttribute = new Attribute("testKey", "testKey");
        assertTrue(keySameAsValueAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testEquals() {
        Attribute equalAttribute = new Attribute("testKey", "testValue");
        assertEquals(attribute, equalAttribute);
    }

    @Test
    public void testEqualsWithDifferentValues() {
        Attribute differentValueAttribute = new Attribute("testKey", "differentValue");
        assertNotEquals(attribute, differentValueAttribute);
    }

    @Test
    public void testEqualsWithNullKeyInAttribute() {
        Attribute nullKeyAttribute = new Attribute(null, "testValue");
        assertNotEquals(attribute, nullKeyAttribute);
    }

    @Test
    public void testHashCode() {
        Attribute equalAttribute = new Attribute("testKey", "testValue");
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
    }
}

