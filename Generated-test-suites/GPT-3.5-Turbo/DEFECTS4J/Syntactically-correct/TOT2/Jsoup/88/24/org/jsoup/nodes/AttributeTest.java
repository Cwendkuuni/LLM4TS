import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
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
    public void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("unencodedKey", "encodedValue");
        assertEquals("unencodedKey", newAttribute.getKey());
        assertEquals("encodedValue", newAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(Attribute.isDataAttribute("key"));
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
        Attribute sameAttribute = new Attribute("key", "value");
        Attribute diffAttribute = new Attribute("differentKey", "differentValue");
        
        assertTrue(attribute.equals(sameAttribute));
        assertFalse(attribute.equals(diffAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("key", "value");
        
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertNotSame(attribute, clonedAttribute);
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}