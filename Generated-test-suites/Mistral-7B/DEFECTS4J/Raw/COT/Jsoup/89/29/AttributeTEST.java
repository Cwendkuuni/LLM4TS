import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for org.jsoup.nodes.Attribute
 */
public class AttributeTest {

    @Test
    public void testAttributeConstructorWithKeyAndValue() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
    }

    @Test
    public void testAttributeConstructorWithKeyValueAndParent() {
        String key = "testKey";
        String value = "testValue";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, value, parent);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
        Assert.assertNotNull(attribute.parent);
    }

    @Test
    public void testSetKey() {
        String key = "testKey";
        String newKey = "newTestKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value, null);

        attribute.setKey(newKey);

        Assert.assertEquals(newKey, attribute.getKey());
    }

    @Test
    public void testGetValueAndSetValue() {
        String key = "testKey";
        String oldValue = "oldTestValue";
        String newValue = "newTestValue";
        Attribute attribute = new Attribute(key, oldValue, null);

        String returnedValue = attribute.setValue(newValue);

        Assert.assertEquals(newValue, attribute.getValue());
        Assert.assertEquals(oldValue, returnedValue);
    }

    @Test
    public void testHtmlMethod() throws IOException {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value, null);

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());

        String expectedHtml = String.format("%s=%s", key, value);
        Assert.assertEquals(expectedHtml, sb.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "testKey";
        String encodedValue = "testValue%3D%3D";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);

        Assert.assertEquals(unencodedKey, attribute.getKey());
        Assert.assertEquals("testValue", attribute.getValue());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlMethodWithIOException() throws IOException {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value, null);

        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());

        throw new IOException();
    }

    @Test
    public void testIsDataAttribute() {
        String key = "testKey";
        Attribute attribute = new Attribute(key, null);

        Assert.assertFalse(attribute.isDataAttribute());

        key = "data-testKey";
        attribute = new Attribute(key, null);

        Assert.assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        String htmlOutputSettingsSyntax = "html";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(htmlOutputSettingsSyntax);

        String key = "testKey";
        String value = "";
        Attribute attribute = new Attribute(key, value, null);

        Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        value = key;
        Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        key = "testBoolKey";
        value = null;
        Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        key = "testKey";
        value = "testValue";
        Assert.assertFalse(attribute.shouldCollapseAttribute(outputSettings));

        key = "testKey";
        value = "testValue";
        outputSettings.syntax("xml");
        Assert.assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testEquals() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        Attribute attribute1 = new Attribute(key1, value1, null);

        String key2 = "testKey2";
        String value2 = "testValue2";
        Attribute attribute2 = new Attribute(key2, value2, null);

        Attribute attribute3 = new Attribute(key1, value1, null);

        Attribute equalAttributes = new Attribute(key1, value1, null);

        Map<String, Attribute> attributesMap = new HashMap<>();
        attributesMap.put(key1, attribute1);

        Attribute clonedAttribute = attribute1.clone();

        Assert.assertEquals(attribute1, attribute3);
        Assert.assertEquals(attribute1, equalAttributes);
        Assert.assertEquals(attribute1, attribute1);
        Assert.assertEquals(attribute1, attribute1);
        Assert.assertFalse(attribute1.equals(null));
        Assert.assertFalse(attribute1.equals(""));
        Assert.assertFalse(attribute1.equals(new Object()));
        Assert.assertFalse(attribute1.equals(attribute2));
        Assert.assertFalse(attribute1.equals(new Attribute("testKey3", "testValue3", null)));
        Assert.assertFalse(attribute1.equals(attributesMap));
        Assert.assertTrue(attribute1.equals(clonedAttribute));
    }

    @Test
    public void testHashCode() {
        String key1 = "testKey1";
        String value1 = "testValue1";
        Attribute attribute1 = new Attribute(key1, value1, null);

        String key2 = "testKey2";
        String value2 = "testValue2";
        Attribute attribute2 = new Attribute(key2, value2, null);

        Assert.assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value, null);

        Attribute clonedAttribute = attribute.clone();

        Assert.assertEquals(key, clonedAttribute.getKey());
        Assert.assertEquals(value, clonedAttribute.getValue());
    }
}