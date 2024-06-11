import org.junit.Test;
import org.junit.Assert;

public class AttributeTest {

    // Test case for the constructor Attribute(String key, String value)
    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    // Test case for the constructor Attribute(String key, String value, Attributes parent)
    @Test
    public void testConstructorWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertEquals(parent, attribute.getParent());
    }

    // Test case for the setKey(String key) method
    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    // Test case for the setValue(String value) method
    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.setValue("newValue");
        Assert.assertEquals("value", oldValue);
        Assert.assertEquals("newValue", attribute.getValue());
    }

    // Test case for the html() method
    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String expectedHtml = "key=\"value\"";
        Assert.assertEquals(expectedHtml, attribute.html());
    }

    // Test case for the toString() method
    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String expectedString = "key=\"value\"";
        Assert.assertEquals(expectedString, attribute.toString());
    }

    // Test case for the createFromEncoded(String unencodedKey, String encodedValue) method
    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    // Test case for the isDataAttribute() method
    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        Assert.assertTrue(attribute.isDataAttribute());
    }

    // Test case for the shouldCollapseAttribute(Document.OutputSettings out) method
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute1 = new Attribute("key", "value");
        Assert.assertFalse(attribute1.shouldCollapseAttribute(out));
        Attribute attribute2 = new Attribute("key", "key");
        Assert.assertTrue(attribute2.shouldCollapseAttribute(out));
    }

    // Test case for the isBooleanAttribute(String key) method
    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("booleanAttr", "value");
        Assert.assertTrue(attribute1.isBooleanAttribute());
        Attribute attribute2 = new Attribute("key", null);
        Assert.assertTrue(attribute2.isBooleanAttribute());
    }

    // Test case for the equals(Object o) method
    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "differentValue");
        Assert.assertEquals(attribute1, attribute2);
        Assert.assertNotEquals(attribute1, attribute3);
    }

    // Test case for the hashCode() method
    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    // Test case for the clone() method
    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1.clone();
        Assert.assertEquals(attribute1.getKey(), attribute2.getKey());
        Assert.assertEquals(attribute1.getValue(), attribute2.getValue());
    }

}