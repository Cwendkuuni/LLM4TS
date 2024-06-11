import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {

    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    @Test
    public void testGet_emptyAttributes() {
        String result = attributes.get("key");
        assertEquals("", result);
    }

    @Test
    public void testGet_existingKey() {
        attributes.put("key", "value");
        String result = attributes.get("key");
        assertEquals("value", result);
    }

    @Test
    public void testGet_emptyStringForNonExistentKey() {
        String result = attributes.get("nonexistent");
        assertEquals("", result);
    }

    @Test
    public void testGetIgnoreCase_emptyAttributes() {
        String result = attributes.getIgnoreCase("KEY");
        assertEquals("", result);
    }

    @Test
    public void testGetIgnoreCase_existingKey() {
        attributes.put("key", "value");
        String result = attributes.getIgnoreCase("KEY");
        assertEquals("value", result);
    }

    @Test
    public void testGetIgnoreCase_emptyStringForNonExistentKey() {
        String result = attributes.getIgnoreCase("nonexistent");
        assertEquals("", result);
    }

    @Test
    public void testPut_newAttribute() {
        attributes.put("key", "value");
        String result = attributes.get("key");
        assertEquals("value", result);
    }

    @Test
    public void testPut_replaceExistingAttribute() {
        attributes.put("key", "value");
        attributes.put("key", "newValue");
        String result = attributes.get("key");
        assertEquals("newValue", result);
    }

    @Test
    public void testPut_booleanValue_true() {
        attributes.put("key", true);
        String result = attributes.get("key");
        assertEquals("", result);
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testPut_booleanValue_false() {
        attributes.put("key", false);
        String result = attributes.get("key");
        assertEquals("", result);
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemove_existingKey() {
        attributes.put("key", "value");
        attributes.remove("key");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemove_nonExistentKey() {
        attributes.remove("nonexistent");
        assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testRemoveIgnoreCase_existingKey() {
        attributes.put("key", "value");
        attributes.removeIgnoreCase("KEY");
        assertFalse(attributes.hasKey("key"));
    }

    @Test
    public void testRemoveIgnoreCase_nonExistentKey() {
        attributes.removeIgnoreCase("nonexistent");
        assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testHasKey_existingKey() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKey("key"));
    }

    @Test
    public void testHasKey_nonExistentKey() {
        assertFalse(attributes.hasKey("nonexistent"));
    }

    @Test
    public void testHasKeyIgnoreCase_existingKey() {
        attributes.put("key", "value");
        assertTrue(attributes.hasKeyIgnoreCase("KEY"));
    }

    @Test
    public void testHasKeyIgnoreCase_nonExistentKey() {
        assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
    }

    @Test
    public void testSize_emptyAttributes() {
        int result = attributes.size();
        assertEquals(0, result);
    }

    @Test
    public void testSize_nonEmptyAttributes() {
        attributes.put("key", "value");
        int result = attributes.size();
        assertEquals(1, result);
    }

    @Test
    public void testAddAll_attributesToAddEmpty() {
        Attributes attributesToAdd = new Attributes();
        attributes.addAll(attributesToAdd);
        int result = attributes.size();
        assertEquals(0, result);
    }

    @Test
    public void testAddAll_attributesToAddNonEmpty() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes attributesToAdd = new Attributes();
        attributesToAdd.put("key3", "value3");
        attributesToAdd.put("key4", "value4");
        attributes.addAll(attributesToAdd);

        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
        assertEquals("value3", attributes.get("key3"));
        assertEquals("value4", attributes.get("key4"));
        assertEquals(4, attributes.size());
    }

    @Test
    public void testIterator_emptyAttributes() {
        assertFalse(attributes.iterator().hasNext());
    }

    @Test
    public void testIterator_nonEmptyAttributes() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }

        assertEquals(attributes.size(), count);
    }

    @Test
    public void testAsList_emptyAttributes() {
        assertEquals(0, attributes.asList().size());
    }

    @Test
    public void testAsList_nonEmptyAttributes() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        assertEquals(2, attributes.asList().size());
        assertTrue(attributes.asList().contains(new Attribute("key1", "value1")));
        assertTrue(attributes.asList().contains(new Attribute("key2", "value2")));
    }

    @Test
    public void testDataset_emptyAttributes() {
        assertEquals(0, attributes.dataset().size());
    }

    @Test
    public void testDataset_nonEmptyAttributes() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");

        assertEquals(2, attributes.dataset().size());
        assertTrue(attributes.dataset().containsKey("key1"));
        assertEquals("value1", attributes.dataset().get("key1"));
        assertTrue(attributes.dataset().containsKey("key2"));
        assertEquals("value2", attributes.dataset().get("key2"));
    }

    @Test
    public void testHtml_emptyAttributes() {
        String result = attributes.html();
        assertEquals("", result);
    }

    @Test
    public void testHtml_nonEmptyAttributes() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        String result = attributes.html();
        assertEquals(" key1=\"value1\" key2=\"value2\"", result);
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(attributes.equals(attributes));
    }

    @Test
    public void testEquals_equalAttributes() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");

        assertTrue(attributes.equals(otherAttributes));
    }

    @Test
    public void testEquals_differentAttributes() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key3", "value3");
        otherAttributes.put("key4", "value4");

        assertFalse(attributes.equals(otherAttributes));
    }

    @Test
    public void testHashCode_sameObject() {
        assertEquals(attributes.hashCode(), attributes.hashCode());
    }

    @Test
    public void testHashCode_equalAttributes() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");

        assertEquals(attributes.hashCode(), otherAttributes.hashCode());
    }

    @Test
    public void testClone_emptyAttributes() {
        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes, clonedAttributes);
        assertNotSame(attributes, clonedAttributes);
    }

    @Test
    public void testClone_nonEmptyAttributes() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");

        Attributes clonedAttributes = attributes.clone();
        assertEquals(attributes, clonedAttributes);
        assertNotSame(attributes, clonedAttributes);
        assertNotSame(attributes.asList(), clonedAttributes.asList());
    }

    @Test
    public void testClone_modifyClonedAttributes() {
        attributes.put("key1", "value1");

        Attributes clonedAttributes = attributes.clone();
        clonedAttributes.put("key2", "value2");

        assertFalse(attributes.hasKey("key2"));
        assertTrue(clonedAttributes.hasKey("key2"));
    }

    @Test
    public void testDataKey() {
        String dataKey = attributes.dataKey("key");
        assertEquals("data-key", dataKey);
    }

}