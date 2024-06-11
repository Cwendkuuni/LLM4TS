package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("testKey", "testValue");
    }

    @Test
    public void testGetKey() {
        String key = attribute.getKey();
        assertEquals("testKey", key);
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        String key = attribute.getKey();
        assertEquals("newKey", key);
    }

    @Test
    public void testGetValue() {
        String value = attribute.getValue();
        assertEquals("testValue", value);
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        String value = attribute.getValue();
        assertEquals("newValue", value);
    }

    @Test
    public void testHtml() {
        String html = attribute.html();
        assertEquals("testKey=\"testValue\"", html);
    }

    @Test
    public void testToString() {
        String string = attribute.toString();
        assertEquals("testKey=\"testValue\"", string);
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("testKey", "testValue");
        Attribute differentAttribute = new Attribute("differentKey", "differentValue");

        assertTrue(attribute.equals(sameAttribute));
        assertFalse(attribute.equals(differentAttribute));
    }

    @Test
    public void testHashCode() {
        int hashCode = attribute.hashCode();
        assertEquals(110806421, hashCode);
    }

    @Test
    public void testClone() {
        Attribute cloneAttribute = attribute.clone();

        assertEquals(attribute.getKey(), cloneAttribute.getKey());
        assertEquals(attribute.getValue(), cloneAttribute.getValue());
    }
}