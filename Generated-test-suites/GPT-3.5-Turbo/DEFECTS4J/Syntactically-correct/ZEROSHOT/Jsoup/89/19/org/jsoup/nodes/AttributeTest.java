import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AttributeTest {

    private Attribute attribute;
    private Attributes parent;

    @Before
    public void setUp() {
        parent = new Attributes();
        attribute = new Attribute("key", "value", parent);
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
        String oldValue = attribute.setValue("newValue");
        assertEquals("value", oldValue);
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
    public void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("unencodedKey", "encodedValue");
        assertEquals("unencodedKey", newAttribute.getKey());
        assertEquals("encodedValue", newAttribute.getValue());
        assertNull(newAttribute.parent);
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("key", "value", null);
        assertTrue(attribute.equals(sameAttribute));
        assertFalse(attribute.equals(null));
        assertFalse(attribute.equals(new Object()));
        assertFalse(attribute.equals(new Attribute("differentKey", "value", null)));
        assertFalse(attribute.equals(new Attribute("key", "differentValue", null)));
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("key", "value", null);
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertEquals(attribute.parent, clonedAttribute.parent);
    }
}