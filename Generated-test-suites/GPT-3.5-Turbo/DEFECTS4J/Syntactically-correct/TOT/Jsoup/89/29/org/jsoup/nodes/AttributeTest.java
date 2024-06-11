import org.junit.Before;
import org.junit.Test;
import org.jsoup.SerializationException;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
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
    public void testEmptyKey() {
        try {
            attribute.setKey("");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Key must not be empty", e.getMessage());
        }
    }

    @Test
    public void testNullKey() {
        try {
            attribute.setKey(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Key must not be null", e.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        String oldValue = attribute.setValue("newValue");
        assertEquals("testValue", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testEmptyValue() {
        try {
            attribute.setValue("");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Value must not be empty", e.getMessage());
        }
    }

    @Test
    public void testNullValue() {
        try {
            attribute.setValue(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Value must not be null", e.getMessage());
        }
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
        Attribute newAttribute = Attribute.createFromEncoded("newKey", "&lt;a&gt;");
        assertEquals("newKey", newAttribute.getKey());
        assertEquals("<a>", newAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        assertTrue(Attribute.isDataAttribute("data-key"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute());
        assertTrue(Attribute.shouldCollapseAttribute("key", "key", new Document.OutputSettings()));
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("testKey", "testValue");
        Attribute differentAttribute = new Attribute("differentKey", "testValue");

        assertTrue(attribute.equals(sameAttribute));
        assertFalse(attribute.equals(differentAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("testKey", "testValue");
        Attribute differentAttribute = new Attribute("differentKey", "testValue");

        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
        assertNotEquals(attribute.hashCode(), differentAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();

        assertNotSame(attribute, clonedAttribute);
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}