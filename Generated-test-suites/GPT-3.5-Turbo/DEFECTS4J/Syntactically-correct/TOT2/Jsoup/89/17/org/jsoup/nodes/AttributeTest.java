import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.setValue("newValue"));
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("href", "index.html");
        
        // Testing for SerializationException
        try {
            attribute.html();
            fail("SerializationException not thrown");
        } catch (SerializationException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-foo", "value");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("checked", "");
        assertTrue(attribute.shouldCollapseAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }

}