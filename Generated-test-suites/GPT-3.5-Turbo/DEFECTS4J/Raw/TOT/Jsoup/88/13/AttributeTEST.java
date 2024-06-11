import org.junit.After;
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

    @After
    public void tearDown() {
        attribute = null;
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
        Attribute encodedAttribute = Attribute.createFromEncoded("encodedKey", "encodedValue");
        assertEquals("encodedKey", encodedAttribute.getKey());
        assertEquals("encodedValue", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsDataAttribute_withNonDataAttributeKey() {
        Attribute nonDataAttribute = new Attribute("nonDataKey", "value");
        assertFalse(nonDataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttribute_withNonBooleanAttribute() {
        Attribute nonBooleanAttribute = new Attribute("key", "key");
        assertFalse(nonBooleanAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("key", "value");
        assertTrue(attribute.equals(sameAttribute));
    }

    @Test
    public void testEquals_withDifferentAttributeKey() {
        Attribute differentAttribute = new Attribute("differentKey", "value");
        assertFalse(attribute.equals(differentAttribute));
    }

    @Test
    public void testEquals_withDifferentAttributeValue() {
        Attribute differentAttribute = new Attribute("key", "differentValue");
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
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}