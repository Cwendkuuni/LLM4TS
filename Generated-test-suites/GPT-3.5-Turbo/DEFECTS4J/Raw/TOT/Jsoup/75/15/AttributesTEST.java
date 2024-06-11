package org.jsoup.nodes;

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
    public void testGet_existingKey_returnsValue() {
        attributes.put("key", "value");
        String result = attributes.get("key");
        assertEquals("value", result);
    }

    @Test
    public void testGet_nonExistingKey_returnsEmptyString() {
        String result = attributes.get("key");
        assertEquals("", result);
    }
    
    @Test
    public void testGet_emptyKey_returnsEmptyString() {
        attributes.put("", "value");
        String result = attributes.get("");
        assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCase_existingKey_returnsValue() {
        attributes.put("Key", "value");
        String result = attributes.getIgnoreCase("key");
        assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCase_nonExistingKey_returnsEmptyString() {
        String result = attributes.getIgnoreCase("key");
        assertEquals("", result);
    }

    @Test
    public void testPut_newAttribute_returnsAttributesWithAttribute() {
        Attributes result = attributes.put("key", "value");
        assertTrue(result.hasKey("key"));
    }

    @Test
    public void testPut_existingAttribute_returnsAttributesWithUpdatedValue() {
        attributes.put("key", "value1");
        Attributes result = attributes.put("key", "value2");
        assertEquals("value2", result.get("key"));
    }

    @Test
    public void testRemove_existingKey_removedKeyFromAttributes() {
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemove_nonExistingKey_noChangeInAttributes() {
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testHasKey_existingKey_returnsTrue() {
        attributes.put("key", "value");
        boolean result = attributes.hasKey("key");
        assertTrue(result);
    }

    @Test
    public void testHasKey_nonExistingKey_returnsFalse() {
        boolean result = attributes.hasKey("key");
        assertFalse(result);
    }

    @Test
    public void testHasKeyIgnoreCase_existingKey_returnsTrue() {
        attributes.put("Key", "value");
        boolean result = attributes.hasKeyIgnoreCase("key");
        assertTrue(result);
    }

    @Test
    public void testHasKeyIgnoreCase_nonExistingKey_returnsFalse() {
        boolean result = attributes.hasKeyIgnoreCase("key");
        assertFalse(result);
    }

    @Test
    public void testSize_emptyAttributes_returnsZero() {
        int result = attributes.size();
        assertEquals(0, result);
    }

    @Test
    public void testSize_attributesWithKeys_returnsNumberOfKeys() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        int result = attributes.size();
        assertEquals(2, result);
    }

    @Test
    public void testClone_withAttributes_returnsClonedAttributes() {
        attributes.put("key", "value");
        Attributes clonedAttributes = attributes.clone();
        
        assertEquals(attributes.get("key"), clonedAttributes.get("key"));
        assertNotSame(attributes, clonedAttributes);
    }
}