import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        Assert.assertEquals("key", attr.getKey());
        Assert.assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        Assert.assertEquals("key", attrWithParent.getKey());
        Assert.assertEquals("value", attrWithParent.getValue());
        Assert.assertNull(attrWithParent.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKey() {
        new Attribute("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        new Attribute("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyValue() {
        new Attribute("key", "");
    }

    @Test
    public void testSettingKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        Assert.assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testSettingValue() {
        Attribute attr = new Attribute("key", "value");
        String oldVal = attr.setValue("newValue");
        Assert.assertEquals("newValue", attr.getValue());
        Assert.assertEquals("value", oldVal);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String html = StringUtil.releaseBuilder(sb);
        Assert.assertEquals("key=\"value\"", html);
    }

    @Test
    public void testShouldCollapseAttribute() throws IOException {
        Attribute attr = new Attribute("key", "");
        Assert.assertTrue(attr.shouldCollapseAttribute(new Document.OutputSettings()));
        attr.setValue("value");
        Assert.assertFalse(attr.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        Assert.assertFalse(attr.isBooleanAttribute());

        Attribute booleanAttr = new Attribute("checked", null);
        Assert.assertTrue(booleanAttr.isBooleanAttribute());
    }
}