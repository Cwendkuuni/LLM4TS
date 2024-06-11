import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {
    private Attributes attributes;

    @Before
    public void setUp() {
        attributes = new Attributes();
    }

    //Test case for get method
    @Test
    public void testGet() {
        attributes.put("key1", "value1");
        
        String result = attributes.get("key1");
        
        assertEquals("value1", result);
    }

    //Test case for getIgnoreCase method
    @Test
    public void testGetIgnoreCase() {
        attributes.put("Key1", "value1");
        
        String result = attributes.getIgnoreCase("key1");
        
        assertEquals("value1", result);
    }

    //Test case for put method
    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        
        String result = attributes.get("key1");
        
        assertEquals("value1", result);
    }

    //Test case for put (boolean) method (value is true)
    @Test
    public void testPutBooleanTrue() {
        attributes.put("key1", true);
        
        String result = attributes.get("key1");
        
        assertEquals("", result);
    }

    //Test case for put (boolean) method (value is false, so attribute should be removed)
    @Test
    public void testPutBooleanFalse() {
        attributes.put("key1", "value1");
        
        boolean value = false;
        attributes.put("key1", value);
        
        boolean hasKey = attributes.hasKey("key1");
        
        assertFalse(hasKey);
    }

    //Test case for put(Attribute) method
    @Test
    public void testPutAttribute() {
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);
        
        String result = attributes.get("key1");
        
        assertEquals("value1", result);
    }

    //Test case for remove method (case sensitive)
    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        
        attributes.remove("key1");
        
        boolean hasKey = attributes.hasKey("key1");
        
        assertFalse(hasKey);
    }

    //Test case for removeIgnoreCase method
    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("Key1", "value1");
        
        attributes.removeIgnoreCase("key1");
        
        boolean hasKey = attributes.hasKey("Key1");
        
        assertFalse(hasKey);
    }

    //Test case for hasKey method (case sensitive)
    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");
        
        boolean result = attributes.hasKey("key1");
        
        assertTrue(result);
    }

    //Test case for hasKeyIgnoreCase method
    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("Key1", "value1");
        
        boolean result = attributes.hasKeyIgnoreCase("key1");
        
        assertTrue(result);
    }

    //Test case for size method
    @Test
    public void testSize() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        int result = attributes.size();
        
        assertEquals(2, result);
    }

    //Test case for addAll method
    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");
        
        attributes.addAll(incoming);
        
        boolean hasKey1 = attributes.hasKey("key1");
        boolean hasKey2 = attributes.hasKey("key2");
        
        assertTrue(hasKey1);
        assertTrue(hasKey2);
    }

    //Test case for iterator method
    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        
        assertEquals(2, count);
    }

    //Test case for asList method
    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        int result = attributes.asList().size();
        
        assertEquals(2, result);
    }

    //Test case for dataset method
    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        
        int result = attributes.dataset().size();
        
        assertEquals(2, result);
    }

    //Test case for html method
    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        String result = attributes.html();
        
        String expected = " key1=\"value1\" key2=\"value2\"";
        
        assertEquals(expected, result);
    }

    //Test case for equals method
    @Test
    public void testEquals() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        Attributes otherAttributes = new Attributes();
        otherAttributes.put("key1", "value1");
        otherAttributes.put("key2", "value2");
        
        boolean result = attributes.equals(otherAttributes);
        
        assertTrue(result);
    }

    //Test case for hashCode method
    @Test
    public void testHashCode() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        int hashCode = attributes.hashCode();
        int expectedHashCode = 1985229366;
        
        assertEquals(expectedHashCode, hashCode);
    }

    //Test case for clone method
    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        Attributes clone = attributes.clone();
        
        boolean hasKey1 = clone.hasKey("key1");
        boolean hasKey2 = clone.hasKey("key2");
        
        assertTrue(hasKey1);
        assertTrue(hasKey2);
    }

    //Test case for Dataset class
    @Test
    public void testDatasetClass() {
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        
        int result = attributes.dataset().size();
        
        assertEquals(2, result);
    }

    //Test case for dataKey method
    @Test
    public void testDataKey() {
        String key = attributes.dataKey("key1");
        
        assertEquals("data-key1", key);
    }
}