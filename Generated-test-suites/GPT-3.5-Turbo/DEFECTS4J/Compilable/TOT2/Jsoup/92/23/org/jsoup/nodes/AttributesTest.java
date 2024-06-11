package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.junit.Test;

public class AttributesTest {

  @Test
  public void testGet() {
    // Typical use case
    Attributes attributes = new Attributes();
    attributes.put("key", "value");
    assertEquals("value", attributes.get("key"));
    
    // Edge case - attribute not set
    assertEquals("", attributes.get("nonexistent"));
    
    // Exception handling - null key
    assertEquals("", attributes.get(null));
  }
  
  @Test
  public void testGetIgnoreCase() {
    // Typical use case
    Attributes attributes = new Attributes();
    attributes.put("Key", "value");
    assertEquals("value", attributes.getIgnoreCase("key"));
    
    // Edge case - attribute not set
    assertEquals("", attributes.getIgnoreCase("nonexistent"));
    
    // Exception handling - null key
    assertEquals("", attributes.getIgnoreCase(null));
  }
  
  @Test
  public void testPut() {
    // Typical use case
    Attributes attributes = new Attributes();
    attributes.put("key", "value");
    assertEquals("value", attributes.get("key"));
    
    // Edge case - replacing existing attribute
    attributes.put("key", "new value");
    assertEquals("new value", attributes.get("key"));
    
    // Exception handling - null key
    attributes.put(null, "value");
    assertEquals("value", attributes.get(null));
  }
  
  // Add more test methods for other public methods in the Attributes class
  
}