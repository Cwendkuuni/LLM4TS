import org.junit.Test;
import org.junit.Assert;

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
        attribute.setKey("new_key");
        String key = attribute.getKey();
        Assert.assertEquals("new_key", key);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.setValue("new_value");
        String value = attribute.getValue();
        Assert.assertEquals("value", oldValue);
        Assert.assertEquals("new_value", value);
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
        String str = attribute.toString();
        Assert.assertEquals("key=\"value\"", str);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value&amp;");
        String value = attribute.getValue();
        Assert.assertEquals("value&", value);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        boolean isDataAttribute = attribute.isDataAttribute();
        Assert.assertTrue(isDataAttribute);
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute1 = new Attribute("key", "value");
        Document.OutputSettings out1 = new Document.OutputSettings();
        boolean shouldCollapse1 = attribute1.shouldCollapseAttribute(out1);

        Attribute attribute2 = new Attribute("key", "");
        Document.OutputSettings out2 = new Document.OutputSettings();
        boolean shouldCollapse2 = attribute2.shouldCollapseAttribute(out2);

        Attribute attribute3 = new Attribute("key", "key");
        Document.OutputSettings out3 = new Document.OutputSettings();
        boolean shouldCollapse3 = attribute3.shouldCollapseAttribute(out3);

        Assert.assertFalse(shouldCollapse1);
        Assert.assertTrue(shouldCollapse2);
        Assert.assertTrue(shouldCollapse3);
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("key", "value");
        boolean isBoolean1 = attribute1.isBooleanAttribute();

        Attribute attribute2 = new Attribute("allowfullscreen", "value");
        boolean isBoolean2 = attribute2.isBooleanAttribute();

        Attribute attribute3 = new Attribute("key", null);
        boolean isBoolean3 = attribute3.isBooleanAttribute();

        Assert.assertFalse(isBoolean1);
        Assert.assertTrue(isBoolean2);
        Assert.assertTrue(isBoolean3);
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "different_value");

        Assert.assertTrue(attribute1.equals(attribute2));
        Assert.assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "different_value");

        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        int hashCode3 = attribute3.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
        Assert.assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1.clone();

        Assert.assertEquals(attribute1.getKey(), attribute2.getKey());
        Assert.assertEquals(attribute1.getValue(), attribute2.getValue());
    }

}