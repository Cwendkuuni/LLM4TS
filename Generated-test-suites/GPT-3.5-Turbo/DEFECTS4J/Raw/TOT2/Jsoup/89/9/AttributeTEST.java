import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("key1", "value1");
        assertNotNull(attribute);
        assertEquals("key1", attribute.getKey());
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key1", "value1");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key1", "value1");
        assertEquals("value1", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key1", "value1");
        assertEquals("key1=\"value1\"", attribute.html());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        assertEquals(attribute1, attribute2);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key1", "value1");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }

    // Add more test cases for other methods and edge cases

}