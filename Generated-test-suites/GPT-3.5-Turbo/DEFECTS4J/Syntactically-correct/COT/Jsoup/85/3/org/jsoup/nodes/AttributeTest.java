
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
        String toString = attribute.toString();
        assertEquals("testKey=\"testValue\"", toString);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("encodedKey", "encodedValue");
        assertNotNull(encodedAttribute);
        assertEquals("encodedKey", encodedAttribute.getKey());
        assertEquals("encodedValue", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        boolean isDataAttribute = attribute.isDataAttribute();
        assertFalse(isDataAttribute);
    }

    @Test
    public void testShouldCollapseAttribute() {
        boolean shouldCollapseAttribute = attribute.shouldCollapseAttribute(new Document.OutputSettings());
        assertFalse(shouldCollapseAttribute);
    }

    @Test
    public void testIsBooleanAttribute() {
        boolean isBooleanAttribute = attribute.isBooleanAttribute();
        assertFalse(isBooleanAttribute);
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("testKey", "testValue");
        assertEquals(attribute, sameAttribute);
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
        assertNotSame(attribute, clonedAttribute);
    }
}
