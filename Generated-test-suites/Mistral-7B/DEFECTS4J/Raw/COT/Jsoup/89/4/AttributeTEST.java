import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testConstructorWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testConstructorWithKeyValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertNotNull(attribute.getParent());
        Assert.assertEquals(parent, attribute.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyKey() {
        new Attribute("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithEmptyKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueWithNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        Assert.assertTrue(attribute.isDataAttribute());
        Attribute attribute1 = new Attribute("key", "value");
        Assert.assertFalse(attribute1.isDataAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", null);
        Assert.assertTrue(attribute.isBooleanAttribute());
        Attribute attribute1 = new Attribute("key", "value");
        Assert.assertFalse(attribute1.isBooleanAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("checked", "");
        Assert.assertTrue(attribute.shouldCollapseAttribute(out));
        Attribute attribute1 = new Attribute("key", "value");
        Assert.assertFalse(attribute1.shouldCollapseAttribute(out));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");
        Assert.assertFalse(attribute1.equals(attribute2));
        Assert.assertTrue(attribute1.equals(attribute3));
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}