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

    assertEquals("value1", attributes.getIgnoreCase("key1"));
    assertEquals("value1", attributes.getIgnoreCase("Key1"));
    assertEquals("", attributes.getIgnoreCase("key2"));
  }

  @Test
  public void testPut() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");

    assertEquals("value1", attributes.get("key1"));
    assertEquals("value2", attributes.get("key2"));

    attributes.put("key1", "updated value1");
    assertEquals("updated value1", attributes.get("key1"));

    attributes.put("key3", "value3");
    assertEquals("value3", attributes.get("key3"));
  }

  @Test
  public void testRemove() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");

    attributes.remove("key1");
    assertNull(attributes.get("key1"));
    assertEquals("value2", attributes.get("key2"));

    attributes.remove("key2");
    assertNull(attributes.get("key2"));
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

    assertTrue(attributes.hasKeyIgnoreCase("key1"));
    assertTrue(attributes.hasKeyIgnoreCase("Key1"));
    assertFalse(attributes.hasKeyIgnoreCase("key2"));
  }

  @Test
  public void testSize() {
    Attributes attributes = new Attributes();
    assertEquals(0, attributes.size());

    attributes.put("key1", "value1");
    assertEquals(1, attributes.size());

    attributes.put("key2", "value2");
    assertEquals(2, attributes.size());

    attributes.remove("key1");
    assertEquals(1, attributes.size());
  }

  @Test
  public void testPutBoolean() {
    Attributes attributes = new Attributes();

    attributes.put("bool1", true);
    assertEquals("", attributes.get("bool1"));

    attributes.put("bool2", false);
    assertFalse(attributes.hasKey("bool2"));
  }

  @Test
  public void testPutAttribute() {
    Attributes attributes = new Attributes();
    Attribute attribute = new Attribute("key1", "value1");

    attributes.put(attribute);
    assertEquals("value1", attributes.get("key1"));

    attribute.setValue("updated value1");
    assertEquals("value1", attributes.get("key1"));
  }

  @Test
  public void testPutAll() {
    Attributes attributes1 = new Attributes();
    attributes1.put("key1", "value1");
    attributes1.put("key2", "value2");

    Attributes attributes2 = new Attributes();
    attributes2.put("key3", "value3");

    attributes2.addAll(attributes1);

    assertEquals("value1", attributes2.get("key1"));
    assertEquals("value2", attributes2.get("key2"));
    assertEquals("value3", attributes2.get("key3"));
  }
  
  // Add more methods to test edge cases, typical use cases, and error scenarios

}