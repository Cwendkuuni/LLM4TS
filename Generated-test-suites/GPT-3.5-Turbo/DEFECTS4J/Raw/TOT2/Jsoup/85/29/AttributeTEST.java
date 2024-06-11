import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testAttributeConstructor() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testKey", attribute.getKey());
        assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("testKey", "testValue");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("testKey", "testValue");
        String expectedHtml = "testKey=\"testValue\"";
        assertEquals(expectedHtml, attribute.html());
    }

    @Test
    public void testFromStringConstructor() {
        Attribute attribute = Attribute.createFromEncoded("testKey", "test&amp;Value");
        assertEquals("testKey", attribute.getKey());
        assertEquals("test&Value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-test", "value");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("testKey", "testKey");
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }
    
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("readonly", null);
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("testKey", "testValue");
        Attribute attribute2 = new Attribute("testKey", "testValue");
        assertEquals(attribute1, attribute2);
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals(attribute.getKey().hashCode() + attribute.getValue().hashCode(), attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("testKey", "testValue");
        Attribute clonedAttribute = attribute.clone();
        assertNotSame(attribute, clonedAttribute);
        assertEquals(attribute, clonedAttribute);
    }
}