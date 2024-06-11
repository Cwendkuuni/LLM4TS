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
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", attributes);
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertNotNull(attribute.parent);
    }

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

    @Test(expected = NullPointerException.class)
    public void testSetKeyWithNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        String oldValue = attribute.setValue(newValue);
        Assert.assertEquals(newValue, attribute.getValue());
        Assert.assertEquals("value", oldValue);
    }

    @Test(expected = NullPointerException.class)
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

    @Test(expected = SerializationException.class)
    public void testHtmlWithIOException() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        throw new IOException();
        attribute.html(sb, new Document("").outputSettings());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data-key", "data-value");
        Assert.assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isBooleanAttribute());
        Attribute booleanAttribute = new Attribute("checked", null);
        Assert.assertTrue(booleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.shouldCollapseAttribute(out));
        Attribute booleanAttribute = new Attribute("checked", null);
        Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        Assert.assertTrue(attribute1.equals(attribute2));
        Assert.assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}