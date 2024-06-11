import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, attribute.html());
    }

    @Test
    public void testToString() {
        String expectedString = "key=\"value\"";
        assertEquals(expectedString, attribute.toString());
    }

    @Test
    public void testEquals() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertTrue(attribute.equals(equalAttribute));
    }

    @Test
    public void testNotEquals() {
        Attribute notEqualAttribute = new Attribute("key", "newValue");
        assertFalse(attribute.equals(notEqualAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}