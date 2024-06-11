import org.junit.Test;
import static org.junit.Assert.*;

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
    attribute.setValue("newValue");
    assertEquals("newValue", attribute.getValue());
  }
  
  @Test
  public void testHtml() {
    Attribute attribute = new Attribute("href", "index.html");
    assertEquals("href=\"index.html\"", attribute.html());
  }
  
  @Test
  public void testToString() {
    Attribute attribute = new Attribute("key", "value");
    assertEquals("key=\"value\"", attribute.toString());
  }
  
  @Test
  public void testCreateFromEncoded() {
    Attribute attribute = Attribute.createFromEncoded("key", "encodedValue");
    assertEquals("key", attribute.getKey());
    assertEquals("encodedValue", attribute.getValue());
  }
  
  @Test
  public void testIsDataAttribute() {
    Attribute attribute = new Attribute("data-key", "value");
    assertTrue(attribute.isDataAttribute());
  }
  
  @Test
  public void testShouldCollapseAttribute() {
    Attribute attribute = new Attribute("key", "key");
    Document.OutputSettings outputSettings = new Document.OutputSettings();
    assertTrue(attribute.shouldCollapseAttribute(outputSettings));
  }
  
  @Test
  public void testIsBooleanAttribute() {
    Attribute attribute = new Attribute("booleanAttribute", null);
    assertTrue(attribute.isBooleanAttribute());
  }
  
  @Test
  public void testEquals() {
    Attribute attribute1 = new Attribute("key", "value");
    Attribute attribute2 = new Attribute("key", "value");
    assertEquals(attribute1, attribute2);
  }
  
  @Test
  public void testHashCode() {
    Attribute attribute = new Attribute("key", "value");
    assertEquals(Objects.hash("key", "value"), attribute.hashCode());
  }
  
  @Test
  public void testClone() {
    Attribute attribute = new Attribute("key", "value");
    Attribute clone = attribute.clone();
    assertEquals(attribute, clone);
  }
}