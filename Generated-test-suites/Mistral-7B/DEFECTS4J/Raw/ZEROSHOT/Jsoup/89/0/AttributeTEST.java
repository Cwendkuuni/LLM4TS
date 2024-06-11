import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        String key = "testKey";
        String val = "testVal";
        Attribute attribute = new Attribute(key, val);
        Attribute expectedAttribute = new Attribute(key, val, null);

        assertEquals(expectedAttribute.getKey(), attribute.getKey());
        assertEquals(expectedAttribute.getValue(), attribute.getValue());
    }

    @Test
    public void testAttributeCreationWithParent() {
        String key = "testKey";
        String val = "testVal";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, val, parent);

        assertEquals(key, attribute.getKey());
        assertEquals(val, attribute.getValue());
        assertEquals(parent, attribute.parent);
    }

    @Test
    public void testSettingKey() {
        String initialKey = "initialKey";
        String initialVal = "initialVal";
        Attribute attribute = new Attribute(initialKey, initialVal);
        String newKey = "newKey";

        attribute.setKey(newKey);

        assertEquals(newKey, attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullKey() {
        String initialKey = "initialKey";
        String initialVal = "initialVal";
        Attribute attribute = new Attribute(initialKey, initialVal);

        attribute.setKey(null);
    }

    @Test
    public void testSettingEmptyKey() {
        String initialKey = "initialKey";
        String initialVal = "initialVal";
        Attribute attribute = new Attribute(initialKey, initialVal);

        attribute.setKey("");

        assertEquals("", attribute.getKey());
    }

    @Test
    public void testSettingKeyWithParent() {
        String initialKey = "initialKey";
        String initialVal = "initialVal";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(initialKey, initialVal, parent);

        String newKey = "newKey";
        attribute.setKey(newKey);

        assertEquals(newKey, attribute.getKey());
        assertEquals(parent, attribute.parent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullParent() {
        String key = "key";
        String val = "val";

        Attribute attribute = new Attribute(key, val);

        attribute.setParent(null);
    }

    @Test
    public void testSettingValue() {
        String key = "key";
        String initialVal = "initialVal";
        String newVal = "newVal";
        Attribute attribute = new Attribute(key, initialVal);

        attribute.setValue(newVal);

        assertEquals(newVal, attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettingNullValue() {
        String key = "key";
        String val = "val";
        Attribute attribute = new Attribute(key, val);

        attribute.setValue(null);
    }

    @Test
    public void testSettingValueWithParent() {
        String key = "key";
        String initialVal = "initialVal";
        String newVal = "newVal";
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute(key, initialVal, parent);

        attribute.setValue(newVal);

        assertEquals(newVal, attribute.getValue());
        assertEquals(parent, attribute.parent);
    }

    @Test
    public void testGettingHtml() throws IOException {
        String key = "testKey";
        String val = "testVal";
        Attribute attribute = new Attribute(key, val);

        StringBuilder expectedHtml = new StringBuilder();
        attribute.html(expectedHtml, new org.jsoup.nodes.Document.OutputSettings());

        StringBuilder actualHtml = new StringBuilder();
        attribute.html(actualHtml, new org.jsoup.nodes.Document.OutputSettings());

        assertEquals(expectedHtml.toString(), actualHtml.toString());
    }

    @Test
    public void testCreatingFromEncoded() {
        String unencodedKey = "unencodedKey";
        String encodedValue = "encodedValue=\"unencodedKey testValue\"";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);

        assertEquals(unencodedKey, attribute.getKey());
        assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        String key = "data-test";
        Attribute attribute = new Attribute(key, null);

        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsDataAttributeWithNullKey() {
        Attribute attribute = new Attribute(null, null);

        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        Attribute attribute1 = new Attribute("key1", "value1");
        assertFalse(attribute1.shouldCollapseAttribute(outputSettings));

        Attribute attribute2 = new Attribute("key2", "");
        assertTrue(attribute2.shouldCollapseAttribute(outputSettings));

        Attribute attribute3 = new Attribute("booleanAttribute", "");
        assertTrue(attribute3.shouldCollapseAttribute(outputSettings));

        Attribute attribute4 = new Attribute("booleanAttribute", "false");
        assertFalse(attribute4.shouldCollapseAttribute(outputSettings));

        Attribute attribute5 = new Attribute("booleanAttribute", "true");
        assertFalse(attribute5.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("booleanAttribute1", null);
        assertFalse(attribute1.isBooleanAttribute());

        Attribute attribute2 = new Attribute("checked", null);
        assertTrue(attribute2.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        String key1 = "key1";
        String val1 = "val1";
        String key2 = "key2";
        String val2 = "val2";

        Attribute attribute1 = new Attribute(key1, val1);
        Attribute attribute2 = new Attribute(key2, val2);

        assertFalse(attribute1.equals(attribute2));

        Attribute attribute3 = new Attribute(key1, val1);
        assertTrue(attribute1.equals(attribute3));

        Attribute attribute4 = new Attribute(key1, val1);
        Attribute attribute5 = new Attribute(key1, val1);
        assertTrue(attribute4.equals(attribute5));
    }

    @Test
    public void testClone() {
        String key = "key";
        String val = "val";
        Attribute attribute = new Attribute(key, val);

        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}