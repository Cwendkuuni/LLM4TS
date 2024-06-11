import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        String key = attribute.getKey();
        assertEquals("key", key);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        String key = attribute.getKey();
        assertEquals("newKey", key);
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        String value = attribute.getValue();
        assertEquals("value", value);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        String value = attribute.getValue();
        assertEquals("newValue", value);
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.html();
        assertEquals("key=\"value\"", html);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String toString = attribute.toString();
        assertEquals("key=\"value\"", toString);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("unencodedKey", "&lt;value&gt;");
        String key = attribute.getKey();
        String value = attribute.getValue();
        assertEquals("unencodedKey", key);
        assertEquals("<value>", value);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        boolean isAttribute = attribute.isDataAttribute();
        assertTrue(isAttribute);
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean shouldCollapse = attribute.shouldCollapseAttribute(outputSettings);
        assertFalse(shouldCollapse);
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        boolean isAttribute = attribute.isBooleanAttribute();
        assertFalse(isAttribute);
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        boolean equals = attribute1.equals(attribute2);
        assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        int hashCode = attribute.hashCode();
        assertEquals(103663, hashCode);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        assertEquals(attribute, clone);
    }

}