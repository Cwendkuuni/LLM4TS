import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);
        Attribute expectedAttribute = new Attribute(key, value, null);
        assertEquals(expectedAttribute, attribute);
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeCreationWithNullKey() {
        String key = null;
        String value = "value";
        new Attribute(key, value);
    }

    @Test(expected = NullPointerException.class)
    public void testAttributeCreationWithNullValue() {
        String key = "key";
        String value = null;
        new Attribute(key, value);
    }

    @Test
    public void testSetKey() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);
        String newKey = "newKey";
        attribute.setKey(newKey);
        Attribute expectedAttribute = new Attribute(newKey, value, null);
        assertEquals(expectedAttribute, attribute);
    }

    @Test(expected = NullPointerException.class)
    public void testSetKeyWithNullKey() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);
        String newKey = null;
        attribute.setKey(newKey);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyWithEmptyKey() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);
        String newKey = "";
        attribute.setKey(newKey);
    }

    @Test
    public void testSetValue() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);
        String newValue = "newValue";
        String oldValue = attribute.getValue();
        attribute.setValue(newValue);
        Attribute expectedAttribute = new Attribute(key, newValue, null);
        assertEquals(expectedAttribute, attribute);
        assertEquals(oldValue, value);
    }

    @Test(expected = NullPointerException.class)
    public void testSetValueWithNullValue() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);
        String newValue = null;
        attribute.setValue(newValue);
    }

    @Test
    public void testHtml() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);
        StringBuilder expectedResult = new StringBuilder();
        expectedResult.append(key).append("=\"").append(value).append("\"");
        String result = attribute.html();
        assertEquals(expectedResult.toString(), result);
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "key";
        String encodedValue = "key=\"value\"";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        Attribute expectedAttribute = new Attribute(unencodedKey, "value", null);
        assertEquals(expectedAttribute, attribute);
    }

    @Test
    public void testIsDataAttribute() {
        String key = "data-key";
        Attribute attribute = new Attribute(key, null);
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsDataAttributeFalse() {
        String key = "key";
        Attribute attribute = new Attribute(key, null);
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
        value = "";
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
        key = "allowfullscreen";
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        String key = "key";
        Attribute attribute = new Attribute(key, null);
        assertFalse(attribute.isBooleanAttribute());
        key = "checked";
        attribute = new Attribute(key, null);
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        String key1 = "key1";
        String value1 = "value1";
        Attribute attribute1 = new Attribute(key1, value1);
        String key2 = "key2";
        String value2 = "value2";
        Attribute attribute2 = new Attribute(key2, value2);
        Attribute attribute3 = new Attribute(key1, value1);
        Attribute attribute4 = new Attribute(key1, value2);
        Attribute attribute5 = new Attribute(key2, value1);
        Attribute attribute6 = new Attribute(key1, value1, attribute1);
        assertTrue(attribute1.equals(attribute3));
        assertTrue(attribute1.equals(attribute6));
        assertFalse(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute4));
        assertFalse(attribute1.equals(attribute5));
    }

    @Test
    public void testClone() {
        String key = "key";
        String value = "value";
        Attribute attribute = new Attribute(key, value);
        Attribute clonedAttribute = attribute.clone();
        Attribute expectedAttribute = new Attribute(key, value, null);
        assertEquals(expectedAttribute, clonedAttribute);
    }
}