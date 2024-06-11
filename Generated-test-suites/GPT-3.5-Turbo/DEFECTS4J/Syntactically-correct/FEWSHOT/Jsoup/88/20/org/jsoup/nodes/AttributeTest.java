
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

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
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
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
    public void testEquals() {
        Attribute sameAttribute = new Attribute("key", "value");
        Attribute differentAttribute = new Attribute("differentKey", "value");
        assertEquals(attribute, sameAttribute);
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
        assertNotEquals(attribute, differentAttribute);
        assertNotEquals(attribute.hashCode(), differentAttribute.hashCode());
    }

    @Test(expected = RuntimeException.class)
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
        assertEquals(attribute.hashCode(), clonedAttribute.hashCode());
        assertNotSame(attribute, clonedAttribute);
        clonedAttribute.setKey("newKey");
        assertNotEquals(attribute, clonedAttribute);
        assertNotEquals(attribute.hashCode(), clonedAttribute.hashCode());
    }
}
