
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;

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

    @Test(expected = NullPointerException.class)
    public void testSetKeyWithNull() {
        attribute.setKey(null);
    }

    @Test
    public void testGetValue() {
        assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueWithNull() {
        attribute.setValue(null);
    }

    @Test
    public void testHtml() {
        assertEquals("\"testKey\"=\"testValue\"", attribute.html());
    }

    @Test
    public void testToString() {
        assertEquals("\"testKey\"=\"testValue\"", attribute.toString());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testIsDataAttributeWithDataPrefix() {
        attribute = new Attribute("data-key", "testValue");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanAttribute() {
        attribute = new Attribute("checked", null);
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeWithBooleanAttribute() {
        attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("testKey", "testValue");
        assertTrue(attribute.equals(sameAttribute));
    }

    @Test
    public void testEqualsWithDifferentKey() {
        Attribute differentAttribute = new Attribute("differentKey", "testValue");
        assertFalse(attribute.equals(differentAttribute));
    }

    @Test
    public void testEqualsWithDifferentValue() {
        Attribute differentAttribute = new Attribute("testKey", "differentValue");
        assertFalse(attribute.equals(differentAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("testKey", "testValue");
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}

