
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributesTest {
  
  @Test
  public void testGet() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    
    String result = attributes.get("key1");
    
    assertEquals("value1", result);
  }
  
  @Test
  public void testGetIgnoreCase() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    
    String result = attributes.getIgnoreCase("Key1");
    
    assertEquals("value1", result);
  }
  
  @Test
  public void testPut() {
    Attributes attributes = new Attributes();
    
    attributes.put("key1", "value1");
    
    assertTrue(attributes.hasKey("key1"));
    assertFalse(attributes.hasKey("key2"));
  }
  
  @Test
  public void testPutBoolean() {
    Attributes attributes = new Attributes();
    
    attributes.put("key1", true);
    
    assertTrue(attributes.hasKey("key1"));
    assertEquals("key1", attributes.asList().get(0).getKey());
    assertEquals("true", attributes.asList().get(0).getValue());
    
    attributes.put("key1", false);
    
    assertFalse(attributes.hasKey("key1"));
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
    
    attributes.remove("key1");
    
    assertFalse(attributes.hasKey("key1"));
  }
  
  @Test
  public void testRemoveIgnoreCase() {
    Attributes attributes = new Attributes();
    attributes.put("Key1", "value1");
    
    attributes.removeIgnoreCase("key1");
    
    assertFalse(attributes.hasKey("key1"));
  }
  
  @Test
  public void testSize() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    
    int size = attributes.size();
    
    assertEquals(2, size);
  }
  
  @Test
  public void testAddAll() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    
    Attributes incoming = new Attributes();
    incoming.put("key2", "value2");
    
    attributes.addAll(incoming);
    
    assertTrue(attributes.hasKey("key1"));
    assertTrue(attributes.hasKey("key2"));
  }
  
  @Test
  public void testIterator() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    
    Iterator<Attribute> iterator = attributes.iterator();
    
    assertTrue(iterator.hasNext());
    assertEquals("key1", iterator.next().getKey());
    assertEquals("key2", iterator.next().getKey());
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testAsList() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    
    List<Attribute> list = attributes.asList();
    
    assertEquals(2, list.size());
    assertEquals("key1", list.get(0).getKey());
    assertEquals("key2", list.get(1).getKey());
  }
  
  @Test
  public void testDataset() {
    Attributes attributes = new Attributes();
    attributes.put("key1", "value1");
    attributes.put("data-key2", "value2");
    
    Map<String, String> dataset = attributes.dataset();
    
    assertEquals(1, dataset.size());
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
    
    Attributes attributes2 = attributes1.clone();
    
    assertTrue(attributes1.equals(attributes2));
  }
  
}
