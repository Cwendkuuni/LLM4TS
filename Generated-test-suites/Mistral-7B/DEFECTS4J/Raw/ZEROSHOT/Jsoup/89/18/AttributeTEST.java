import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    @Test
    public void testNewAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNewAttributeWithNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNewAttributeWithNullValue() {
        new Attribute("key", null);
    }

    @Test
    public void testNewAttributeWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertNotNull(attribute.parent);
        Assert.assertEquals(attribute, parent.get(attribute.getKey()));
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
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        Attribute newAttribute = attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
        Assert.assertEquals("value", newAttribute.getValue());
        Assert.assertNotSame(attribute, newAttribute);
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueWithNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder expected = StringUtil.borrowBuilder();
        attribute.html(expected, new Document("").outputSettings());
        String result = StringUtil.releaseBuilder(expected);
        Assert.assertEquals("key=\"value\"", result);

        StringBuilder expected2 = StringUtil.borrowBuilder();
        attribute.html(expected2, new Document("").outputSettings());
        Assert.assertEquals("key=\"value\"", expected2.toString());

        StringBuilder actual = StringUtil.borrowBuilder();
        attribute.html(actual, new Document("").outputSettings());
        String resultActual = StringUtil.releaseBuilder(actual);
        Assert.assertEquals("key=\"value\"", resultActual);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", null);
        assertTrue(attribute.isDataAttribute());

        Attribute attribute2 = new Attribute("key", null);
        assertFalse(attribute2.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        Attribute attribute = new Attribute("key", "value");
        assertTrue(attribute.shouldCollapseAttribute(out));

        Attribute attribute2 = new Attribute("booleanAttribute", null);
        assertTrue(attribute2.shouldCollapseAttribute(out));

        Attribute attribute3 = new Attribute("key", "");
        assertTrue(attribute3.shouldCollapseAttribute(out));

        Attribute attribute4 = new Attribute("key", "value");
        assertFalse(attribute4.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("key", null);
        assertFalse(attribute.isBooleanAttribute());

        Attribute attribute2 = new Attribute("checked", null);
        assertTrue(attribute2.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
        Assert.assertNotSame(attribute, clonedAttribute);
    }
}