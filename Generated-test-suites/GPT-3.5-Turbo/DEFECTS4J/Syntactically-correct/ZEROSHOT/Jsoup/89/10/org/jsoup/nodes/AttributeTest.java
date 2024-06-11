import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

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
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute, attribute2);
    }

    @Test
    public void testEqualsWithDifferentKey() {
        Attribute attribute2 = new Attribute("differentKey", "value");
        assertNotEquals(attribute, attribute2);
    }

    @Test
    public void testEqualsWithDifferentValue() {
        Attribute attribute2 = new Attribute("key", "differentValue");
        assertNotEquals(attribute, attribute2);
    }

    @Test
    public void testEqualsWithNullKey() {
        Attribute attribute2 = new Attribute(null, "value");
        assertNotEquals(attribute, attribute2);
    }

    @Test
    public void testEqualsWithNullValue() {
        Attribute attribute2 = new Attribute("key", null);
        assertNotEquals(attribute, attribute2);
    }

    @Test
    public void testHashCode() {
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentKey() {
        Attribute attribute2 = new Attribute("differentKey", "value");
        assertNotEquals(attribute.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentValue() {
        Attribute attribute2 = new Attribute("key", "differentValue");
        assertNotEquals(attribute.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute cloneAttribute = attribute.clone();
        assertEquals(attribute, cloneAttribute);
    }
}