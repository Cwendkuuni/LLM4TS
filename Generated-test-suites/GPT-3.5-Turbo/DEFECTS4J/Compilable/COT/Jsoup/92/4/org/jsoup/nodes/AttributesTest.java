package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {

  @Test
  public void testGet() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    assertEquals("value1", attributes.get("key1"));
  }

  @Test
  public void testGetIgnoreCase() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    assertEquals("value1", attributes.getIgnoreCase("KEY1"));
  }

  @Test
  public void testPut() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    assertEquals("value1", attributes.get("key1"));
  }

  @Test
  public void testPutBoolean() {
    Attributes attributes = new Attributes();
    attributes.put("key1", true);
    assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
  }

  @Test
  public void testRemove() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.remove("key1");
    assertFalse(attributes.hasKeyIgnoreCase("KEY1"));
  }

  @Test
  public void testRemoveIgnoreCase() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.removeIgnoreCase("KEY1");
    assertFalse(attributes.hasKeyIgnoreCase("KEY1"));
  }

  @Test
  public void testHasKey() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    assertTrue(attributes.hasKey("key1"));
  }

  @Test
  public void testHasKeyIgnoreCase() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
  }

  @Test
  public void testSize() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    assertEquals(1, attributes.size());
  }

  @Test
  public void testAddAll() {
    Attributes attributes1 = new Attributes();
    attributes1.put("key1", "value1");
    
    Attributes attributes2 = new Attributes();
    attributes2.put("key2", "value2");
    attributes2.put("key3", "value3");
    
    attributes2.addAll(attributes1);
    
    assertTrue(attributes2.hasKeyIgnoreCase("KEY1"));
    assertTrue(attributes2.hasKeyIgnoreCase("KEY2"));
    assertTrue(attributes2.hasKeyIgnoreCase("KEY3"));
  }

  @Test
  public void testIterator() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    
    int count = 0;
    for (Attribute attr : attributes) {
      count++;
    }
    
    assertEquals(2, count);
  }

  @Test
  public void testAsList() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    
    assertEquals(2, attributes.asList().size());
  }

  @Test
  public void testDataset() {
    Attributes attributes = new Attributes();
    attributes.put("data-key1", "value1");
    attributes.put("data-key2", "value2");
    
    assertEquals(2, attributes.dataset().size());
  }

  @Test
  public void testHtml() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    
    assertEquals(" key1=\"value1\" key2=\"value2\"", attributes.html());
  }

  @Test
  public void testEquals() {
    Attributes attributes1 = new Attributes();
    attributes1.put("key1", "value1");
    attributes1.put("key2", "value2");
    
    Attributes attributes2 = new Attributes();
    attributes2.put("key1", "value1");
    attributes2.put("key2", "value2");
    
    assertEquals(attributes1, attributes2);
  }

  @Test
  public void testHashCode() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    
    assertEquals(-1137359612, attributes.hashCode());
  }

  @Test
  public void testClone() {
    Attributes attributes1 = new Attributes();
    attributes1.put("key1", "value1");
    attributes1.put("key2", "value2");
    
    Attributes attributes2 = attributes1.clone();
    
    assertEquals(attributes1, attributes2);
  }
}
