import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key1", "value1");
        assertEquals("key1", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key1", "value1");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key1", "value1");
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key1", "value1");
        assertEquals("value1", attribute.setValue("newValue"));
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key1", "value1");
        assertEquals("key1=\"value1\"", attribute.html());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlException() {
        Attribute attribute = new Attribute("key1", "value1");
        attribute.html(); // This will throw a SerializationException
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key1", "value1");
        assertEquals("key1", attribute.getKey());
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value1");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key1", "value1");
        assertTrue(attribute.shouldCollapseAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertFalse(Attribute.isBooleanAttribute("class"));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key1", "value1");
        assertEquals(112785, attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key1", "value1");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}