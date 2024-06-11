import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", attributes);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(attributes, attribute.getParent());
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
        String newValue = "newValue";
        String oldValue = attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
        assertEquals("value", oldValue);
    }

    @Test
    public void testHtmlMethod() {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("key=\"value\"");

        String actualOutput = attribute.html();
        assertEquals(expectedOutput.toString(), actualOutput);
    }

    @Test
    public void testCreateFromEncodedMethod() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethodThrowsSerializationException() {
        Attribute attribute = new Attribute("key", "value");
        attribute.html(null, null);
    }

    @Test
    public void testIsDataAttributeMethod() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());

        Attribute dataAttribute = new Attribute("data-key", "value");
        assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttributeMethod() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));

        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testEqualsAndHashcodeMethods() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");

        assertTrue(attribute1.equals(attribute3));
        assertTrue(attribute1.hashCode() == attribute3.hashCode());

        assertFalse(attribute1.equals(attribute2));
        assertFalse(attribute1.hashCode() == attribute2.hashCode());
    }

    @Test
    public void testCloneMethod() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute, clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
        assertNotNull(clonedAttribute);

        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}