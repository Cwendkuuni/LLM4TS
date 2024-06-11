import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
  
  @Test
  public void testGetKey() {
    Attribute attr = new Attribute("key", "value");
    assertEquals("key", attr.getKey());
  }
  
  @Test
  public void testSetKey() {
    Attribute attr = new Attribute("key", "value");
    attr.setKey("newKey");
    assertEquals("newKey", attr.getKey());
  }
  
  @Test
  public void testGetValue() {
    Attribute attr = new Attribute("key", "value");
    assertEquals("value", attr.getValue());
  }
  
  @Test
  public void testSetValue() {
    Attribute attr = new Attribute("key", "value");
    attr.setValue("newValue");
    assertEquals("newValue", attr.getValue());
  }
  
  @Test
  public void testHtml() {
    Attribute attr = new Attribute("key", "value");
    String expectedHtml = "key=\"value\"";
    assertEquals(expectedHtml, attr.html());
  }
  
  @Test
  public void testToString() {
    Attribute attr = new Attribute("key", "value");
    String expectedString = "key=\"value\"";
    assertEquals(expectedString, attr.toString());
  }
  
  @Test
  public void testCreateFromEncoded() {
    String unencodedKey = "key";
    String encodedValue = "value&amp;";
    Attribute attr = Attribute.createFromEncoded(unencodedKey, encodedValue);
    assertEquals(unencodedKey, attr.getKey());
    assertEquals("value&", attr.getValue());
  }
  
  @Test
  public void testIsDataAttribute() {
    Attribute attr1 = new Attribute("data-key", "value");
    assertTrue(attr1.isDataAttribute());
    
    Attribute attr2 = new Attribute("key", "value");
    assertFalse(attr2.isDataAttribute());
  }
  
  @Test
  public void testShouldCollapseAttribute() {
    Document.OutputSettings out = new Document.OutputSettings();
    Attribute attr1 = new Attribute("key", "key");
    assertTrue(attr1.shouldCollapseAttribute(out));
    
    Attribute attr2 = new Attribute("key", "");
    assertTrue(attr2.shouldCollapseAttribute(out));
    
    Attribute attr3 = new Attribute("key", "value");
    assertFalse(attr3.shouldCollapseAttribute(out));
  }
  
  @Test
  public void testEquals() {
    Attribute attr1 = new Attribute("key", "value");
    Attribute attr2 = new Attribute("key", "value");
    assertTrue(attr1.equals(attr2));
  }
  
  @Test
  public void testHashCode() {
    Attribute attr1 = new Attribute("key", "value");
    Attribute attr2 = new Attribute("key", "value");
    assertEquals(attr1.hashCode(), attr2.hashCode());
  }
  
  @Test
  public void testClone() {
    Attribute attr = new Attribute("key", "value");
    Attribute clone = attr.clone();
    assertEquals(attr, clone);
  }
  
}