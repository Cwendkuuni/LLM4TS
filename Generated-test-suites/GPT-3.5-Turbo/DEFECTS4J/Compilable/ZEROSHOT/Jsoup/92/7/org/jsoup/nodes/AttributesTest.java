package org.jsoup.nodes;
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
  public void testGet() {
    attributes.put("key", "value");
    assertEquals("value", attributes.get("key"));
  }
  
  @Test
  public void testGetIgnoreCase() {
    attributes.put("key", "value");
    assertEquals("value", attributes.getIgnoreCase("KEY"));
  }
  
  @Test
  public void testPut() {
    attributes.put("key", "value");
    assertTrue(attributes.hasKey("key"));
    assertEquals("value", attributes.get("key"));
  }
  
  @Test
  public void testPutBoolean() {
    attributes.put("key", true);
    assertTrue(attributes.hasKey("key"));
    assertEquals("", attributes.get("key"));
  }
  
  @Test
  public void testRemove() {
    attributes.put("key", "value");
    attributes.remove("key");
    assertFalse(attributes.hasKey("key"));
  }
  
  @Test
  public void testRemoveIgnoreCase() {
    attributes.put("key", "value");
    attributes.removeIgnoreCase("KEY");
    assertFalse(attributes.hasKey("key"));
  }
  
  @Test
  public void testHasKey() {
    attributes.put("key", "value");
    assertTrue(attributes.hasKey("key"));
    assertFalse(attributes.hasKey("nonexistent"));
  }
  
  @Test
  public void testHasKeyIgnoreCase() {
    attributes.put("key", "value");
    assertTrue(attributes.hasKeyIgnoreCase("KEY"));
    assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
  }
  
  @Test
  public void testSize() {
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    assertEquals(2, attributes.size());
  }
  
  @Test
  public void testAddAll() {
    Attributes other = new Attributes();
    other.put("key1", "value1");
    other.put("key2", "value2");
    attributes.addAll(other);
    assertTrue(attributes.hasKey("key1") && attributes.hasKey("key2"));
  }
  
  @Test
  public void testIterator() {
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
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    assertEquals(2, attributes.asList().size());
  }
  
  @Test
  public void testHtml() {
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    String html = attributes.html();
    assertTrue(html.contains("key1=\"value1\"") && html.contains("key2=\"value2\""));
  }
  
  @Test
  public void testEquals() {
    Attributes other = new Attributes();
    other.put("key1", "value1");
    other.put("key2", "value2");
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    assertTrue(attributes.equals(other));
  }
  
  @Test
  public void testHashCode() {
    Attributes other = new Attributes();
    other.put("key1", "value1");
    other.put("key2", "value2");
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    assertEquals(attributes.hashCode(), other.hashCode());
  }
  
  @Test
  public void testClone() {
    attributes.put("key", "value");
    Attributes cloned = attributes.clone();
    assertEquals(attributes, cloned);
  }
  
  @Test
  public void testNormalize() {
    attributes.put("KEY", "value");
    attributes.normalize();
    assertTrue(attributes.hasKey("key"));
  }
  
  @Test
  public void testDataset() {
    attributes.put("data-key1", "value1");
    attributes.put("data-key2", "value2");
    assertEquals(attributes.get("data-key1"), attributes.dataset().get("key1"));
  }
}