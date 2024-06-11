package org.test; // replace with your test package name

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import static org.jsoup.nodes.Attributes.dataPrefix;

public class AttributesTest {

    

    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.get("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertNull(attributes.get(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetWithEmptyStringKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertNull(attributes.get(""));
    }

    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertEquals("value", attributes.getIgnoreCase("key"));
        assertEquals("value", attributes.getIgnoreCase("Key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIgnoreCaseWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertNull(attributes.getIgnoreCase(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIgnoreCaseWithEmptyStringKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        assertNull(attributes.getIgnoreCase(""));
    }

    @Test
    public void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("key", "value2");
        assertEquals("value2", attributes.get("key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put(null, "value");
        fail(); // test should fail
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithEmptyStringKey() {
        Attributes attributes = new Attributes();
        attributes.put("", "value");
        fail(); // test should fail
    }

    @Test
    public void testPutIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value1");
        attributes.put("Key", "value2");
        assertEquals("value2", attributes.get("key"));
        assertEquals("value2", attributes.get("Key"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutIgnoreCaseWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase(null, "value");
        fail(); // test should fail
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutIgnoreCaseWithEmptyStringKey() {
        Attributes attributes = new Attributes();
        attributes.putIgnoreCase("", "value");
        fail(); // test should fail
    }

    @Test
    public void testPutBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        assertFalse(attributes.hasKey("booleanKey"));
        assertNull(attributes.get("booleanKey"));
        assertTrue(attributes.hasKeyIgnoreCase("booleankey"));
        assertTrue(attributes.getIgnoreCase("booleanKey").equals("true"));
    }

    @Test
    public void testPutReplaceBooleanAttribute() {
        Attributes attributes = new Attributes();
        attributes.put("booleanKey", true);
        attributes.put("booleanKey", false);
        assertFalse(attributes.hasKey("booleanKey"));
        assertNull(attributes.get("booleanKey"));
        assertFalse(attributes.hasKeyIgnoreCase("booleankey"));
        assertNull(attributes.getIgnoreCase("booleanKey"));
    }

    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        assertEquals(2, attributes.size());
    }

    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.remove("key1");
        assertEquals(1, attributes.size());
        assertNull(attributes.get("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove(null);
        fail(); // test should fail
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithEmptyStringKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("");
        fail(); // test should fail
    }

    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.removeIgnoreCase("key1");
        assertEquals(1, attributes.size());
        assertNull(attributes.get("key1"));
        assertNull(attributes.getIgnoreCase("key1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveIgnoreCaseWithNullKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.removeIgnoreCase(null);
        fail(); // test should fail
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveIgnoreCaseWithEmptyStringKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.removeIgnoreCase("");
        fail(); // test should fail
    }

    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("key2"));
        assertFalse(attributes.hasKeyIgnoreCase("Key1"));
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
    }

    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        assertTrue(attributes.hasKeyIgnoreCase("key1"));
        assertTrue(attributes.hasKeyIgnoreCase("Key1"));
        assertFalse(attributes.hasKeyIgnoreCase("Key2"));
    }

    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key2", "value2");
        attributes1.addAll(attributes2);
        assertEquals(2, attributes1.size());
        assertTrue(attributes1.hasKey("key1"));
        assertTrue(attributes1.hasKey("key2"));
    }

    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Map.Entry<String, String>> entries = attributes.asList();
        Iterator<Map.Entry<String, String>> iterator = attributes.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            count++;
            Map.Entry<String, String> entry = iterator.next();
            assertEquals(entry.getKey(), entries.get(count - 1).getKey());
            assertEquals(entry.getValue(), entries.get(count - 1).getValue());
        }
    }

    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        List<Map.Entry<String, String>> expected = attributes.asList();
        List<Map.Entry<String, String>> actual = Arrays.asList(new Map.Entry[] {
                new AbstractMap.SimpleEntry<>("key1", "value1"),
                new AbstractMap.SimpleEntry<>("key2", "value2")
        });
        assertEquals(expected, actual);
    }

    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("dataKey1", "dataValue1");
        attributes.put("dataKey2", "dataValue2");
        Map<String, String> dataset = attributes.dataset();
        assertEquals(2, dataset.size());
        assertTrue(dataset.containsKey("dataKey1"));
        assertTrue(dataset.containsKey("dataKey2"));
    }

    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        StringBuilder expected = new StringBuilder();
        attributes.html(expected, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        StringBuilder actual = new StringBuilder();
        attributes.html(actual, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        assertTrue(attributes1.equals(attributes2));
    }

    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        Attributes attributes2 = new Attributes();
        attributes1.put("key1", "value1");
        attributes2.put("key1", "value1");
        int hashCode1 = attributes1.hashCode();
        int hashCode2 = attributes2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        Attributes cloned = attributes.clone();
        assertEquals(attributes.size(), cloned.size());
        assertTrue(attributes.hasKey("key1"));
        assertTrue(cloned.hasKey("key1"));
        assertEquals(attributes.get("key1"), cloned.get("key1"));
    }

    @Test
    public void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("Key1", "value1");
        attributes.normalize();
        assertTrue(attributes.hasKey("key1"));
        assertFalse(attributes.hasKey("Key1"));
    }

    
}