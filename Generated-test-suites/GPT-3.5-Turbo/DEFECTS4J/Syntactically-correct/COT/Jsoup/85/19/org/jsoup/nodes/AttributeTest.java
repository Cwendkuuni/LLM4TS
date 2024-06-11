import org.junit.Test;
import org.jsoup.nodes.Attribute;

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
        String string = attribute.toString();
        assertEquals("key=\"value\"", string);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    // Additional Test Cases for edge cases and exception handling

    @Test
    public void testSetKeyWithEmptyKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");
        String key = attribute.getKey();
        assertEquals("key", key); // key should not be changed if set to empty string
    }

    @Test
    public void testSetValueWithParent() {
        Attribute attribute = new Attribute("key", "value");
        Attribute parent = new Attribute("parentKey", "parentValue");
        attribute.parent = parent;
        attribute.setValue("newValue");
        String value = attribute.getValue();
        assertEquals("newValue", value);
        String parentValue = parent.get(attribute.getKey());
        assertEquals("newValue", parentValue);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        boolean isDataAttribute = attribute.isDataAttribute();
        assertTrue(isDataAttribute);
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", "true");
        boolean isBooleanAttribute = attribute.isBooleanAttribute();
        assertTrue(isBooleanAttribute);
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        int hashCode = attribute.hashCode();
        assertEquals(3495269, hashCode);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        assertEquals(attribute.getKey(), clone.getKey());
        assertEquals(attribute.getValue(), clone.getValue());
    }
}