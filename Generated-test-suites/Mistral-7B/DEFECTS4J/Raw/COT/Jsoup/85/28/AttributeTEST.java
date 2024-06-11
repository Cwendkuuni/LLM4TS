import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.jsoup.helper.Entities;
import java.util.Arrays;

public class AttributeTest {

    @Test
    public void testAttributeConstructionWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeConstructionWithKeyValueAndParent() {
        Attribute attribute = new Attribute("key", "value", null);
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeConstructionWithEncodedKeyAndValue() {
        Attribute attribute = Attribute.createFromEncoded("key", "value=\"encodedValue\"");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("encodedValue", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyInAttributeConstruction() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueInAttributeConstruction() {
        new Attribute("key", null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullParentInAttributeConstruction() {
        new Attribute("key", "value", null);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyInSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        String oldValue = attribute.setValue(newValue);
        Assert.assertEquals("newValue", attribute.getValue());
        Assert.assertEquals("value", oldValue);
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueInSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtml() throws Exception {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String expected = "key=\"value\"";
        Assert.assertEquals(expected, StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isDataAttribute());

        attribute = new Attribute("data-key", "value");
        Assert.assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() throws Exception {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        Assert.assertFalse(attribute.shouldCollapseAttribute(out));

        out.syntax(Document.OutputSettings.Syntax.html);
        Assert.assertTrue(attribute.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isBooleanAttribute());

        attribute = new Attribute("checked", null);
        Assert.assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsDataAttributeWithDataPrefix() {
        Attribute attribute = new Attribute("data-key", "value");
        Assert.assertTrue(attribute.isDataAttribute());
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
        Assert.assertEquals(attribute, clonedAttribute);
    }

}