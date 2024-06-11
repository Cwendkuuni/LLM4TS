import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testAttributeConstructionWithKeyAndValue() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
    }

    @Test
    public void testAttributeConstructionWithKeyValueAndParent() {
        String key = "testKey";
        String value = "testValue";
        Attributes parent = new Attributes();

        Attribute attribute = new Attribute(key, value, parent);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
        Assert.assertNull(parent.get(key));
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeNullKey() {
        String key = null;
        String value = "testValue";

        try {
            new Attribute(key, value);
        } catch (NullPointerException e) {
            Assert.assertEquals("key", e.getMessage());
            throw e;
        }
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeNullValue() {
        String key = "testKey";
        String value = null;

        try {
            new Attribute(key, value);
        } catch (NullPointerException e) {
            Assert.assertEquals("val", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testAttributeSetKey() {
        String originalKey = "oldKey";
        String key = "newKey";
        String value = "testValue";
        Attribute attribute = new Attribute(originalKey, value, null);

        attribute.setKey(key);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeNullKeyToSet() {
        String originalKey = "oldKey";
        String key = null;
        String value = "testValue";
        Attribute attribute = new Attribute(originalKey, value, null);

        attribute.setKey(key);
    }

    @Test
    public void testAttributeHtml() throws IOException {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value, null);

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        String htmlOutput = StringUtil.releaseBuilder(sb);

        String expectedHtmlOutput = String.format("%s=\"%s\"", key, value);
        Assert.assertEquals(expectedHtmlOutput, htmlOutput);
    }

    @Test
    public void testAttributeIsDataAttribute() {
        String key = "testDataAttribute";
        Attribute attribute = new Attribute(key, null, null);

        Assert.assertTrue(attribute.isDataAttribute());

        String nonDataAttributeKey = "testNonDataAttribute";
        Attribute nonDataAttribute = new Attribute(nonDataAttributeKey, null, null);

        Assert.assertFalse(nonDataAttribute.isDataAttribute());
    }

    @Test
    public void testAttributeShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String booleanAttributeKey = "checked";

        Assert.assertTrue(Attribute.shouldCollapseAttribute(outputSettings));
        Assert.assertFalse(Attribute.shouldCollapseAttribute("notABooleanAttribute", null, outputSettings));
    }

    @Test
    public void testAttributeEquals() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        String key2 = "testKey2";
        String value2 = "testValue2";

        Attribute attribute1 = new Attribute(key1, value1);
        Attribute attribute2 = new Attribute(key2, value2);
        Attribute attribute3 = new Attribute(key1, value1);

        Assert.assertTrue(attribute1.equals(attribute3));
        Assert.assertFalse(attribute1.equals(attribute2));
    }

    @Test
    public void testAttributeClone() throws CloneNotSupportedException {
        String key = "testKey";
        String value = "testValue";
        Attribute originalAttribute = new Attribute(key, value, null);

        Attribute clonedAttribute = originalAttribute.clone();

        Assert.assertNotSame(originalAttribute, clonedAttribute);
        Assert.assertEquals(originalAttribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(originalAttribute.getValue(), clonedAttribute.getValue());
    }
}