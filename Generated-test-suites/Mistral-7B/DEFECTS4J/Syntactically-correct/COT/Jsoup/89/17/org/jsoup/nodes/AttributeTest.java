import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
        Assert.assertSame(parentAttributes, attribute.getParent());
    }

    @Test
    public void testSettingKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingEmptyKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("");
    }

    @Test
    public void testSettingValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlMethod() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder expectedOutput = StringUtil.borrowBuilder();
        attribute.html(expectedOutput, new Document("").outputSettings());
        String actualOutput = StringUtil.releaseBuilder(expectedOutput);
        String expected = "key=\"value\"";
        Assert.assertEquals(expected, actualOutput);
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethodWithIOException() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder output = StringUtil.borrowBuilder();
        attribute.html(output, new Document("").outputSettings());
        throw new IOException(); // simulate IOException
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("unencodedKey", "encodedValue=\"key=value\"");
        Assert.assertEquals("unencodedKey", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("booleanAttribute", null);
        Assert.assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("notBooleanAttribute", "value");
        Assert.assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Assert.assertTrue(Attribute.shouldCollapseAttribute("booleanAttribute", null, outputSettings));
        Assert.assertTrue(Attribute.shouldCollapseAttribute("booleanAttribute", "", outputSettings));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("key", "value", outputSettings));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Assert.assertEquals(attribute1, attribute2);

        Attribute attribute3 = new Attribute("key2", "value2");
        Assert.assertNotEquals(attribute1, attribute3);

        Attribute attribute4 = new Attribute("key1", null);
        Assert.assertNotEquals(attribute1, attribute4);

        Attribute attribute5 = new Attribute(null, "value1");
        Assert.assertNotEquals(attribute1, attribute5);
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());

        Attribute attribute3 = new Attribute("key2", "value2");
        Attribute attribute4 = new Attribute("key3", "value3");
        Assert.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
        Assert.assertNotEquals(attribute1.hashCode(), attribute4.hashCode());

        Attribute attribute5 = new Attribute("key1", null);
        Attribute attribute6 = new Attribute(null, "value1");
        Assert.assertNotEquals(attribute1.hashCode(), attribute5.hashCode());
        Assert.assertNotEquals(attribute1.hashCode(), attribute6.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        Assert.assertEquals(attribute, clonedAttribute);
    }
}