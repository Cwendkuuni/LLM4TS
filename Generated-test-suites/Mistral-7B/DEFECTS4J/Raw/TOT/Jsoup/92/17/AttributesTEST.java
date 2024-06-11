package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

 @Test
 public void testAttributes() {
 Attributes attributes = new Attributes();
 // Test set and get attributes
 attributes.put("key1", "value1");
 attributes.put("key2", "value2");
 assertEquals("value1", attributes.get("key1"));
 assertEquals("value2", attributes.get("key2"));
 // Test get ignore case
 assertEquals("value1", attributes.getIgnoreCase("key1"));
 assertEquals("value2", attributes.getIgnoreCase("key2"));
 // Test hasKey
 assertTrue(attributes.hasKey("key1"));
 assertTrue(attributes.hasKeyIgnoreCase("key1"));
 assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
 // Test hasKey false
 assertFalse(attributes.hasKey("nonExistentKey"));
 // Test hasKey ignore case false
 assertFalse(attributes.hasKeyIgnoreCase("nonExistentKey"));
 // Test hasKey ignore case false with different casing
 assertFalse(attributes.hasKeyIgnoreCase("NonExistentKey"));
 // Test size
 assertEquals(2, attributes.size());
 // Test remove
 attributes.remove("key1");
 assertEquals(1, attributes.size());
 assertFalse(attributes.hasKey("key1"));
 // Test put with existing key
 attributes.put("key1", "value3");
 assertEquals("value3", attributes.get("key1"));
 // Test put ignore case with existing key
 attributes.putIgnoreCase("key1", "value4");
 assertEquals("value4", attributes.get("key1"));
 // Test put ignore case with different casing
 attributes.putIgnoreCase("Key1", "value5");
 assertEquals("value5", attributes.get("key1"));
 // Test put duplicate key
 attributes.put("key1", "value6");
 assertEquals("value6", attributes.get("key1"));
 // Test remove duplicate key
 attributes.put("key1", "value7");
 assertEquals("value7", attributes.get("key1"));
 attributes.remove("key1");
 assertFalse(attributes.hasKey("key1"));
 // Test addAll
 Attributes attributes2 = new Attributes();
 attributes2.put("key2", "value2");
 attributes2.put("key3", "value3");
 attributes.addAll(attributes2);
 assertEquals(3, attributes.size());
 // Test addAll duplicate key
 Attributes attributes3 = new Attributes();
 attributes3.put("key1", "value1");
 attributes.addAll(attributes3);
 assertEquals(3, attributes.size());
 assertEquals("value1", attributes.get("key1"));
 // Test dataset
 Attributes datasetAttributes = attributes.dataset();
 assertEquals(2, datasetAttributes.size());
 // Test html
 String html = attributes.html();
 assertEquals("<key1=value3 key2=value2>", html);
 }
}