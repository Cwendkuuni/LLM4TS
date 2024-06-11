import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeTest {
  
  @Test
  public void testGetKey() {
    Attribute attribute = new Attribute("key", "value");
    assertEquals("key", attribute.getKey());
  }
  
  @Test
  public void testSetKey() {
    Attribute attribute = new Attribute("key", "value");
    attribute.setKey("newKey");
    assertEquals("newKey", attribute.getKey());
  }
  
  @Test
  public void testGetValue() {
    Attribute attribute = new Attribute("key", "value");
    assertEquals("value", attribute.getValue());
  }
  
  @Test
  public void testSetValue() {
    Attribute attribute = new Attribute("key", "value");
    String oldValue = attribute.setValue("newValue");
    assertEquals("value", oldValue);
    assertEquals("newValue", attribute.getValue());
  }
  
  @Test
  public void testHtml() {
    Attribute attribute = new Attribute("key", "value");
    String html = attribute.html();
    assertEquals("key=\"value\"", html);
  }
  
  @Test
  public void testToString() {
    Attribute attribute = new Attribute("key", "value");
    String str = attribute.toString();
    assertEquals("key=\"value\"", str);
  }
  
  @Test
  public void testCreateFromEncoded() {
    Attribute attribute = Attribute.createFromEncoded("key", "&amp;");
    assertEquals("key", attribute.getKey());
    assertEquals("&", attribute.getValue());
  }
  
  @Test
  public void testIsDataAttribute_true() {
    Attribute attribute = new Attribute("data-key", "value");
    assertTrue(attribute.isDataAttribute());
  }
  
  @Test
  public void testIsDataAttribute_false() {
    Attribute attribute = new Attribute("key", "value");
    assertFalse(attribute.isDataAttribute());
  }
  
  @Test
  public void testShouldCollapseAttribute_true() {
    Attribute attribute = new Attribute("key", "key");
    Document.OutputSettings out = new Document.OutputSettings();
    assertTrue(attribute.shouldCollapseAttribute(out));
  }
  
  @Test
  public void testShouldCollapseAttribute_false() {
    Attribute attribute = new Attribute("key", "value");
    Document.OutputSettings out = new Document.OutputSettings();
    assertFalse(attribute.shouldCollapseAttribute(out));
  }
  
  @Test
  public void testIsBooleanAttribute_true() {
    Attribute attribute = new Attribute("key", null);
    assertTrue(attribute.isBooleanAttribute());
  }
  
  @Test
  public void testIsBooleanAttribute_false() {
    Attribute attribute = new Attribute("key", "value");
    assertFalse(attribute.isBooleanAttribute());
  }
  
  @Test
  public void testEquals_true() {
    Attribute attribute1 = new Attribute("key", "value");
    Attribute attribute2 = new Attribute("key", "value");
    assertTrue(attribute1.equals(attribute2));
  }
  
  @Test
  public void testEquals_false_keyDifferent() {
    Attribute attribute1 = new Attribute("key1", "value");
    Attribute attribute2 = new Attribute("key2", "value");
    assertFalse(attribute1.equals(attribute2));
  }
  
  @Test
  public void testEquals_false_valueDifferent() {
    Attribute attribute1 = new Attribute("key", "value1");
    Attribute attribute2 = new Attribute("key", "value2");
    assertFalse(attribute1.equals(attribute2));
  }
  
  @Test
  public void testHashCode() {
    Attribute attribute = new Attribute("key", "value");
    int hashCode = attribute.hashCode();
    assertNotNull(hashCode);
  }
  
  @Test
  public void testClone() {
    Attribute attribute = new Attribute("key", "value");
    Attribute clone = attribute.clone();
    assertEquals(attribute.getKey(), clone.getKey());
    assertEquals(attribute.getValue(), clone.getValue());
  }
}