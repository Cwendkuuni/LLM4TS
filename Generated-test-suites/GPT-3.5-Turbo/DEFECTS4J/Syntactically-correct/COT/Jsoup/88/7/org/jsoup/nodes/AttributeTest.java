import org.junit.Assert;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        String key = attribute.getKey();
        Assert.assertEquals("key", key);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        String key = attribute.getKey();
        Assert.assertEquals("newKey", key);
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        String value = attribute.getValue();
        Assert.assertEquals("value", value);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        String value = attribute.getValue();
        Assert.assertEquals("newValue", value);
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.html();
        Assert.assertEquals("key=\"value\"", html);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String toString = attribute.toString();
        Assert.assertEquals("key=\"value\"", toString);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("unencodedKey", "&lt;encodedValue&gt;");
        String key = attribute.getKey();
        String value = attribute.getValue();
        Assert.assertEquals("unencodedKey", key);
        Assert.assertEquals("<encodedValue>", value);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-test", "value");
        boolean isDataAttribute = attribute.isDataAttribute();
        Assert.assertTrue(isDataAttribute);
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "value");
        boolean shouldCollapseAttribute = attribute.shouldCollapseAttribute(new Document().outputSettings());
        Assert.assertFalse(shouldCollapseAttribute);
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", "true");
        boolean isBooleanAttribute = attribute.isBooleanAttribute();
        Assert.assertTrue(isBooleanAttribute);
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "otherValue");
        boolean equals1 = attribute1.equals(attribute2);
        boolean equals2 = attribute1.equals(attribute3);
        Assert.assertTrue(equals1);
        Assert.assertFalse(equals2);
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        int hashCode = attribute.hashCode();
        Assert.assertNotEquals(0, hashCode);
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        Assert.assertNotSame(attribute, clone);
    }

}