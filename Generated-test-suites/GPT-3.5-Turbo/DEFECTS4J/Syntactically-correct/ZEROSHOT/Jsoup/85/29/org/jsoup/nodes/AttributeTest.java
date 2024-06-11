import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void getKeyTest() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void setKeyTest() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void getValueTest() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void setValueTest() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void htmlTest() {
        Attribute attribute = new Attribute("key", "value");
        String expectedHtml = "key=\"value\"";
        assertEquals(expectedHtml, attribute.html());
    }

    @Test
    public void createFromEncodedTest() {
        String unencodedKey = "key";
        String encodedValue = "value";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void isDataAttributeTest() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void shouldCollapseAttributeTest() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void equalsTest() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void hashCodeTest() {
        Attribute attribute = new Attribute("key", "value");
        int expectedHashCode = 31 * "key".hashCode() + "value".hashCode();
        assertEquals(expectedHashCode, attribute.hashCode());
    }

    @Test
    public void cloneTest() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}