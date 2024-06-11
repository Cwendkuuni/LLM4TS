import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testAttributeConstructor() {
        Attribute attribute = new Attribute("key", "value");

        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertNull(attribute.getParent());

        Attribute attributeWithParent = new Attribute("key", "value", new Attributes());

        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertNotNull(attribute.getParent());
    }

    @Test
    public void testAttributeSetKey() {
        Attribute attribute = new Attribute("key", "value");

        Assert.assertEquals("key", attribute.getKey());

        attribute.setKey("newKey");

        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testAttributeSetValue() {
        Attribute attribute = new Attribute("key", "value");

        Assert.assertEquals("value", attribute.getValue());

        String newValue = "newValue";
        attribute.setValue(newValue);

        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());

        String expectedHtml = "key=\"value\"";
        Assert.assertEquals(expectedHtml, StringUtil.releaseBuilder(sb));
    }

    @Test(expected = SerializationException.class)
    public void testAttributeHtmlIOException() throws IOException {
        Attribute attribute = new Attribute("key", "value");

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());

        attribute.html(sb, new Document("").outputSettings()); // second call to throw an IOException
    }

    @Test
    public void testAttributeCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");

        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");

        Assert.assertTrue(attribute.isDataAttribute());

        Attribute attributeWithoutPrefix = new Attribute("key", "value");

        Assert.assertFalse(attributeWithoutPrefix.isDataAttribute());
    }

    @Test
    public void testAttributeShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        Attribute attribute = new Attribute("booleanAttribute", "");

        Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        Attribute attributeWithValue = new Attribute("booleanAttribute", "true");

        Assert.assertFalse(attributeWithValue.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testAttributeIsBooleanAttribute() {
        Attribute attribute = new Attribute("booleanAttribute", null);

        Assert.assertTrue(attribute.isBooleanAttribute());

        Attribute attributeWithValue = new Attribute("key", "value");

        Assert.assertFalse(attributeWithValue.isBooleanAttribute());
    }

    @Test
    public void testAttributeEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");

        Attribute attribute3 = new Attribute("key2", "value2");

        Assert.assertTrue(attribute1.equals(attribute2));

        Assert.assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testAttributeClone() {
        Attribute attribute = new Attribute("key", "value");

        Attribute clonedAttribute = attribute.clone();

        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
        Assert.assertNull(clonedAttribute.getParent());
    }
}