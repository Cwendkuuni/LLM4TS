package org.jsoup.nodes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {
    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @After
    public void tearDown() {
        attribute = null;
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
        Attribute equalsAttribute = new Attribute("key", "value");
        assertEquals(equalsAttribute, attribute);

        equalsAttribute.setKey("newKey");
        assertNotEquals(equalsAttribute, attribute);

        equalsAttribute.setKey("key");
        equalsAttribute.setValue("newValue");
        assertNotEquals(equalsAttribute, attribute);
    }

    @Test
    public void testHashCode() {
        Attribute equalsAttribute = new Attribute("key", "value");
        assertEquals(equalsAttribute.hashCode(), attribute.hashCode());

        equalsAttribute.setKey("newKey");
        assertNotEquals(equalsAttribute.hashCode(), attribute.hashCode());

        equalsAttribute.setKey("key");
        equalsAttribute.setValue("newValue");
        assertNotEquals(equalsAttribute.hashCode(), attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clone = attribute.clone();
        assertEquals(attribute, clone);
        assertNotSame(attribute, clone);
    }
}