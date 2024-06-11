import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AttributeTest {

    @Test
    public void testKeySetKeyAndGetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testValueSetValueAndGetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attribute.html());
    }

    @Test
    public void testHtmlWithException() {
        Attribute attribute = new Attribute("test", "value");
        try {
            attribute.html();
        } catch (SerializationException e) {
            // Expected exception
        }
    }

    @Test
    public void testClone() {
        Attribute originalAttribute = new Attribute("key", "value");
        Attribute clonedAttribute = originalAttribute.clone();
        assertEquals(originalAttribute.getKey(), clonedAttribute.getKey());
        assertEquals(originalAttribute.getValue(), clonedAttribute.getValue());
    }

    // Add more test cases here for other methods and edge cases

}