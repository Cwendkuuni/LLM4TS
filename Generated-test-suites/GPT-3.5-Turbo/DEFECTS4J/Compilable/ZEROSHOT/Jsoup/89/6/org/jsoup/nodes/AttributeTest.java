package org.jsoup.nodes;
import org.junit.Test;
import org.junit.Before;
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
    public void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("unencodedKey", "encodedValue");
        assertEquals("unencodedKey", newAttribute.getKey());
        assertEquals("encodedValue", newAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        assertTrue(Attribute.isDataAttribute("data-test"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        assertTrue(attribute.shouldCollapseAttribute("key", null, new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        assertTrue(Attribute.isBooleanAttribute("checked"));
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("key", "value");
        Attribute differentAttribute = new Attribute("key", "differentValue");

        assertTrue(attribute.equals(sameAttribute));
        assertFalse(attribute.equals(differentAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("key", "value");

        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute, clonedAttribute);
    }

}