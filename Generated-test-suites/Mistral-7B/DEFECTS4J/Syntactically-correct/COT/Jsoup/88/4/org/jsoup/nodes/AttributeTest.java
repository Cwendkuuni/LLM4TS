import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
    }

    @Test
    public void testSettingKey() {
        String key = "testKey";
        String newKey = "newTestKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        attribute.setKey(newKey);

        Assert.assertEquals(newKey, attribute.getKey());
    }

    @Test(expected = AssertionError.class)
    public void testSettingEmptyOrNullKey() {
        String key = "";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        attribute.setKey(null); // or an empty string
    }

    @Test
    public void testSettingValue() {
        String key = "testKey";
        String oldValue = "oldValue";
        String newValue = "newValue";
        Attribute attribute = new Attribute(key, oldValue);

        String newValueReturned = attribute.setValue(newValue);

        Assert.assertEquals(newValue, attribute.getValue());
        Assert.assertEquals(oldValue, newValueReturned);
    }

    @Test(expected = AssertionError.class)
    public void testSettingNullValue() {
        String key = "testKey";
        Attribute attribute = new Attribute(key, null);

        attribute.setValue(null);
    }

    @Test
    public void testHtmlMethod() throws IOException {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());

        String expectedHtml = key + "=\"testValue\"";
        String actualHtml = StringUtil.releaseBuilder(sb);

        Assert.assertEquals(expectedHtml, actualHtml);
    }

    @Test(expected = SerializationException.class)
    public void testSerializationException() throws IOException {
        String key = "testKey";
        Attribute attribute = new Attribute(key, null);

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
    }

    @Test
    public void testToStringMethod() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        String expectedToString = key + "=\"testValue\"";
        String actualToString = attribute.toString();

        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "testKey";
        String encodedValue = "testValue=\"testValue\"";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);

        Assert.assertEquals(unencodedKey, attribute.getKey());
        Assert.assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        String key = "data-test";
        Attribute attribute = new Attribute(key, null);

        Assert.assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        String key = "test";
        String value = "test";
        Attribute attribute = new Attribute(key, value);

        Assert.assertTrue(attribute.shouldCollapseAttribute(null));
    }

    @Test
    public void testShouldCollapseAttributeFalse() {
        String key = "test";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        Assert.assertFalse(attribute.shouldCollapseAttribute(null));
    }

    @Test
    public void testIsBooleanAttribute() {
        String key = "checked";
        Attribute attribute = new Attribute(key, null);

        Assert.assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttributeFalse() {
        String key = "testKey";
        Attribute attribute = new Attribute(key, null);

        Assert.assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testEqualsMethod() {
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
    public void testHashCodeMethod() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        Attribute attribute1 = new Attribute(key1, value1);
        String key2 = "testKey2";
        String value2 = "testValue2";
        Attribute attribute2 = new Attribute(key2, value2);

        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testCloneMethod() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        Attribute clone = attribute.clone();

        Assert.assertEquals(key, clone.getKey());
        Assert.assertEquals(value, clone.getValue());
    }
}