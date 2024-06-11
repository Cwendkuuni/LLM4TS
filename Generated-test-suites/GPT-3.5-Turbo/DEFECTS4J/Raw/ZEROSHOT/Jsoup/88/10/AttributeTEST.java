import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setup() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testGetKey() {
        String key = attribute.getKey();
        assertEquals("key", key);
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
        assertEquals("value", value);
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
        assertEquals("key=\"value\"", html);
    }

    @Test
    public void testToString() {
        String toString = attribute.toString();
        assertEquals("key=\"value\"", toString);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("encodedKey", "encodedValue");
        String key = newAttribute.getKey();
        String value = newAttribute.getValue();
        assertEquals("encodedKey", key);
        assertEquals("encodedValue", value);
    }

    @Test
    public void testIsDataAttribute() {
        boolean isDataAttribute = attribute.isDataAttribute();
        assertFalse(isDataAttribute);
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean shouldCollapseAttribute = attribute.shouldCollapseAttribute(outputSettings);
        assertFalse(shouldCollapseAttribute);
    }

    @Test
    public void testIsBooleanAttribute() {
        boolean isBooleanAttribute = attribute.isBooleanAttribute();
        assertFalse(isBooleanAttribute);
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("key", "value");
        boolean equals = attribute.equals(sameAttribute);
        assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("key", "value");
        int hashCode1 = attribute.hashCode();
        int hashCode2 = sameAttribute.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertNotNull(clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}