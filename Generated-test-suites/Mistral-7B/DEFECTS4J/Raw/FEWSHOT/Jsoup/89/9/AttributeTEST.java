package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testConstructorWithNullKey() {
        try {
            new Attribute(null, "value");
            fail("Should throw a NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithEmptyKey() {
        try {
            new Attribute("", "value");
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithValidArguments() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertNull(attr.getParent());
    }

    @Test
    public void testConstructorWithValidArgumentsAndParent() {
        Attributes attributes = new Attributes();
        Attribute attr = new Attribute("key", "value", attributes);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertSame(attributes, attr.getParent());
    }

    @Test
    public void testSetKeyWithNullKey() {
        Attribute attr = new Attribute("key", "value");
        try {
            attr.setKey(null);
            fail("Should throw a NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testSetKeyWithEmptyKey() {
        Attribute attr = new Attribute("key", "value");
        try {
            attr.setKey("");
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSetKeyWithDifferentKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testSetKeyWithSameKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("key");
        assertEquals("key", attr.getKey());
    }

    @Test
    public void testSetValueWithNullValue() {
        Attribute attr = new Attribute("key", "value");
        try {
            attr.setValue(null);
            fail("Should throw a NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testSetValueWithValidValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }

    @Test
    public void testSetValueWithSameValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("value");
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testShouldCollapseAttributeWithNullParent() {
        Attribute attr = new Attribute("key", "value");
        assertFalse(attr.shouldCollapseAttribute(null));
    }

    @Test
    public void testShouldCollapseAttributeWithEmptyParentOutputSettings() {
        Attribute attr = new Attribute("key", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        assertFalse(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanAttributeAndEmptyValue() {
        Attribute attr = new Attribute("checked", "");
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanAttributeAndValueSameAsKey() {
        Attribute attr = new Attribute("checked", "checked");
        Document.OutputSettings out = new Document.OutputSettings();
        assertTrue(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeWithBooleanAttributeAndValueDifferentFromKey() {
        Attribute attr = new Attribute("checked", "selected");
        Document.OutputSettings out = new Document.OutputSettings();
        assertFalse(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testShouldCollapseAttributeWithNonBooleanAttribute() {
        Attribute attr = new Attribute("key", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        assertFalse(attr.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttributeWithValidBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
    }

    @Test
    public void testIsBooleanAttributeWithNonBooleanAttribute() {
        assertFalse(Attribute.isBooleanAttribute("key"));
    }

    @Test
    public void testIsBooleanAttributeWithNullValueBooleanAttribute() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
        assertTrue(Attribute.isBooleanAttribute(""));
    }

    @Test
    public void testClone() {
        Attribute original = new Attribute("key", "value");
        Attribute cloned = original.clone();
        assertEquals(original.getKey(), cloned.getKey());
        assertEquals(original.getValue(), cloned.getValue());
        assertNull(cloned.getParent());
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        assertFalse(attr1.equals(attr2));
    }

    @Test
    public void testEqualsWithSameObject() {
        Attribute attr1 = new Attribute("key", "value");
        assertTrue(attr1.equals(attr1));
    }

    @Test
    public void testEqualsWithSameKeyAndDifferentValue() {
        Attribute attr1 = new Attribute("key", "value1");
        Attribute attr2 = new Attribute("key", "value2");
        assertFalse(attr1.equals(attr2));
    }

    @Test
    public void testEqualsWithSameKeyAndSameValue() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        assertTrue(attr1.equals(attr2));
    }

    @Test
    public void testHashCodeWithSameObjects() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        assertEquals(attr1.hashCode(), attr2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentObjects() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key2", "value2");
        assertNotEquals(attr1.hashCode(), attr2.hashCode());
    }

    @Test
    public void testToString() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key=\"value\"", attr.toString());
    }

    @Test
    public void testCreateFromEncodedWithNullUnencodedKey() {
        try {
            Attribute.createFromEncoded(null, "key=\"value\"");
            fail("Should throw a NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFromEncodedWithEmptyUnencodedKey() {
        try {
            Attribute.createFromEncoded("", "key=\"value\"");
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testCreateFromEncodedWithNullEncodedValue() {
        try {
            Attribute.createFromEncoded("key", null);
            fail("Should throw a NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFromEncodedWithEmptyEncodedValue() {
        try {
            Attribute.createFromEncoded("key", "");
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFromEncodedWithEncodedValueThatIsNotAnAttribute() {
        try {
            Attribute.createFromEncoded("key", "key=value&anotherKey=anotherValue");
            fail("Should throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFromEncodedWithEncodedValueThatContainsMultipleAttributes() {
        Attributes attributes = new Attributes();
        Map<String, Attribute> expectedAttributes = new HashMap<>();
        expectedAttributes.put("key1", new Attribute("key1", "value1"));
        expectedAttributes.put("key2", new Attribute("key2", "value2"));
        Document document = new Document("");
        Attributes attributesFromDocument = document.select("*").attrMap();
        for (Map.Entry<String, Attribute> entry : expectedAttributes.entrySet()) {
            Attribute attribute = Attribute.createFromEncoded(entry.getKey(), entry.getValue().html());
            assertEquals(attribute.getKey(), entry.getKey());
            assertEquals(attribute.getValue(), entry.getValue().getValue());
            assertSame(attributesFromDocument, attribute.getParent());
        }
    }

    @Test
    public void testIsDataAttributeWithDataPrefixAndKeyStartingWithDataPrefix() {
        assertTrue(Attribute.isDataAttribute("data-key"));
    }

    @Test
    public void testIsDataAttributeWithDataPrefixAndKeyNotStartingWithDataPrefix() {
        assertFalse(Attribute.isDataAttribute("key"));
    }
}