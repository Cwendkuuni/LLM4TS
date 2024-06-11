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
        assertEquals("newValue", attribute.getValue());
        assertEquals("value", oldValue);
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
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data-key", "value");
        assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testEquals() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertEquals(attribute, equalAttribute);

        Attribute notEqualAttribute = new Attribute("differentKey", "value");
        assertNotEquals(attribute, notEqualAttribute);

        Attribute nullValueAttribute = new Attribute("key", null);
        assertNotEquals(attribute, nullValueAttribute);

        assertNotEquals(attribute, null);
        assertNotEquals(attribute, "string");
    }

    @Test
    public void testHashCode() {
        Attribute equalAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), equalAttribute.hashCode());

        Attribute notEqualAttribute = new Attribute("differentKey", "value");
        assertNotEquals(attribute.hashCode(), notEqualAttribute.hashCode());

        Attribute nullValueAttribute = new Attribute("key", null);
        assertNotEquals(attribute.hashCode(), nullValueAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute cloneAttribute = attribute.clone();
        assertEquals(cloneAttribute, attribute);
        assertNotSame(cloneAttribute, attribute);
    }

}