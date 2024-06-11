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
    assertEquals("value", attributes.getIgnoreCase("kEy"));
  }
  
  @Test
  public void testPut() {
    attributes.put("key", "value");
    assertTrue(attributes.hasKey("key"));
  }
  
  @Test
  public void testPutBooleanTrue() {
    attributes.put("key", true);
    assertTrue(attributes.hasKey("key"));
    assertEquals("", attributes.get("key"));
  }
  
  @Test
  public void testPutBooleanFalse() {
    attributes.put("key", false);
    assertFalse(attributes.hasKey("key"));
  }
  
  @Test
  public void testPutAttribute() {
    Attribute attribute = new Attribute("key", "value");
    attributes.put(attribute);
    assertTrue(attributes.hasKey("key"));
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
    attributes.removeIgnoreCase("kEy");
    assertFalse(attributes.hasKey("key"));
  }
  
  @Test
  public void testHasKey() {
    attributes.put("key", "value");
    assertTrue(attributes.hasKey("key"));
    assertFalse(attributes.hasKey("missing"));
  }
  
  @Test
  public void testHasKeyIgnoreCase() {
    attributes.put("key", "value");
    assertTrue(attributes.hasKeyIgnoreCase("kEy"));
    assertFalse(attributes.hasKeyIgnoreCase("missing"));
  }
  
  @Test
  public void testSize() {
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    assertEquals(2, attributes.size());
  }
  
  @Test
  public void testAddAll() {
    Attributes incoming = new Attributes();
    incoming.put("key1", "value1");
    incoming.put("key2", "value2");
    attributes.addAll(incoming);
    assertTrue(attributes.hasKey("key1"));
    assertTrue(attributes.hasKey("key2"));
  }
  
  @Test
  public void testIterator() {
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    Iterator<Attribute> iterator = attributes.iterator();
    assertTrue(iterator.hasNext());
    Attribute attribute1 = iterator.next();
    assertTrue(iterator.hasNext());
    Attribute attribute2 = iterator.next();
    assertFalse(iterator.hasNext());
    assertEquals("key1", attribute1.getKey());
    assertEquals("value1", attribute1.getValue());
    assertEquals("key2", attribute2.getKey());
    assertEquals("value2", attribute2.getValue());
  }
  
  @Test
  public void testAsList() {
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    List<Attribute> attributeList = attributes.asList();
    assertEquals(2, attributeList.size());
    Attribute attribute1 = attributeList.get(0);
    Attribute attribute2 = attributeList.get(1);
    assertEquals("key1", attribute1.getKey());
    assertEquals("value1", attribute1.getValue());
    assertEquals("key2", attribute2.getKey());
    assertEquals("value2", attribute2.getValue());
  }
  
  @Test
  public void testDataset() {
    attributes.put("data-key1", "value1");
    attributes.put("data-key2", "value2");
    Map<String, String> dataset = attributes.dataset();
    assertEquals(2, dataset.size());
    assertEquals("value1", dataset.get("key1"));
    assertEquals("value2", dataset.get("key2"));
  }
  
  @Test
  public void testHtml() {
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    String expectedHtml = " key1=\"value1\" key2=\"value2\"";
    assertEquals(expectedHtml, attributes.html());
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
    attributes.put("key1", "value1");
    attributes.put("key2", "value2");
    Attributes clone = attributes.clone();
    assertTrue(clone.hasKey("key1"));
    assertTrue(clone.hasKey("key2"));
  }
}