package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testGetKey() {
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newkey");
        assertEquals("newkey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newvalue");
        assertEquals("newvalue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testToString() {
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testClone() {
        Attribute clone = attribute.clone();
        assertNotNull(clone);
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("encodedkey", "encodedvalue");
        assertNotNull(encodedAttribute);
        assertEquals("encodedkey", encodedAttribute.getKey());
        assertEquals("encodedvalue", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(null));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("key", "value");
        assertTrue(attribute.equals(sameAttribute));

        Attribute differentKeyAttribute = new Attribute("differentkey", "value");
        assertFalse(attribute.equals(differentKeyAttribute));

        Attribute differentValueAttribute = new Attribute("key", "differentvalue");
        assertFalse(attribute.equals(differentValueAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());

        Attribute differentKeyAttribute = new Attribute("differentkey", "value");
        assertNotEquals(attribute.hashCode(), differentKeyAttribute.hashCode());

        Attribute differentValueAttribute = new Attribute("key", "differentvalue");
        assertNotEquals(attribute.hashCode(), differentValueAttribute.hashCode());
    }
}