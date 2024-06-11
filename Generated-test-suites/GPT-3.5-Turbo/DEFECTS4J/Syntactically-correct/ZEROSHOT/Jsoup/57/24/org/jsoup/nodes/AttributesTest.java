import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertEquals("value1", attributes.get("key1"));
        assertEquals("value2", attributes.get("key2"));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("KEY2", "value2");
        
        assertEquals("value1", attributes.getIgnoreCase("key1"));
        assertEquals("value2", attributes.getIgnoreCase("key2"));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        
        assertTrue(attributes.hasKey("key1"));
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("key1", true);
        
        assertTrue(attributes.hasKey("key1"));
        assertEquals("true", attributes.get("key1"));
        
        attributes.put("key2", false);
        
        assertFalse(attributes.hasKey("key2"));
        assertEquals("", attributes.get("key2"));
    }

    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        Attribute attribute = new Attribute("key1", "value1");
        attributes.put(attribute);
        
        assertTrue(attributes.hasKey("key1"));
        assertEquals("value1", attributes.get("key1"));
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        
        attributes.remove("key1");
        
        assertFalse(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("KEY2", "value2");
        
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        
        attributes.removeIgnoreCase("kEy1");
        
        assertFalse(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertTrue(attributes.hasKey("key1"));
        assertTrue(attributes.hasKey("key2"));
        assertFalse(attributes.hasKey("key3"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("KEY2", "value2");
        
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("key3"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        assertEquals(0, attributes.size());
        
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assertEquals(2, attributes.size());
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key2", "value2");
        
        attributes1.addAll(attributes2);
        
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
        assertEquals("value1", attributes1.get("key1"));
        assertEquals("value2", attributes1.get("key2"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        int count = 0;
        for (Attribute attribute : attributes) {
            count++;
        }
        
        assertEquals(2, count);
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        List<Attribute> list = attributes.asList();
        
        assertEquals(2, list.size());
        assertEquals("value1", list.get(0).getValue());
        assertEquals("value2", list.get(1).getValue());
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        
        Map<String, String> dataset = attributes.dataset();
        
        assertEquals(2, dataset.size());
        assertEquals("value1", dataset.get("key1"));
        assertEquals("value2", dataset.get("key2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        String html = attributes.html();
        
        assertEquals(" key1=\"value1\" key2=\"value2\"", html);
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        
        assertTrue(attributes1.equals(attributes2));
        
        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        
        assertFalse(attributes1.equals(attributes3));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        
        assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }

    @Test
    public void testClone() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        
        Attributes attributes2 = attributes1.clone();
        
        assertTrue(attributes1.equals(attributes2));
        assertNotSame(attributes1, attributes2);
    }

    @Test
    public void testSizeOfDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        
        int datasetSize = attributes.dataset().size();
        
        assertEquals(2, datasetSize);
    }

    @Test
    public void testPutInDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        
        attributes.dataset().put("key2", "value2");
        
        assertTrue(attributes.hasKey("data-key1"));
        assertTrue(attributes.hasKey("data-key2"));
        assertEquals("value1", attributes.get("data-key1"));
        assertEquals("value2", attributes.get("data-key2"));
    }

    @Test
    public void testEntrySetOfDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        
        int count = 0;
        for (Map.Entry<String, String> entry : attributes.dataset().entrySet()) {
            count++;
        }
        
        assertEquals(2, count);
    }

    @Test
    public void testRemoveFromDatasetIterator() {
        Attributes attributes = new Attributes();
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        
        Iterator<Map.Entry<String, String>> iterator = attributes.dataset().entrySet().iterator();
        
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        
        assertEquals(0, attributes.dataset().size());
    }

    @Test
    public void testDataKey() {
        String key = "key1";
        
        String dataKey = Attributes.dataKey(key);
        
        assertEquals("data-key1", dataKey);
    }

}