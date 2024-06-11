import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        Assert.assertEquals("key", attributeWithParent.getKey());
        Assert.assertEquals("value", attributeWithParent.getValue());
    }

    @Test
    public void testAttributeKeySet() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        attributeWithParent.setKey("newKey");
        Assert.assertEquals("newKey", attributeWithParent.getKey());
    }

    @Test
    public void testAttributeValueSet() {
        Attribute attribute = new Attribute("key", "value");
        String oldValue = attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
        Assert.assertEquals("value", oldValue);

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());
        String oldValueParent = attributeWithParent.setValue("newValue");
        Assert.assertEquals("newValue", attributeWithParent.getValue());
        Assert.assertEquals("value", oldValueParent);
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder expected = StringUtil.borrowBuilder();
        attribute.html(expected, new Document("").outputSettings());
        Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(expected));

        StringBuilder expected2 = StringUtil.borrowBuilder();
        attribute.html(expected2, null);
        Assert.assertEquals("key=\"value\"", StringUtil.releaseBuilder(expected2));
    }

    @Test
    public void testAttributeCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeNullValue() {
        Attribute attribute = new Attribute("key", null);
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeNullKey() {
        Attribute attribute = new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeEmptyKey() {
        Attribute attribute = new Attribute("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeEmptyValue() {
        Attribute attribute = new Attribute("key", "");
    }

    @Test
    public void testAttributeIsBooleanAttribute() {
        Assert.assertTrue(Attribute.isBooleanAttribute("checked"));
        Assert.assertTrue(Attribute.isBooleanAttribute("disabled"));
        Assert.assertFalse(Attribute.isBooleanAttribute("key"));
    }
}