import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.SerializationException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class AttributeTest {
    private Attribute attribute;
    private Attribute nullValueAttribute;
    private Attributes parent;

    @Before
    public void setUp() {
        parent = new Attributes();
        attribute = new Attribute("key", "value", parent);
        nullValueAttribute = new Attribute("nullKey", null, parent);
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
    public void testSetKey_NullKey() {
        try {
            attribute.setKey(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testSetKey_EmptyKey() {
        try {
            attribute.setKey("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
        assertNull(nullValueAttribute.getValue());
    }

    @Test
    public void testSetValue() {
        String oldValue = attribute.setValue("newValue");
        assertEquals("value", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testSetValue_NullValue() {
        try {
            attribute.setValue(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testSetValue_KeyNotPresentInParent() {
        try {
            attribute.setValue("value");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertNotNull(e.getMessage());
        }
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
}