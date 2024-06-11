import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
  
  // Test getKey() method
  @Test
  public void testGetKey() {
    Attribute attr = new Attribute("key", "value");
    assertEquals("key", attr.getKey());
  }
  
  // Test setKey() method
  @Test
  public void testSetKey() {
    Attribute attr = new Attribute("key", "value");
    attr.setKey("newKey");
    assertEquals("newKey", attr.getKey());
  }
  
  // Test getValue() method
  @Test
  public void testGetValue() {
    Attribute attr = new Attribute("key", "value");
    assertEquals("value", attr.getValue());
  }
  
  // Test setValue() method
  @Test
  public void testSetValue() {
    Attribute attr = new Attribute("key", "value");
    attr.setValue("newValue");
    assertEquals("newValue", attr.getValue());
  }
  
  // Test html() method
  @Test
  public void testHtml() {
    Attribute attr = new Attribute("key", "value");
    assertEquals("key=\"value\"", attr.html());
  }
  
  // Test createFromEncoded() method
  @Test
  public void testCreateFromEncoded() {
    Attribute attr = Attribute.createFromEncoded("key", "value&amp;");
    assertEquals("key", attr.getKey());
    assertEquals("value&", attr.getValue());
  }
  
  // Test isDataAttribute() method
  @Test
  public void testIsDataAttribute() {
    Attribute attr1 = new Attribute("data-key", "value");
    Attribute attr2 = new Attribute("key", "value");
    assertTrue(attr1.isDataAttribute());
    assertFalse(attr2.isDataAttribute());
  }
  
  // Test shouldCollapseAttribute() method
  @Test
  public void testShouldCollapseAttribute() {
    Attribute attr1 = new Attribute("checked", "");
    Attribute attr2 = new Attribute("key", "value");
    assertTrue(attr1.shouldCollapseAttribute());
    assertFalse(attr2.shouldCollapseAttribute());
  }
  
  // Test isBooleanAttribute() method
  @Test
  public void testIsBooleanAttribute() {
    Attribute attr1 = new Attribute("checked", "");
    Attribute attr2 = new Attribute("key", "value");
    assertTrue(attr1.isBooleanAttribute());
    assertFalse(attr2.isBooleanAttribute());
  }
  
  // Test equals() method
  @Test
  public void testEquals() {
    Attribute attr1 = new Attribute("key", "value");
    Attribute attr2 = new Attribute("key", "value");
    Attribute attr3 = new Attribute("key", "newValue");
    assertTrue(attr1.equals(attr2));
    assertFalse(attr1.equals(attr3));
  }
  
  // Test hashCode() method
  @Test
  public void testHashCode() {
    Attribute attr1 = new Attribute("key", "value");
    Attribute attr2 = new Attribute("key", "value");
    Attribute attr3 = new Attribute("key", "newValue");
    assertEquals(attr1.hashCode(), attr2.hashCode());
    assertNotEquals(attr1.hashCode(), attr3.hashCode());
  }
  
  // Test clone() method
  @Test
  public void testClone() {
    Attribute attr = new Attribute("key", "value");
    Attribute cloneAttr = attr.clone();
    assertEquals(attr.getKey(), cloneAttr.getKey());
    assertEquals(attr.getValue(), cloneAttr.getValue());
    assertNotSame(attr, cloneAttr);
  }
}