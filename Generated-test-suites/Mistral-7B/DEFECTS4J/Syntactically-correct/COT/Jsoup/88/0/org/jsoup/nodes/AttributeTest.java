import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    // Basic test cases for public methods

    @Test
    public void testAttributeCreationWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeCreationWithKeyValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(attribute, parent.get(attribute.getKey()));
    }

    @Test
    public void testSettingKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSettingValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        String oldValue = attribute.getValue();
        attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
        assertEquals(oldValue, attribute.getOldValue()); // getOldValue is not public, assuming it's a getter for oldValue
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String htmlOutput = StringUtil.releaseBuilder(sb);
        String expectedHtmlOutput = "key=\"value\"";
        assertEquals(expectedHtmlOutput, htmlOutput);
    }

    @Test
    public void testToStringMethod() {
        Attribute attribute = new Attribute("key", "value");
        String toStringOutput = attribute.toString();
        String expectedToStringOutput = attribute.html();
        assertEquals(expectedToStringOutput, toStringOutput);
    }

    @Test
    public void testCreateFromEncodedMethod() {
        Attribute attribute = Attribute.createFromEncoded("unencodedKey", "encodedValue");
        assertEquals("unencodedKey", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttributeMethod() {
        Attribute attribute = new Attribute("data-test", "test");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsDataAttributeWithInvalidKey() {
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttributeMethod() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute = new Attribute("checked", "");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeWithNonBooleanAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "value");
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testEqualsMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");
        assertFalse(attribute1.equals(attribute2));
        assertTrue(attribute1.equals(attribute3));
    }

    @Test
    public void testHashCodeMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");
        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        int hashCode3 = attribute3.hashCode();
        assertNotEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode3);
    }

    @Test
    public void testCloneMethod() throws CloneNotSupportedException {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testCreatingAttributeWithNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatingAttributeWithEmptyKey() {
        new Attribute("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatingAttributeWithNullValue() {
        new Attribute("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingEmptyKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");
    }

    @Test(expected = NullPointerException.class)
    public void testSettingNullParent() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setParent(null);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethodWithIOException() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        throw new IOException();
        attribute.html(sb, new Document("").outputSettings());
    }
}