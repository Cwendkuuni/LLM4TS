import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setup() {
        attributes = new Attributes();
    }

    @Test
    public void testGet_ReturnsEmptyStringWhenAttributesIsNull() {
        String key = "key";

        String result = attributes.get(key);

        assertEquals("", result);
    }

    @Test
    public void testGet_ReturnsEmptyStringWhenAttributeIsNotSet() {
        String key = "key";

        attributes.put("otherKey", "value");

        String result = attributes.get(key);

        assertEquals("", result);
    }

    @Test
    public void testGet_ReturnsAttributeValueWhenAttributeIsSet() {
        String key = "key";
        String value = "value";
        attributes.put(key, value);

        String result = attributes.get(key);

        assertEquals(value, result);
    }

    @Test
    public void testGetIgnoreCase_ReturnsEmptyStringWhenAttributesIsNull() {
        String key = "key";

        String result = attributes.getIgnoreCase(key);

        assertEquals("", result);
    }

    @Test
    public void testGetIgnoreCase_ReturnsEmptyStringWhenAttributeIsNotSet() {
        String key = "key";

        attributes.put("otherKey", "value");

        String result = attributes.getIgnoreCase(key);

        assertEquals("", result);
    }

    @Test
    public void testGetIgnoreCase_ReturnsAttributeValueWhenAttributeIsSet() {
        String key = "key";
        String value = "value";
        attributes.put(key, value);

        String result = attributes.getIgnoreCase(key);

        assertEquals(value, result);
    }

    @Test
    public void testPut_AddsNewAttribute() {
        String key = "key";
        String value = "value";

        attributes.put(key, value);

        assertTrue(attributes.hasKey(key));
        assertEquals(value, attributes.get(key));
    }

    @Test
    public void testPut_ReplacesExistingAttribute() {
        String key = "key";
        String value1 = "value1";
        String value2 = "value2";

        attributes.put(key, value1);
        attributes.put(key, value2);

        assertTrue(attributes.hasKey(key));
        assertEquals(value2, attributes.get(key));
    }

    @Test
    public void testPut_AddsBooleanAttributeWhenValueIsTrue() {
        String key = "key";
        boolean value = true;

        attributes.put(key, value);

        assertTrue(attributes.hasKey(key));
        assertEquals("", attributes.get(key));
    }

    @Test
    public void testPut_RemovesAttributeWhenValueIsFalse() {
        String key = "key";
        boolean value = false;

        attributes.put(key, value);

        assertFalse(attributes.hasKey(key));
        assertEquals("", attributes.get(key));
    }

    @Test
    public void testRemove_RemovesAttributeByKey() {
        String key = "key";
        attributes.put(key, "value");

        attributes.remove(key);

        assertFalse(attributes.hasKey(key));
    }

    @Test
    public void testRemoveIgnoreCase_RemovesAttributeByKeyCaseInsensitive() {
        String key = "key";
        attributes.put(key, "value");

        attributes.removeIgnoreCase(key.toUpperCase());

        assertFalse(attributes.hasKey(key));
    }

    @Test
    public void testHasKey_ReturnsTrueWhenAttributeExists() {
        String key = "key";
        attributes.put(key, "value");

        assertTrue(attributes.hasKey(key));
    }

    @Test
    public void testHasKey_ReturnsFalseWhenAttributeDoesNotExist() {
        String key = "key";

        assertFalse(attributes.hasKey(key));
    }

    @Test
    public void testHasKeyIgnoreCase_ReturnsTrueWhenAttributeExists() {
        String key = "key";
        attributes.put(key, "value");

        assertTrue(attributes.hasKeyIgnoreCase(key.toUpperCase()));
    }

    @Test
    public void testHasKeyIgnoreCase_ReturnsFalseWhenAttributeDoesNotExist() {
        String key = "key";

        assertFalse(attributes.hasKeyIgnoreCase(key));
    }

    @Test
    public void testSize_ReturnsZeroWhenAttributesIsNull() {
        assertEquals(0, attributes.size());
    }

    @Test
    public void testSize_ReturnsSizeOfAttributes() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll_AddsAllAttributesFromIncomingAttributes() {
        Attributes incoming = new Attributes();
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");

        attributes.addAll(incoming);

        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testIterator_ReturnsEmptyIteratorWhenAttributesIsNull() {
        assertFalse(attributes.iterator().hasNext());
    }

    @Test
    public void testIterator_ReturnsIteratorOverAttributes() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String[] expectedKeys = {"key1", "key2"};
        String[] expectedValues = {"value1", "value2"};

        int index = 0;
        for (Attribute attribute : attributes) {
            assertEquals(expectedKeys[index], attribute.getKey());
            assertEquals(expectedValues[index], attribute.getValue());
            index++;
        }
    }

    @Test
    public void testAsList_ReturnsEmptyListWhenAttributesIsNull() {
        assertTrue(attributes.asList().isEmpty());
    }

    @Test
    public void testAsList_ReturnsUnmodifiableList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String[] expectedKeys = {"key1", "key2"};
        String[] expectedValues = {"value1", "value2"};

        List<Attribute> attributeList = attributes.asList();
        assertEquals(2, attributeList.size());

        for (int i = 0; i < attributeList.size(); i++) {
            assertEquals(expectedKeys[i], attributeList.get(i).getKey());
            assertEquals(expectedValues[i], attributeList.get(i).getValue());
        }

        try {
            attributeList.add(new Attribute("key3", "value3"));
            fail("UnsupportedOperationException should have been thrown.");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testDataset_ReturnsDatasetWithCustomDataAttributes() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        attributes.put("otherKey", "otherValue");

        Map<String, String> dataset = attributes.dataset();

        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("key1"));
        assertTrue(dataset.containsKey("key2"));
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml_ReturnsEmptyStringWhenAttributesIsNull() {
        assertEquals("", attributes.html());
    }

    @Test
    public void testHtml_ReturnsHtmlRepresentationOfAttributes() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String expectedHtml = " key1=\"value1\" key2=\"value2\"";

        assertEquals(expectedHtml, attributes.html());
    }

    @Test
    public void testClone_ReturnsClonedAttributesWithSameValues() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clone = attributes.clone();

        assertNotSame(attributes, clone);
        assertTrue(attributes.equals(clone));
        assertEquals(attributes.html(), clone.html());
    }

    @Test
    public void testEquals_ReturnsTrueWhenOtherObjectIsSame() {
        assertTrue(attributes.equals(attributes));
    }

    @Test
    public void testEquals_ReturnsFalseWhenOtherObjectIsNull() {
        assertFalse(attributes.equals(null));
    }

    @Test
    public void testEquals_ReturnsFalseWhenOtherObjectIsNotAttributesInstance() {
        assertFalse(attributes.equals("string"));
    }

    @Test
    public void testEquals_ReturnsFalseWhenAttributesAreDifferent() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");

        assertFalse(attributes.equals(otherAttributes));
    }

    @Test
    public void testEquals_ReturnsTrueWhenAttributesAreEqual() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");

        assertTrue(attributes.equals(otherAttributes));
    }

    @Test
    public void testHashCode_ReturnsZeroWhenAttributesIsNull() {
        assertEquals(0, attributes.hashCode());
    }

    @Test
    public void testHashCode_ReturnsHashCodeOfAttributes() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(attributes.asList().hashCode(), attributes.hashCode());
    }
}