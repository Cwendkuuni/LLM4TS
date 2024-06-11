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
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
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

    @Test
    public void testHtmlWithEmptyValue() throws IOException {
        Attribute attribute = new Attribute("key", "");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        Assert.assertEquals("key=\"\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testHtmlWithSameValueAsKey() throws IOException {
        Attribute attribute = new Attribute("key", "key");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        Assert.assertEquals("key", StringUtil.releaseBuilder(sb));
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
        Attribute dataAttribute = new Attribute("data-key", "value");
        Assert.assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttributeWithHtmlSyntaxAndEmptyValueOrSameValueAsName() {
        Assert.assertTrue(Attribute.shouldCollapseAttribute("key", "", new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html)));
        Assert.assertTrue(Attribute.shouldCollapseAttribute("checked", null, new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testShouldCollapseAttributeWithHtmlSyntaxAndValueNotEmptyOrNotSameAsName() {
        Assert.assertFalse(Attribute.shouldCollapseAttribute("key", "value", new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html)));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("checked", "checked", new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.html)));
    }

    @Test
    public void testShouldCollapseAttributeWithXmlSyntax() {
        Assert.assertFalse(Attribute.shouldCollapseAttribute("key", null, new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml)));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("checked", null, new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml)));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isBooleanAttribute());
        Attribute booleanAttribute = new Attribute("checked", null);
        Assert.assertTrue(attribute.isBooleanAttribute());
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
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");
        Assert.assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
        Assert.assertEquals(attribute1.hashCode(), attribute3.hashCode());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        Assert.assertEquals(attribute.getKey(), clone.getKey());
        Assert.assertEquals(attribute.getValue(), clone.getValue());
    }
}