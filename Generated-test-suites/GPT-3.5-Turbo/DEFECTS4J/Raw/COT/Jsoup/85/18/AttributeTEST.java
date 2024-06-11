import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String expected = "key=\"value\"";
        assertEquals(expected, attribute.html());
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String expected = "key=\"value\"";
        assertEquals(expected, attribute.toString());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", null);
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertEquals(attribute1, attribute2);
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        int expected = 31 * "key".hashCode() + "value".hashCode();
        assertEquals(expected, attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}