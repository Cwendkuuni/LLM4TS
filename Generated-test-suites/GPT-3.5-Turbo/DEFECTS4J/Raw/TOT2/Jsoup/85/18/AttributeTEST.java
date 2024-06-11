import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testAttributeConstructor() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.setValue("newValue"));
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testEqualsAndHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute1, attribute2);
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}