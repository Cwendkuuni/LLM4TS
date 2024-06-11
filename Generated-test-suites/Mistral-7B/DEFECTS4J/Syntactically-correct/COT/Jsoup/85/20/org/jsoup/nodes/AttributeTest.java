import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testAttributeConstructionWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeConstructionWithKeyValueAndParent() {
        Attributes parentAttributes = new Attributes();
        Attribute attribute = new Attribute("key", "value", parentAttributes);
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertSame(parentAttributes, attribute.parent);
    }

    @Test
    public void testSetKeyMethod() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyMethodWithNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSetValueMethod() {
        Attribute attribute = new Attribute("key", "value");
        Attribute newAttribute = attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
        Assert.assertEquals("value", attribute.getOldValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueMethodWithNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder expectedBuilder = new StringBuilder();
        attribute.html(expectedBuilder, new Document.OutputSettings());
        String expectedHtml = StringUtil.releaseBuilder(expectedBuilder);
        String actualHtml = attribute.html();
        Assert.assertEquals(expectedHtml, actualHtml);
    }

    @Test(expected = IOException.class)
    public void testHtmlMethodWithIOException() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document.OutputSettings());
        throw new IOException("Exception message");
    }

    @Test
    public void testShouldCollapseAttributeMethodWithBooleanAttributes() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Assert.assertTrue(Attribute.shouldCollapseAttribute("checked", null, outputSettings));
        Assert.assertTrue(Attribute.shouldCollapseAttribute("checked", "", outputSettings));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("key", "value", outputSettings));
    }

    @Test
    public void testIsDataAttributeMethod() {
        Assert.assertTrue(new Attribute("data-key", null).isDataAttribute());
        Assert.assertFalse(new Attribute("key", null).isDataAttribute());
    }

    @Test
    public void testCloneMethod() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        Assert.assertNotSame(attribute, clonedAttribute);
        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }

    @Test
    public void testEqualsMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        Assert.assertTrue(attribute1.equals(attribute2));
        Assert.assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testHashCodeMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
        Assert.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
    }
}