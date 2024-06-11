import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testConstructor() {
        String key = "key";
        String val = "value";
        Attribute attribute = new Attribute(key, val);
        assertNotNull(attribute);
        assertEquals(key, attribute.getKey());
        assertEquals(val, attribute.getValue());
        assertNull(attribute.parent);

        attribute = new Attribute(key, val, new Attributes());
        assertNotNull(attribute);
        assertEquals(key, attribute.getKey());
        assertEquals(val, attribute.getValue());
        assertNotNull(attribute.parent);
    }

    @Test
    public void testSetKey() {
        String key1 = "key1";
        String key2 = "key2";
        String val = "value";
        Attribute attribute = new Attribute(key1, val);
        attribute.setKey(key2);
        assertEquals(key2, attribute.getKey());

        Attributes parent = new Attributes();
        attribute = new Attribute(key1, val, parent);
        attribute.setKey(key2);
        assertEquals(key2, attribute.getKey());
        assertEquals(key2, parent.keys[0]);
    }

    @Test
    public void testSetValue() {
        String key = "key";
        String val1 = "value1";
        String val2 = "value2";
        Attribute attribute = new Attribute(key, val1);
        String oldValue = attribute.setValue(val2);
        assertEquals(val2, attribute.getValue());
        assertEquals(val1, oldValue);

        Attributes parent = new Attributes();
        attribute = new Attribute(key, val1, parent);
        oldValue = attribute.setValue(val2);
        assertEquals(val2, attribute.getValue());
        assertEquals(val1, oldValue);
        assertEquals(val2, parent.vals[0]);
    }

    @Test
    public void testHtml() {
        String key = "key";
        String val = "value";
        Attribute attribute = new Attribute(key, val);
        String expectedHtml = key + "=\"" + val + "\"";
        assertEquals(expectedHtml, attribute.html());
    }

    @Test
    public void testToString() {
        String key = "key";
        String val = "value";
        Attribute attribute = new Attribute(key, val);
        String expectedString = key + "=\"" + val + "\"";
        assertEquals(expectedString, attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "unencodedKey";
        String encodedValue = "encodedValue";
        Attribute attribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        assertEquals(unencodedKey, attribute.getKey());
        assertEquals(encodedValue, attribute.getValue());
        assertNull(attribute.parent);
    }

    @Test
    public void testIsDataAttribute() {
        String dataAttributeKey = "data-key";
        String nonDataAttributeKey = "key";
        assertTrue(Attribute.isDataAttribute(dataAttributeKey));
        assertFalse(Attribute.isDataAttribute(nonDataAttributeKey));
    }

    @Test
    public void testShouldCollapseAttribute() {
        String key = "key";
        String val1 = null;
        String val2 = "";
        String val3 = "key";
        String val4 = "value";
        Document.OutputSettings out = new Document.OutputSettings();
        assertFalse(Attribute.shouldCollapseAttribute(key, val1, out));
        assertTrue(Attribute.shouldCollapseAttribute(key, val2, out));
        assertTrue(Attribute.shouldCollapseAttribute(key, val3, out));
        assertFalse(Attribute.shouldCollapseAttribute(key, val4, out));
    }

    @Test
    public void testIsBooleanAttribute() {
        String booleanAttributeKey = "checked";
        String nonBooleanAttributeKey = "key";
        assertTrue(Attribute.isBooleanAttribute(booleanAttributeKey));
        assertFalse(Attribute.isBooleanAttribute(nonBooleanAttributeKey));
    }

    @Test
    public void testEquals() {
        String key1 = "key1";
        String val1 = "value1";
        String key2 = "key2";
        String val2 = "value2";
        Attribute attribute1 = new Attribute(key1, val1);
        Attribute attribute2 = new Attribute(key1, val1);
        assertTrue(attribute1.equals(attribute2));
        assertTrue(attribute2.equals(attribute1));

        attribute2 = new Attribute(key2, val2);
        assertFalse(attribute1.equals(attribute2));
        assertFalse(attribute2.equals(attribute1));
    }

    @Test
    public void testHashCode() {
        String key = "key";
        String val = "value";
        Attribute attribute = new Attribute(key, val);
        int expectedHashCode = 31 * (key != null ? key.hashCode() : 0) + (val != null ? val.hashCode() : 0);
        assertEquals(expectedHashCode, attribute.hashCode());
    }

    @Test
    public void testClone() {
        String key = "key";
        String val = "value";
        Attribute attribute1 = new Attribute(key, val);
        Attribute attribute2 = attribute1.clone();
        assertEquals(attribute1, attribute2);
        assertNotSame(attribute1, attribute2);
    }
}