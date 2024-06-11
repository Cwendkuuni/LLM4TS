import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

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
        String oldValue = attribute.setValue("newValue");
        assertEquals("value1", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key1", "value1");
        assertEquals("key1=\"value1\"", attribute.html());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key1", "value1");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }

    // Add more test cases as needed

}