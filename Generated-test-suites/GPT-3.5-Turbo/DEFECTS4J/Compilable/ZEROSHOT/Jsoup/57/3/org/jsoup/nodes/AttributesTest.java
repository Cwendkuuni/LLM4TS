package org.jsoup.nodes;
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
    assertEquals("", attributes.get("key3"));
  }

  @Test
  public void testGetIgnoreCase() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
  
    assertEquals("value1", attributes.getIgnoreCase("key1"));
    assertEquals("value2", attributes.getIgnoreCase("key2"));
    assertEquals("value1", attributes.getIgnoreCase("KEY1"));
    assertEquals("value2", attributes.getIgnoreCase("KEY2"));
    assertEquals("", attributes.getIgnoreCase("key3"));
  }

  @Test
  public void testPut() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
  
    assertEquals("value1", attributes.get("key1"));
    assertEquals("value2", attributes.get("key2"));
  }

  @Test
  public void testPutBoolean() {
    Attributes attributes = new Attributes();
    attributes.put("key1", true);
    attributes.put("key2", false);
  
    assertTrue(attributes.hasKey("key1"));
    assertFalse(attributes.hasKey("key2"));
    assertEquals("true", attributes.get("key1"));
    assertEquals("", attributes.get("key2"));
  }

  @Test
  public void testRemove() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    attributes.remove("key1");
  
    assertFalse(attributes.hasKey("key1"));
    assertTrue(attributes.hasKey("key2"));
    assertEquals("", attributes.get("key1"));
    assertEquals("value2", attributes.get("key2"));
  }

  @Test
  public void testRemoveIgnoreCase() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    attributes.removeIgnoreCase("KEY1");
  
    assertFalse(attributes.hasKey("key1"));
    assertTrue(attributes.hasKey("key2"));
    assertEquals("", attributes.get("key1"));
    assertEquals("value2", attributes.get("key2"));
  }

  @Test
  public void testHasKey() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
  
    assertTrue(attributes.hasKey("key1"));
    assertFalse(attributes.hasKey("key2"));
  }

  @Test
  public void testHasKeyIgnoreCase() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
  
    assertTrue(attributes.hasKeyIgnoreCase("kEy1"));
    assertFalse(attributes.hasKeyIgnoreCase("key2"));
  }

  @Test
  public void testSize() {
    Attributes attributes = new Attributes();
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
  public void testAsList() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
  
    assertEquals(2, attributes.asList().size());
    assertEquals("value1", attributes.asList().get(0).getValue());
    assertEquals("value2", attributes.asList().get(1).getValue());
  }

  @Test
  public void testDataset() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    attributes.put("data-key1", "dataValue1");
    attributes.put("data-key2", "dataValue2");
  
    assertEquals(2, attributes.dataset().size());
    assertEquals("dataValue1", attributes.dataset().get("key1"));
    assertEquals("dataValue2", attributes.dataset().get("key2"));
  }

  @Test
  public void testHtml() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
  
    String expected = " key1=\"value1\" key2=\"value2\"";
    assertEquals(expected, attributes.html());
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
  
    assertEquals(attributes1, attributes2);
  }

}