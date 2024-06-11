
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.setValue("newValue");
        Assert.assertEquals("value", oldValue);
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "&lt;value&gt;");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("<value>", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        Assert.assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute1 = new Attribute("key1", "key1");
        Attribute attribute2 = new Attribute("key2", "");
        Attribute attribute3 = new Attribute("key3", null);
        Assert.assertFalse(attribute1.shouldCollapseAttribute());
        Assert.assertFalse(attribute2.shouldCollapseAttribute());
        Assert.assertTrue(attribute3.shouldCollapseAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("allowfullscreen", "value");
        Attribute attribute2 = new Attribute("random", "value");
        Assert.assertTrue(attribute1.isBooleanAttribute());
        Assert.assertFalse(attribute2.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "newValue");
        Assert.assertEquals(attribute1, attribute2);
        Assert.assertNotEquals(attribute1, attribute3);
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        Assert.assertEquals(attribute, clone);
    }
}
